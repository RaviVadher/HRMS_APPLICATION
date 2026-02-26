package com.roima.hrms.travel.service;

import com.roima.hrms.auth.model.UserPrincipal;
import com.roima.hrms.common.notification.entity.NotificationType;
import com.roima.hrms.common.notification.service.NotificationService;
import com.roima.hrms.common.notification.service.NotificationServiceImpl;
import com.roima.hrms.mail.EmailService;
import com.roima.hrms.mail.EmailTemplate;
import com.roima.hrms.travel.dto.TravelAssignResponseDto;
import com.roima.hrms.travel.dto.TravelCreateRequestDto;
import com.roima.hrms.travel.dto.TravelResponseDto;
import com.roima.hrms.travel.entity.Travel;
import com.roima.hrms.travel.entity.TravelAssign;
import com.roima.hrms.travel.exception.AllReadyAssignedException;
import com.roima.hrms.travel.exception.WrongdateException;
import com.roima.hrms.travel.mapper.AssignMapper;
import com.roima.hrms.travel.mapper.TravelMapper;
import com.roima.hrms.travel.repository.TravelAssignRepository;
import com.roima.hrms.travel.repository.TravelRepository;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;

@Service
public class TravelServiceImpl implements TravelService {

    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;
    private final UserRepository userRepository;
    private final TravelAssignRepository travelAssignRepository;
    private final EmailService emailService;
    private final AssignMapper assignMapper;
    private final NotificationService notificationService;


    public TravelServiceImpl(TravelRepository travelRepository, TravelMapper travelMapper, UserRepository userRepository
    , TravelAssignRepository travelAssignRepository,
                             EmailService emailService,
                             AssignMapper assignMapper,
                             NotificationService notificationService) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
        this.userRepository = userRepository;
        this.travelAssignRepository = travelAssignRepository;
        this.emailService = emailService;
        this.assignMapper = assignMapper;
        this.notificationService = notificationService;
    }


    @Override
    public TravelResponseDto createTravel(TravelCreateRequestDto dto)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user  =userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        Travel travel = travelMapper.toEntity(dto);
        if(!travel.isEndDateAfterStartDate())
        {
            throw new WrongdateException("End date is must after start date");
        }
        travel.setUser(user);
        travel.setCreated_date(LocalDate.now());
        return travelMapper.toDto(travelRepository.save(travel));
    }

    @Override
    public List<TravelResponseDto> findAllTravels()
    {
        return travelRepository.findAllOrderByCreatedDateAsc()
                .stream()
                .map(travelMapper::toDto)
                .toList();
    }

    @Override
    public TravelResponseDto findTravelById(Long travelId)
    {
        Travel travel = travelRepository.findById(travelId).orElseThrow(() -> new RuntimeException("travel not found"));
        return travelMapper.toDto(travel);
    }

    public TravelAssign assignTravel(Long travelId,Long userId)
    {
        User user  =userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        Travel travel = travelRepository.findById(travelId).orElseThrow(()-> new RuntimeException("Travel not found"));
      if(travelAssignRepository.existsByUser_idAndTravel_id(userId,travelId))
      {
          throw new AllReadyAssignedException("Travel has already been assigned");
      }
        TravelAssign travelAssign = new TravelAssign();
        travelAssign.setTravel(travel);
        travelAssign.setUser(user);
        travelAssign.setAssignedDate(LocalDate.now());


        //notification
        notificationService.crateNotification(user.getId(),"You have been assigned to a travel plan: "+travel.getTravel_title(), NotificationType.TravelAssign,travelId,false);


        //sending mail to assigned User
        emailService.sendEmail(user.getEmail(),"Travel Assigned -"+ travel.getTravel_title(),EmailTemplate.travelAssigned(user.getName(),travel.getTravel_title(),travel.getStart_date(),travel.getEnd_date()));
        return travelAssignRepository.save(travelAssign);
    }

    @Override
    public  List<TravelAssignResponseDto> findAllTravelsAssign(Long travelId)
    {
        return travelAssignRepository.findByTravel_id(travelId)
                .stream()
                .map(AssignMapper::toDto)
                .toList();
    }


    @Override
    public  List<TravelAssignResponseDto> findMyTravelsAssign()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user  =userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));
        return travelAssignRepository.findByUser_id(user.getId())
                .stream()
                .map(AssignMapper::myTravel)
                .toList();
    }

    @Override
    public  TravelAssignResponseDto findMyTravelsAssign(Long assignId)
    {
        TravelAssign dto = travelAssignRepository.findById(assignId).orElseThrow(()-> new RuntimeException("assin not found"));
        return assignMapper.myTravel(dto);

    }

    @Override
    public List<TravelAssignResponseDto> findMyTeamTravelsAssign()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long managerId = userPrincipal.getUserId();

        return travelAssignRepository.findByUser_Manager_id(managerId)
                .stream()
                .map(AssignMapper::myTravel)
                .toList();

    }



}
