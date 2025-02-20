package id.project.education.service;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public String sendNotification(String token, String title, String body) throws FirebaseMessagingException {
        if (token != null) {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();
            return FirebaseMessaging.getInstance().send(message);
        }
        return "Token not found";
    }
}
