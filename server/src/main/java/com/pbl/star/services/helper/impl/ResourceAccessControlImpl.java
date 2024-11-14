package com.pbl.star.services.helper.impl;

import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.repositories.FollowingRepository;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.services.helper.ResourceAccessControl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceAccessControlImpl implements ResourceAccessControl {

    private final UserRepository userRepository;
    private final FollowingRepository followingRepository;

    @Override
    public boolean isPrivateProfileBlock(String currentUserId, String targetUserId) {

        if (currentUserId.equals(targetUserId)) {
            return false;
        }

        if (!userRepository.getPrivateProfileById(targetUserId).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        )) {
            return false;
        }

        if (followingRepository.isFollowing(currentUserId, targetUserId)) {
            return false;
        }

        return true;
    }
}
