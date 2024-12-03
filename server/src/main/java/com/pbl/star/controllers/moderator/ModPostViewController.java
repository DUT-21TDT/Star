package com.pbl.star.controllers.moderator;

import com.pbl.star.enums.PostStatus;
import com.pbl.star.usecase.moderator.ModeratePostUsecase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moderator")
public class ModPostViewController {

    private final ModeratePostUsecase postManageUsecase;

    @GetMapping("/rooms/{roomId}/posts")
    public ResponseEntity<?> getPostsInRoomAsMod(@PathVariable String roomId,
                                                 @RequestParam(defaultValue = "PENDING") PostStatus status,
                                                 @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
                                                 @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(postManageUsecase.getPostsInRoomAsMod(roomId, status, limit, after));
    }
}
