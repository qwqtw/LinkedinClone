package com.example.linkedinclone.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import com.example.linkedinclone.entity.User ;
import com.example.linkedinclone.repository.UserRepository ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/login")
    public String login() {
        return "login"; // This should match the name of your login template
    }

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



}
