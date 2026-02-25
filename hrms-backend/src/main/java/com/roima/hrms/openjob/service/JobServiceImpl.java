package com.roima.hrms.openjob.service;

import com.roima.hrms.auth.model.UserPrincipal;
import com.roima.hrms.mail.EmailService;
import com.roima.hrms.mail.EmailTemplate;
import com.roima.hrms.openjob.dto.*;
import com.roima.hrms.openjob.entity.*;
import com.roima.hrms.openjob.enums.JobStatus;
import com.roima.hrms.openjob.exception.JobNotFoundException;
import com.roima.hrms.openjob.mapper.JobMapper;
import com.roima.hrms.openjob.mapper.ReferMapper;
import com.roima.hrms.openjob.mapper.SharedJobMapper;
import com.roima.hrms.openjob.repository.*;
import com.roima.hrms.common.filestorage.FileStorageService;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final SharedJobRepository sharedJobRepository;
    private final EmailService emailService;
    private final JobCvReviewerRepository jobCvReviewerRepository;
    private final ReferRepository referRepository;
    private final AppConfigRepository appConfigRepository;
     public JobServiceImpl(JobRepository jobRepository,
                           UserRepository userRepository,
                           FileStorageService fileStorageService,
                           SharedJobRepository sharedJobRepository,
                           EmailService emailService,
                           JobCvReviewerRepository jobCvReviewerRepository,
                           ReferRepository referRepository,
                           AppConfigRepository appConfigRepository

     ) {
         this.jobRepository = jobRepository;
         this.userRepository = userRepository;
         this.fileStorageService = fileStorageService;
         this.sharedJobRepository = sharedJobRepository;
         this.emailService = emailService;
         this.jobCvReviewerRepository = jobCvReviewerRepository;
         this.referRepository = referRepository;
         this.appConfigRepository = appConfigRepository;
     }

//create job
@Override
public JobDto createJob(JobCreateDto dto) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    User user = userRepository.findById(userPrincipal.getUserId()).orElseThrow(()->new UsernameNotFoundException("User not found"));

    String path = fileStorageService.store(
            dto.getFile(),
            user.getId(),
            user.getId(),
            "JD",
            "Job"
    );

    Job job = new Job();
    job.setTitle(dto.getTitle());
    job.setJob_summary(dto.getSummary());
    job.setCreatedBy(user);
    job.setEmail_hr(user.getEmail());
    job.setCreated_at(LocalDateTime.now());
    job.setStatus(JobStatus.Open);
    job.setJd_path(path);

    Job j=  jobRepository.save(job);
    if(dto.getReviewerIds()!=null)
    {
        List<User> reviewers = userRepository.findAllById(dto.getReviewerIds());
        List<JobCvReviewer> list = reviewers.stream()
                .map(u -> JobCvReviewer.builder()
                        .job(job)
                        .reviewer(u)
                        .build())
                .toList();

        jobCvReviewerRepository.saveAll(list);
        log.info("Job is created");
    }
    return JobMapper.toDto(j);
}


    //get all job by hr
     @Override
     public List<JobDto> getAllJobs()
     {
         return jobRepository.findAll()
                 .stream()
                 .map(JobMapper::toDto)
                 .toList();
     }

     //get job by id
     @Override
     public JobDto  getJobById(Long id)
     {
         return JobMapper.toDto(jobRepository.findById(id).orElseThrow(()-> new JobNotFoundException("Job not found")));
     }


     //share job
     @Override
     public ResponseEntity<String> shareJob(Long jobId,String email)
     {
         SharedJob sharedJob = new SharedJob();
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

         User user = userRepository.findById(userPrincipal.getUserId()).orElseThrow(()->new UsernameNotFoundException("User not found"));
         Job job = jobRepository.findById(jobId).orElseThrow(()->new JobNotFoundException("Job not found"));

          sharedJob.setSharedby(user);
          sharedJob.setJob(job);
          sharedJob.setShared_at(LocalDateTime.now());
          sharedJob.setShared_email(email);

          Path path = Paths.get(job.getJd_path());
          String jdPath =  path.toString();

         emailService.sendMailWithAttachment(email,"Open Position",EmailTemplate.sharedJob(job.getTitle(),user.getName()),jdPath );
         sharedJobRepository.save(sharedJob);

         return ResponseEntity.ok("Job Shared Successfully");
     }

     //find all shared
     @Override
     public List<SharedJobResponseDto> findAllShered(Long jobId){
        return sharedJobRepository.findByJob_JobId(jobId)
                 .stream()
                 .map(SharedJobMapper::toDto)
                 .toList();
    }

    //refer to friend
    @Override
    public void refer(ReferRequestDto dto)
    {
        Job job = jobRepository.findById(dto.getJobId()).orElseThrow(()->new JobNotFoundException("Job not found"));

        String path = fileStorageService.store(
                dto.getFile(),
                 dto.getJobId(),
                dto.getJobId(),
                "Refer",
                "CV"
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getUserId()).orElseThrow(()->new UsernameNotFoundException("User not found"));

        Refer refer = new Refer();
        refer.setJob(job);
        refer.setRefer_cvpath(path);
        refer.setRefer_email(dto.getFriendEmail());
        refer.setRefer_date(LocalDateTime.now());
        refer.setRefer_name(dto.getFriendName());
        refer.setJob(job);
        refer.setUser(user);
        refer.setRefer_description(dto.getNote());

        List<String> to = collectionto(job);
        String cvPath =  path.toString();
        emailService.sendMailWithAttachmentALl(to, "Refer Job",
                EmailTemplate.referJob(job.getJobId(),job.getTitle(),user.getName(),dto.getFriendName(),dto.getFriendName()),cvPath);
        referRepository.save(refer);
    }



    //find all configured mail.
    private List<String >collectionto (Job job)
    {
        List<String> list = new ArrayList<>();
        list.add(job.getEmail_hr());

        List<String>  defaultHr = appConfigRepository.findAll()
                .stream().map(AppConfig::getValue).toList();

        list.addAll(defaultHr);

        List<String> reviewers = jobCvReviewerRepository.findByJob_JobId(job.getJobId())
        .stream()
            .map( r -> r.getReviewer().getEmail())
            .toList();

        list.addAll(reviewers);
        return list;
    }

    //refer details
    @Override
    public List<ReferJobResponseDto> getRefer(Long jobId){
         return referRepository.findByJob_JobId(jobId)
                 .stream()
                 .map(ReferMapper::toDto)
                 .toList();
    }

    //view cv
    @Override
    public Resource downloadCv(Long referId){

        Refer ref = referRepository.findById(referId).orElseThrow(()->new RuntimeException("Invelid referId"));
        return  fileStorageService.load(ref.getRefer_cvpath());
    }

    //change job status
    @Override
    public ResponseEntity<String> changeJobStatus(Long jobId,ChangeStatusDto dto){
         Job job = jobRepository.findById(jobId).orElseThrow(()->new JobNotFoundException("Job not found"));
         job.setStatus(dto.getStatus());
         job.setUpdated_at(LocalDateTime.now());
         jobRepository.save(job);
         return ResponseEntity.ok("Job Status Updated Successfully");
    }
}
