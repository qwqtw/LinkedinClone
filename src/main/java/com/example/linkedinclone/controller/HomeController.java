package com.example.linkedinclone.controller;

import com.example.linkedinclone.entity.Comment;
import com.example.linkedinclone.entity.Post;
import com.example.linkedinclone.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.linkedinclone.service.UserService;
import com.example.linkedinclone.repository.PostRepository;
import com.example.linkedinclone.repository.CommentRepository;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Post> posts= postRepository.findAll();

        for (Post post : posts) {
            List<Comment> comments = commentRepository.findByPostId(post.getId());
            // Optionally, fetch likes count or other data if needed
            post.setComments(comments);
        }

        model.addAttribute("user", user);
        model.addAttribute("currentURI", request.getRequestURI());
        model.addAttribute("posts", posts);
//        model.addAttribute("connections", connectionService.findConnectionsByUser(user));
//        model.addAttribute("suggestedJobs", jobService.findSuggestedJobsForUser(user));
        return "index";
    }


    @GetMapping("/network")
    public String network(HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
        return "network";
    }

    @GetMapping("/jobs")
    public String jobs(HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
        return "jobs";
    }
}
