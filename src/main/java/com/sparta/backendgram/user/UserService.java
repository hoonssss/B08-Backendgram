package com.sparta.backendgram.user;

import com.sparta.backendgram.profile.ProfileResponseDto;
import com.sparta.backendgram.profile.ProfileModifyPasswordRequestDto;
import com.sparta.backendgram.profile.ProfileModifyPasswordResponse;
import com.sparta.backendgram.profile.ProfileUpdateProfileRequestDto;
import com.sparta.backendgram.profile.exception.PasswordIsNotMatchException;
import com.sparta.backendgram.profile.exception.RejectedUserExecutionException;
import com.sparta.backendgram.profile.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void signup(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = passwordEncoder.encode(userRequestDto.getPassword());

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 유저 입니다.");
        }

        User user = new User(username, password);
        userRepository.save(user);
    }

    public void login(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 없습니다."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
    public ProfileResponseDto getProfile(Long userId) {
        User targetUser = existId(userId);
        return ProfileResponseDto.of(targetUser);
    }
    private User existId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NullPointerException("프로필이 존재하지 않습니다."));
    }



    public ProfileResponseDto updateProfile(Long userId, ProfileUpdateProfileRequestDto requestDto, User user) {
        User targetProfile = writerId(userId, user);
        targetProfile.setContent(requestDto.getContent());
        userRepository.save(targetProfile);
        return ProfileResponseDto.of(targetProfile);
    }
    private User writerId(Long userId, User user) {
        User targetProfile = existId(userId);
        if (!user.getUsername().equals(targetProfile.getUsername())) {
            throw new RejectedUserExecutionException(UserErrorCode.REJECTED_USER_EXECUTION);
        }
        return targetProfile;
    }

    public ProfileModifyPasswordResponse modifyPassword(Long userId, ProfileModifyPasswordRequestDto requestDto, User user) {
        User targetUser = targetId(userId, user);
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(),
                targetUser.getPassword())) {
            throw new PasswordIsNotMatchException(UserErrorCode.PASSWORD_IS_NOT_MATCH);
        }
        targetUser.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(targetUser);
        return new ProfileModifyPasswordResponse(targetUser);
    }

    private User targetId(Long userId, User user) {
        User targetUser = existId(userId);
        if (!user.getUsername().equals(targetUser.getUsername())) {
            throw new RejectedUserExecutionException(UserErrorCode.REJECTED_USER_EXECUTION);
        }
        return targetUser;
    }
}
