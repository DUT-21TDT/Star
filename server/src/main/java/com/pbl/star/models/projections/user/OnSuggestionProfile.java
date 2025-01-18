package com.pbl.star.models.projections.user;

import com.pbl.star.enums.SuggestType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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