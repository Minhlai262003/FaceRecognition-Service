package com.enclave.FaceRecognition.service;


import com.enclave.FaceRecognition.dto.Request.AuthenticationRequest;
import com.enclave.FaceRecognition.dto.Response.AuthenticationResponse;
import com.enclave.FaceRecognition.entity.User;
import com.enclave.FaceRecognition.exception.AppException;
import com.enclave.FaceRecognition.exception.ErrorCode;
import com.enclave.FaceRecognition.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService {
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;
    @Value("${jwt.access-duration}")
    long ACCESS_DURATION;
    @Value("${jwt.refresh-duration}")
    long REFRESH_DURATION;

    final UserRepository userRepository;



    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        var verified = signedJWT.verify(verifier);
        Date expirationTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                .plus(REFRESH_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        if (!verified || expirationTime.before(new Date())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        return signedJWT;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated)
            throw new AppException(ErrorCode.INVALID_PASSWORD);

        String token = generateToken(request.getUsername());
        return AuthenticationResponse.builder()
                .accessToken(token)
                .build();
    }

    private String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.ES512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("enclave.vn")
                .claim("scope",bullScope(userRepository.findByEmail(username).orElseThrow(
                        () -> new AppException(ErrorCode.USER_NOT_EXISTED))))
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(ACCESS_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();
        JWSObject jwsObject = new JWSObject(header, new Payload(jwtClaimsSet.toJSONObject()));

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error signing JWT: {}", e.getMessage());
            throw new RuntimeException(e);
        }


    }

    private String bullScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (user.getRole() != null) {
            joiner.add(user.getRole());
        }

        return joiner.toString();
    }
}
