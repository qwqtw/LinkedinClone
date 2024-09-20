package com.example.linkedinclone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


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

    public Post() {}

    public Post(String content, String username) {
        this.content = content;
        this.username = username;  // Initialize with username
        this.createdAt = LocalDateTime.now();
    }
}

