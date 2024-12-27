package com.pbl.star.controllers.enduser;

import com.pbl.star.services.external.SSEManager;
import com.pbl.star.usecase.enduser.ManageNotificationUsecase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Instant;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final ManageNotificationUsecase manageNotificationUsecase;

    private final SSEManager sseManager;
    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getNotifications(@RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
                                              @RequestParam(required = false) Instant after
    ) {
        return ResponseEntity.ok(manageNotificationUsecase.getNotifications(limit, after));
    }

    @GetMapping(value = "/sse-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamNotifications(@RequestParam String userId) {
        return sseManager.getUserSink(userId).asFlux();
    }
}
