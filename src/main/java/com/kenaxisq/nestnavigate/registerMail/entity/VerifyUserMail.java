package com.kenaxisq.nestnavigate.registerMail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerifyUserMail {
    @Id
    @Column(name = "email")
    private String email;
    @Column(name = "verification_code")
    private String verificationCode;
    @Column(name = "verification_expiry")
    private LocalDateTime verificationExpiry;
    @Column(name = "verified")
    private boolean verified;
}
