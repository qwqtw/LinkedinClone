package com.example.linkedinclone.controller;

import com.example.linkedinclone.entity.Profile;
import com.example.linkedinclone.entity.User;
import com.example.linkedinclone.service.ProfileService;
import com.example.linkedinclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    // ProfileController.java

    @GetMapping
    public String showProfile(Model model) {
        String currentUsername = getCurrentUsername();
        Profile profile = profileService.getProfileByUsername(currentUsername);  // Use username
        model.addAttribute("profile", profile);
        model.addAttribute("username", currentUsername);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam("address") String address, Model model) {
        String currentUsername = getCurrentUsername();
        Profile profile = profileService.getProfileByUsername(currentUsername);  // Use username
        profile.setAddress(address);
        profileService.saveProfile(profile);
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteProfile(Model model) {
        String currentUsername = getCurrentUsername();
        profileService.deleteProfileByUsername(currentUsername);  // Use username
        return "redirect:/login";
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            throw new RuntimeException("User not authenticated");
        }
    }

}
