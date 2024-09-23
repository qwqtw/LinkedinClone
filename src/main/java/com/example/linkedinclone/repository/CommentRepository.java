package com.example.linkedinclone.repository;

import com.example.linkedinclone.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId); // Method to fetch comments by post ID
    void deleteByUsername(String username);

}