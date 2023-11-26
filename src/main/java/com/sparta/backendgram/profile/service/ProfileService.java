package com.sparta.backendgram.profile.service;

import com.sparta.backendgram.profile.dto.ProfileRequestDto;
import com.sparta.backendgram.profile.dto.ProfileResponseDto;
import com.sparta.backendgram.profile.entity.Profile;
import com.sparta.backendgram.profile.repository.ProfileRepository;
import com.sparta.backendgram.user.User;
import com.sparta.backendgram.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${profile.image.directory}")
    private String profileImageDirectory;

    public ProfileResponseDto getProfileByUserId(Long userId) {
        User user = getUser(userId);

        Profile profile = user.getProfile();
        if (profile == null) {
            return new ProfileResponseDto();
        }

        String profileImageURL = profile.getProfileImage() != null ?
                ProfileResponseDto.createWithImageURL(user.getId(), profile.getTitle(), profile.getIntroduction(), profile.getProfileImage()).getProfileImageURL() :
                null;

        return new ProfileResponseDto(user.getId(), profile.getTitle(), profile.getIntroduction(), profileImageURL);
    }

    public void createProfile(Long userId, ProfileRequestDto profileRequestDto) {
        User user = getUser(userId);
        Profile profile = getProfile(user);

        profile.setTitle(profileRequestDto.getTitle());
        profile.setIntroduction(profileRequestDto.getIntroduction());

        profileRepository.save(profile);
        userRepository.save(user);
    }

    public void updatePassword(Long userId, String currentPassword, String newPassword) {
        User user = getUser(userId);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            System.out.println("현재 비밀번호 : " + currentPassword);
            System.out.println("저장된 비밀번호 : " + user.getPassword());
            throw new RuntimeException("암호 오류");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void updateProfileImage(Long userId, MultipartFile profileImage) {
        User user = getUser(userId);

        Profile profile = getProfile(user);

        String imageName = saveProfileImage(profileImage);
        profile.setProfileImage(imageName);

        profileRepository.save(profile);
        userRepository.save(user);
    }

    //디렉터리에 저장
    private String saveProfileImage(MultipartFile profileImage) {
        String imageName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();
        try {
            Files.copy(profileImage.getInputStream(), Paths.get(profileImageDirectory, imageName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("실패", e);
        }

        return imageName;
    }

    private User getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("실"));
        return user;
    }

    private static Profile getProfile(User user) {
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
            user.setProfile(profile);
        }
        return profile;
    }
}