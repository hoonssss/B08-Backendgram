package com.sparta.backendgram.profile.service;

import com.sparta.backendgram.profile.dto.ProfileResponseDto;
import com.sparta.backendgram.profile.entity.Profile;
import com.sparta.backendgram.profile.repository.ProfileRepository;
import com.sparta.backendgram.user.User;
import com.sparta.backendgram.user.UserRepository;
import lombok.RequiredArgsConstructor;
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

    private final String profileImageDirectory = "your/profile/image/directory";

    public ProfileResponseDto getProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("유저 오류"));

        Profile profile = user.getProfile();
        if (profile == null) {
            return new ProfileResponseDto();
        }

        // 이미지 URL 생성
        String profileImageURL = profile.getProfileImage() != null ?
                ProfileResponseDto.createWithImageURL(user.getId(), profile.getIntroduction(), profile.getProfileImage()).getProfileImageURL() :
                null;

        return new ProfileResponseDto(user.getId(), profile.getIntroduction(), profileImageURL);
    }

    public void createProfile(Long userId, String introduction) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("유저 오류"));

        Profile profile = getProfile(user);
        profile.setIntroduction(introduction);

        userRepository.save(user);
    }

    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("유저 오류"));

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }


    public void updateProfileImage(Long userId, MultipartFile profileImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("유저 오류"));

        Profile profile = getProfile(user);

        String imageName = saveProfileImage(profileImage);
        profile.setProfileImage(imageName);

        userRepository.save(user);
    }

    private String saveProfileImage(MultipartFile profileImage) {
        String imageName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();
        try {
            Files.copy(profileImage.getInputStream(), Paths.get(profileImageDirectory, imageName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 저장 실패", e);
        }
        return imageName;
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