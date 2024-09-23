package com.example.linkedinclone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.linkedinclone.entity.Profile;
import com.example.linkedinclone.repository.ProfileRepository;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    // Get the profile of the current user based on the username
    public Profile getProfileByUsername(String username) {
        // Use findByUsername to find the user's Profile
        return (Profile) profileRepository.findByUsername(username);
    }

    // Save or update a profile
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }


    // Alternative version if you delete by username instead:
    public void deleteProfileByUsername(String username) {
        Profile profile = getProfileByUsername(username); // Get profile by username
        if (profile != null) {
            profileRepository.delete(profile);
        } else {
            throw new RuntimeException("Profile not found for username: " + username);
        }
    }
}
