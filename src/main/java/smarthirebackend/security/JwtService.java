// This is the UTILITY class that handles everything jwt related
package smarthirebackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// @Service makes this a Spring managed component
@Service
public class JwtService
{
    // Reads jwt.secret from application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    // Reads jwt.expiration from application.properties
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // ─── PUBLIC METHODS ───────────────────────────────────────

    // Generate token with just email (subject)
    public String generateToken(String email) {
        return generateToken(new HashMap<>(), email);
    }

    // Generate token with extra claims (like role)
    public String generateToken(Map<String, Object> extraClaims,
                                String email) {
        return Jwts.builder()
                // Extra data to include in token
                .claims(extraClaims)
                // Subject = who this token belongs to
                .subject(email)
                // When was token created
                .issuedAt(new Date(System.currentTimeMillis()))
                // When does token expire
                .expiration(new Date(System.currentTimeMillis()
                        + jwtExpiration))
                // Sign with our secret key
                .signWith(getSigningKey())
                .compact();
    }

    // Extract email from token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract role from token
    public String extractRole(String token) {
        return extractClaim(token,
                claims -> claims.get("role", String.class));
    }

    // Check if token is valid for this email
    public boolean isTokenValid(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return extractedEmail.equals(email) && !isTokenExpired(token);
    }

    // ─── PRIVATE METHODS ─────────────────────────────────────

    // Check if token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic method to extract any claim from token
    private <T> T extractClaim(String token,
                               Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parse and verify the token, extract all claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Convert secret string to cryptographic key
    private SecretKey getSigningKey() {
        byte[] keyBytes = hexStringToByteArray(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Helper - convert hex string to byte array
    private byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
