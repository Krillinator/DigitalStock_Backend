package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.config.security;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserPermission;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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
    // TODO - Question #11 - Will static be found? It's inside the Java folder!

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public AppSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable) // TODO - Remove disable, in production
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/user/*", "/static/**", "/logout", "/custom-logout").permitAll()     // TODO - /register Post Permission? Cause: Might be GET permissions ,Security Check
                        // .requestMatchers("/user/**")                                      // TODO - This will allow ADMINS to enter localhost:8080/user <-- NOT GOOD
                        .requestMatchers(HttpMethod.GET,"/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/**").permitAll()     // New implementation
                        .requestMatchers(HttpMethod.DELETE,"/api/**").hasAuthority(UserPermission.DELETE.getPermission())
                        .requestMatchers("/admin").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/user").hasRole(UserRole.USER.name())     // TODO - ADMIN CAN ENTER
                        // .requestMatchers("/admin").hasAuthority(UserPermission.DELETE.getPermission()) // TODO ROLE_ not necessary here?
                        .anyRequest().authenticated()
                )

                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                                .loginPage("/login")
                        // TODO - Implement redirecting on SUCCESS & FAILURE
                )

                .logout(logoutConfigurer -> logoutConfigurer
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)      // TODO - Should Clear Authentication?
                        .deleteCookies("remember-me", "JSESSIONID")
                        .logoutUrl("/custom-logout")           // TODO - Endpoint for logging out?
                )

                // TODO - Parameter for cookies 'secure'
                .rememberMe(rememberMeConfigurer -> rememberMeConfigurer
                        .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))   // Validity from DAYS to Seconds TODO - Do we want to SHOW this information?
                        .key("someSecureKey")                                         // TODO - generate secure key/token
                        .userDetailsService(customUserDetailsService)
                        .rememberMeParameter("remember-me")             // Default name
                );

        return http.build();
    }

    /* TODO - List of What is being overridden through formLogin.loginPage():
        / login GET - the login form
        / login POST - process the credentials and if valid authenticate the user
        / login?error GET - redirect here for failed authentication attempts
        / login?logout GET - redirect here after successfully logging out
            If "/ authenticate" was passed to this method it update the defaults as shown below:
            / authenticate GET - the login form
            / authenticate POST - process the credentials and if valid authenticate the user
            / authenticate?error GET - redirect here for failed authentication attempts
            / authenticate?logout GET - redirect here after successfully logging out
    *
    *
    * */

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
