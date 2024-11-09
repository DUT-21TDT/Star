package com.pbl.star.controllers.moderator;

import com.pbl.star.enums.PostStatus;
import com.pbl.star.usecase.PostManageUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moderator")
public class ModPostViewController {

    private final PostManageUsecase postManageUsecase;

    @GetMapping("/rooms/{roomId}/posts")
    public ResponseEntity<?> getPostsInRoomAsMod(@PathVariable String roomId,
                                                 @RequestParam(defaultValue = "PENDING") PostStatus status,
                                                 @RequestParam(defaultValue = "20") int limit,
                                                 @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(postManageUsecase.getPostsInRoomAsMod(roomId, status, limit, after));
    }
}
