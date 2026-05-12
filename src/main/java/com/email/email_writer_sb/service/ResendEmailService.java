package com.email.email_writer_sb.service;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResendEmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    public void sendOtpEmail(String to, String otp) {

        Resend resend = new Resend(resendApiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to(to)
                .subject("Your OTP Code")
                .html("<h2>Your OTP is: " + otp + "</h2>")
                .build();

        resend.emails().send(params);
    }
}
