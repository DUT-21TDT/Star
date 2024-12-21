package com.pbl.star.dtos.response.user;

import com.pbl.star.enums.SuggestType;
import com.pbl.star.models.projections.user.MutualRelation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OnSuggestionProfileResponse {
    private String userId;
    private String username;
    private String avatarUrl;
    private String firstName;
    private String lastName;

    // Metadata
    private MutualRelation commonRoomRelation;
    private MutualRelation mutualFollowingRelation;
    private MutualRelation mutualFriendRelation;

    private SuggestType suggestType;

    private Integer totalRelationScore;
}
