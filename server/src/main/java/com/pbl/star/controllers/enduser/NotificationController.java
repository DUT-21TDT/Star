package com.pbl.star.controllers.enduser;

import com.pbl.star.usecase.enduser.ManageNotificationUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final ManageNotificationUsecase manageNotificationUsecase;
    @GetMapping
    public ResponseEntity<?> getNotifications(@RequestParam int limit, @RequestParam Instant after) {
        return ResponseEntity.ok(manageNotificationUsecase.getNotifications(limit, after));
    }
}
