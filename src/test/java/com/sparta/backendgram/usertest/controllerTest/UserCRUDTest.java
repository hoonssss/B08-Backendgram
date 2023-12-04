package com.sparta.backendgram.usertest.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.backendgram.configuration.WebSecurityConfig;
import com.sparta.backendgram.jwt.JwtUtil;
import com.sparta.backendgram.newsfeed.NewsfeedController;
import com.sparta.backendgram.newsfeed.NewsfeedRequestDTO;
import com.sparta.backendgram.newsfeed.NewsfeedService;
import com.sparta.backendgram.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.awt.*;
import java.security.Principal;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = {UserController.class, NewsfeedController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }//제외
)
public class UserCRUDTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    NewsfeedService newsfeedService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }//가짜 Security

    @Test
    @DisplayName("회원가입")
    void signUp() throws Exception {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("wogns8030", "wo331846");
        User user = new User(userRequestDto);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        mockPrincipal = new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
        String requestBody = objectMapper.writeValueAsString(userRequestDto);//String -> Json 변환

        //when - then
        mvc.perform(post("/api/user/signup")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        //given
        this.signUp();
        //when - then
        UserRequestDto userRequestDto = new UserRequestDto("sollertia4351", "robbie1234");
        String requestBody = objectMapper.writeValueAsString(userRequestDto);
        mvc.perform(post("/api/user/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 등록")
    void createTest() throws Exception {
        //given
        this.login();
        String title = "titleTest";
        String content = "contentTest";
        NewsfeedRequestDTO newsfeedRequestDTO = new NewsfeedRequestDTO(title,content);
        String createInfo = objectMapper.writeValueAsString(newsfeedRequestDTO);
        //when - then
        mvc.perform(post("/api/newsfeed")
                .content(createInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정")
    void patchTest() throws Exception {
        this.createTest();
        long newsfeedId = 1L;
        String title = "수정test";
        String content = "수정test";
        NewsfeedRequestDTO newsfeedRequestDTO = new NewsfeedRequestDTO(title, content);
        String request = objectMapper.writeValueAsString(newsfeedRequestDTO);

            mvc.perform(put("/api/newsfeed/{newsfeedId}", newsfeedId)
                            .content(request)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .principal(mockPrincipal))
                    .andExpect(status().isOk())
                    .andDo(print());
    }
}
