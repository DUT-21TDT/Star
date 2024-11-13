package com.pbl.star.services.domain.impl;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final FollowingRepository followingRepository;

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
}
