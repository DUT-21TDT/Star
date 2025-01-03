package com.pbl.star.controllers.enduser;

import com.pbl.star.services.external.SSEManager;
import com.pbl.star.usecase.enduser.ManageNotificationUsecase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

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

    @PatchMapping("/{notificationId}/read")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> markAsRead(@PathVariable String notificationId) {
        manageNotificationUsecase.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/sse-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> streamNotifications(@RequestParam String userId) {
        Flux<String> stream = sseManager.getUserFlux(userId)
                .doFinally(signalType -> {
                    if (signalType == SignalType.CANCEL) {
                        sseManager.removeUser(userId);
                    }
                });

        return ResponseEntity.ok()
                .header("Transfer-Encoding", "identity") // Đảm bảo Transfer-Encoding là identity
                .header("Cache-Control", "no-store") // Ngăn caching, phù hợp cho SSE
                .body(stream);
    }
}
