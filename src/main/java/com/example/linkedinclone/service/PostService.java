package com.example.linkedinclone.service;

import com.example.linkedinclone.entity.Comment;
import com.example.linkedinclone.entity.Like;
import com.example.linkedinclone.entity.Post;
import com.example.linkedinclone.repository.CommentRepository;
import com.example.linkedinclone.repository.LikeRepository;
import com.example.linkedinclone.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.linkedinclone.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Long id, String content, String username) {
        return postRepository.findById(id)
                .filter(post -> post.getUsername().equals(username))  // Check if the user is the owner
                .map(post -> {
                    post.setContent(content);
                    return postRepository.save(post);
                })
                .orElse(null);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByUsername(String username) {
        return postRepository.findByUsername(username);
    }

    public Post getPostById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        return postOptional.orElse(null); // Return null if not found
    }

    public boolean deletePost(Long id, String username) {
        return postRepository.findById(id)
                .filter(post -> post.getUsername().equals(username) || userService.isAdmin(username)) // Check ownership or admin
                .map(post -> {
                    postRepository.delete(post);
                    return true;
                })
                .orElse(false);
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

    public boolean deleteComment(Long postId, Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            return true;
        }
        return false;
    }


}
