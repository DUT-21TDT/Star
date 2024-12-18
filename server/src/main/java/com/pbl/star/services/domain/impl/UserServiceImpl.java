package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.query.user.*;
import com.pbl.star.dtos.request.user.AdminGetUsersParams;
import com.pbl.star.dtos.request.user.ChangePasswordParams;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.dtos.query.user.OnWallProfile;
import com.pbl.star.entities.User;
import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.Gender;
import com.pbl.star.enums.SortDirection;
import com.pbl.star.exceptions.*;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.services.domain.UserService;
import com.pbl.star.utils.ImageUtil;
import com.pbl.star.utils.SliceTransfer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Optional<GeneralInformation> getGeneralInformation(String userId) {
        return userRepository.getGeneralInformationById(userId);
    }

    @Override
    public Slice<OnSearchProfile> searchUsers(String currentUserId, String keyword, int limit, String afterId) {
        List<OnSearchProfile> usersList = userRepository.searchUsers(limit + 1, afterId, currentUserId, keyword);
        return SliceTransfer.trimToSlice(usersList, limit);
    }

    @Override
    public OnWallProfile getProfileOnWall(String currentUserId, String targetUserId) {
        OnWallProfile publicProfileResponse = userRepository.getPublicProfile(currentUserId, targetUserId);

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
    public User updatePersonalInformation(User user, UpdateProfileParams updateProfileParams) {

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
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }

    @Override
    public Page<OnDashboardProfileDTO> getUsersAsAdmin(AdminGetUsersParams params) {

        Pageable pageable = PageRequest.of(params.getPage(), params.getSize());

        String keyword = null;
        if (!StringUtils.isBlank(params.getKeyword())) {
            keyword = params.getKeyword().trim();
        }

        AccountStatus status = null;
        if (params.getStatus() != null) {
            status = AccountStatus.valueOf(params.getStatus());
        }

        String sortBy = params.getSortBy();

        SortDirection direction = params.getDirection().equalsIgnoreCase("asc") ?
                SortDirection.ASC : SortDirection.DESC;

        return userRepository.findUsersAsAdmin(pageable, keyword, status, sortBy, direction);
    }

    @Override
    public void changePassword(String userId, ChangePasswordParams changePasswordParams) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        if (!passwordEncoder.matches(changePasswordParams.getOldPassword(), user.getPassword())) {
            throw new IllegalRequestArgumentException("Old password is incorrect");
        }

        user.setPassword(changePasswordParams.getNewPassword());
        userRepository.save(user);
    }

    @Override
    public void changeUserStatus(String userId, AccountStatus targetStatus) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        if (user.getStatus() == targetStatus) {
            throw new IllegalRequestArgumentException("User is already " + targetStatus);
        }

        if (user.getStatus() == AccountStatus.INACTIVE) {
            throw new IllegalRequestArgumentException("User is inactive");
        }

        user.setStatus(targetStatus);
        userRepository.save(user);
    }
}
