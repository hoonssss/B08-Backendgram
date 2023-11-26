package com.sparta.backendgram.profile;

import com.sparta.backendgram.user.*;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class ProfileController {

    private final UserService userService;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long userId) {
        ProfileResponseDto userResponseDto = userService.getProfile(userId);
        return ResponseEntity.ok().body(userResponseDto);
    }
    @PatchMapping("/profile/{userId}")
    public ResponseEntity<ProfileResponseDto> updateProfile(
            @PathVariable Long userId,
            @RequestBody ProfileUpdateProfileRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileResponseDto userResponseDto = userService.updateProfile(userId, requestDto,
                userDetails.getUser());
        return ResponseEntity.status(201).body(userResponseDto);
    }

    @PutMapping("/password/{userId}")
    public ResponseEntity<ProfileModifyPasswordResponse> modifyPassword(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileModifyPasswordRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileModifyPasswordResponse userResponseDto = userService.modifyPassword(userId, requestDto,
                userDetails.getUser());
        return ResponseEntity.ok().body(userResponseDto);
    }


}
