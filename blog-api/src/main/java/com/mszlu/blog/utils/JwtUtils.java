package com.mszlu.blog.utils;


import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class JwtUtils {
    private static final String jwtToken = "123456Mszlu";

    /**
     * 创建token
     *
     * @param userId
     * @return
     */
    public static String createToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        log.info("userId： " + userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 60 * 1000));
        String token = jwtBuilder.compact();
        log.info("token: " + token);
        return token;
    }


    /**
     * 解密
     * @param token
     * @return
     */
    public static Map<String, Object> checkToken(String token) {

            try {
                Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
                return (Map<String, Object>) parse.getBody();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }



    // @Test
    // public void mainTest() {
    //     long num =1 ;
    //     String token = JWTUtils.createToken(num);
    //     System.out.println(token);
    //     Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
    //     System.out.println(stringObjectMap.get("userId"));
    // }


}
