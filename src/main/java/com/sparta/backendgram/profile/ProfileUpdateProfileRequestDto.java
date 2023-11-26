package com.sparta.backendgram.profile;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProfileUpdateProfileRequestDto {
    @Size(max = 500, message = "500자 이내로 작성해 주세요!")
    @Column(nullable = false)
    private String content;

}
