package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.entity.RefreshToken;
import com.taskmaster.taskmaster.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    private static final Integer TOKEN_VALIDITY = 1000 * 60 * 10; // 10 minutes
    private static final Integer REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7; // 7 days

    private final UserDetailsService userDetailsService;

    private final RefreshTokenRepository refreshTokenRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
            .signWith(getSignInKey())
            .compact();
    }

    public String generateRefreshToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
            .signWith(getSignInKey())
            .compact();
    }

    public boolean isRefreshTokenValid(String token) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Refresh token not found!"));

        refreshTokenRepository.deleteByUserAndExpiredAtBefore(storedRefreshToken.getUser(),LocalDateTime.now());

        return !storedRefreshToken.getExpiredAt().isBefore(LocalDateTime.now());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] bytes = Base64.getDecoder()
            .decode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return new SecretKeySpec(bytes, "HmacSHA256");
    }

}
