package com.example.linkedinclone.repository;

import com.example.linkedinclone.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // You can add methods to query likes if needed
}