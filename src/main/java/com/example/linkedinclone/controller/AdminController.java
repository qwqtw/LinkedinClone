package com.example.linkedinclone.controller;

import com.example.linkedinclone.entity.Comment;
import com.example.linkedinclone.entity.Post;
import com.example.linkedinclone.entity.User;
import com.example.linkedinclone.repository.CommentRepository;
import com.example.linkedinclone.repository.PostRepository;
import com.example.linkedinclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;


import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('admin')") // Ensure only admins can access this
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public String adminBoard(Model model) {
        List<User> users = userService.findAllUsers();
        List<Post> posts = postRepository.findAll();
        List<Comment> comments = commentRepository.findAll();

        model.addAttribute("users", users);
        model.addAttribute("posts", posts);
        model.addAttribute("comments", comments);
        return "adminBoard"; // Create an adminBoard.html template
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam String username) {
        userService.deleteUser(username);
        return "redirect:/admin"; // Redirect back to the admin board
    }

    @PostMapping("/posts/delete")
    public String deletePost(@RequestParam Long postId) {
        postRepository.deleteById(postId);
        return "redirect:/admin"; // Redirect back to the admin board
    }

    @PostMapping("/comments/delete")
    public String deleteComment(@RequestParam Long commentId) {
        commentRepository.deleteById(commentId);
        return "redirect:/admin"; // Redirect back to the admin board
    }

}