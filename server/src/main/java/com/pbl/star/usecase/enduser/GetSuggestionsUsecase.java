package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.query.user.OnSuggestionProfile;

import java.util.List;

public interface GetSuggestionsUsecase {
    List<OnSuggestionProfile> getFollowSuggestions(int limit);
}
