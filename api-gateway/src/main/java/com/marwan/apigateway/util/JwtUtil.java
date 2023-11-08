package com.marwan.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {


    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

//    public boolean isManager(String jwtToken) {
//        try {
//
//            Claims claims = extractAllClaims(jwtToken);
//
//            // Check if the "role" claim is equal to "MANAGER"
//            String roleClaim = (String) claims.get("role");
//            return "CUSTOMER".equals(roleClaim);
//        } catch (Exception e) {
//            // Handle token parsing or validation errors
//            System.out.println("Fe haga ghalat fe is manager!!!!!!!!!!!!!!!!!!!!!!!!");
//            return false;
//        }
//    }



    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
