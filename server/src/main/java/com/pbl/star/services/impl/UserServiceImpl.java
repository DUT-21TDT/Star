package com.pbl.star.services.impl;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.query.user.PublicProfile;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.dtos.response.user.PublicProfileResponse;
import com.pbl.star.entities.User;
import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.Gender;
import com.pbl.star.enums.UserRole;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.exceptions.IllegalRequestArgumentException;
import com.pbl.star.exceptions.RequiredFieldMissingException;
import com.pbl.star.repositories.UserRepository;
import com.pbl.star.services.UserService;
import com.pbl.star.utils.AuthUtil;
import com.pbl.star.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public GeneralInformation getGeneralInformation(String userId) {
        return userRepository.getGeneralInformationById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }

    @Override
    public PublicProfileResponse getPublicProfile(String userId) {
        PublicProfile publicProfile = userRepository.getPublicProfile(userId);

        if (publicProfile == null) {
            throw new EntityNotFoundException("User not found");
        }

        PublicProfileResponse publicProfileResponse = new PublicProfileResponse();
        publicProfileResponse.setPublicProfile(publicProfile);

        String currentUserId = AuthUtil.getCurrentUser().getId();
        publicProfileResponse.setCurrentUser(userId.equals(currentUserId));

        // TODO: Replace with actual following status
        publicProfileResponse.setFollowing(false);

        return publicProfileResponse;
    }

    @Override
    public Collection<GrantedAuthority> getUserAuthorities(String userId) {
        UserRole role = userRepository.getRoleByUserId(userId);
        return List.of(new SimpleGrantedAuthority(role.name()));
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
    public void updatePersonalInformation(UpdateProfileParams updateProfileParams) {
        User user = userRepository.findById(AuthUtil.getCurrentUser().getId())
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

        userRepository.save(user);
    }

    @Override
    public void removeInactiveAccountByEmail(String email) {
        List<User> inactiveUsers = userRepository.findByEmailAndStatus(email, AccountStatus.INACTIVE);

        if (!inactiveUsers.isEmpty()) {
            userRepository.deleteAll(inactiveUsers);
        }
    }
}
