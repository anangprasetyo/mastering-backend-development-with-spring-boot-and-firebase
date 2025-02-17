package id.backend.session5_2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import id.backend.session5_2.dto.NotificationRequest;
import id.backend.session5_2.dto.NotificationTopicRequest;
import id.backend.session5_2.service.NotificationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> sendNotification(@Valid @RequestBody NotificationRequest request) {
        try {
            String response = notificationService.sendNotification(request.getStudent_id(), request.getTitle(),
                    request.getBody());
            return ResponseEntity.ok("Notification sent: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending notification: " + e.getMessage());
        }
    }

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> subscribeToTopic(@RequestParam int student_id, @RequestParam String topic) {
        try {
            notificationService.subscribeToTopic(student_id, topic);
            return ResponseEntity.ok("Subscribed to topic: " + topic);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error subscribing: " + e.getMessage());
        }
    }

    @PostMapping("/topic")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> sendToTopic(@Valid @RequestBody NotificationTopicRequest request) {
        try {
            String response = notificationService.sendNotificationToTopic(request.getTopic(), request.getTitle(),
                    request.getBody());
            return ResponseEntity.ok("Notification sent to topic: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending notification: " + e.getMessage());
        }
    }
}