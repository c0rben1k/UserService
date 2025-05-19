package by.algin.userservice.service;

import by.algin.userservice.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final AppProperties appProperties;

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public void sendConfirmationEmail(String to, String token) {
        String confirmationUrl = appProperties.getConfirmation().getUrl() + token;

        String subject = "Task Management System - Account Confirmation";
        if (appProperties.getMail().getSubject().getRegistration() != null &&
                !appProperties.getMail().getSubject().getRegistration().isEmpty()) {
            subject = appProperties.getMail().getSubject().getRegistration();
        }

        String emailBody = "Thank you for registering with Task Management System. " +
                "Please click on the link below to confirm your account:";
        if (appProperties.getMail().getBody().getRegistration() != null &&
                !appProperties.getMail().getBody().getRegistration().isEmpty()) {
            emailBody = appProperties.getMail().getBody().getRegistration();
        }

        String text = "Hello,\n\n" +
                emailBody + "\n\n" +
                confirmationUrl + "\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Regards,\n" +
                "Task Management System Team";

        sendEmail(to, subject, text);
    }
}