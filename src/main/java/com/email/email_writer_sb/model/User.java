package com.email.email_writer_sb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "full_name")
    private String fullName;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    // 🔥 OTP fields
    private String otp;

    private LocalDateTime otpExpiry;

    // 🔥 Email verification token
    @Column(name = "verification_token")
    private String verificationToken;

    // 🔥 Password reset token
    @Column(name = "reset_token")
    private String resetToken;

    // ✅ Account verification status
    @Column(name = "is_verified")
    private boolean isVerified = false;

    // ✅ Created time
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // ✅ Auto set created time
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}