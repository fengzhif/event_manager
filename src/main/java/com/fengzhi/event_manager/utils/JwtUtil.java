package com.fengzhi.event_manager.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    // 自定义密钥（必须保密！）
    private static final String SECRET = "MySuperSecretKey123!@#";

    // 签名算法（HS256）
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    // 过期时间（1小时）
    private static final long EXPIRATION_TIME = 60 * 60 * 1000;

    /**
     * 生成 JWT Token
     *
     * @param claims 需要存储的数据（如 userId、role）
     * @return 生成的 JWT
     */
    public static String generateToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withIssuedAt(new Date()) // 签发时间
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .sign(ALGORITHM); // 签名
    }

    /**
     * 解析 JWT Token
     *
     * @param token JWT 字符串
     * @return 解析后的 DecodedJWT 对象
     */
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

    /**
     * 校验 JWT 是否有效
     *
     * @param token JWT 字符串
     * @return 是否有效（true/false）
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
