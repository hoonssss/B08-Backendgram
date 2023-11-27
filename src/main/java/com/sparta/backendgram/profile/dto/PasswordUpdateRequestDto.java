package com.sparta.backendgram.profile.dto;

import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {
    private String currentPassword;
    private String newPassword;
}
