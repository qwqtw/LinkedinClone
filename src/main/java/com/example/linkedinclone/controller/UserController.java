package Controller;

import Entity.User;
import Repository.UserRepository;
import Service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes) {
        // Check email is already in use
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.info("Email is already in use: {}", user.getEmail());
            result.rejectValue("email", "email.exists", "Email is already in use.");
        }

        // password should match password2
        if (!user.getPassword().equals(user.getPassword2())) {
            result.rejectValue("password", "passwordsDoNotMatch", "Passwords must match");
            result.rejectValue("password2", "passwordsDoNotMatch", "Passwords must match");
        }

        if (result.hasErrors()) {
            log.info("Validation errors found: {}", result);
            return "register";
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "Registration successful. Please login.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    // Admin's index page
    @GetMapping("/admin/index")
    public String showAdminIndex() {
        return "admin/index"; // This will look for admin/index.html in the templates directory
    }

    // User's index page
    @GetMapping("/user/index")
    public String showUserIndex() {
        return "user/index"; // This will look for user/index.html in the templates directory
    }

    // Recruiter's index page
    @GetMapping("/recruiter/index")
    public String showRecruiterIndex() {
        return "recruiter/index"; // This will look for recruiter/index.html in the templates directory
    }

}
