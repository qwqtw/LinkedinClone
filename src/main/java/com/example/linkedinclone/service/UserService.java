package com.example.linkedinclone.service;

import com.example.linkedinclone.entity.Post;
import com.example.linkedinclone.entity.User;
import com.example.linkedinclone.repository.CommentRepository;
import com.example.linkedinclone.repository.LikeRepository;
import com.example.linkedinclone.repository.PostRepository;
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
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    @Lazy
    private PostService postService;


    @Transactional
    public boolean registerUser(User user) {
        // Check for existing user
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false; // User already exists
        }

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true; // Registration successful
    }

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

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll(); // Adjust if your repository has a different method
    }




}
