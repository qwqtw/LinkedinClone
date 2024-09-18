package com.example.linkedinclone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model) {
        // Add current request URI to the model
        model.addAttribute("currentURI", request.getRequestURI());
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
