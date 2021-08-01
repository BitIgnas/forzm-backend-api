package org.forzm.demo.config;

import lombok.AllArgsConstructor;
import org.forzm.demo.model.User;
import org.forzm.demo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import java.time.Instant;

@Configuration
@AllArgsConstructor
public class AppConfig implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("igna@gmg.com");
        user.setEnabled(true);
        user.setDateCreated(Instant.now());

        userRepository.save(user);
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
