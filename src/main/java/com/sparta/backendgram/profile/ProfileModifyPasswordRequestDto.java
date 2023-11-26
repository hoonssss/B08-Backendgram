package com.sparta.backendgram.profile;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileModifyPasswordRequestDto {

    @NotBlank
    private String currentPassword;
    @NotBlank
    @Column(nullable = false)
    private String newPassword;

}
