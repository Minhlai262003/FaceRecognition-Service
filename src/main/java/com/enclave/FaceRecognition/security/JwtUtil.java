//package com.enclave.FaceRecognition.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.stereotype.Component;
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//
//    @Value("${jwt.signerKey}")
//    private String jwtSecret;
//
//    @Value("${jwt.access-duration}")
//    private long accessTokenExpirationMs;
//
//    @Value("${jwt.refresh-duration}")
//    private long refreshTokenExpirationMs;
//
//    private SecretKey key;
//
//    @PostConstruct
//    public void init() {
//        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
//    }
//
//    public String generateAccessToken(String username) {
//        return generateToken(username, accessTokenExpirationMs);
//    }
//
//    public String generateRefreshToken(String username) {
//        return generateToken(username, refreshTokenExpirationMs);
//    }
//
//    private String generateToken(String username, long expirationMs) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String getUsernameFromToken(String token) {
//        JwtParser parser = Jwts.parser().verifyWith(key).build();
//        return parser.parseSignedClaims(token).getPayload().getSubject();
//    }
//
//    public Date getExpirationFromToken(String token) {
//        return Jwts.parser()
//                .verifyWith(key)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getExpiration();
//    }
//
//    public boolean validateJwtToken(String token) {
//        try {
//            Jwts.parser()
//                    .verifyWith(key)
//                    .build()
//                    .parseSignedClaims(token);
//            return true;
//        } catch (BadCredentialsException e) {
//            throw new BadCredentialsException("Invalid or expired JWT token");
//        }
//    }
//}
