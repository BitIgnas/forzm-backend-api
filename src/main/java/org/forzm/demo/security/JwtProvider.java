package org.forzm.demo.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
@AllArgsConstructor
public class JwtProvider {

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationMilli;

    @Value("${jwt.keystore.password}")
    private String keystorePassword;

    private KeyStore keyStore;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream inputStream = getClass().getResourceAsStream("/keystore.jks");
            keyStore.load(inputStream, keystorePassword.toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        }
    }

    public PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("keystore", keystorePassword.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    public PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("keystore").getPublicKey();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        return null;
    }




}
