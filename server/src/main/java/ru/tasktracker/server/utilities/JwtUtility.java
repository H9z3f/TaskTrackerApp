package ru.tasktracker.server.utilities;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtility {
    private static final String SECRET_KEY = SystemUtility.getEnvironmentVariableOrDefault("SECRET_KEY", "q8vEwpOC~k5SGpWtQHv5{B2w1EBp#MbnBivSxftgbe8Njw%3KiUP8lUe~884cqL~{Mr9IYsQtql$SwARO|W~?0t8PLyL~}p$PQS");

    public static String generate(Long id) {
        Date creationDate = new Date();
        Date expirationDate = new Date(creationDate.getTime() + 86_400_000L);

        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(creationDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Long parse(String jwt) {
        try {
            return Long.valueOf(Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject());
        } catch (JwtException e) {
            return null;
        }
    }
}
