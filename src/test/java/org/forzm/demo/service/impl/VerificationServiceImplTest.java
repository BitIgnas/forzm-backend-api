package org.forzm.demo.service.impl;

import org.forzm.demo.model.User;
import org.forzm.demo.model.VerificationToken;
import org.forzm.demo.repository.UserRepository;
import org.forzm.demo.repository.VerificationTokenRepository;
import org.forzm.demo.service.MailSendingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerificationServiceImplTest {

    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MailSendingService mailSendingService;
    @InjectMocks
    private VerificationServiceImpl verificationService;
    @Captor
    private ArgumentCaptor<VerificationToken> verificationTokenArgumentCaptor;

    @Test
    void generateVerificationToken() {
        when(verificationTokenRepository.save(any(VerificationToken.class))).thenAnswer(i -> i.getArguments()[0]);

        verificationService.generateVerificationToken(new User());
        verify(verificationTokenRepository, times(1)).save(any(VerificationToken.class));
        verifyNoMoreInteractions(verificationTokenRepository);
    }

    @Test
    void verifyUser() {
        User user = new User(1L, "test", "test", "test", true, null, null);
        VerificationToken verificationToken = new VerificationToken(1L, "TEST-Token", user, Instant.now().plusSeconds(360000));

        when(verificationTokenRepository.findVerificationTokenByToken(anyString())).thenReturn(Optional.of(verificationToken));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        doNothing().when(verificationTokenRepository).delete(any(VerificationToken.class));

        verificationService.verifyUser("TEST-TOKEN");

        verify(verificationTokenRepository, times(1)).delete(any(VerificationToken.class));
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(verificationTokenRepository, times(1)).findVerificationTokenByToken(anyString());
        verifyNoMoreInteractions(verificationTokenRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteExpiredVerificationsTokens() {
    }

    @Test
    void sendMail() {
        doNothing().when(mailSendingService).sendVerificationMail(anyString(), anyString());
        verificationService.sendMail("TO", "TOKEN");

        verify(mailSendingService, times(1)).sendVerificationMail(anyString(), anyString());
        verifyNoMoreInteractions(mailSendingService);
    }
}