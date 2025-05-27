package by.algin.userservice.mapper;

import by.algin.userservice.config.AppProperties;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailMapper {

    private final AppProperties appProperties;

    public MimeMessage createConfirmationEmail(MimeMessage mimeMessage, String to, String token) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String confirmationUrl = appProperties.getConfirmation().getUrl() + token;
            String subject = appProperties.getMail().getRegistrationSubject();
            String emailBody = appProperties.getMail().getRegistrationBody();

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Account Confirmation</title>
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #007bff; color: white; padding: 10px; text-align: center; }
                        .content { margin: 20px 0; }
                        .button { display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; }
                        .footer { font-size: 0.9em; color: #777; text-align: center; margin-top: 20px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h2>Welcome to Task Management System</h2>
                        </div>
                        <div class="content">
                            <p>Hello,</p>
                            <p>%s</p>
                            <p><a href="%s" class="button">Confirm Your Account</a></p>
                            <p>If you did not request this, please ignore this email.</p>
                        </div>
                        <div class="footer">
                            <p>Regards,<br>Task Management System Team</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(emailBody, confirmationUrl);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            return mimeMessage;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create confirmation email: " + e.getMessage(), e);
        }
    }
}