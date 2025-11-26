package com.finance.dashboard.security;

import com.finance.dashboard.model.User;
import com.finance.dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found: "+ username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))) //auth in spring security in future add more but as a list
                .build();

    }

}
