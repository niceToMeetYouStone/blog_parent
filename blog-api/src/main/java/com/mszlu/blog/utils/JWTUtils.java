package com.mszlu.blog.utils;


import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    private static final String jwtToken = "123456Mszlu!@#$$";

    /**
     * 创建token
     *
     * @param userId
     * @return
     */
    public static String createToken(Long userId) {
        // Map<String,Object> claims = new HashMap<>();
        // claims.put("userId",userId);
        // JwtBuilder jwtBuilder = Jwts.builder()
        //         .signWith(SignatureAlgorithm.HS256, jwtToken)
        //         .setClaims(claims).setIssuedAt(new Date())
        //         .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 60 * 1000));
        // String token = jwtBuilder.compact();
        // System.out.println(token+"---------------------");
        String  token = "$2a$10$RZECQ90DjOT/t1mhnXsl5.XSuZWc0Sa1XduPxj2rb4yaSYcje3nWW";
        return token;
    }



    //TODO 加密有异常，待解决
    public static Map<String, Object> checkToken(String token) {

        //     try {
        //         Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
        //         return (Map<String, Object>) parse.getBody();
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        //     return null;
        // }

        Map<String,Object> map = new HashMap<>();
        map.put("1","admin");
        return map;
    }

    @Test
    public void mainTest() {
        long num =1 ;
        String token = JWTUtils.createToken(num);
        System.out.println(token);
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        System.out.println(stringObjectMap.get("userId"));
    }


}
