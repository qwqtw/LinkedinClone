package com.example.linkedinclone.service;

import com.example.linkedinclone.entity.Connect;
import com.example.linkedinclone.entity.User;
import com.example.linkedinclone.repository.ConnectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectService {

    @Autowired
    private ConnectRepository connectRepository;

    // Get connections sorted by a specified field
    public List<Connect> getConnectionsSorted(User user, String sortBy) {
        // Use Sort to sort by the specified field
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        return connectRepository.findByUserIdAndStatus(user.getId(), "ACCEPTED", sort);
    }

    // Get pending connection requests
    public List<Connect> getPendingRequests(User user) {
        return connectRepository.findByReceiverIdAndStatus(user.getId(), "PENDING");
    }

    // Accept a connection request
    public void acceptConnectionRequest(Long requestId, Long receiverId) {
        // Find the connection request by ID
        Connect connect = connectRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Connection request not found"));

        // Ensure the receiver is authorized to accept the request
        if (!connect.getReceiver().getId().equals(receiverId)) {
            throw new RuntimeException("Not authorized to accept this request");
        }

        // Update the status of the connection to 'ACCEPTED'
        connect.setStatus("ACCEPTED");
        // Save the updated connection status to the repository
        connectRepository.save(connect);
    }

    // Reject a connection request
    public void rejectConnectionRequest(Long requestId, Long receiverId) {
        // Find the connection request by ID
        Connect connect = connectRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Connection request not found"));

        // Ensure the receiver is authorized to reject the request
        if (!connect.getReceiver().getId().equals(receiverId)) {
            throw new RuntimeException("Not authorized to reject this request");
        }

        // Delete the connection request from the repository (reject)
        connectRepository.delete(connect);
    }
}
