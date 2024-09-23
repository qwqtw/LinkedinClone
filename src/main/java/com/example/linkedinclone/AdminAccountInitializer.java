package com.example.linkedinclone;

import com.example.linkedinclone.entity.User;
import com.example.linkedinclone.repository.UserRepository; // Ensure you have this repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder; // If you're using Spring Security for password encoding

@Component
public class AdminAccountInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository; // Inject your user repository

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject password encoder if using Spring Security

    @Override
    public void run(String... args) throws Exception {
        // Check if an admin user already exists
        if (userRepository.findByUsername("admin") == null) {
            // Create admin user
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@example.com"); // Set admin email
            adminUser.setPassword(passwordEncoder.encode("AdminPassword123!")); // Encode the password
            adminUser.setRole("admin");

            // Save the admin user to the database
            userRepository.save(adminUser);
            System.out.println("Admin account created successfully.");
        } else {
            System.out.println("Admin account already exists.");
        }
    }
}
