package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.response.user.OnSuggestionProfileResponse;
import java.util.List;

public interface GetSuggestionsUsecase {
    List<OnSuggestionProfileResponse> getFollowSuggestions(int limit);
}
