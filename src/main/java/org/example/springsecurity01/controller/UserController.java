package org.example.springsecurity01.controller;

import org.example.springsecurity01.model.Customer;
import org.example.springsecurity01.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        Customer savedCustomer = null;
        ResponseEntity response = null;
        try{
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);
            savedCustomer = customerRepository.save(customer);
            if(savedCustomer.getId() > 0){
                response = ResponseEntity.status(HttpStatus.CREATED)
                        .body("Given user details are successfully registered.");
            }

        }catch(Exception e){
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occured due to" + e.getMessage());
        }
        return response;
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        return customerRepository.findByEmail(email)
                .map(customer -> {
                    if (passwordEncoder.matches(password, customer.getEmail())) {
                        return ResponseEntity.ok("Login successful");
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password"));


    }
}
