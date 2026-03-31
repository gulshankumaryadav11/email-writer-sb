package com.email.email_writer_sb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // ✅ OTP EMAIL
    public void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("🔐 EMIPI - OTP Verification");
        message.setText(
                "Hello,\n\n" +
                        "Your OTP for verification is: " + otp + "\n\n" +
                        "⏳ This OTP is valid for 5 minutes.\n\n" +
                        "If you did not request this, please ignore.\n\n" +
                        "Thanks,\nEMIPI Team 🚀"
        );

        mailSender.send(message);
    }

    // ✅ GENERIC EMAIL (future use)
    public void sendEmail(String toEmail, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}