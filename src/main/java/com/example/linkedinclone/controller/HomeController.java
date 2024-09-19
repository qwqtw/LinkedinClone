package com.example.linkedinclone.controller;

import com.example.linkedinclone.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.linkedinclone.service.UserService;

import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
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
