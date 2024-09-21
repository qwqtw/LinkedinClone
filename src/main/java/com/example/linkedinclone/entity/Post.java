package com.example.linkedinclone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public Post(String content, String username) {
        this.content = content;
        this.username = username;  // Initialize with username
        this.createdAt = LocalDateTime.now();
    }

}

