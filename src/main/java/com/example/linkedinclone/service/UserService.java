package com.example.linkedinclone.service;

import com.example.linkedinclone.dto.UserSettingsDto;
import com.example.linkedinclone.entity.User;
import com.example.linkedinclone.repository.CommentRepository;
import com.example.linkedinclone.repository.LikeRepository;
import com.example.linkedinclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    @Lazy
    private PostService postService;

    // New method to find user by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean isAdmin(String username) {
        User user = userRepository.findByUsername(username);
        return user != null && "admin".equals(user.getRole());
    }

    @Transactional
    public void deleteUser(String username) {
        // Delete likes associated with the username
        likeRepository.deleteByUsername(username);

        // Delete comments associated with the username
        commentRepository.deleteByUsername(username);

        // Delete posts associated with the username
        postService.deletePostsByUsername(username);

        // Delete the user
        User user = userRepository.findByUsername(username);
        if (user != null) {
            System.out.println("Deleting user: " + user.getUsername());
            userRepository.delete(user);
        } else {
            System.out.println("User not found for username: " + username);
        }
    }


    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    @Transactional
    public boolean updateUserSettings(String currentUsername, UserSettingsDto userSettingsDto) {
        User user = userRepository.findByUsername(currentUsername);

        if (user == null) {
            return false; // User not found
        }

        // Update the username if it's different and available
        if (!user.getUsername().equals(userSettingsDto.getUsername())) {
            if (userRepository.existsByUsername(userSettingsDto.getUsername())) {
                return false; // Username already taken
            }
            user.setUsername(userSettingsDto.getUsername());
        }

        // Update password only if provided and valid
        if (userSettingsDto.getPassword() != null && !userSettingsDto.getPassword().isEmpty()
                && userSettingsDto.getPassword().equals(userSettingsDto.getPassword2())) {
            user.setPassword(passwordEncoder.encode(userSettingsDto.getPassword()));
        }

        userRepository.save(user);
        return true;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }



}
