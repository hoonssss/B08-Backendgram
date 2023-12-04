package com.sparta.backendgram.usertest.serviceTest;

import com.sparta.backendgram.user.User;
import com.sparta.backendgram.user.UserRepository;
import com.sparta.backendgram.user.UserRequestDto;
import com.sparta.backendgram.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입")
    void test() {
        //given
        String username = "wogns8020";
        String password = "hoon1234";
        UserRequestDto userRequestDto = new UserRequestDto(username, password);
        //when
        userService.signup(userRequestDto);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User(username, password)));
        User saveUser = userRepository.findByUsername(username).orElse(null);
        //then
        System.out.println("입력값 : " + username + " : " + password + " / " + "저장값 : " + saveUser.getUsername() + " : " + saveUser.getPassword());
        assertEquals(username, saveUser.getUsername());
        assertEquals(password, saveUser.getPassword());
    }

    @Test
    @DisplayName("로그인")
    void test1(){
        String username = "wogns8020";
        String password = "hoon1234";
        UserRequestDto requestDto = new UserRequestDto(username,password);
        User user = new User(requestDto);
        userService.signup(requestDto);
        userRepository.save(user);

        String hashedPassword = user.getPassword();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(true);

        userService.login(requestDto);
        assertEquals(password,hashedPassword);
    }

    @Test
    @DisplayName("중복 회원가입")
    void test2() {
        //given
        String username = "wogns8020";
        String password = "hoon1234";
        UserRequestDto userRequestDto = new UserRequestDto(username, password);
        //when
        userService.signup(userRequestDto);
        User user = new User(userRequestDto);
        userRepository.save(user);
        // 두 번째 회원가입 시도
        // 동일한 사용자명(username)으로 회원가입을 시도하면 이미 존재하는 사용자로 판단
        // UserRepository 동작을 모킹하여 동일한 사용자가 존재한다고 가정
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User(username, password)));

        // 두 번째 회원가입 시도
        // then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.signup(userRequestDto);
        });
        assertEquals("이미 존재하는 유저 입니다.", exception.getMessage());
    }
}