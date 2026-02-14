package com.roima.hrms.travel.service;
import com.roima.hrms.auth.model.UserPrincipal;
import com.roima.hrms.travel.dto.RequiredTravelDocumentRequestDto;
import com.roima.hrms.travel.dto.SubmitedDocumentDto;
import com.roima.hrms.travel.entity.RequiredDocument;
import com.roima.hrms.travel.entity.SubmittedTravelDocs;
import com.roima.hrms.travel.entity.Travel;
import com.roima.hrms.travel.entity.TravelAssign;
import com.roima.hrms.travel.mapper.DocumentMapper;
import com.roima.hrms.travel.mapper.ExpenseMapper;
import com.roima.hrms.travel.repository.RequiredDocumentRepository;
import com.roima.hrms.travel.repository.SubmittedTravelDocumentRepository;
import com.roima.hrms.travel.repository.TravelAssignRepository;
import com.roima.hrms.travel.repository.TravelRepository;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TravelDocumentServiceImpl implements TravelDocumentService {

    private final SubmittedTravelDocumentRepository submittedTravelDocumentRepository;
    private final TravelAssignRepository travelAssignRepository;
    private final RequiredDocumentRepository requiredDocumentRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final TravelRepository travelRepository;

    public TravelDocumentServiceImpl(FileStorageService fileStorageService,UserRepository userRepository,
                                     TravelAssignRepository travelAssignRepository,
                                     RequiredDocumentRepository requiredDocumentRepository,
                                     SubmittedTravelDocumentRepository submittedTravelDocumentRepository,
                                     TravelRepository travelRepository)
    {
        this.fileStorageService = fileStorageService;
        this.travelAssignRepository = travelAssignRepository;
        this.requiredDocumentRepository = requiredDocumentRepository;
        this.submittedTravelDocumentRepository = submittedTravelDocumentRepository;
        this.userRepository = userRepository;
        this.travelRepository = travelRepository;
    }


    @Override
    public void uploadDocument(Long assignedId,String documentName, MultipartFile file,String filetype) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        TravelAssign travelAssign = travelAssignRepository.findById(assignedId).orElseThrow(() -> new RuntimeException("required travel assign not found"));;
       // TravelAssign travelAssign = travelAssignRepository.findByUser_idAndTravel_id(userPrincipal.getUserId(), travelId).orElseThrow(() -> new RuntimeException("travel not assigned"));
        //RequiredDocument requiredDocument = requiredDocumentRepository.findById(requiredDocId).orElseThrow(() -> new RuntimeException("required document not found"));

        String path = fileStorageService.store(
                file,
                assignedId,
                travelAssign.getUser().getId(),
                documentName
        );

        User user = userRepository.findById(userPrincipal.getUserId()).orElseThrow(() -> new RuntimeException("user not found"));
        SubmittedTravelDocs doc = new SubmittedTravelDocs();
        doc.setTravelAssign(travelAssign);
        doc.setDocumentName(documentName);
        doc.setFilepath(path);
        doc.setUser(user);
        doc.setFiletype(filetype);
        submittedTravelDocumentRepository.save(doc);
    }


        @Override
        public Resource downloadDocument(Long id)
        {
            SubmittedTravelDocs doc =submittedTravelDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("document not found"));

//            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            boolean isOwner = doc.getTravelAssign().getUser().getId().equals(user.getId());
//            boolean isHr = user.getRole().equals("Hr");
//
//            if(!isOwner && !isHr){
//                throw new RuntimeException("document not found");
//            }

            return fileStorageService.load(doc.getFilepath());

        }


        public RequiredDocument postDocument(Long travelId, RequiredTravelDocumentRequestDto dto) {

               Travel travel = travelRepository.findById(travelId).orElseThrow(() -> new RuntimeException("travel not found"));
               RequiredDocument doc = new RequiredDocument();
               doc.setDoc_name(dto.getFilename());
               doc.setTravel(travel);
              return requiredDocumentRepository.save(doc);
        }


    @Override
    public List<RequiredDocument> getDocument(Long travelId) {
        List<RequiredDocument> list = requiredDocumentRepository.findByTravel_Id(travelId);
        return list;
    }

    @Override
    public List<SubmitedDocumentDto> findByAssigned_id(Long assignedId) {
           List<SubmittedTravelDocs> docs =submittedTravelDocumentRepository.findByTravelAssign_id(assignedId);
        return docs.stream()
                .map(DocumentMapper::toDto)
                .toList();

    }
}
