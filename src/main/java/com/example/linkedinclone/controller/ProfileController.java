package com.example.linkedinclone.controller;

import com.example.linkedinclone.entity.User;
import com.example.linkedinclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class ProfileController {


    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String profile(Model model) {
        Optional<User> user = userRepository.findById(1L);

        model.addAttribute("user", user);
        return "profile";
    }
}
