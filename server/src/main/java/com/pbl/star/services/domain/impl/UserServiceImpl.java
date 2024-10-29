package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.dtos.response.user.FollowResponse;
import com.pbl.star.dtos.response.user.PublicProfileResponse;
import com.pbl.star.entities.Following;
import com.pbl.star.entities.User;
import com.pbl.star.enums.FollowRequestAction;
import com.pbl.star.enums.FollowRequestStatus;
import com.pbl.star.enums.FollowStatus;
import com.pbl.star.enums.Gender;
import com.pbl.star.exceptions.*;
import com.pbl.star.repositories.FollowingRepository;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.services.domain.UserService;
import com.pbl.star.utils.AuthUtil;
import com.pbl.star.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FollowingRepository followingRepository;

    @Override
    public Optional<GeneralInformation> getGeneralInformation(String userId) {
        return userRepository.getGeneralInformationById(userId);
    }

    @Override
    public PublicProfileResponse getPublicProfile(String userId) {
        String currentUserId = AuthUtil.getCurrentUser().getId();

        PublicProfileResponse publicProfileResponse = userRepository.getPublicProfile(currentUserId, userId);

        if (publicProfileResponse == null) {
            throw new EntityNotFoundException("User not found");
        }

        return publicProfileResponse;
    }

    @Override
    public PersonalInformation getPersonalInformation(String userId) {
        PersonalInformation personalInformation = userRepository.getPersonalInformation(userId);
        if (personalInformation == null) {
            throw new EntityNotFoundException("User not found");
        }

        return personalInformation;
    }

    @Override
    @Transactional
    public User updatePersonalInformation(String userId, UpdateProfileParams updateProfileParams) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (StringUtils.isBlank(updateProfileParams.getUsername())) {
            throw new RequiredFieldMissingException("Username cannot be empty");
        }

        if (!user.getUsername().equals(updateProfileParams.getUsername())) {
            if (userRepository.existsByUsername(updateProfileParams.getUsername())) {
                throw new EntityConflictException("Username already exists");
            }

            user.setUsername(updateProfileParams.getUsername());
        }

        user.setFirstName(
                StringUtils.isNotBlank(updateProfileParams.getFirstName()) ? updateProfileParams.getFirstName() : null
        );

        user.setLastName(
                StringUtils.isNotBlank(updateProfileParams.getLastName()) ? updateProfileParams.getLastName() : null
        );

        user.setBio(
                StringUtils.isNotBlank(updateProfileParams.getBio()) ? updateProfileParams.getBio() : null
        );

        String imagePrefixUrl = ImageUtil.getImagePrefixUrl();
        user.setAvatarUrl(
                StringUtils.isNotBlank(updateProfileParams.getAvatarFileName()) ?
                        imagePrefixUrl + updateProfileParams.getAvatarFileName() : null
        );

        user.setPrivateProfile(updateProfileParams.isPrivateProfile());

        try {
            user.setGender(Optional.ofNullable(updateProfileParams.getGender())
                    .filter(StringUtils::isNotEmpty)
                    .map(Gender::valueOf)
                    .orElse(null));
        } catch (IllegalArgumentException e) {
            throw new IllegalRequestArgumentException("Gender is invalid");
        }

        try {
            user.setDateOfBirth(Optional.ofNullable(updateProfileParams.getDateOfBirth())
                    .filter(StringUtils::isNotEmpty)
                    .map(date -> LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .orElse(null));
        } catch (DateTimeParseException e) {
            throw new IllegalRequestArgumentException("Date of birth is invalid");
        }

        return userRepository.save(user);
    }

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
