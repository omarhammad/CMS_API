package com.example.clinicmanagementsystem.configs;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //@formatter:off
        httpSecurity.authorizeHttpRequests(auth -> {

            auth.requestMatchers(
                        antMatcher(HttpMethod.GET,"/js/**"),
                        antMatcher(HttpMethod.GET, "/css/**"),
                        antMatcher(HttpMethod.GET, "/media/**"),
                        antMatcher(HttpMethod.GET,"/webjars/**"),
                        antMatcher(HttpMethod.GET, "/"),
                        antMatcher(HttpMethod.GET,"/signup"),
                        antMatcher(HttpMethod.POST,"/api/auth/**")
                    ).permitAll()

                    // for Client request.
                    .requestMatchers(
                            "/api/doctors"
                    ).permitAll()
                    .requestMatchers(
                            "/doctors/details/{id}",
                            "/api/doctors/{id}"
                    ).permitAll()

                    .anyRequest().authenticated();
        })
                .formLogin(formLogin -> formLogin.loginPage("/signin").permitAll().defaultSuccessUrl("/",true).failureUrl("/signin?error=true"))

                .logout(logout-> logout.permitAll())

                .exceptionHandling(exception -> exception.authenticationEntryPoint(((request, response, authException) -> {
                    if (!response.isCommitted()) {
                        if (request.getRequestURI().startsWith("/api")) {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                        } else {
                            response.sendRedirect(request.getContextPath() + "/signin");
                        }
                    }
                })));


        //@formatter:on
        // this is for the separate Client project
        httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers(
                antMatcher(HttpMethod.POST, "/api/doctors")
        ));
        return httpSecurity.build();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final @NotNull CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:9000")
                        .allowedMethods(
                                HttpMethod.GET.name(),
                                HttpMethod.POST.name());
            }
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
