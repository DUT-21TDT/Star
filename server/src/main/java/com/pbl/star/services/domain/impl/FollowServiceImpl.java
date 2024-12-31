package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.response.PaginationSlice;
import com.pbl.star.enums.FollowRequestAction;
import com.pbl.star.enums.FollowRequestStatus;
import com.pbl.star.enums.SuggestType;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.exceptions.IllegalRequestArgumentException;
import com.pbl.star.exceptions.ResourceOwnershipException;
import com.pbl.star.models.entities.Following;
import com.pbl.star.models.projections.follow.FollowCount;
import com.pbl.star.models.projections.user.*;
import com.pbl.star.repositories.FollowingRepository;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.services.domain.FollowService;
import com.pbl.star.services.helper.ResourceAccessControl;
import com.pbl.star.utils.RandomSuggest;
import com.pbl.star.utils.SliceTransfer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final FollowingRepository followingRepository;

    private final ResourceAccessControl resourceAccessControl;

    @Override
    @Transactional
    public Following sendFollowRequest(String followerId, String followeeId) {

        if (followerId.equals(followeeId)) {
            throw new IllegalRequestArgumentException("Cannot follow self");
        }

        boolean followeePrivateProfile = userRepository.getPrivateProfileById(followeeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (followingRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            throw new EntityConflictException("Already following or requested");
        }

        Following following = Following.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .status(followeePrivateProfile ? FollowRequestStatus.PENDING : FollowRequestStatus.ACCEPTED)
                .followAt(Instant.now())
                .build();

        return followingRepository.save(following);
    }

    @Override
    @Transactional
    public Following updateFollowRequestStatus(String followeeId, String followingId, FollowRequestAction action) {
        Following following = followingRepository.findById(followingId)
                .orElseThrow(() -> new EntityNotFoundException("Following not found"));

        if (!following.getFolloweeId().equals(followeeId)) {
            throw new ResourceOwnershipException("Following does not belong to user");
        }

        if (following.getStatus() != FollowRequestStatus.PENDING) {
            throw new IllegalRequestArgumentException("Cannot update status of non-pending request");
        }

        Following savedFollowing;

        switch (action) {
            case ACCEPT -> {
                following.setStatus(FollowRequestStatus.ACCEPTED);
                following.setFollowAt(Instant.now());
                savedFollowing = followingRepository.save(following);
            }

            case REJECT -> {
                followingRepository.delete(following);
                savedFollowing = null;
            }

            default -> throw new IllegalRequestArgumentException("Invalid action");
        }

        return savedFollowing;
    }

    @Override
    @Transactional
    public Following removeFollowing(String followerId, String followeeId) {
        Following following = followingRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new EntityNotFoundException("Following not found"));

        followingRepository.delete(following);
        return following;
    }

    @Override
    @Transactional
    public void acceptAllFollowRequests(String userId) {
        followingRepository.updateAllPendingRequestsToAccepted(userId);
    }

    @Override
    public PaginationSlice<OnFollowProfile> getFollowingsOfUser(String currentUserId, String targetUserId, int limit, Instant after) {

        if (resourceAccessControl.isPrivateProfileBlock(currentUserId, targetUserId)) {
            throw new ResourceOwnershipException("User has private profile");
        }

        List<OnFollowProfile> followingsList = followingRepository.getFollowingsOfUser(limit + 1, after, currentUserId, targetUserId);
        Slice<OnFollowProfile> followings = SliceTransfer.trimToSlice(followingsList, limit);
        PaginationSlice<OnFollowProfile> followingsPage = new PaginationSlice<>(followings);

        if (after == null) {
            followingsPage.setTotalElements(
                    followingRepository.countByFollowerIdAndStatus(targetUserId, FollowRequestStatus.ACCEPTED).intValue()
            );
        }

        return followingsPage;
    }

    @Override
    public PaginationSlice<OnFollowProfile> getFollowersOfUser(String currentUserId, String targetUserId, int limit, Instant after) {

        if (resourceAccessControl.isPrivateProfileBlock(currentUserId, targetUserId)) {
            throw new ResourceOwnershipException("User has private profile");
        }

        List<OnFollowProfile> followersList = followingRepository.getFollowersOfUser(limit + 1, after, currentUserId, targetUserId);
        Slice<OnFollowProfile> followers = SliceTransfer.trimToSlice(followersList, limit);
        PaginationSlice<OnFollowProfile> followersPage = new PaginationSlice<>(followers);

        if (after == null) {
            followersPage.setTotalElements(
                    followingRepository.countByFolloweeIdAndStatus(targetUserId, FollowRequestStatus.ACCEPTED).intValue()
            );
        }

        return followersPage;
    }

    @Override
    public PaginationSlice<OnFollowRequestProfile> getFollowRequestsOfUser(String userId, int limit, Instant after) {

        List<OnFollowRequestProfile> requestsList = followingRepository.getFollowRequestsOfUser(limit + 1, after, userId);
        Slice<OnFollowRequestProfile> requests = SliceTransfer.trimToSlice(requestsList, limit);
        PaginationSlice<OnFollowRequestProfile> requestsPage = new PaginationSlice<>(requests);

        if (after == null) {
            requestsPage.setTotalElements(
                followingRepository.countByFolloweeIdAndStatus(userId, FollowRequestStatus.PENDING).intValue()
            );
        }

        return requestsPage;
    }

    @Override
    public FollowCount countFollowSection(String currentUserId, String targetUserId) {

        if (resourceAccessControl.isPrivateProfileBlock(currentUserId, targetUserId)) {
            throw new ResourceOwnershipException("User has private profile");
        }

        return followingRepository.countFollowSection(currentUserId, targetUserId);
    }

    @Override
    public List<OnSuggestionProfile> suggestFollow(String currentUserId, int limit) {
        // Suggest pool size is 4 times the limit
        int suggestsPoolSize = limit * 4;
        // Weights are used to determine the priority of each suggestion type
        List<Integer> weights = generateWeights(3, 5, 8, 13);
        List<OnSuggestionProfile> suggestionsList = followingRepository.suggestFollow(currentUserId, suggestsPoolSize, weights);

        // Find and set representatives for each suggestion
        setRepresentatives(currentUserId, suggestionsList);

        // Return a list of suggestions with the given limit, using weighted random selection
        return RandomSuggest.weightedRandom(suggestionsList, limit);
    }

    private List<Integer> generateWeights(Integer... weights) {
        List<Integer> weightsValue = Arrays.asList(weights);
        Collections.shuffle(weightsValue);
        return weightsValue;
    }

    private void setRepresentatives(String currentUserId, List<OnSuggestionProfile> suggestionsList) {

        List<OnSuggestionProfile> mutualFriendRelation = new ArrayList<>();
        List<OnSuggestionProfile> mutualFollowingRelation = new ArrayList<>();
        List<OnSuggestionProfile> commonRoomRelation = new ArrayList<>();

        for (OnSuggestionProfile suggestion : suggestionsList) {

            if (findMaxSuggestionsType(suggestion) == SuggestType.COMMON_ROOM) {
                suggestion.setSuggestType(SuggestType.COMMON_ROOM);
                commonRoomRelation.add(suggestion);
            } else if (findMaxSuggestionsType(suggestion) == SuggestType.MUTUAL_FOLLOWING) {
                suggestion.setSuggestType(SuggestType.MUTUAL_FOLLOWING);
                mutualFollowingRelation.add(suggestion);
            } else {
                suggestion.setSuggestType(SuggestType.MUTUAL_FRIEND);
                mutualFriendRelation.add(suggestion);
            }
        }

        List<SuggestionRep> mutualFriendReps = followingRepository.findRepresentationMutualFriend(currentUserId, mutualFriendRelation.stream().map(OnSuggestionProfile::getUserId).toList());
        List<SuggestionRep> mutualFollowingReps = followingRepository.findRepresentationMutualFollowing(currentUserId, mutualFollowingRelation.stream().map(OnSuggestionProfile::getUserId).toList());
        List<SuggestionRep> commonRoomReps = followingRepository.findRepresentationCommonRoom(currentUserId, commonRoomRelation.stream().map(OnSuggestionProfile::getUserId).toList());

        mapRepresentative(mutualFriendRelation, mutualFriendReps, SuggestType.MUTUAL_FRIEND);
        mapRepresentative(mutualFollowingRelation, mutualFollowingReps, SuggestType.MUTUAL_FOLLOWING);
        mapRepresentative(commonRoomRelation, commonRoomReps, SuggestType.COMMON_ROOM);
    }

    private SuggestType findMaxSuggestionsType(OnSuggestionProfile suggestion) {

        int commonRoomScore = suggestion.getCommonRoomRelation().getScore();
        int mutualFollowingScore = suggestion.getMutualFollowingRelation().getScore();
        int mutualFriendScore = suggestion.getMutualFriendRelation().getScore();

        if (commonRoomScore > mutualFollowingScore && commonRoomScore > mutualFriendScore) {
            return SuggestType.COMMON_ROOM;
        } else if (mutualFollowingScore > commonRoomScore && mutualFollowingScore > mutualFriendScore) {
            return SuggestType.MUTUAL_FOLLOWING;
        } else {
            return SuggestType.MUTUAL_FRIEND;
        }
    }

    private void mapRepresentative(List<OnSuggestionProfile> suggestionsList, List<SuggestionRep> reps, SuggestType suggestType) {
        for (OnSuggestionProfile suggestion : suggestionsList) {
            SuggestionRep rep = reps.stream().filter(
                    suggestionRep -> suggestionRep.getId().equals(suggestion.getUserId())
            ).findFirst().orElseThrow(() -> new EntityNotFoundException("Suggestion representation not found"));

            MutualRelation mutualRelation = switch (suggestType) {
                case COMMON_ROOM -> suggestion.getCommonRoomRelation();
                case MUTUAL_FOLLOWING -> suggestion.getMutualFollowingRelation();
                case MUTUAL_FRIEND -> suggestion.getMutualFriendRelation();
            };

            mutualRelation.setRepId(rep.getRepId());
            mutualRelation.setRepName(rep.getRepName());
        }
    }
}
