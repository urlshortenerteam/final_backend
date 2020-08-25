package org.reevoo.url.util;

import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtil {
    private static final long EXPIRE_TIME = 15 * 60 * 1000;

    private static final String TOKEN_SECRET_ENV = System.getenv("JASYPT_ENCRYPTOR_PASSWORD");
    private static final String TOKEN_SECRET_PRO = System.getProperty("JASYPT_ENCRYPTOR_PASSWORD");
    private static final String TOKEN_SECRET = (TOKEN_SECRET_ENV == null ? TOKEN_SECRET_PRO : TOKEN_SECRET_ENV) + "12";

    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.decodeBase64(TOKEN_SECRET);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public static String sign(long id, String username, int type, boolean isRefresh) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expTime = new Date(isRefresh ? nowMillis + EXPIRE_TIME + 10 * 60 * 1000 : nowMillis + EXPIRE_TIME);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        JwtBuilder builder = Jwts.builder()
                .claim("id", id)
                .claim("username", username)
                .claim("role", type)
                .claim("isRefresh", isRefresh)
                .setSubject(username)
                .setIssuer("user")
                .setIssuedAt(now)
                .setExpiration(expTime)
                .signWith(signatureAlgorithm, secretKey);
        return builder.compact();
    }

    public static boolean verify(String token) {
        try {
            parseJWT(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Claims parseJWT(String jwt) {
        SecretKey key = generalKey();
        return Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
    }
}
