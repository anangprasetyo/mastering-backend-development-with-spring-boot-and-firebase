package id.backend.session_5.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import id.backend.session_5.model.UserToken;
import id.backend.session_5.repository.UserTokenRepository;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

        @Autowired
        private UserTokenRepository userTokenRepository;

        public String sendNotification(int userId, String title, String body) throws FirebaseMessagingException {
                Optional<UserToken> tokenOpt = userTokenRepository.findByUserId(userId);
                if (tokenOpt.isPresent()) {
                        Notification notification = Notification.builder()
                                        .setTitle(title)
                                        .setBody(body)
                                        .build();

                        Message message = Message.builder()
                                        .setToken(tokenOpt.get().getToken())
                                        .setNotification(notification)
                                        .build();
                        return FirebaseMessaging.getInstance().send(message);
                }
                return "Token not found for user: " + userId;
        }

        public void subscribeToTopic(int userId, String topic) throws FirebaseMessagingException {
                Optional<UserToken> tokenOpt = userTokenRepository.findByUserId(userId);
                if (tokenOpt.isPresent()) {
                        FirebaseMessaging.getInstance().subscribeToTopic(
                                        Collections.singletonList(tokenOpt.get().getToken()),
                                        topic);
                }
        }

        public String sendNotificationToTopic(String topic, String title, String body)
                        throws FirebaseMessagingException {
                Message message = Message.builder()
                                .setTopic(topic)
                                .setNotification(Notification.builder()
                                                .setTitle(title)
                                                .setBody(body)
                                                .build())
                                .build();
                return FirebaseMessaging.getInstance().send(message);
        }
}