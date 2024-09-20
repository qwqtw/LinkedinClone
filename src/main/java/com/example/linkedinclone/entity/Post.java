package com.example.linkedinclone.entity;

import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@Entity
@Data
@AllArgsConstructor

@Table(name = "post")
public class Post {

    // Setter for id
    // Getter for id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Setter for content
    // Getter for content
    private String content;

    // Default constructor
    public Post() {}

    // Parameterized constructor
    public Post(String content) {
        this.content = content;
    }

}
