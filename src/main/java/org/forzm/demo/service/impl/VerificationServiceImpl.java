package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.forzm.demo.exception.ExpiredVerificationTokenException;
import org.forzm.demo.exception.VerificationTokenException;
import org.forzm.demo.model.User;
import org.forzm.demo.model.VerificationToken;
import org.forzm.demo.repository.UserRepository;
import org.forzm.demo.repository.VerificationTokenRepository;
import org.forzm.demo.service.MailSendingService;
import org.forzm.demo.service.VerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class VerificationServiceImpl implements VerificationService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final MailSendingService mailSendingService;

    @Override
    @Transactional
    public String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setTokenDuration(Instant.now().plusMillis(3600000L));
        verificationTokenRepository.save(verificationToken);

        return token;
    }

    @Override
    @Transactional
    public void verifyUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findVerificationTokenByToken(token)
                .orElseThrow(() -> new VerificationTokenException("Token is invalid"));

       if(verificationToken.getTokenDuration().isAfter(Instant.now())) {
           String username = verificationToken.getUser().getUsername();
           User user = userRepository.findByUsername(username)
                   .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
           user.setEnabled(true);
           verificationTokenRepository.deleteAllByUserUsername(user.getUsername());
       } else if(verificationToken.getTokenDuration().isBefore(Instant.now())) {
           throw new ExpiredVerificationTokenException("Verification Token is expired");
       }
    }

    @Override
    @Transactional
    public void resendVerificationMail(String username, String email) {
        if(verificationTokenRepository.findAllByUserUsername(username).size() < 3) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = generateVerificationToken(user);

            sendMail(email, token);
        } else {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
        }
    }


    @Override
    @Transactional
    @Scheduled(fixedRate = 17200000)
    public void deleteExpiredVerificationsTokens() {
        verificationTokenRepository.deleteAllByTokenDurationLessThan(Instant.now());
    }

    @Override
    public void sendMail(String to, String token) {
        mailSendingService.sendVerificationMail(to, token);
    }
}
