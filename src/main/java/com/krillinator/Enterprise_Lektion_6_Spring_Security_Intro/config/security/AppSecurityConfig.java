package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.config.security;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserPermission;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    // Override Filter CHain
    // localhost:8080/ <-- Index is now available for EVERYONE
    // But - what's happening with /login?
    // TODO - Question - Why doesn't ("/login").permitAll() <-- work?
    // TODO - Question - FormLogin.html, where is /login?
    // TODO - Question - Do you want this class in .gitignore?
    // TODO - Question #2 - What does anyRequest & Authenticated, do that isn't done by default?
    // TODO - Question #8 - Bean alternative to Autowired

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/user/*").permitAll()     // TODO - /register Post Permission? Cause: Might be GET permissions ,Security Check
                        // .requestMatchers("/user/**")                          // TODO - This will allow ADMINS to enter localhost:8080/user <-- NOT GOOD
                        .requestMatchers(HttpMethod.GET,"/api/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/**").hasAuthority(UserPermission.DELETE.getPermission())
                        .requestMatchers("/admin").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/user").hasRole(UserRole.USER.name()) // TODO - ADMIN CAN ENTER
                        // .requestMatchers("/admin").hasAuthority(UserPermission.DELETE.getPermission()) // TODO ROLE_ not necessary here?
                        .anyRequest().authenticated()
                )

                .formLogin(withDefaults());

        return http.build();
    }

    /*
    // DEBUG USER
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .passwordEncoder(passwordEncoder::encode) // TODO THE CHANGE
                .username("benny")
                .password("123") // TODO THE CHANGE
                .authorities(UserRole.USER.getAuthorities()) // ROLE + Permissions
                .build();

        System.out.println("PASSWORD FOR DEBUG USER " + user.getPassword());

        return new InMemoryUserDetailsManager(user);
    }

     */

}
