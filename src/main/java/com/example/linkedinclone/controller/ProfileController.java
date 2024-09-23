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

    @GetMapping
    public String showProfile(Model model) {
        Long currentUserId = getCurrentUserId();
        Profile profile = profileService.getProfileByUserId(currentUserId);
        model.addAttribute("profile", profile);
        model.addAttribute("userId", currentUserId);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam("address") String address, Model model) {
        Long currentUserId = getCurrentUserId();
        Profile profile = profileService.getProfileByUserId(currentUserId);
        profile.setAddress(address);
        profileService.saveProfile(profile);
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteProfile(Model model) {
        Long currentUserId = getCurrentUserId();
        profileService.deleteProfileByUserId(currentUserId);
        return "redirect:/login";
    }

    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            User currentUser = userService.findByUsername(username);
            return currentUser.getId();
        } else {
            throw new RuntimeException("User not authenticated");
        }
    }
}
