package com.example.linkedinclone.controller;

import com.example.linkedinclone.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import com.example.linkedinclone.entity.User ;
import com.example.linkedinclone.repository.UserRepository ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/login")
    public String login() {
        return "login"; // This should match the name of your login template
    }

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        User user = userRepo.findByUsername(username);

        if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            // Authentication successful
            return "redirect:/home";
        } else {
            // Authentication failed, add an error message
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }
    }


    @GetMapping("/register")
    public String viewRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid User user, BindingResult result) {

        if (result.hasErrors()) {
            log.debug(String.valueOf(result));
            return "register";
        }
            if (!user.getPassword().equals(user.getPassword2())) {
                result.rejectValue("password2", "passwordsDoNotMatch", "Passwords must match");
                return "register";
            }

            if (userRepo.findByUsername(user.getUsername()) != null) {
                result.rejectValue("username", "usernameExists", "Username already exists");
                return "register";
            }

            if (userRepo.findByEmail(user.getEmail()) != null) {
                result.rejectValue("email", "emailExists", "Email already exists");
                return "register";
            }

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            userRepo.save(user);

            return "login";
        }

    @PostMapping("/deleteAccount")
    public String deleteAccount(Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName(); // Get current username
        userService.deleteUser(username);
        redirectAttributes.addFlashAttribute("message", "Your account has been deleted successfully.");
        return "redirect:/login"; // Redirect to login or home page
    }

    @PostMapping("/changeUsername")
    @ResponseBody // This will make it return JSON
    public ResponseEntity<Map<String, String>> changeUsername(@RequestParam String newUsername, Principal principal) {
        String currentUsername = principal.getName();
        User user = userService.findByUsername(currentUsername);
        Map<String, String> response = new HashMap<>();

        if (user == null) {
            response.put("errorMessage", "User not found.");
            return ResponseEntity.badRequest().body(response);
        }

        if (userService.findByUsername(newUsername) != null) {
            response.put("errorMessage", "Username already exists.");
            return ResponseEntity.badRequest().body(response);
        }

        user.setUsername(newUsername);
        userService.save(user); // Ensure this method exists

        response.put("successMessage", "Username changed successfully.");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/changePassword")
    @ResponseBody // This will make it return JSON
    public ResponseEntity<Map<String, String>> changePassword(@RequestParam String newPassword, @RequestParam String confirmPassword, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Map<String, String> response = new HashMap<>();

        if (user == null) {
            response.put("errorMessage", "User not found.");
            return ResponseEntity.badRequest().body(response);
        }

        if (!newPassword.equals(confirmPassword)) {
            response.put("errorMessage", "Passwords do not match.");
            return ResponseEntity.badRequest().body(response);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        response.put("successMessage", "Password changed successfully.");
        return ResponseEntity.ok(response);
    }





}
