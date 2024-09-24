package com.example.linkedinclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequest {

    // Getter for content
    private String content;

    // Setter for content
    public void setContent(String content) {
        this.content = content;
    }
}
