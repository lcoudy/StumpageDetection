package com.stumdet.service;

import com.stumdet.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service: JWTService
 * Author: Yuyang Zhao
 */
@Service
@Scope("prototype")
public class JWTService {

    @Value("${core.jwt.secret-key}")
    private String secretKey;   //密钥

    private TokenUtils tokenUtils = new TokenUtils();

    /**
     * 获得JWT
     * @param claims 有效载荷
     * @param span 过期时间
     * @return
     */
    public String getJWT(Map<String, Object> claims, int span){
        this.tokenUtils.setLifeSpan(span)
                .setSecretKey(this.secretKey);
        return this.tokenUtils.createToken(claims);
    }

    public String getPayload(String token, String key){
        this.tokenUtils.setSecretKey(this.secretKey);
        return this.tokenUtils.getPayload(token, key);
    }

    @Deprecated
    public void _setSecretKey(String sKey){
        this.secretKey = sKey;
    }

}
