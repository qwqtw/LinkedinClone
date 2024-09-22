package com.example.linkedinclone.repository;

import com.example.linkedinclone.entity.Connect;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectRepository extends JpaRepository<Connect, Long> {

    // Find connections by user ID and status, sorted by the given field
    List<Connect> findByUserIdAndStatus(Long userId, String status, Sort sort);

    // Find pending connection requests for a user
    List<Connect> findByReceiverIdAndStatus(Long receiverId, String status);
}
