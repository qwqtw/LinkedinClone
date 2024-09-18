package com.example.linkedinclone.controller;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PostController {

    @PostMapping("/posts")
    public Map<String, Object> createPost(@RequestBody Map<String, String> payload) {
        String content = payload.get("content");

        // Simulate the post being created (in practice, save to the database)
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Your Name");  // This would come from the logged-in user
        response.put("basicInfo", "Job Title, Location");  // User's basic info
        response.put("avatarUrl", "/images/avatar.jpg");  // User's avatar
        response.put("createdAt", LocalDateTime.now().toString());
        response.put("content", content);

        return response;
    }
}
