package com.pbl.star.repositories.extensions;

import com.pbl.star.models.projections.follow.FollowCount;
import com.pbl.star.models.projections.user.OnFollowProfile;
import com.pbl.star.models.projections.user.OnFollowRequestProfile;
import com.pbl.star.models.projections.user.OnSuggestionProfile;
import com.pbl.star.models.projections.user.SuggestionRep;

import java.time.Instant;
import java.util.List;

public interface FollowingRepositoryExtension {
    List<OnFollowProfile> getFollowingsOfUser(int limit, Instant after, String currentUserId, String targetUserId);
    List<OnFollowProfile> getFollowersOfUser(int limit, Instant after, String currentUserId, String targetUserId);
    List<OnFollowRequestProfile> getFollowRequestsOfUser(int limit, Instant after, String userId);
    FollowCount countFollowSection(String currentUserId, String targetUserId);
    List<OnSuggestionProfile> suggestFollow(String currentUserId, int limit, List<Integer> weights);
    List<SuggestionRep> findRepresentationMutualFollowing(String currentUserId, List<String> suggestionIds);
    List<SuggestionRep> findRepresentationMutualFriend(String currentUserId, List<String> suggestionIds);
    List<SuggestionRep> findRepresentationCommonRoom(String currentUserId, List<String> suggestionIds);
}
