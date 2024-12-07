package com.pbl.star.dtos.query.user;

import com.pbl.star.enums.SuggestType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OnSuggestionProfile {
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