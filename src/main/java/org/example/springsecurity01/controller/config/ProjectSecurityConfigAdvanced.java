package org.example.springsecurity01.controller.config;

import jakarta.servlet.http.HttpServletRequest;
import org.example.springsecurity01.filter.JWTTokenGeneratorFilter;
import org.example.springsecurity01.filter.JWTTokenValidatorFilter;
import org.example.springsecurity01.model.Customer;
import org.example.springsecurity01.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class ProjectSecurityConfigAdvanced {

    @Bean
    public CommandLineRunner createDefaultUser(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "admin@example.com";
            if (customerRepository.findByEmail(email).isEmpty()) {
                Customer admin = new Customer();
                admin.setEmail(email);
                admin.setPwd(passwordEncoder.encode("admin1234"));
                admin.setRole("ROLE_ADMIN");
                customerRepository.save(admin);
                System.out.println("admin created");
            } else {
                System.out.println("admin already exists");
            }
        };
    }



    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csfr");
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                                                config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setAllowedHeaders(Arrays.asList("Authorization"));
                                                config.setMaxAge(3600L);
                                                return config;
                                                }
                })).csrf((csrf)-> csrf
                                .csrfTokenRequestHandler(requestHandler)
                                .ignoringRequestMatchers("/contact", "/register", "/dologin")
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                                                        .authorizeHttpRequests((requests) -> requests
                                                                .requestMatchers("/myAccount").hasRole("USER")
                                                                .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                                                                .requestMatchers("/myLoans").hasRole("USER")
                                                                .requestMatchers("/myCards").hasRole("USER")
                                                                .requestMatchers("/user").authenticated()
                                                                .requestMatchers("/notices", "/contact", "/register", "/dologin")
                                                                .permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
                return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }



}
