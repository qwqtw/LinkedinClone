package com.example.linkedinclone.dto;

import com.example.linkedinclone.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String content;
    private String username;
    private String avatarUrl;
    private String basicInfo;
    private LocalDateTime createdAt;
    private int likesCount;
    private boolean userHasLiked;
    private boolean isLiked;

    public PostResponse() {
    }

}
