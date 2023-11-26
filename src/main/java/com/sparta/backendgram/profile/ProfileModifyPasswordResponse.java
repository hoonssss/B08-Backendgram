package com.sparta.backendgram.profile;


import com.sparta.backendgram.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileModifyPasswordResponse {

    private String password;

    public ProfileModifyPasswordResponse(User user) {
        this.password = user.getPassword();
    }
}
