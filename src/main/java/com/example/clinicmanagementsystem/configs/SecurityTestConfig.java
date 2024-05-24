//package com.example.clinicmanagementsystem.configs;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
//
//@Configuration
//@EnableWebSecurity
//@Profile("test")
//public class SecurityTestConfig {
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        //@formatter:off
//        httpSecurity.authorizeHttpRequests(auths -> {
//
//                    auths.requestMatchers(antMatcher(HttpMethod.GET,"/js/**"),
//                                    antMatcher(HttpMethod.GET, "/css/**"),
//                                    antMatcher(HttpMethod.GET, "/media/**"),
//                                    antMatcher(HttpMethod.GET, "/"),
//                                    antMatcher(HttpMethod.GET,"/signup"),
//                                    antMatcher(HttpMethod.POST,"/api/auth/**")).permitAll()
//                            .anyRequest().authenticated();
//                })
//                .formLogin(formLogin -> formLogin.loginPage("/signin").permitAll().defaultSuccessUrl("/",true).failureUrl("/signin?error=true"))
//
//                .logout(logout-> logout.permitAll())
//
//                .exceptionHandling(exception ->
//                        exception.authenticationEntryPoint(((request, response, authException) -> {
//                            if (request.getRequestURI().startsWith("/api")){
//                                response.setStatus(HttpStatus.FORBIDDEN.value());
//                            }else{
//                                response.sendRedirect(request.getContextPath()+"/signin");
//                            }
//
//                        })));
//
//        //@formatter:on
//        //  httpSecurity.csrf(AbstractHttpConfigurer::disable);
//        return httpSecurity.build();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
