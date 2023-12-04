package com.sparta.backendgram.usertest.serviceTest;

import com.sparta.backendgram.newsfeed.*;
import com.sparta.backendgram.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsfeedServiceTest {

    @Mock
    private NewsfeedRepository newsfeedRepository;

    @InjectMocks
    private NewsfeedService newsfeedService;

    @Test
    @DisplayName("Create Newsfeed")
    void testCreateNewsfeed() {
        NewsfeedRequestDTO requestDTO = new NewsfeedRequestDTO("Test Title", "Test Content");
        Long id = 1L;
        Newsfeed newsfeed = new Newsfeed(requestDTO);
        User user = new User();
        newsfeed.setUser(user);
        newsfeed.setId(id);

        when(newsfeedRepository.save(any(Newsfeed.class))).thenReturn(newsfeed);
        NewsfeedResponseDTO responseDTO = newsfeedService.createNewsfeed(requestDTO, user);

        System.out.println(responseDTO.getId() + ", " + responseDTO.getTitle() + ", " + responseDTO.getContent());
        assertEquals("Test Title", responseDTO.getTitle());
    }

    @Test
    @DisplayName("Update Newsfeed")
    void testUpdateNewsfeed() {
        Long id = 1L;
        String title = "Title";
        String content = "Content";

        NewsfeedRequestDTO requestDTO = new NewsfeedRequestDTO(title,content);

        User user = new User();
        user.setId(id);

        Newsfeed newsfeed = new Newsfeed(requestDTO);
        newsfeed.setId(id);
        newsfeed.setUser(user);

        when(newsfeedRepository.save(any(Newsfeed.class))).thenReturn(newsfeed);
        newsfeedService.createNewsfeed(requestDTO,user);

        System.out.println(newsfeed.getId() + " " + newsfeed.getTitle() + " " + newsfeed.getContent());

        //given
        Long findid = 1L;
        String UpdateTitle = "updateTitle";
        String UPdateContent = "updateContent";
        NewsfeedRequestDTO updateDTO = new NewsfeedRequestDTO(UpdateTitle,UPdateContent);

        User updateUser = new User();
        updateUser.setId(findid);

        Newsfeed updateNewsfeed = new Newsfeed(updateDTO);
        updateNewsfeed.setUser(updateUser);
        updateNewsfeed.setId(findid);

        when(newsfeedRepository.findById(findid)).thenReturn(Optional.of(updateNewsfeed));
        //when
        newsfeedService.updateNewsfeed(findid,updateDTO,updateUser);
        System.out.println(updateNewsfeed.getId() + " " + updateNewsfeed.getTitle() + " " + updateNewsfeed.getContent());
        //then
        verify(newsfeedRepository, times(1)).findById(findid);
        verify(newsfeedRepository, times(1)).save(updateNewsfeed);
    }

    @Test
    @DisplayName("Delete Newsfeed")
    void testDeleteNewsfeed() {
        // given
        Long id = 1L;
        String title = "Test Title";
        String content = "Test Content";
        NewsfeedRequestDTO requestDTO = new NewsfeedRequestDTO(title, content);

        User user = new User();
        user.setId(id);

        Newsfeed newsfeed = new Newsfeed(requestDTO);
        newsfeed.setId(id);
        newsfeed.setUser(user);

        when(newsfeedRepository.save(any(Newsfeed.class))).thenReturn(newsfeed);
        when(newsfeedRepository.findById(id)).thenReturn(Optional.of(newsfeed));
        // when
        newsfeedService.deleteNewsfeed(id,user);

        // then
        verify(newsfeedRepository, times(1)).findById(id);
        verify(newsfeedRepository, times(1)).delete(newsfeed);
    }

    @Test
    @DisplayName("like")
    void likeTest(){
        //given
        Long userfeedId = 1L;
        User user = new User();
        user.setId(userfeedId);

        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setId(userfeedId);

        when(newsfeedRepository.findById(userfeedId)).thenReturn(Optional.of(newsfeed));
        //when
        NewsfeedResponseDTO responseDTO = newsfeedService.likeNewsFeed(userfeedId,user);
        System.out.println(responseDTO.getLikesCount());
        //then
        verify(newsfeedRepository, times(1)).findById(userfeedId);
        verify(newsfeedRepository, times(1)).save(newsfeed);

        assertEquals(1, responseDTO.getLikesCount());
    }

    @Test
    @DisplayName("unLike")
    void unLikeTest(){
        Long newsfeedId = 1L;
        User user = new User();
        user.setId(newsfeedId);

        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setId(newsfeedId);
        newsfeed.setLikes(Collections.singletonList(user));

        when(newsfeedRepository.findById(newsfeedId)).thenReturn(Optional.of(newsfeed));

        NewsfeedResponseDTO likeDTO = newsfeedService.likeNewsFeed(newsfeedId,user);
        NewsfeedResponseDTO unlikeDTO = newsfeedService.unlikeNewsFeed(newsfeedId,user);

        verify(newsfeedRepository,times(2)).findById(newsfeedId);
        verify(newsfeedRepository,times(2)).save(newsfeed);

        assertEquals(1,likeDTO.getLikesCount());
        assertEquals(0,unlikeDTO.getLikesCount());
    }
}
