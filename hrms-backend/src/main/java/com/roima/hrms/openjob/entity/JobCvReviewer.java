package com.roima.hrms.openjob.entity;

import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="job_cv_reviewers")
public class JobCvReviewer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewer_id;

    @ManyToOne(optional = false)
    @JoinColumn(name="fk_job_id")
    private Job job;

    @ManyToOne(optional = false)
    @JoinColumn(name="fk_user")
    private User reviewer;

    private LocalDateTime reviewed_at;
}
