package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.config.security;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.model.CustomUser;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service()
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CustomUser customUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        System.out.println("---DEBUGGING LOGIN---");
        System.out.println("IT WENT THROUGH");
        System.out.println("---DEBUGGING LOGIN---");

        return new CustomUserDetails(customUser);
    }
}
