package com.keyvalueserver.project.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil{

    public static final long JWT_TOKEN_VALIDITY = 5*60*60;

    // inject secret key from application.properties
    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims -> Claims.getSubject());
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims -> Claims.getIssuedAt());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims -> Claims.getExpiration());
    }

    /*
    getClaimFromToken first retrieves all claims from the token
    and then uses the provided claimsResolver to retrieve the specific claim value
    claimsResolver will be provided by the caller of this method
    claimsResolver acts as a decoder function for the claims, and returns the decoded value
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /*
    retrieve all claims from token by parsing the token
    claims are the payload of the token as key-value pairs
    claims stores data like issuer, expiration, subject, and the ID
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        // Date.before() returns true if this Date is before the Date argument; false otherwise.
        return expiration.before(new Date());
    }

    private Boolean ignoreTokenExpiration(String token) {
        // ignore token expiration if this method returns true
        return false;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /*
    use HS512 algorithm and secret key to sign the JWT

     */

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
