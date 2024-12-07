package com.pbl.star.controllers.enduser;

import com.pbl.star.usecase.enduser.GetSuggestionsUsecase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/suggestions")
@RequiredArgsConstructor
public class SuggestionController {

    private final GetSuggestionsUsecase getSuggestionsUsecase;

    @GetMapping("/people")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getFollowSuggestions(@RequestParam(defaultValue = "5") @Min(1) @Max(50) int limit) {
        return ResponseEntity.ok(getSuggestionsUsecase.getFollowSuggestions(limit));
    }
}
