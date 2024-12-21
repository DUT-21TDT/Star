package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.response.user.OnSuggestionProfileResponse;
import com.pbl.star.mapper.user.UserDTOMapper;
import com.pbl.star.models.projections.user.OnSuggestionProfile;
import com.pbl.star.services.domain.FollowService;
import com.pbl.star.usecase.enduser.GetSuggestionsUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetSuggestionsUsecaseImpl implements GetSuggestionsUsecase {
    private final FollowService followService;

    private final UserDTOMapper mapper;

    @Override
    public List<OnSuggestionProfileResponse> getFollowSuggestions(int limit) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return followService.suggestFollow(currentUserId, limit)
                .stream().map(mapper::toDTO).toList();
    }
}
