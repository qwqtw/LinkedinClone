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
import com.example.linkedinclone.repository.LikeRepository;

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

    @Autowired
    private LikeRepository likeRepository;

    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirect to login if not authenticated
        }

        User user = userService.findByUsername(principal.getName());
        if (user == null) {
            // Handle the case where the user could not be found
            model.addAttribute("error", "User not found.");
            return "error"; // Redirect to an error page or handle it accordingly
        }

        List<Post> posts = postRepository.findAll();
        model.addAttribute("currentUser", user.getUsername());
        model.addAttribute("userRole", user.getRole()); // Add user role to model

        for (Post post : posts) {
            List<Comment> comments = commentRepository.findByPostId(post.getId());
            post.setComments(comments);

            boolean userHasLiked = likeRepository.findByPostIdAndUsername(post.getId(), user.getUsername()).isPresent();
            post.setUserHasLiked(userHasLiked);
            post.setLikesCount(likeRepository.countByPostId(post.getId()));
        }

        model.addAttribute("user", user);
        model.addAttribute("currentURI", request.getRequestURI());
        model.addAttribute("posts", posts);

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
