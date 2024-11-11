package com.example.proydbp.auth.utils;

import com.example.proydbp.user.domain.Role;
import com.example.proydbp.user.domain.User;
import com.example.proydbp.user.domain.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtils {

    final private UserService userService;

    AuthorizationUtils(UserService userService) {
        this.userService = userService;
    }

    public boolean isAdminOrResourceOwner(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().toArray()[0].toString();
        User passenger = userService.findByEmail(username, role);
        return passenger.getId().equals(id) || passenger.getRole().equals(Role.ADMIN);
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        } catch (ClassCastException e) {
            return null;
        }
    }
}
