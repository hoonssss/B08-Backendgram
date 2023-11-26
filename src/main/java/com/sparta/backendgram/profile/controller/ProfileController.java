package com.sparta.backendgram.profile.controller;


import com.sparta.backendgram.profile.dto.ProfileResponseDto;
import com.sparta.backendgram.profile.service.ProfileService;
import com.sparta.backendgram.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Long userId = userDetails.getUser().getId();
            ProfileResponseDto profileRequestDto = profileService.getProfileByUserId(userId);
            return ResponseEntity.ok(profileRequestDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create/introduction")
    public ResponseEntity<String> createIntroduction(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestParam String introduction) {
        try {
            Long userId = userDetails.getUser().getId();
            profileService.createProfile(userId, introduction);
            return ResponseEntity.ok("Updated introduction successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/password")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String newPassword) {
        try {
            Long userId = userDetails.getUser().getId();
            profileService.updatePassword(userId, newPassword);
            return ResponseEntity.ok("성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update/profile/image")
    public ResponseEntity<String> updateProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam MultipartFile profileImage) {
        try {
            Long userId = userDetails.getUser().getId();
            profileService.updateProfileImage(userId, profileImage);
            return ResponseEntity.ok("성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
