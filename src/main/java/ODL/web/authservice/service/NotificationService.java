package ODL.web.authservice.service;

import org.springframework.stereotype.Service;

import ODL.web.authservice.entity.UserToken;
import ODL.web.authservice.service.email.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

//TODO: Create a notification service for that. Use Kafka or HTTP calls for integration
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {

    EmailService mailService;

    public void userRegistered(UserToken userToken) {
        mailService.confirmEmail(userToken.getUser().getEmail(), userToken.getToken());
    }

    public void userResetPassword(UserToken userToken) {
        mailService.resetPassword(userToken.getUser().getEmail(), userToken.getToken());
    }

    public void userRegisteredWithGeneratedPassword(String email, String password) {
        mailService.generateAccount(email, password);
    }
}
