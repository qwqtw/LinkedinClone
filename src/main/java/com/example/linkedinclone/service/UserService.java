package com.example.linkedinclone.service;

import com.example.linkedinclone.entity.User;
import com.example.linkedinclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void registerUser(User user) {
    //
    userRepository.save(user);
    }
}
