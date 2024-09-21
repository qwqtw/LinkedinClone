package com.example.linkedinclone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String username;  // Store the username instead of user ID

    private String avatarUrl = "/images/avatar.jpg";
    private String basicInfo = "Not provided";
    private LocalDateTime createdAt = LocalDateTime.now();
    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public Post() {}

    public Post(String content, String username) {
        this.content = content;
        this.username = username;  // Initialize with username
        this.createdAt = LocalDateTime.now();
    }

    // Setter for comments
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    // Getter for comments
    public List<Comment> getComments() {
        return comments;
    }
}

