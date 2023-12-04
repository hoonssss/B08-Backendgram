package com.sparta.backendgram.newsfeed;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsfeedRequestDTO {
    private String title;
    private String content;
}
