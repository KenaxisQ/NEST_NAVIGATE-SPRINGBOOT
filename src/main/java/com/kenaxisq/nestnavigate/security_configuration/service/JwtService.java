package com.kenaxisq.nestnavigate.security_configuration.service;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.security_configuration.entity.Token;
import com.kenaxisq.nestnavigate.security_configuration.repository.TokenRepository;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.mysql.cj.xdevapi.JsonArray;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSelector;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.Date;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.proc.JWSVerifierFactory;
import com.nimbusds.jose.proc.SecurityContext;


@Getter
@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long accessTokenExpire;

    @Value("${security.jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    private final TokenRepository tokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private static final String GOOGLE_JWKS_URL = "https://www.googleapis.com/oauth2/v3/certs";

    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails userDetails) {
        String userId = extractUserId(token);

        boolean validToken = tokenRepository
                .findByAccessToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return userId.equals(userDetails.getUsername()) && isTokenActive(token) && validToken;
    }

    public boolean isValidRefreshToken(String token, User user) {
        String userId = extractUserId(token);

        boolean validRefreshToken = tokenRepository
                .findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return userId.equals(user.getUsername()) && isTokenActive(token) && validRefreshToken;
    }

    private boolean isTokenActive(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(User user) {
        return generateToken(user.getUsername(), accessTokenExpire);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user.getUsername(), refreshTokenExpire);
    }

    private String generateToken(String subject, long expireTime) {
        return Jwts
                .builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigninKey())
                .compact();
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void saveToken(String accessToken,String refreshToken,User user) {
        Token newToken = new Token();
        newToken.setRefreshToken(refreshToken);
        newToken.setAccessToken(accessToken);
        newToken.setUser(user);
        newToken.setLoggedOut(false);
        tokenRepository.save(newToken);
    }

    public boolean decodeAndCheckExpirationTime(long expirationTime) {
        Instant instant = Instant.ofEpochSecond(expirationTime);
        LocalDateTime expirationDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDateTime currentDateTime = LocalDateTime.now();
        logger.info("Token expiration: " + expirationDateTime);
        return expirationDateTime.isBefore(currentDateTime);
    }

    public String decodeGoogleJwtToken(String googleJwtToken) {
        try {
            // Parse the JWT
            SignedJWT signedJWT = SignedJWT.parse(googleJwtToken);
            JWSHeader header = signedJWT.getHeader();

            URL jwkSetURL = new URL(GOOGLE_JWKS_URL);
            JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(jwkSetURL);
            JWKSet jwkSet = JWKSet.load(jwkSetURL);
            List<JWK> keys = jwkSet.getKeys();

            JWK matchedJWK = keys.stream()
                    .filter(key -> key.getKeyID().equals(header.getKeyID()))
                    .findFirst()
                    .orElseThrow(() -> new ApiException("NO_MATCHING_JWK", "No matching JWK found for key ID: " + header.getKeyID(), HttpStatus.BAD_REQUEST));

            JWSVerifier verifier = new RSASSAVerifier(matchedJWK.toRSAKey());

            // Verify the signature
            if (!signedJWT.verify(verifier)) {
                throw new ApiException("INVALID_SIGNATURE", "Invalid JWT signature", HttpStatus.UNAUTHORIZED);
            }

            // Extract the JWT claims
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Date expirationTime = claims.getExpirationTime();

            // Check if the token is expired
            if (expirationTime != null && expirationTime.before(new Date())) {
                logger.error("JWT token is expired");
                throw new ApiException("INVALID_TOKEN", "JWT token is expired", HttpStatus.UNAUTHORIZED);
            }

            // Extract email from JWT claims
            String email = claims.getStringClaim("email");
            if (email == null || email.isEmpty()) {
                throw new ApiException("INVALID_CLAIMS", "JWT token does not contain a valid email claim", HttpStatus.BAD_REQUEST);
            }

            return email;
        } catch (ParseException | IOException e) {
            logger.error("Error decoding Google JWT token: " + e.getMessage());
            throw new ApiException("INVALID_TOKEN", "Invalid Google JWT token", HttpStatus.UNAUTHORIZED);
        } catch (JOSEException e) {
            logger.error("JOSE Exception: " + e.getMessage());
            throw new ApiException("JWT_VERIFICATION_ERROR", "Error verifying JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void logPayload(Map<String, Object> payload) {
        logger.info("JWT Payload:");
        payload.forEach((key, value) -> logger.info(key + ": " + value));
    }
}