package com.example.linkedinclone.service;

import com.example.linkedinclone.entity.Comment;
import com.example.linkedinclone.entity.Like;
import com.example.linkedinclone.entity.Post;
import com.example.linkedinclone.repository.CommentRepository;
import com.example.linkedinclone.repository.LikeRepository;
import com.example.linkedinclone.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository; // Add this
    @Autowired
    private CommentRepository commentRepository; // Add this

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

    public void likePost(Long postId, String username) {
        Optional<Like> existingLike = likeRepository.findByPostIdAndUsername(postId, username);

        if (existingLike.isPresent()) {
            // Unlike the post
            likeRepository.delete(existingLike.get());
            Post post = postRepository.findById(postId).orElseThrow();
            post.setLikesCount(post.getLikesCount() - 1); // Decrement like count
            postRepository.save(post);
        } else {
            // Like the post
            Like like = new Like();
            like.setPostId(postId);
            like.setUsername(username);
            like.setCreatedAt(LocalDateTime.now());
            likeRepository.save(like);

            Post post = postRepository.findById(postId).orElseThrow();
            post.setLikesCount(post.getLikesCount() + 1); // Increment like count
            postRepository.save(post);
        }
    }


    public void unlikePost(Long postId, String username) {
        Optional<Like> existingLike = likeRepository.findByPostIdAndUsername(postId, username);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            Post post = postRepository.findById(postId).orElseThrow();
            post.setLikesCount(post.getLikesCount() - 1); // Decrement like count
            postRepository.save(post);
        }
    }


    public Comment addComment(Long postId, String username, String content) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUsername(username);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }


}
