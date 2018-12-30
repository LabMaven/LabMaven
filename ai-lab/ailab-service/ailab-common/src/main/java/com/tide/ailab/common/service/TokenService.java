package com.tide.ailab.common.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tide.ailab.common.config.Config;
import com.tide.ailab.common.log.Logger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Token处理
 * @author User
 */
@Service
public class TokenService {

    @Autowired
    private Config config;

    public Claims parseJWT(String jsonWebToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(config.getBase64Security()))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        } catch (Exception ex) {
            Logger.e(ex);
            return null;
        }
    }

    public String createJWT(String userId, String userName) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //生成签名密钥 就是一个base64加密后的字符串？
        String secret = config.getBase64Security();
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", userName);
        jsonObject.put("userId", userId);
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setIssuedAt(now) //创建时间
                .setSubject(jsonObject.toString()) //主题，也差不多是个人的一些信息
                //                .setIssuer(issuer) //发送谁
                //                .setAudience(audience) //个人签名
                .signWith(signatureAlgorithm, signingKey); //估计是第三段密钥
        //添加Token过期时间, token.expiry.time配置参数的单位是s
        if (config.getExpiryTime() >= 0) {
            //过期时间
            long expMillis = nowMillis + config.getExpiryTime() * 1000;
            //现在是什么时间
            Date exp = new Date(expMillis);
            //系统时间之前的token都是不可以被承认的
            builder.setExpiration(exp).setNotBefore(now);
        }
        //生成JWT
        return builder.compact();
    }
}
