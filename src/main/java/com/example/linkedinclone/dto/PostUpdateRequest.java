package com.example.linkedinclone.dto;

public class PostUpdateRequest {

    private String content;

    // Default constructor
    public PostUpdateRequest() {
    }

    // Parameterized constructor
    public PostUpdateRequest(String content) {
        this.content = content;
    }

    // Getter for content
    public String getContent() {
        return content;
    }

    // Setter for content
    public void setContent(String content) {
        this.content = content;
    }
}
