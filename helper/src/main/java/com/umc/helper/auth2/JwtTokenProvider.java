package com.umc.helper.auth2;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    Logger logger= LoggerFactory.getLogger(JwtTokenProvider.class);
//    @Value("${jwt.secret.access}")
//    private String SECRET_KEY;
//    @Value("${jwt.secret.refresh}")
//    private String REFRESH_KEY;
    private String SECRET_KEY="236979CB6F1AD6B6A6184A31E6BE37DB3818CC36871E26235DD67DCFE404149232r32rwerf";

    private String REFRESH_KEY="afkljeoiwajtgfasjdfhwerklawejriwjrewkhrjwajfejfkjawekraewradfaer3247hfkjashf9o3wrhkjgdfakhf3e9o5y8y4hfkjhfakjdhfwkerhwkahrfklwejr1l2kjeksekf";

    //private final long ACCESS_TOKEN_VALID_TIME = 1000 * 60 * 5;
//    private final long ACCESS_TOKEN_VALID_TIME = 1 * 60 * 1000L;   // 1분
    private final long ACCESS_TOKEN_VALID_TIME = 1 * 30 * 1000L;   // 30초
    private final long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 24 * 7 * 1000L;   // 1주

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        System.out.println("SECRET_KEY: "+SECRET_KEY);
        System.out.println("REFRESH_KEY = " + REFRESH_KEY);
        //SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        //REFRESH_KEY = Base64.getEncoder().encodeToString(REFRESH_KEY.getBytes());
    }

    // JWT 토큰 생성
    public String createAccessToken(String email) {
        Claims claims = Jwts.claims();//.setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put("email", email);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME)) // set Expire Time
                .signWith(getSignKey(SECRET_KEY))  // 사용할 암호화 알고리즘과
                .compact();
    }

    private Key getSignKey(String key){
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String createRefreshToken(String email) {
        Claims claims = Jwts.claims();
        claims.put("email", email); //
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSignKey(REFRESH_KEY))
                .compact();
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveAccessToken(HttpServletRequest request) {
        return request.getHeader("ACCESS_TOKEN");
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader("REFRESH_TOKEN");
    }

    //Refresh 토큰의 DB상의 인덱스 번호를 해시로 받음
    public Long resolveRefreshIndexToken(HttpServletRequest request) {
        return Long.parseLong(request.getHeader("REFRESH_TOKEN_INDEX"));
    }

    public Claims getClaimsFormToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getClaimsToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(REFRESH_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidAccessToken(String token) {
        System.out.println("isValidAccessToken is : " +token);
        try {
            Claims accessClaims = getClaimsFormToken(token);
            System.out.println("Access expireTime: " + accessClaims.getExpiration());
            System.out.println("Access email: " + accessClaims.get("email"));
            return true;
        } catch (ExpiredJwtException exception) {
            System.out.println("Token Expired email : " + exception.getClaims().get("email"));
            return false;
        } catch (JwtException exception) {
            System.out.println("Access Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            System.out.println("Token is null");
            return false;
        }
    }
    public boolean isValidRefreshToken(String token) {
        System.out.println("isValidRefreshToken is : " +token);

        try {
            Claims refreshClaims = getClaimsToken(token);
//            Claims accessClaims = getClaimsFormToken(token);
//            logger.info("Access expireTime: {}",accessClaims.getExpiration());
//            logger.info("Access email: {}",accessClaims.get("email"));

            logger.info("Refresh expireTime: {}",refreshClaims.getExpiration());
            logger.info("Refresh email: {}",refreshClaims.get("email"));
            return true;
        } catch (ExpiredJwtException exception) {
            System.out.println("Token Expired email : " + exception.getClaims().get("email"));
            return false;
        } catch (JwtException exception) {
            System.out.println("Refresh Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            System.out.println("Token is null");
            return false;
        }
    }
    public boolean isOnlyExpiredToken(String token) {
        System.out.println("isValidToken is : " +token);
        try {
            Claims accessClaims = getClaimsFormToken(token);
            System.out.println("Access expireTime: " + accessClaims.getExpiration());
            System.out.println("Access email: " + accessClaims.get("email"));
            return false;
        } catch (ExpiredJwtException exception) {
            System.out.println("Token Expired email : " + exception.getClaims().get("email"));
            return true;
        } catch (JwtException exception) {
            System.out.println("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            System.out.println("Token is null");
            return false;
        }
    }
}