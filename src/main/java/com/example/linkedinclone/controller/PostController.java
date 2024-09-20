package com.example.linkedinclone.controller;

import com.example.linkedinclone.entity.Post;
import com.example.linkedinclone.service.PostService;
import com.example.linkedinclone.dto.PostUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPost(@RequestBody Map<String, String> payload) {
        String content = payload.get("content");

        // Retrieve the logged-in username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the actual username from authentication

        // Set additional fields
        String avatarUrl = payload.getOrDefault("avatarUrl", "/images/avatar.jpg");
        String basicInfo = payload.getOrDefault("basicInfo", "Not provided");

        // Create and save the post
        Post newPost = new Post(content, username);
        newPost.setAvatarUrl(avatarUrl);
        newPost.setBasicInfo(basicInfo);
        newPost.setCreatedAt(LocalDateTime.now());

        Post savedPost = postService.savePost(newPost);

        // Prepare the response
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedPost.getId().toString());
        response.put("avatarUrl", savedPost.getAvatarUrl());
        response.put("basicInfo", savedPost.getBasicInfo());
        response.put("createdAt", savedPost.getCreatedAt().toString());
        response.put("content", savedPost.getContent());
        response.put("username", savedPost.getUsername()); // Include username in the response

        return ResponseEntity.ok(response);
    }




    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id, @RequestBody PostUpdateRequest updateRequest) {
        Post updatedPost = postService.updatePost(id, updateRequest.getContent());
        if (updatedPost != null) {
            return ResponseEntity.ok(updatedPost);  // Return updated post
        } else {
            return ResponseEntity.notFound().build();  // Handle post not found case
        }

    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
        boolean isDeleted = postService.deletePost(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if successful
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if the post doesn't exist
        }
    }


}
