package org.example.springsecurity01.service;

import org.example.springsecurity01.model.Customer;
import org.example.springsecurity01.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BankLoadUserService  implements UserDetailsService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName, password = null;
        List<GrantedAuthority> authorities = new ArrayList<>();
        Optional<Customer> customer = customerRepository.findByEmail(username);
        if (customer.isPresent()) {
            userName = customer.get().getEmail();
            password = customer.get().getPwd();

            authorities.add(new SimpleGrantedAuthority(customer.get().getRole()));
        } else {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(userName, password, authorities);
    }
}
