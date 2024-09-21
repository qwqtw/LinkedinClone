package com.example.linkedinclone.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;  // Store username directly

    private Long postId;  // Or reference a Post entity

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Like(String username, Long postId, LocalDateTime createdAt) {
        this.username = username;
        this.postId = postId;
        this.createdAt = createdAt;
    }

}
