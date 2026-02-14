package com.roima.hrms.openjob.entity;

import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.time.LocalDateTime;

@Entity
@Table(name="shared_job")
public class SharedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk_shared_job;

    @ManyToOne(optional = false)
    @JoinColumn(name="fk_user")
    private User sharedby;

    @ManyToOne(optional = false)
    @JoinColumn(name="fk_job")
    private Job job;

    private LocalDateTime shared_at;

    @Email
    private String shared_email;
}
