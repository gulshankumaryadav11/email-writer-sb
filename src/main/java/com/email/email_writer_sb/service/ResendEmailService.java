package com.email.email_writer_sb.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResendEmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    public void sendOtpEmail(String to, String otp) {

        try {

            Resend resend = new Resend(resendApiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("onboarding@resend.dev")
                    .to(to)
                    .subject("Your OTP Code")
                    .html(
                            "<h2>Email Verification</h2>" +
                            "<p>Your OTP is:</p>" +
                            "<h1>" + otp + "</h1>" +
                            "<p>This OTP expires in 5 minutes.</p>"
                    )
                    .build();

            resend.emails().send(params);

        } catch (ResendException e) {
            throw new RuntimeException("Failed to send OTP email");
        }
    }
}
