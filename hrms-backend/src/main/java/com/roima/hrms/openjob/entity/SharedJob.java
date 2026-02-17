package com.roima.hrms.openjob.entity;

import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="shared_job")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    private LocalDateTime shared_at=LocalDateTime.now();

    @Email
    private String shared_email;
}
