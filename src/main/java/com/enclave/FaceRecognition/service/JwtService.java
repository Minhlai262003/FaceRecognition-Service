package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.entity.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final String SIGNER_KEY;
    private final JWK jwk;
    public JwtService(@Value("${jwt.signerKey}") String signerKey) {
        this.SIGNER_KEY = signerKey;
        this.jwk = new OctetSequenceKey.Builder(signerKey.getBytes()).build();
    }


    public String generateToken(User user, long expirationMillis, String type) throws JOSEException {
        JWSSigner signer = new MACSigner(jwk.toOctetSequenceKey().toSecretKey());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .claim("role", user.getRole())
                .claim("typ", type)
                .jwtID(UUID.randomUUID().toString())
                .issueTime(now)
                .expirationTime(expiry)
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claims
        );

        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    public JWTClaimsSet verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(jwk.toOctetSequenceKey().toSecretKey());

        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Invalid JWT signature");
        }

        return signedJWT.getJWTClaimsSet();
    }
    public boolean validateJwtToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(jwk.toOctetSequenceKey().toSecretKey());
            if (!signedJWT.verify(verifier)) {
                return false;
            }
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationTime == null || expirationTime.before(new Date())) {
               return false;
            }
            return true;
        } catch (ParseException | JOSEException e) {
            throw new BadCredentialsException("Unauthorized");
        }
    }
    public String getUsernameFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject(); // subject là username (email)
        } catch (ParseException e) {
            System.out.println("❌ Failed to parse JWT: " + e.getMessage());
            return null;
        }
    }


}
