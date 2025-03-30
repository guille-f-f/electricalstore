package com.electricalstore.electricalstore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityWeb {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("Entry to filterChain");
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll() // static documents
                .requestMatchers("/", "/login", "/register", "/image/**", "/error", "/error/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/logincheck")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/index", true)
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(customizer -> customizer
                        .accessDeniedHandler(customAccessDeniedHandler()) // errors 403
                        .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/error")) // authetication errors
                );

        return http.build();
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendRedirect("/error"); // redirect to /error
        };
    }

    // This method allows access to thymeleaf data
    // th:src="@{/images/user/{id}(id=${session.usersession.id})}
    @Configuration
    public class ThymeleafConfig {
        @Bean
        public SpringSecurityDialect securityDialect() {
            return new SpringSecurityDialect();
        }
    }

}
