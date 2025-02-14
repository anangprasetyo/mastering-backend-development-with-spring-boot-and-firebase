package id.backend.session_5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.backend.session_5.model.NotificationRequest;
import id.backend.session_5.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        try {
            String response = notificationService.sendNotification(notificationRequest.getToken(),
                    notificationRequest.getTitle(),
                    notificationRequest.getBody());
            return ResponseEntity.ok("Successfully sent notification: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send notification: " + e.getMessage());
        }
    }

    @PostMapping("/send/message")
    public ResponseEntity<String> sendMessage(@RequestBody NotificationRequest notificationRequest) {
        try {
            String response = notificationService.sendToTopic(notificationRequest.getTitle(),
                    notificationRequest.getBody(), notificationRequest.getTopic());
            return ResponseEntity.ok("Successfully sent message: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send message: " + e.getMessage());
        }
    }
}