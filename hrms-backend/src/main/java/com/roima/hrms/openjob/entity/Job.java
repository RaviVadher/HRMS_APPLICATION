package com.roima.hrms.openjob.entity;

import com.roima.hrms.openjob.enums.JobStatus;
import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String job_summary;

    @NotBlank
    @Column(nullable = false)
    private String jd_path;


    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_user")
    private User createdBy;

    @Email
    @Column(nullable = false)
    private String email_hr;

  @Enumerated(EnumType.STRING)
    private JobStatus status;

  private LocalDateTime created_at;
  private LocalDateTime updated_at;

  @PrePersist
    void onCreate() {
      created_at = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
      updated_at = LocalDateTime.now();
    }
}
