package org.forzm.demo.service;

import org.springframework.scheduling.annotation.Async;

public interface MailSendingService {
    @Async
    void sendVerificationMail(String to, String token);
}
