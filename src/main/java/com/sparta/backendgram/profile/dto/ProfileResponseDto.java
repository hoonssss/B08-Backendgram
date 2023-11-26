package com.sparta.backendgram.profile.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileResponseDto {
    private Long id;
    private String title;
    private String introduction;
    private String profileImageURL;

    public ProfileResponseDto(Long id, String title, String introduction, String profileImageURL) {
        this.id = id;
        this.title = title;
        this.introduction = introduction;
        this.profileImageURL = profileImageURL;
    }

    // 이미지 URL을 받아서 ProfileResponseDto를 생성하는 메서드 추가
    public static ProfileResponseDto createWithImageURL(Long id, String title, String introduction, String profileImageURL) {
        return new ProfileResponseDto(id, title, introduction, profileImageURL);
    }

    // 이미지 URL이 없는 경우를 처리하는 ProfileResponseDto를 생성하는 메서드 추가
    public static ProfileResponseDto createWithoutImage(Long id, String title, String introduction) {
        return new ProfileResponseDto(id, title, introduction, null);
    }
}