package com.sparta.backendgram.newsfeed;

import com.sparta.backendgram.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsfeedService {
    private final NewsfeedRepository newsfeedRepository;

    //CREATE
    public NewsfeedResponseDTO createNewsfeed(NewsfeedRequestDTO dto, User user) {
        Newsfeed newsfeed = new Newsfeed(dto);
        newsfeed.setUser(user);

        newsfeedRepository.save(newsfeed);

        return new NewsfeedResponseDTO(newsfeed);
    }

    //READ
    public NewsfeedResponseDTO getNewsfeedDTO(Long newsfeedId) {
        Newsfeed newsfeed = getNewsfeed(newsfeedId);
        return new NewsfeedResponseDTO(newsfeed);
    }

    //JH
    public List<NewsfeedResponseDTO> getAllNewsFeed() {
        List<Newsfeed> newsfeed = newsfeedRepository.findAll();

        return newsfeed.stream()
                .map(
                        NewsfeedResponseDTO::new
                ).collect(Collectors.toList());
    }

    //UPDATE
    @Transactional
    public NewsfeedResponseDTO updateNewsfeed(Long newsfeedId, NewsfeedRequestDTO newsfeedRequestDTO, User user) {
        Newsfeed newsfeed = getUserNewsfeed(newsfeedId, user);

        newsfeed.setTitle(newsfeedRequestDTO.getTitle());
        newsfeed.setContent(newsfeedRequestDTO.getContent());

        return new NewsfeedResponseDTO(newsfeed);
    }

    //DELETE
    @Transactional
    public void deleteNewsfeed(Long newsfeedId, User user) {
        Newsfeed newsfeed = getUserNewsfeed(newsfeedId, user);

        newsfeedRepository.delete(newsfeed);
    }

    //like
    public NewsfeedResponseDTO likeNewsFeed(Long userfeedId, User user) {
        Newsfeed newsfeed = findById(userfeedId);
        newsfeed.getLikes().add(user);
        newsfeedRepository.save(newsfeed);
        return convertToDto(newsfeed);
    }

    //unlike
    public NewsfeedResponseDTO unlikeNewsFeed(Long userfeedId, User user) {
        Newsfeed newsfeed = findById(userfeedId);
        newsfeed.getLikes().remove(user);
        newsfeedRepository.save(newsfeed);
        return convertToDto(newsfeed);
    }

    //Dto 변환
    private NewsfeedResponseDTO convertToDto(Newsfeed newsfeed) {
        NewsfeedResponseDTO responseDTO = new NewsfeedResponseDTO();
        responseDTO.setId(newsfeed.getId());
        responseDTO.setContent(newsfeed.getContent());

        responseDTO.setLikesCount(newsfeed.getLikes().size());

        return responseDTO;
    }

    public Newsfeed getNewsfeed(Long NewsfeedId) {

        return newsfeedRepository.findById(NewsfeedId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 할일 ID 입니다."));
    }

    public Newsfeed getUserNewsfeed(Long NewsfeedId, User user) {
        Newsfeed Newsfeed = getNewsfeed(NewsfeedId);

        if (!user.getId().equals(Newsfeed.getUser().getId())) {
            throw new RejectedExecutionException("작성자만 수정할 수 있습니다.");
        }
        return Newsfeed;
    }

    private Newsfeed findById(Long userfeedId) {
        return newsfeedRepository.findById(userfeedId).orElseThrow(
                () -> new UsernameNotFoundException("오류")
        );
    }
}
