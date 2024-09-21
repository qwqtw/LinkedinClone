package com.example.linkedinclone.service;

import com.example.linkedinclone.entity.Connect;
import com.example.linkedinclone.entity.User;
import com.example.linkedinclone.repository.ConnectRepository;
import com.example.linkedinclone.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectService {

    private final ConnectRepository connectRepository;
    private final UserRepository userRepository;

    public ConnectService(ConnectRepository connectRepository, UserRepository userRepository) {
        this.connectRepository = connectRepository;
        this.userRepository = userRepository;
    }

    // Get connections and sort by receiver's username in memory
    public List<Connect> getConnections(User user) {
        // Get all connections from the repository
        List<Connect> connections = connectRepository.findByUserIdAndStatus(user.getId(), "ACCEPTED");

        // Sort the list by receiver's username using Java Streams
        return connections.stream()
                .sorted(Comparator.comparing(c -> c.getReceiver().getUsername()))  // Ascending order by username
                .collect(Collectors.toList());
    }

    // Get all pending requests (unsorted)
    public List<Connect> getPendingRequests(User user) {
        return connectRepository.findByReceiverIdAndStatus(user.getId(), "PENDING");
    }

    // Send a new connection request
    public void sendConnectionRequest(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Send are not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receive are not found"));

        Connect connection = new Connect();
        connection.setUser(sender);
        connection.setReceiver(receiver);
        connection.setStatus("PENDING");
        connectRepository.save(connection);
    }

    // Accept a connection request
    public void acceptConnectionRequest(Long requestId, Long receiverId) {
        Connect connect = connectRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Connection request not found"));

        if (!connect.getReceiver().getId().equals(receiverId)) {
            throw new RuntimeException("Not authorized to accept this request");
        }

        connect.setStatus("ACCEPTED");
        connectRepository.save(connect);
    }

    // Reject a connection request
    public void rejectConnectionRequest(Long requestId, Long receiverId) {
        Connect connect = connectRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Connection request not found"));

        if (!connect.getReceiver().getId().equals(receiverId)) {
            throw new RuntimeException("Not authorized to reject this request");
        }

        connectRepository.delete(connect);
    }
}
