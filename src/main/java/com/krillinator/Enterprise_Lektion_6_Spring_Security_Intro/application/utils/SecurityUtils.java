package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.utils;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.CustomUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

public class SecurityUtils {

    /**
     * Get the username of the currently authenticated user
     * @return <code><b>String</b></code> username of the authenticated user or <code><b>null</b></code>
     */
    public static String getCurrentLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // Return the username of the currently authenticated user
            return authentication.getName();  // returns username (the principal)
        }
        return null;  // or throw an exception if you prefer
    }

    /**
     * Get the {@link CustomUser} object of the currently authenticated user
     * @return the {@link CustomUser} of the authenticated user, or <code><b>null</b></code> if not authenticated
     */
    public static CustomUser getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // Assuming that your authentication is a UserDetails object (e.g., Spring Security's User)
            return (CustomUser) authentication.getPrincipal();  // returns the CustomUser object
        }
        return null;  // or throw an exception if you prefer
    }

    /**
     * Check if there is an authenticated user
     * @return <code><b>true</b></code> if a user is authenticated, otherwise <code><b>false</b></code>
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
