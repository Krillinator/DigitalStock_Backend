package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.config;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserPermission;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    private final AppPasswordConfig bcrypt;

    @Autowired
    public AppSecurityConfig(AppPasswordConfig bcrypt) {
        this.bcrypt = bcrypt;
    }

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
                        .requestMatchers("/", "/login").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/**").permitAll()
                        // .requestMatchers("/admin").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/user").hasRole(UserRole.USER.name())
                        .requestMatchers("/admin").hasAuthority(UserPermission.DELETE.getPermission()) // TODO ROLE_ not necessary here?
                        .anyRequest().authenticated()
                )

                .formLogin(withDefaults());

        return http.build();
    }

    // DEBUG USER
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("benny")
                .password(bcrypt.bcryptPasswordEncoder().encode("123"))
                .authorities(UserRole.USER.getAuthorities()) // ROLE + Permissions
                .build();

        return new InMemoryUserDetailsManager(user);
    }

}
