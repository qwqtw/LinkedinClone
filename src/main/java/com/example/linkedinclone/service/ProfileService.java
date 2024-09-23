package com.example.linkedinclone.service;

import com.example.linkedinclone.entity.Profile;
import com.example.linkedinclone.exception.ProfileNotFoundException;
import com.example.linkedinclone.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    // Get the profile of the current user based on the user ID
    public Profile getProfileByUserId(Long userId) {
        // Use findByUserId to find the user's Profile. If not found, throw ProfileNotFoundException.
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for user id: " + userId));
    }


    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public void deleteProfileByUserId(Long userId) {
        Profile profile = getProfileByUserId(userId);
        profileRepository.delete(profile);
    }

}
