package org.reins.url.util;

import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/*借鉴https://blog.csdn.net/AdminGuan/article/details/100147488#JWT%E6%98%AF%E4%BB%80%E4%B9%88%EF%BC%9F*/
@Component
public class JwtUtil {

  // 过期时间，单位毫秒
  private static final long EXPIRE_TIME = 15 * 60 * 1000; // 15分钟

  // 加密密文，私钥
  private static final String TOKEN_SECRET = "SXSNB";

  // 由字符串生成加密key
  public static SecretKey generalKey() {
    System.out.println("进入由字符串生成加密key方法！");
    // 本地的密码解码
    byte[] encodedKey = Base64.decodeBase64(TOKEN_SECRET);
    // 根据给定的字节数组使用AES加密算法构造一个密钥
    SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    return key;
  }

  // 生成签名
  public static String sign(long id, String username, int type, boolean isRefresh) {
    System.out.println("生成签名方法开始执行！");
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
    Date expTime = new Date(isRefresh ? nowMillis + EXPIRE_TIME + 10 * 60 * 1000 : nowMillis + EXPIRE_TIME);
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    SecretKey secretKey = generalKey();
    JwtBuilder builder = Jwts.builder()
            .claim("id", id)
            .claim("username", username) // 前端输入的用户名
            .claim("role", type)
            .claim("isRefresh", isRefresh)
            .setSubject(username)   // 主题
            .setIssuer("user")     // 签发者
            .setIssuedAt(now)      // 签发时间
            .setExpiration(expTime)
            .signWith(signatureAlgorithm, secretKey); // 签名算法以及密匙
    return builder.compact();
  }

  public static boolean verify(String token) {
    System.out.println("进入检验token是否正确方法！");
    Claims claims = null;
    try {
      claims = parseJWT(token);
      System.out.println("验证成功！");
      return true;
    } catch (ExpiredJwtException e) {
      return false;
    } catch (SignatureException e) {
      return false;
    } catch (Exception e) {
      return false;
    }
  }

//    // 获取登录名
//    public String getUsername(String token) {
//        System.out.println("进入获取登录名方法！");
//        try {
//            DecodedJWT jwt = JWT.decode(token);
//            return jwt.getClaim("username").asString();
//        } catch (JWTDecodeException e) {
//            return null;
//        }
//    }

  // 解密jwt
  public static Claims parseJWT(String jwt) throws Exception {
    //System.out.println("进入解密jwt方法！");
    SecretKey key = generalKey(); // 签名秘钥，和生成的签名的秘钥一模一样
    Claims claims = Jwts.parser() // 得到DefaultJwtParser
            .setSigningKey(key) // 设置签名的秘钥
            .parseClaimsJws(jwt).getBody(); // 设置需要解析的jwt
    return claims;
  }

}
