package com.email.email_writer_sb.service;

import com.email.email_writer_sb.dto.AuthDTOs.*;
import com.email.email_writer_sb.dto.VerifyOtpRequest;
import com.email.email_writer_sb.model.User;
import com.email.email_writer_sb.repository.UserRepository;
import com.email.email_writer_sb.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtils jwtUtils;

    // ✅ REGISTER
    public MessageResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists ❌");
        }

        String otp = generateOTP();

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .otp(otp)
                .otpExpiry(LocalDateTime.now().plusMinutes(5))
                .isVerified(false)
                .build();

        userRepository.save(user);
        emailService.sendOtpEmail(user.getEmail(), otp);

        return new MessageResponse("OTP sent to email 📩");
    }

    // ✅ VERIFY OTP (🔥 FIXED)
    public MessageResponse verifyOtp(VerifyOtpRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found ❌"));

        // 🔥 FIX: exception hata diya
        if (user.isVerified()) {
            return new MessageResponse("Already verified ✅");
        }

        if (user.getOtp() == null) {
            throw new RuntimeException("OTP not found ❌");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired ⏰");
        }

        if (!user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP ❌");
        }

        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);

        return new MessageResponse("Account verified ✅");
    }

    // ✅ RESEND OTP (🔥 IMPROVED)
    public MessageResponse resendOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found ❌"));

        // 🔥 OPTIONAL: already verified check
        if (user.isVerified()) {
            return new MessageResponse("User already verified, please login ✅");
        }

        String otp = generateOTP();

        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);
        emailService.sendOtpEmail(email, otp);

        return new MessageResponse("New OTP sent 📩");
    }

    // 🔥 LOGIN
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found ❌"));

        if (!user.isVerified()) {
            throw new RuntimeException("Verify your email first ❌");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password ❌");
        }

        String token = jwtUtils.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }

    private String generateOTP() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}