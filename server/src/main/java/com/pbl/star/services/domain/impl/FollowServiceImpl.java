package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.query.user.OnFollowProfile;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.dtos.response.user.FollowResponse;
import com.pbl.star.entities.Following;
import com.pbl.star.enums.FollowRequestAction;
import com.pbl.star.enums.FollowRequestStatus;
import com.pbl.star.enums.FollowStatus;
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
    public FollowResponse sendFollowRequest(String followerId, String followeeId) {

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

        Following savedFollowing = followingRepository.save(following);
        return FollowResponse.builder()
                .id(savedFollowing.getId())
                .followStatus(savedFollowing.getStatus() == FollowRequestStatus.ACCEPTED ?
                        FollowStatus.FOLLOWING : FollowStatus.REQUESTED
                )
                .build();
    }

    @Override
    @Transactional
    public void updateFollowRequestStatus(String followeeId, String followingId, FollowRequestAction action) {
        Following following = followingRepository.findById(followingId)
                .orElseThrow(() -> new EntityNotFoundException("Following not found"));

        if (!following.getFolloweeId().equals(followeeId)) {
            throw new ResourceOwnershipException("Following does not belong to user");
        }

        if (following.getStatus() != FollowRequestStatus.PENDING) {
            throw new IllegalRequestArgumentException("Cannot update status of non-pending request");
        }

        switch (action) {
            case ACCEPT -> {
                following.setStatus(FollowRequestStatus.ACCEPTED);
                following.setFollowAt(Instant.now());
                followingRepository.save(following);
            }

            case REJECT -> followingRepository.delete(following);

            default -> throw new IllegalRequestArgumentException("Invalid action");
        }
    }

    @Override
    @Transactional
    public void removeFollowRequest(String followerId, String followeeId) {
        Following following = followingRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new EntityNotFoundException("Following not found"));

        followingRepository.delete(following);
    }

    @Override
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

        if (after != null) {
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

        if (after != null) {
            followersPage.setTotalElements(
                    followingRepository.countByFolloweeIdAndStatus(targetUserId, FollowRequestStatus.ACCEPTED).intValue()
            );
        }

        return followersPage;
    }
}
