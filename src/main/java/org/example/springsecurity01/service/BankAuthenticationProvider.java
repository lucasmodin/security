package org.example.springsecurity01.service;

import org.example.springsecurity01.model.Customer;
import org.example.springsecurity01.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate (Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        System.out.println("loadUser kaldt: user=" + username);
        Optional<Customer> customer = null;
        try {
            customer = customerRepository.findByEmail(username);
        }catch (Exception ex) {
            System.out.println("Database fejl =" + ex.getMessage());
        }
        if(customer.isPresent()) {
            if(passwordEncoder.matches(pwd, customer.get().getPwd())) {
                List<GrantedAuthority> authorityList = new ArrayList<>();
                authorityList.add(new SimpleGrantedAuthority(customer.get().getRole()));
                return new UsernamePasswordAuthenticationToken(username, pwd, authorityList);
            } else {
                throw new BadCredentialsException("invalid password");
            }
        } else {
            throw new BadCredentialsException("No user registered with these details");
        }
    }



    @Override
    public boolean supports(Class<?> authentication) {
        return
                (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }


}
