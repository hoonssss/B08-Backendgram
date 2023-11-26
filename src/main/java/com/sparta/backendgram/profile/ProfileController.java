package com.sparta.backendgram.profile;

import com.sparta.backendgram.user.*;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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



}
