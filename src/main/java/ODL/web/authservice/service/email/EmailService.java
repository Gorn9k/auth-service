package ODL.web.authservice.service.email;

import static ODL.web.authservice.service.email.EmailType.CONFIRM_EMAIL;
import static ODL.web.authservice.service.email.EmailType.GENERATE_ACCOUNT;
import static ODL.web.authservice.service.email.EmailType.RESET_PASSWORD;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ODL.web.authservice.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Locale EMAIL_LOCALE = new Locale("ru");
    private static final Map<EmailType, String> EMAIL_TEMPLATES;

    private static final String BUTTON_REGISTRATION = "templates.thymeleaf/email/images/button-registration.png";
    private static final String VSTU_LOGO = "templates.thymeleaf/email/images/vstu-logo.png";
    private static final String RESET_PASSWORD_BUTTON = "templates.thymeleaf/email/images/button-reset.png";

    private static final String PNG_MIME = "image/png";

    static {
        EMAIL_TEMPLATES = new HashMap<>();
        EMAIL_TEMPLATES.put(RESET_PASSWORD, "reset-password");
        EMAIL_TEMPLATES.put(CONFIRM_EMAIL, "confirm-email");
        EMAIL_TEMPLATES.put(GENERATE_ACCOUNT, "generate-account");
    }

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${mail.confirm.path}")
    private String confirmPath;

    @Value("${mail.reset-password.path}")
    private String resetPasswordPath;

    private final JavaMailSender mailSender;
    private final ThymeleafTemplateService thymeleafTemplateService;

    /**
     * Confirm user email
     * 
     * @param email user email for send
     * @param token user token (token has exist)
     */
    @Async
    public void confirmEmail(String email, String token) {
        String subject = "[VSTU][Confirm your registration]";
        Map<String, Object> model = new HashMap<>();
        model.put("link", confirmPath.concat(token));
        sendImmediately(EmailType.CONFIRM_EMAIL, email, subject, model);
    }

    /**
     * Reset user password
     * 
     * @param email user email for send
     * @param token user token (token has exist)
     */
    @Async
    public void resetPassword(String email, String token) {
        String subject = "[VSTU][Reset password message]";
        Map<String, Object> model = new HashMap<>();
        model.put("link", resetPasswordPath.concat(token));
        sendImmediately(EmailType.RESET_PASSWORD, email, subject, model);
    }

    /**
     * Generate new user
     * 
     * @param email    user email
     * @param password user password
     */
    @Async
    public void generateAccount(String email, String password) {
        String subject = "[VSTU][Registration letter]";
        Map<String, Object> model = new HashMap<>();
        model.put("password", password);
        sendImmediately(EmailType.GENERATE_ACCOUNT, email, subject, model);
    }

    /**
     * Send email
     * 
     * @param emailType type of email
     * @param email     user email for send
     * @param subject   email subjet
     * @param model     data for thymeleaf template engine
     */
    private void sendImmediately(EmailType emailType, String email, String subject, Map<String, Object> model) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            msg.setFrom(emailFrom);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            msg.setSubject(subject);

            MimeMessageHelper helper = new MimeMessageHelper(msg, true, StandardCharsets.UTF_8.name());
            String text = thymeleafTemplateService.process(EMAIL_TEMPLATES.get(emailType), EMAIL_LOCALE, model);
            helper.setText(text, true);

            helper.addInline("register-button", new ClassPathResource(BUTTON_REGISTRATION), PNG_MIME);
            helper.addInline("reset-button", new ClassPathResource(RESET_PASSWORD_BUTTON), PNG_MIME);
            helper.addInline("vstu-logo", new ClassPathResource(VSTU_LOGO), PNG_MIME);

            mailSender.send(msg);
        } catch (MessagingException ex) {
            throw new BusinessException("Email can't be send. Smth wrong!");
        }
    }

}
