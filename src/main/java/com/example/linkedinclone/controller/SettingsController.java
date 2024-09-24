package com.example.linkedinclone.controller;


import org.springframework.ui.Model;
import com.example.linkedinclone.dto.UserSettingsDto;
import com.example.linkedinclone.entity.User;
import com.example.linkedinclone.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class SettingsController {

    @Autowired
    private UserService userService;

    @GetMapping("/settings")
    public String showSettingsPage(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        UserSettingsDto userSettingsDto = new UserSettingsDto();
        userSettingsDto.setUsername(user.getUsername());
        model.addAttribute("userSettingsDto", userSettingsDto);
        return "settings";
    }

    @PostMapping("/settings")
    public String updateUserSettings(@Valid @ModelAttribute("userSettingsDto") UserSettingsDto userSettingsDto,
                                     BindingResult bindingResult, Model model, Principal principal) {

        String currentUsername = principal.getName();

        // Check if the username is being changed
        if (!userSettingsDto.getUsername().equals(currentUsername)) {
            // Check if the new username already exists
            if (userService.existsByUsername(userSettingsDto.getUsername())) {
                bindingResult.rejectValue("username", "error.userSettingsDto", "Username is already taken");
            }
        }

        // Password validation
        if (!userSettingsDto.getPassword().isEmpty() || !userSettingsDto.getPassword2().isEmpty()) {
            if (userSettingsDto.getPassword().length() < 6 || userSettingsDto.getPassword().length() > 100) {
                bindingResult.rejectValue("password", "error.userSettingsDto", "Password must contain between 6 and 100 characters");
            }
            String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d\\W]).+$";
            if (!userSettingsDto.getPassword().matches(passwordPattern)) {
                bindingResult.rejectValue("password", "error.userSettingsDto", "Password must contain at least one uppercase letter, one lowercase letter, and one number or special character");
            }
            if (!userSettingsDto.getPassword().equals(userSettingsDto.getPassword2())) {
                bindingResult.rejectValue("password2", "error.userSettingsDto", "Passwords do not match");
            }
        }

        // Return the form with errors if any validation fails
        if (bindingResult.hasErrors()) {
            return "settings";
        }

        // Proceed with the update if everything is valid
        boolean success = userService.updateUserSettings(currentUsername, userSettingsDto);

        if (success) {
            model.addAttribute("message", "Settings updated successfully.");
        } else {
            model.addAttribute("message", "Failed to update settings. Please try again.");
        }

        return "settings";
    }


}
