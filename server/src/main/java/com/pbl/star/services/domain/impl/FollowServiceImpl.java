package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.query.follow.FollowSectionCount;
import com.pbl.star.dtos.query.user.OnFollowProfile;
import com.pbl.star.dtos.query.user.OnFollowRequestProfile;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.entities.Following;
import com.pbl.star.enums.FollowRequestAction;
import com.pbl.star.enums.FollowRequestStatus;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.exceptions.IllegalRequestArgumentException;
import com.pbl.star.exceptions.ResourceOwnershipException;
import com.pbl.star.repositories.FollowingRepository;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.services.domain.FollowService;
import com.pbl.star.services.helper.ResourceAccessControl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

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
    public CustomSlice<OnFollowProfile> getFollowingsOfUser(String currentUserId, String targetUserId, int limit, Instant after) {

        if (resourceAccessControl.isPrivateProfileBlock(currentUserId, targetUserId)) {
            throw new ResourceOwnershipException("User has private profile");
        }

        Pageable pageable = PageRequest.of(0, limit);
        Slice<OnFollowProfile> followings = followingRepository.getFollowingsOfUser(pageable, after, currentUserId, targetUserId);

        CustomSlice<OnFollowProfile> followingsPage = new CustomSlice<>(followings);

        if (after == null) {
            followingsPage.setTotalElements(
                    followingRepository.countByFollowerIdAndStatus(targetUserId, FollowRequestStatus.ACCEPTED).intValue()
            );
        }

        return followingsPage;
    }

    @Override
    public CustomSlice<OnFollowProfile> getFollowersOfUser(String currentUserId, String targetUserId, int limit, Instant after) {

        if (resourceAccessControl.isPrivateProfileBlock(currentUserId, targetUserId)) {
            throw new ResourceOwnershipException("User has private profile");
        }

        Pageable pageable = PageRequest.of(0, limit);
        Slice<OnFollowProfile> followers = followingRepository.getFollowersOfUser(pageable, after, currentUserId, targetUserId);

        CustomSlice<OnFollowProfile> followersPage = new CustomSlice<>(followers);

        if (after == null) {
            followersPage.setTotalElements(
                    followingRepository.countByFolloweeIdAndStatus(targetUserId, FollowRequestStatus.ACCEPTED).intValue()
            );
        }

        return followersPage;
    }

    @Override
    public CustomSlice<OnFollowRequestProfile> getFollowRequestsOfUser(String userId, int limit, Instant after) {

        Pageable pageable = PageRequest.of(0, limit);
        Slice<OnFollowRequestProfile> requests = followingRepository.getFollowRequestsOfUser(pageable, after, userId);

        CustomSlice<OnFollowRequestProfile> requestsPage = new CustomSlice<>(requests);

        if (after == null) {
            requestsPage.setTotalElements(
                    followingRepository.countByFolloweeIdAndStatus(userId, FollowRequestStatus.PENDING).intValue()
            );
        }

        return requestsPage;
    }

    @Override
    public FollowSectionCount countFollowSection(String currentUserId, String targetUserId) {

        if (resourceAccessControl.isPrivateProfileBlock(currentUserId, targetUserId)) {
            throw new ResourceOwnershipException("User has private profile");
        }

        return followingRepository.countFollowSection(currentUserId, targetUserId);
    }
}
