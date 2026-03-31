package com.email.email_writer_sb.controller;

import com.email.email_writer_sb.model.User;
import com.email.email_writer_sb.repository.UserRepository;
import com.email.email_writer_sb.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @GetMapping("/me")
    public User getProfile(@RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        String email = jwtUtils.getEmailFromToken(token);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PutMapping("/update")
    public User updateProfile(
            @RequestHeader("Authorization") String header,
            @RequestBody User updated
    ) {
        String token = header.substring(7);
        String email = jwtUtils.getEmailFromToken(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(updated.getFullName());
        user.setEmail(updated.getEmail());

        return userRepository.save(user);
    }
}