package com.example.linkedinclone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingsDto {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must contain between {min} and {max} characters")
    @Pattern(regexp = "^[a-z0-9]+$", message = "Username must only consist of lower case letters and numbers")
    private String username;

    private String password; // No validation here, since it's optional.

    private String password2; // Optional confirmation password


}
