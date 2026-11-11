package de.ait.myliferando.service;

import de.ait.myliferando.domain.User;
import de.ait.myliferando.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = repository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("Username " + username + " not found"));

        return org.springframework.security.core.userdetails.User.withUsername(
                        user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

    }
}
