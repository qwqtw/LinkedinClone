package com.example.linkedinclone.repository;

import com.example.linkedinclone.entity.Connect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConnectRepository extends JpaRepository<Connect, Long> {

    //find the accepted connections for the users
    List<Connect> findByUserIdAndStatus(Long userId, String status);

    //find the pending connections for the users
    List<Connect> findByReceiverIdAndStatus(Long receiverId, String status);
}
