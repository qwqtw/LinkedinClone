package com.example.linkedinclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Table(name = "connection")
public class Connect {

    // Getters and setters
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who send the connection request
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The user who receives the connection request
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    // The status of the connection request (e.g., PENDING, ACCEPTED, REJECTED)
    @Column(name = "status", nullable = false)
    private String status;

    // Timestamp when the connection was created
    @Getter
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Timestamp when the connection was last updated
    @Setter
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor
    public Connect() {

    }

    // Constructor for creating a new connection
    public Connect(User user, User receiver, String status) {
        this.user = user;
        this.receiver = receiver;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }


}
