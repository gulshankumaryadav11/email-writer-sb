package com.email.email_writer_sb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDTOs {

    // ================= REGISTER =================
    @Data
    public static class RegisterRequest {

        @NotBlank(message = "Full name is required")
        private String fullName;

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;
    }

    // ================= LOGIN =================
    @Data
    public static class LoginRequest {

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;
    }

    // ================= VERIFY OTP =================
    @Data
    public static class VerifyOtpRequest {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String otp;
    }

    // ================= AUTH RESPONSE =================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthResponse {

        private String token;
        private String type = "Bearer"; // optional but useful
        private Long id;
        private String fullName;
        private String email;
        private String message; // 🔥 ADD THIS (frontend use karega)

        // 🔥 Custom constructor (best)
        public AuthResponse(String token, Long id, String fullName, String email) {
            this.token = token;
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.message = "Login successful ✅";
        }
    }

    // ================= ERROR =================
    @Data
    public static class ErrorResponse {

        private int status;
        private String message;
        private long timestamp;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // ================= MESSAGE =================
    @Data
    @AllArgsConstructor
    public static class MessageResponse {
        private String message;
    }
}