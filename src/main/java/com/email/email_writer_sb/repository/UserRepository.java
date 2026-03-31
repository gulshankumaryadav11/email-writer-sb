package com.email.email_writer_sb.repository;

import com.email.email_writer_sb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ Login
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // ✅ Email Verification
    Optional<User> findByVerificationToken(String verificationToken);

    // ✅ Forgot Password
    Optional<User> findByResetToken(String resetToken);
}