package org.example.springsecurity01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity(debug=true)
public class SpringSecurity01Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurity01Application.class, args);
    }

}
