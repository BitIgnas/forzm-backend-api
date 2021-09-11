package org.forzm.demo.service;

import org.forzm.demo.model.User;

public interface VerificationService {
    String generateVerificationToken(User user);
    void verifyUser(String token);
    void resendVerificationMail(String username, String email);
    void sendMail(String to, String token);
    void deleteExpiredVerificationsTokens();

}
