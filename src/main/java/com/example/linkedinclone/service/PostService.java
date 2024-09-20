package com.example.linkedinclone.service;

import com.example.linkedinclone.entity.Post;
import com.example.linkedinclone.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Long id, String content) {
        return postRepository.findById(id)
                .map(post -> {
                    post.setContent(content);  // Set new content
                    return postRepository.save(post);  // Save updated post
                })
                .orElse(null);  // Handle post not found
    }
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByUsername(String username) {
        return postRepository.findByUsername(username);
    }

    public boolean deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }



}
