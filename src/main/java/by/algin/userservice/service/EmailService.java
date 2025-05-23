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
        if (appProperties.getMail().getRegistrationSubject() != null &&
                !appProperties.getMail().getRegistrationSubject().isEmpty()) {
            subject = appProperties.getMail().getRegistrationSubject();
        }

        String emailBody = "Thank you for registering with Task Management System. " +
                "Please click on the link below to confirm your account:";
        if (appProperties.getMail().getRegistrationBody() != null &&
                !appProperties.getMail().getRegistrationBody().isEmpty()) {
            emailBody = appProperties.getMail().getRegistrationBody();
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