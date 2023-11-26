package com.sparta.backendgram.newsfeed;

import com.sparta.backendgram.user.CommonResponseDto;
import com.sparta.backendgram.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@RequestMapping("/api/newsfeed")
@RestController
@RequiredArgsConstructor
public class NewsfeedController {
    private final NewsfeedService newsfeedService;

    //CREATAE
    @PostMapping
    public ResponseEntity<NewsfeedResponseDTO> postNewsfeed(@RequestBody NewsfeedRequestDTO newsfeedRequestDTO,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        NewsfeedResponseDTO responseDTO = newsfeedService.createNewsfeed(newsfeedRequestDTO, userDetails.getUser());

        return ResponseEntity.status(201).body(responseDTO);
    }

    @GetMapping("/gets")
    public ResponseEntity<List<NewsfeedResponseDTO>> getAllNewsFeeds(){
        try {
            List<NewsfeedResponseDTO> responseDTO = newsfeedService.getAllNewsFeed();
            return ResponseEntity.ok().body(responseDTO);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //READ
    @GetMapping("/{newsfeedId}")
    public ResponseEntity<CommonResponseDto> getNewsfeed(@PathVariable Long newsfeedId) {
        try {
            NewsfeedResponseDTO responseDTO = newsfeedService.getNewsfeedDTO(newsfeedId);
            return ResponseEntity.ok().body(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    //UPDATE
    @PutMapping("/{newsfeedId}")
    public ResponseEntity<CommonResponseDto> putNewsfeed(@PathVariable Long newsfeedId, @RequestBody NewsfeedRequestDTO newsfeedRequestDTO,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            NewsfeedResponseDTO responseDTO = newsfeedService.updateNewsfeed(newsfeedId, newsfeedRequestDTO, userDetails.getUser());
            return ResponseEntity.ok().body(responseDTO);
        } catch (RejectedExecutionException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new CommonResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    
    //DELETE
    @DeleteMapping("/{newsfeedId}")
    public ResponseEntity<CommonResponseDto> deleteNewsfeed(@PathVariable Long newsfeedId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            newsfeedService.deleteNewsfeed(newsfeedId, userDetails.getUser());
            return ResponseEntity.ok().body(new CommonResponseDto("정상적으로 삭제 되었습니다.", HttpStatus.OK.value()));
        } catch (RejectedExecutionException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new CommonResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
