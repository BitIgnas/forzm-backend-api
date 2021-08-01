package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.forzm.demo.exception.VerificationTokenException;
import org.forzm.demo.model.User;
import org.forzm.demo.model.VerificationToken;
import org.forzm.demo.repository.UserRepository;
import org.forzm.demo.repository.VerificationTokenRepository;
import org.forzm.demo.service.MailSendingService;
import org.forzm.demo.service.VerificationService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        verificationTokenRepository.save(verificationToken);

        return token;
    }

    @Override
    @Transactional
    public void verifyUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository
                .findVerificationTokenByToken(token)
                .orElseThrow(() -> new VerificationTokenException("Token is invalid"));
       String username = verificationToken.getUser().getUsername();
       User user = userRepository.findByUsername(username)
               .orElseThrow(() -> new UsernameNotFoundException("User was not found"));

       user.setEnabled(true);
       verificationTokenRepository.delete(verificationToken);
    }

    @Override
    public void sendMail(String to, String token) {
        mailSendingService.sendVerificationMail(to, token);
    }
}
