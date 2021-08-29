package org.forzm.demo.repository;

import org.forzm.demo.model.VerificationToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class VerificationTokenRepositoryTest {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Test
    @Sql("classpath:verification-token-test-data.sql")
    void findVerificationTokenByToken() {
        Optional<VerificationToken> token = verificationTokenRepository.findVerificationTokenByToken("VERIFICATION-TOKEN");

        assertThat(token).isNotEmpty();
        assertThat(token.get().getToken()).isEqualTo("VERIFICATION-TOKEN");
    }


}