package com.roima.hrms.openjob.entity;

import com.roima.hrms.openjob.enums.ReferStatus;
import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="refers")
public class Refer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refer_id;

    @ManyToOne(optional = false)
    @JoinColumn(name="fk_user")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name="fk_job")
    private Job job;

    @NotBlank
    private String refer_name;

    @Email
    @NotBlank
    private String refer_email;

    @NotBlank
    private String refer_cvpath;

    @Column(length = 1000)
    private String refer_description;

    @Enumerated(EnumType.STRING)
    private ReferStatus refer_status;

    @ManyToOne
    @JoinColumn(name = "status_changedby")
    private User status_changed_by;
}
