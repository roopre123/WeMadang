package org.example.wemadang.chzzk.client;

import org.apache.catalina.User;
import org.example.wemadang.chzzk.client.dto.ChzzkAuthResponse;
import org.example.wemadang.chzzk.client.dto.ChzzkAuthResponse.TokenRequest;
import org.example.wemadang.common.ChzzkApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class ChzzkClient {

    Logger log = LoggerFactory.getLogger(ChzzkClient.class);

    @Value("${chzzk.client-id}")
    private String clientId;

    @Value("${chzzk.redirection-url}")
    private String redirectionUrl;

    @Value("${chzzk.login-url}")
    private String loginUrl;

    @Value("${chzzk.client-secret}")
    private String clientSecret;

    private final String URI = "https://openapi.chzzk.naver.com";
    RestClient client = RestClient.create(URI);



    // 치지직 로그인 API URL 빌더 생성
    public String buildLoginBuilder(String state) {
        return UriComponentsBuilder
                .fromUriString(loginUrl)
                .queryParam("clientId", clientId)
                .queryParam("redirectUri", redirectionUrl)
                .queryParam("state", state)
                .encode()
                .toUriString();
    }

    // 치지직 token 요청
    public ChzzkAuthResponse.TokenRequest requestToken(String code, String state) {

        // LinkedMultiValueMap은 JSON이 아니라 FORM 형식으로 보내져서 그냥 Map으로 사용함
        Map<String, String> params = Map.of(
                "grantType", "authorization_code",
                "clientId", clientId,
                "clientSecret", clientSecret,
                "code", code,
                "state", state
        );
        ChzzkAuthResponse<TokenRequest> res;
        try{
            res = client.post()
                    .uri("/auth/v1/token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(params)
                    .retrieve()
                    .body(new ParameterizedTypeReference<ChzzkAuthResponse<TokenRequest>>() {});
            /*
             * new ParameterizedTypeReference<ChzzkAuthResponse<TokenRequest>>
             * 제너릭은 자바가 실행될 때 지워버림 그래서 이름 없는 클래스 만들어서 타입을 지정해준다
             * 받을 타입에 <> 있으면 -> ParameterizedTypeReference
             * 받을 타입에 <> 없으면 -> 타입.class
             * */
        }catch (RestClientResponseException e) {
            throw new ChzzkApiException("토큰 발급 HTTP 오류 : " + e.getMessage(), e);
        }

        if(res == null || res.code() != 200 || res.content() == null) {
            throw new ChzzkApiException("토큰 발급 실패 : " + (res == null ? "응답없음" : res.message()));
        }
        return res.content();
    }

    // 치지직 유저 정보 조회 (채널ID, 채널 이름)
    public ChzzkAuthResponse.UsersMe getMe(String accessToken) {

        ChzzkAuthResponse<ChzzkAuthResponse.UsersMe> res;
        try{
            res =  client.get()
                    .uri( "/open/v1/users/me")
                    .headers(headers -> {
                        headers.setBearerAuth(accessToken);
                        headers.setContentType(MediaType.APPLICATION_JSON);
                    }).retrieve()
                    .body(new ParameterizedTypeReference<ChzzkAuthResponse<ChzzkAuthResponse.UsersMe>>() {});
        }catch (RestClientResponseException e) {
            throw new ChzzkApiException("사용자 정보 HTTP 오류 : "+e.getMessage(), e);
        }
        if(res == null || res.code() != 200 || res.content() == null) {
            throw new ChzzkApiException("사용자 정보 요청 실패 : " + (res == null ? "응답없음" : res.message()));
        }
        log.info(res.toString());
        return res.content();
    }
}
