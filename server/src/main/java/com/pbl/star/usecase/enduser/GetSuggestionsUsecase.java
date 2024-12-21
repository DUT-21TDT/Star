package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.response.user.OnSuggestionProfileResponse;
import com.pbl.star.models.projections.user.OnSuggestionProfile;

import java.util.List;

public interface GetSuggestionsUsecase {
    List<OnSuggestionProfileResponse> getFollowSuggestions(int limit);
}
