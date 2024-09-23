package com.example.linkedinclone.repository;

import com.example.linkedinclone.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostIdAndUsername(Long postId, String username);
    long countByPostId(Long postId);
    boolean existsByPostIdAndUsername(Long postId, String username);
    List<Like> findByPostId(Long postId);
    void deleteByUsername(String username);  // New method



}