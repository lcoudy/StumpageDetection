package com.stumdet.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Utils: JWTUtils
 * Author: Yuyang Zhao
 */
public class TokenUtils {
    private String sKey = "token!";
    private String alg = "HS256";
    private String typ = "JWT";
    private int lifeSpan = 24 * 3600;

    public TokenUtils setHeader(String alg, String typ){
        this.alg = alg;
        this.typ = typ;
        return this;
    }

    public TokenUtils setLifeSpan(int span){
        this.lifeSpan = span;
        return this;
    }

    public TokenUtils setSecretKey(String sKey){
        this.sKey = sKey;
        return this;
    }

    /**
     * 所有类型的payload在存入时都转成String
     * @param claims
     * @return
     */
    public String createToken(Map<String, Object> claims){
        Map<String, Object> header = new HashMap<>();
        header.put("alg", this.alg);
        header.put("typ", this.typ);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, this.lifeSpan);

        JWTCreator.Builder tokenBuilder = JWT.create()
                .withHeader(header)
                .withExpiresAt(instance.getTime());
        for (Object key : claims.keySet().toArray()) {
            tokenBuilder.withClaim((String)key, String.valueOf(claims.get(key)));
        }
        String token = tokenBuilder.sign(Algorithm.HMAC256(this.sKey));

        return token;
    }

    /**
     * 所有类型的payload都返回String，要用到的时候再自行转换
     * @param token token
     * @param key 键
     * @return
     */
    public String getPayload(String token, String key){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(this.sKey)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        return String.valueOf(decodedJWT.getClaim(key).asString());
    }

}
