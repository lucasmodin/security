package org.example.springsecurity01.controller.config;

import org.example.springsecurity01.filter.RequestValidationBeforeFilter;
import org.example.springsecurity01.model.Customer;
import org.example.springsecurity01.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

//@Configuration
public class ProjectSecurityConfig {
/*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    }

    @Bean
    public CommandLineRunner createDefaultUser(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "admin@example.com";
            if(customerRepository.findByEmail(email).isEmpty()) {
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
        //the csrf token should not be disabled.
        //http.csrf(csrfConfig -> csrfConfig.disable())
        //instead, ignore the endpoints which doesn't require the user to be logged in
        http.csrf((csrf) -> csrf.ignoringRequestMatchers("/contact", "/register"))
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
        .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/myAccount").hasRole("ADMIN")
                .requestMatchers("/myBalance").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/contact", "/register").permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();

    }

*/
}
