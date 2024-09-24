package com.example.linkedinclone.securingweb;

import java.util.Collection;
import java.util.Collections;

import com.example.linkedinclone.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// This class enables Spring Security to understand how to retrieve and verify user credentials during the login process
@Data
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return a collection of GrantedAuthority based on the user's role
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

    }

    public User getUser() {
        return user;
    }

    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }


    public void setPassword(String password) {
        user.setPassword(password);
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
