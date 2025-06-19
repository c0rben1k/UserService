package by.algin.userservice.service;

import by.algin.userservice.constants.MessageConstants;
import by.algin.userservice.mapper.EmailMapper;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailMapper emailMapper;

    public void sendConfirmationEmail(String to, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessage preparedMessage = emailMapper.createConfirmationEmail(mimeMessage, to, token);
            mailSender.send(preparedMessage);
            log.info(MessageConstants.CONFIRMATION_EMAIL_SENT_TO, to);
        } catch (Exception e) {
            log.error(MessageConstants.FAILED_TO_SEND_EMAIL_TO, to, e);
            throw new RuntimeException(MessageConstants.FAILED_TO_SEND_EMAIL + e.getMessage(), e);
        }
    }
}