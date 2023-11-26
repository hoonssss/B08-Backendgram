package com.sparta.backendgram.newsfeed;

import com.sparta.backendgram.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.RejectedExecutionException;

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
    public Page<NewsfeedResponseDTO> getNewsFeeds(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction,sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);

        Page<Newsfeed> newsFeeds = newsfeedRepository.findAll(pageable);
        return newsFeeds.map(NewsfeedResponseDTO::new);
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

    public Newsfeed getNewsfeed(Long NewsfeedId) {

        return newsfeedRepository.findById(NewsfeedId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 할일 ID 입니다."));
    }

    public Newsfeed getUserNewsfeed(Long NewsfeedId, User user) {
        Newsfeed Newsfeed = getNewsfeed(NewsfeedId);

        if(!user.getId().equals(Newsfeed.getUser().getId())) {
            throw new RejectedExecutionException("작성자만 수정할 수 있습니다.");
        }
        return Newsfeed;
    }
}
