package org.forzm.demo.repository;

import org.forzm.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql("classpath:user-test-data.sql")
    void findByUsername() {
        Optional<User> userOptional = userRepository.findByUsername("test");

        assertThat(userOptional).isNotNull();
        assertThat(userOptional).isInstanceOf(Optional.class);
        assertThat(userOptional.get().getUsername()).isEqualTo("test");
        assertThat(userOptional.get().getEmail()).isEqualTo("test@gmail.com");
        assertThat(userOptional.get().getPassword()).isEqualTo("test");
    }
}