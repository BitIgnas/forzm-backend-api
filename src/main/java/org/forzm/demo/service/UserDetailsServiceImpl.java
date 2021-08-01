package org.forzm.demo.service;

import lombok.AllArgsConstructor;
import org.forzm.demo.model.User;
import org.forzm.demo.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepositorygit.findUserByUsername(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User was not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(user.isEnabled())
                .authorities(new SimpleGrantedAuthority("USER"))
                .credentialsExpired(false)
                .accountExpired(false)
                .accountLocked(false)
                .build();
    }
}
