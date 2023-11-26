package com.sparta.backendgram.profile;

import com.sparta.backendgram.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private String username;
    private String content;

    @Builder
    private ProfileResponseDto(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public static ProfileResponseDto of(User user) {
        return ProfileResponseDto.builder()
            .username(user.getUsername())
            .content(user.getContent())
            .build();
    }

}
