package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MyConfig {

    // UserDetailsService Bean
    @Bean
    public UserDetailsService getUserDetailService() {
        return new UserDetailsServiceImpl();
    }

    // Password Encoder Bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DaoAuthenticationProvider Bean
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    // Security Filter Chain Configuration (New Approach for Spring Security 5.7+)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/user/**").hasRole("USER")
            .requestMatchers("/**").permitAll()
            .and()
            .formLogin()
            .loginPage("/signin")
            .loginProcessingUrl("/dologin")
            .defaultSuccessUrl("/user/index")
            .and()
            .csrf().disable();  // Disable CSRF (for development purposes only)

        return http.build();
    }

    // Method to configure AuthenticationManagerBuilder
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}
