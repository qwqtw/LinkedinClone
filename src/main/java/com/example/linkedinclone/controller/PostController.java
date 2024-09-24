package com.example.linkedinclone.controller;

import com.example.linkedinclone.entity.Comment;
import com.example.linkedinclone.entity.Post;
import com.example.linkedinclone.service.PostService;
import com.example.linkedinclone.service.UserService;
import com.example.linkedinclone.dto.PostUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import com.example.linkedinclone.repository.CommentRepository;
import com.example.linkedinclone.repository.PostRepository;


@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;


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
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id, @RequestBody PostUpdateRequest updateRequest, Principal principal) {
        String username = principal.getName();
        Post post = postService.getPostById(id); // You may need to implement this method

        // Check if the user is the owner of the post or an admin
        if (post == null || (!post.getUsername().equals(username) && !isAdmin(username))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        Post updatedPost = postService.updatePost(id, updateRequest.getContent(), username);
        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id, Principal principal) {
        String username = principal.getName();
        Post post = postService.getPostById(id); // Implement this method

        // Check if the user is the owner of the post or an admin
        if (post == null || (!post.getUsername().equals(username) && !isAdmin(username))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        boolean isDeleted = postService.deletePost(id, username);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }


    @PostMapping("/{postId}/likes")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        // Retrieve the logged-in username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        postService.likePost(postId, username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) {
        // Retrieve the logged-in username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        postService.unlikePost(postId, username);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{postId}/comments")
    public ResponseEntity<Map<String, Object>> addComment(@PathVariable Long postId, @RequestBody Map<String, String> payload) {
        String content = payload.get("content");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Comment comment = postService.addComment(postId, username, content);

        Map<String, Object> response = new HashMap<>();
        response.put("id", comment.getId());
        response.put("username", comment.getUsername());
        response.put("content", comment.getContent());
        response.put("createdAt", comment.getCreatedAt().toString());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Principal principal) {
        // Get the current logged-in user's username
        String currentUsername = principal.getName();

        // Fetch the comment and post from the repositories
        Comment comment = commentRepository.findById(commentId).orElse(null);
        Post post = postRepository.findById(postId).orElse(null);

        // Check if the comment or post doesn't exist
        if (comment == null || post == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        // Check if the current user is either the post owner or the comment sender
        if (!currentUsername.equals(post.getUsername()) && !currentUsername.equals(comment.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        // Proceed with the deletion if authorized
        boolean isDeleted = postService.deleteComment(postId, commentId);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }



    private boolean isAdmin(String username) {
        // Logic to determine if the user is an admin
        // This could involve checking the user's role in the database
        return userService.isAdmin(username); // Implement this in your UserService
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = Optional.ofNullable(postService.getPostById(id));
        if (post.isPresent()) {
            return ResponseEntity.ok(post.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



}
