package com.enclave.FaceRecognition.service;


import com.enclave.FaceRecognition.dto.Request.AuthenticationRequest;
import com.enclave.FaceRecognition.dto.Request.IntrospectRequest;
import com.enclave.FaceRecognition.dto.Request.LogoutRequest;
import com.enclave.FaceRecognition.dto.Request.RefreshRequest;
import com.enclave.FaceRecognition.dto.Response.AuthenticationResponse;
import com.enclave.FaceRecognition.dto.Response.IntrospectResponse;
import com.enclave.FaceRecognition.entity.InvalidatedToken;
import com.enclave.FaceRecognition.entity.User;
import com.enclave.FaceRecognition.exception.AppException;
import com.enclave.FaceRecognition.exception.ErrorCode;
import com.enclave.FaceRecognition.exception.UserNotFoundException;
import com.enclave.FaceRecognition.repository.InvalidatedTokenRepository;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final InvalidatedTokenRepository tokenRepository;
    @Value("${jwt.access-duration}")
    private long ACCESS_TOKEN_EXP;
    @Value("${jwt.refresh-duration}")
    private long REFRESH_TOKEN_EXP;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated){
            throw new BadCredentialsException("Invalid username or password");
        }
        try {
            String accessToken = jwtService.generateToken(user, ACCESS_TOKEN_EXP);
            String refreshToken = jwtService.generateToken(user, REFRESH_TOKEN_EXP);
            return new AuthenticationResponse(accessToken, refreshToken);
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        JWTClaimsSet claims = jwtService.verifyToken(request.getToken());
        boolean isInvalidated = tokenRepository.existsById(request.getToken());

        boolean active = claims.getExpirationTime().after(new Date()) && !isInvalidated;
        return new IntrospectResponse(active, claims.getSubject(), (java.sql.Date) claims.getExpirationTime());
    }
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        JWTClaimsSet claims = jwtService.verifyToken(request.getToken());

        if (tokenRepository.existsById(request.getToken())) {
            throw new RuntimeException("Refresh token has been invalidated");
        }

        User user = userRepository.findByEmail(claims.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String newAccessToken = jwtService.generateToken(user, ACCESS_TOKEN_EXP);
        String newRefreshToken = jwtService.generateToken(user, REFRESH_TOKEN_EXP);

        return new AuthenticationResponse(newAccessToken, newRefreshToken);
    }
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        JWTClaimsSet claims = jwtService.verifyToken(request.getRefreshToken());

        InvalidatedToken token = InvalidatedToken.builder()
                .id(request.getRefreshToken())
                .expiryTime(claims.getExpirationTime())
                .build();

        tokenRepository.save(token);
    }

//    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
//        var token = request.getToken();
//        boolean isValid = true;
//
//        try {
//            verifyToken(token, false);
//        } catch (AppException e) {
//            isValid = false;
//        }
//        return IntrospectResponse.builder()
//                .valid(isValid)
//                .build();
//    }
//
//    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
//
//        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
//        SignedJWT signedJWT = SignedJWT.parse(token);
//        var verified = signedJWT.verify(verifier);
//        Date expirationTime = (isRefresh)
//                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
//                .plus(REFRESH_DURATION, ChronoUnit.SECONDS).toEpochMilli())
//                : signedJWT.getJWTClaimsSet().getExpirationTime();
//        if (!verified || expirationTime.before(new Date())) {
//            throw new AppException(ErrorCode.INVALID_TOKEN);
//        }
//        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
//            log.info("Token is invalidated");
//            throw new AppException(ErrorCode.UNAUTHENTICATED);
//        }
//        return signedJWT;
//    }
//
//    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
//        var jwtToken = verifyToken(request.getToken(), true);
//        String jit = jwtToken.getJWTClaimsSet().getJWTID();
//        Date expiryTime = jwtToken.getJWTClaimsSet().getExpirationTime();
//        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
//                .id(jit)
//                .expiryTime(expiryTime)
//                .build();
//        invalidatedTokenRepository.save(invalidatedToken);
//        var username= jwtToken.getJWTClaimsSet().getSubject();
//        userRepository.findByEmail(username).orElseThrow(()->new AppException(ErrorCode.UNAUTHENTICATED));
//        var token = generateToken(username);
//        return AuthenticationResponse.builder()
//                .accessToken(token)
//                .build();
//    }
//
//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        User user = userRepository.findByEmail(request.getUsername())
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
//
//        if (!authenticated)
//            throw new AppException(ErrorCode.INVALID_PASSWORD);
//
//        String token = generateToken(request.getUsername());
//        return AuthenticationResponse.builder()
//                .accessToken(token)
//                .build();
//    }
//
//
//    public void logout(LogoutRequest request) throws ParseException, JOSEException {
//        try {
//            var signToken = verifyToken(request.getAccessToken(), false);
//            String jit = signToken.getJWTClaimsSet().getJWTID();
//            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
//            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
//                    .id(jit)
//                    .expiryTime(expiryTime)
//                    .build();
//            invalidatedTokenRepository.save(invalidatedToken);
//            log.info("Logout success");
//        } catch (AppException exception) {
//            log.info("Token already expired");
//        }
//    }
//
//
//    private String generateToken(String username) {
//        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
//
//        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
//                .subject(username)
//                .issuer("enclave.vn")
//                .claim("scope",bullScope(userRepository.findByEmail(username).orElseThrow(
//                        () -> new AppException(ErrorCode.USER_NOT_EXISTED))))
//                .issueTime(new Date())
//                .expirationTime(new Date(Instant.now().plus(ACCESS_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
//                .jwtID(UUID.randomUUID().toString())
//                .build();
//        JWSObject jwsObject = new JWSObject(header, new Payload(jwtClaimsSet.toJSONObject()));
//
//        try {
//            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
//            return jwsObject.serialize();
//        } catch (JOSEException e) {
//            log.error("Error signing JWT: {}", e.getMessage());
//            throw new RuntimeException(e);
//        }
//
//
//    }
//
//    private String bullScope(User user) {
//        StringJoiner joiner = new StringJoiner(" ");
//        if (user.getRole() != null) {
//            joiner.add(user.getRole().name());
//        }
//
//        return joiner.toString();
//    }
}
