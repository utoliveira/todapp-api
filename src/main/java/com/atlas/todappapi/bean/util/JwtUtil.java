package com.atlas.todappapi.bean.util;

import com.atlas.todappapi.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("{jwt.secret}")
    private String secretKey;

    private int expirationTime = 1000 * 60 * 60 * 10 * 10;

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, User user) {
        final String id= extractUserId(token);

        return user != null
                && user.getId() != null
                && user.getId().toString().equals(id)
                && !isTokenExpired(token);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
        if(user == null){return null;}
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user.getId());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,  Claims::getExpiration);
    }

    /**
     * @param claims everything that will be included in the payload
     * @param id this case is userId
     * @return
     */
    private String createToken(Map<String, Object> claims, Long id) {
        return Jwts.builder().setClaims(claims).setSubject(id.toString()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

}
