package com.mashreq.room.conference.booking.util;

import com.mashreq.room.conference.booking.exceptions.RoomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
class JwtUtilTest {

    @Value("${jwt.secret}")
    public String   secretKey;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateTokenReturnsValidToken() {
        String username = "testUser";


        String token = jwtUtil.generateToken(username);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtUtil.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, claims.getSubject());
    }

    @Test
    void extractClaimsReturnsCorrectClaims() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 ))
                .signWith(jwtUtil.getSecretKey())
                .compact();


        Claims claims = jwtUtil.extractClaims(token);

        assertEquals("testUser", claims.getSubject());
    }

    @Test
    void extractUsernameReturnsCorrectUsername() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 ))
                .signWith(jwtUtil.getSecretKey())
                .compact();


        String username = jwtUtil.extractUsername(token);

        assertEquals("testUser", username);
    }

    @Test
    void isTokenExpiredReturnsTrueForExpiredToken() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60))
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60))
                .signWith(jwtUtil.getSecretKey())
                .compact();


      assertThrows(ExpiredJwtException.class, () -> {
          jwtUtil.isTokenExpired(token);
        });


    }

    @Test
    void validateTokenReturnsTrueForValidToken() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 ))
                .signWith(jwtUtil.getSecretKey())
                .compact();


        boolean isValid = jwtUtil.validateToken(token, "testUser");

        assertTrue(isValid);
    }

    @Test
    void validateTokenReturnsFalseForInvalidUsername() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 ))
                .signWith(jwtUtil.getSecretKey())
                .compact();


        boolean isValid = jwtUtil.validateToken(token, "wrongUser");

        assertFalse(isValid);
    }
}