package com.sparta.backendgram.newsfeed;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class NewsfeedResponseDTO extends CommonResponseDTO {
    private Long id;
    private String title;
    private String content;
    private UserDTO user;
    private LocalDateTime createDate;

    public NewsfeedResponseDTO(Newsfeed newsfeed) {
        this.id = newsfeed.getId();
        this.title = newsfeed.getTitle();
        this.content = newsfeed.getContent();
        this.user = new UserDTO(newsfeed.getUser());
        this.createDate = newsfeed.getCreateDate();
    }
}