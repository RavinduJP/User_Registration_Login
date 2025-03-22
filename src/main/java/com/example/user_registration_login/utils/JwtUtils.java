package com.example.user_registration_login.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.user_registration_login.dto.requestDto.tokenRequest.TokenRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.validity}")
    private int jwtValidity;

//    @Value("")
    private int refreshValidity;

    public String generateToken(TokenRequest tokenRequest) {
        log.info("Secret key {}", jwtSecret.getBytes());
        System.out.println(jwtSecret.getBytes());
        return JWT.create()
                .withSubject(tokenRequest.getEmail())
                .withIssuedAt(Date.from(tokenRequest.getNow().atZone(ZoneId.systemDefault()).toInstant()))
                .withIssuer("ExampleUser")
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtValidity * 1000L))
                .sign(Algorithm.HMAC256(jwtSecret.getBytes()));

//                Jwts.builder()
//                .setSubject(tokenRequest.getEmail())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtValidity * 1000L))
//                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
//                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            Long expirationTIme = decodedJWT.getClaim("exp").asLong();
            long currentTime = System.currentTimeMillis() / 1000; // covert to seconds

            if ((expirationTIme != null) && (expirationTIme >= currentTime)) {
                log.info("isValidToken -> JWT token is not expired.");
                return true;
            } else {
                log.info("isValidToken -> JWT token is expired.");
            }
        } catch (JWTDecodeException e) {
            log.warn("isValidToken -> Invalid JWT format. ");
        } catch (TokenExpiredException e) {
            log.warn("isValidToken -> Token is expired. ");
        }
        return false;
    }

    public String refreshToken(TokenRequest tokenRequest) {
        return JWT.create()
                .withSubject(tokenRequest.getEmail())
                .withIssuedAt(Date.from(tokenRequest.getNow().atZone(ZoneId.systemDefault()).toInstant()))
                .withIssuer("ExampleUser")
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshValidity * 1000L))
                .sign(Algorithm.HMAC256(jwtSecret.getBytes()));
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        log.info("username {}", decodedJWT.getSubject());
        return decodedJWT.getSubject();
    }
}
