package com.lear.change_management.services;

import com.lear.change_management.entities.User;
import com.lear.change_management.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepo userRepository;
    private static final int EXPIRY_HOURS = 24;
    private final JavaMailSender mailSender;
    @Async
    public void sendWelcomeWithResetLink(User user, String resetToken) {
        //TODO TEST
        //TODO create the view for this URL
        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("changemanagement@lear.com");
        message.setTo(user.getEmail());
        message.setSubject("Welcome – Set your password");
        message.setText("""
            Hello %s,

            Your account has been created.

            Please click the link below to set your password (valid for 24 hours):

            %s

            If you didn't request this, please ignore this email.

            Best regards,
            The Team
            """.formatted(user.getUserName(), resetLink));

        mailSender.send(message);
    }
    //after creating the user
    public void createAndSendResetToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setMustChangePassword(true);
        user.setPasswordResetToken(token);
        user.setPasswordResetExpiry(LocalDateTime.now().plusHours(EXPIRY_HOURS));
        userRepository.save(user);
        sendWelcomeWithResetLink(user, token);
    }
    // when user opens the link
    public Optional<User> findUserByValidToken(String token) {
        return userRepository.findByPasswordResetToken(token)
                .filter(user -> user.getPasswordResetToken() != null)
                .filter(user -> user.getPasswordResetExpiry().isAfter(LocalDateTime.now()));
    }
    // after user sets new password
    public void clearToken(User user) {
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiry(null);
        user.setMustChangePassword(false);
        userRepository.save(user);
    }
}