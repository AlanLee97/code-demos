package top.alanlee.template.util;

import io.jsonwebtoken.*;
import org.springframework.util.Base64Utils;
import top.alanlee.template.entity.Audience;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

public class JwtTokenUtil {
    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 解析Jwt字符串
     * @param jwtString
     * @param base64String
     * @return
     */
    public static Claims parseJwt(String jwtString, String base64String){
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.printBase64Binary(base64String.getBytes()))
                    .parseClaimsJws(jwtString).getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            System.err.println("token已过期");
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 创建Jwt字符串
     * @param userId
     * @param username
     * @param role
     * @param audience
     * @return
     */
    public static String createJwt(Integer userId, String username, String role, Audience audience){
        try {
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

            byte[] base64Binary = DatatypeConverter.parseBase64Binary(audience.getBase64Secret());
            SecretKeySpec secretKeySpec = new SecretKeySpec(base64Binary, signatureAlgorithm.getJcaName());

            JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                    .claim("role", role)
                    .claim("userId", userId.toString())
                    .setSubject(username)
                    .setIssuer(audience.getClientId())
                    .setIssuedAt(new Date())
                    .setAudience(audience.getName())
                    .signWith(signatureAlgorithm, secretKeySpec);
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            //添加Token过期时间
            int TTLMillis = audience.getExpireSecond();
            if (TTLMillis >= 0) {
                long expMillis = nowMillis + TTLMillis;
                Date exp = new Date(expMillis);
                builder.setExpiration(exp)  // 是一个时间戳，代表这个JWT的过期时间；
                        .setNotBefore(now); // 是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的
            }
            return builder.compact();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("签名失败");
        }
        return null;
    }

    /**
     * 获取用户ID
     * @param token
     * @param base64String
     * @return
     */
    public static String getUserId(String token, String base64String){
        String userId = parseJwt(token, base64String).get("userId", String.class);
        return new String(Base64Utils.decode(userId.getBytes()));
    }

    /**
     * 获取用户名
     * @param token
     * @param base64String
     * @return
     */
    public static String getUsername(String token, String base64String){
        return parseJwt(token, base64String).getSubject();
    }

    /**
     * 判断是否过期
     * @param token
     * @param base64String
     * @return
     */
    public static boolean isExpiration(String token, String base64String){
        return parseJwt(token, base64String).getExpiration().before(new Date());
    }
}
