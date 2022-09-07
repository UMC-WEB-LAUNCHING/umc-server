package com.umc.helper.auth.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GoogleOauth implements SocialOauth {
    private final String GOOGLE_SNS_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private final String GOOGLE_SNS_CLIENT_ID = "277732691373-775i9hhmc45ingkpmred7a8rru012b1b.apps.googleusercontent.com";
    private final String GOOGLE_SNS_CALLBACK_URL = "http://localhost:8080/auth/google/callback";
    private final String GOOGLE_SNS_CLIENT_SECRET = "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";
    private final String GOOGLE_SNS_TOKEN_BASE_URL = "https://oauth2.googleapis.com/token";

//    @Value("${sns.google.url}")
//    private String GOOGLE_SNS_BASE_URL;
//    @Value("${sns.google.client.id}")
//    private String GOOGLE_SNS_CLIENT_ID;
//    @Value("${sns.google.callback.url}")
//    private String GOOGLE_SNS_CALLBACK_URL;
//    @Value("${sns.google.client.secret}")
//    private String GOOGLE_SNS_CLIENT_SECRET;

    Logger logger= LoggerFactory.getLogger(GoogleOauth.class);
    private final ObjectMapper objectMapper=new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    private final RestTemplate restTemplate = new RestTemplate();
    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", "email profile");
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return GOOGLE_SNS_BASE_URL + "?" + parameterString;
    }
    public String getOauthProfileRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", "profile");
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return GOOGLE_SNS_BASE_URL + "?" + parameterString;
    }

    // scope - profile, email
//@Override
//public String getOauthRedirectURL() {
//
//    String ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
//    String CLIENT_ID = "652442945302-06tpkhg3bphvvqc71vql8e3sj6ul48gi.apps.googleusercontent.com";
//    String REDIRECT_URI = "http://localhost:8080/auth/google/callback";
//    String RESPONSE_TYPE = "code";
//    String SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
//    return "redirect:"+ ENDPOINT+"?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI
//            +"&response_type="+RESPONSE_TYPE+"&scope="+SCOPE;
//}

    // requestAccessToken == createPostRequest
    @Override
    public ResponseEntity<String> requestAccessToken(String code) {


        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.put("code", Collections.singletonList(code));
        params.put("client_id", Collections.singletonList(GOOGLE_SNS_CLIENT_ID));
        params.put("client_secret", Collections.singletonList(GOOGLE_SNS_CLIENT_SECRET));
        params.put("redirect_uri", Collections.singletonList(GOOGLE_SNS_CALLBACK_URL));
        params.put("grant_type", Collections.singletonList("authorization_code"));

        HttpHeaders headers=new HttpHeaders();
        headers.add("Content-Type","application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String,String>> httpEntity=new HttpEntity<>(params,headers);

        return restTemplate.exchange(GOOGLE_SNS_TOKEN_BASE_URL, HttpMethod.POST, httpEntity, String.class);
//        ResponseEntity<String> responseEntity =
//                restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, params, String.class);
//
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            return responseEntity.getBody();
//        }
       // return "구글 로그인 요청 처리 실패";
    }

    public OAuthToken getAccessToken(ResponseEntity<String> response){
        OAuthToken oAuthToken=null;
        try{
            oAuthToken=objectMapper.readValue(response.getBody(),OAuthToken.class);
            logger.info("oAuthToken: {}",oAuthToken);
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }

        return oAuthToken;
    }

//    public ResponseEntity<String> createGetRequest(OAuthToken oAuthToken){
//        String url = "https://www.googleapis.com/oauth2/v1/userinfo";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + oAuthToken.getAccessToken());
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
//
//        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
//    }

    // json parser
    public ResponseEntity<String> createGetRequest(OAuthToken oAuthToken){
        String url = "https://www.googleapis.com/oauth2/v1/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccessToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);

        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }
    public GoogleUser getUserInfo(ResponseEntity<String> userInfoResponse){
        GoogleUser googleUser = null;
        try {
            googleUser = objectMapper.readValue(userInfoResponse.getBody(), GoogleUser.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return googleUser;
    }
}
