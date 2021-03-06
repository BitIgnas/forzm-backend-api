package org.forzm.demo.repository;

import org.forzm.demo.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findVerificationTokenByToken(String token);
    void deleteAllByTokenDurationLessThan(Instant currentDate);
    void deleteAllByUserUsername(String username);
    List<VerificationToken> findAllByUserUsername(String username);
    Optional<VerificationToken> findFirstByUserUsername(String username);
}
