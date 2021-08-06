package com.exerciseApp.exercise.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {

    private String secretKey = "RbTRfiMF08xzso8DdxGxf6fELlmnI0";

    private final long tokenValidTime = 60 * 60 * 1000L * 2L;

    private final String AUTH = "Authorization";
    @PostConstruct
    protected void init() {
        secretKey = Base64.encodeBase64String(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 토큰 생성
    public String createToken(String SSID) {
        Claims claims = Jwts.claims().setSubject(SSID); // JWT payload 에 저장되는 정보단위
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)
                // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();
    }

    // 토큰에서 회원 정보 추출
    public String getMemberSSID(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
