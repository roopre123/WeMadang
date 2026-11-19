package org.example.wemadang.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.wemadang.chzzk.client.ChzzkClient;
import org.example.wemadang.chzzk.client.dto.ChzzkAuthResponse;
import org.example.wemadang.common.ChzzkApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Value("${app.front-url}")
    private String frontUrl;

    private final ChzzkClient chzzkClient;

    public AuthController(ChzzkClient chzzkClient) {
        this.chzzkClient = chzzkClient;
    }

    // 치지직 로그인 화면
    @GetMapping("/chzzk/login")
    public ResponseEntity<Void> loginPage(HttpSession session) {

        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        String state = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        session.setAttribute("chzzkOAuthState", state);

        String url = chzzkClient.buildLoginBuilder(state);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    // 치지직 로그인 콜백
    @GetMapping("/chzzk/callback")
    public ResponseEntity<Void> chzzkCallback(@RequestParam(required = false) String code,
                                              @RequestParam(required = false) String state,
                                              HttpSession session,
                                              HttpServletRequest request) {
        String loginState = (String) session.getAttribute("chzzkOAuthState");
        session.removeAttribute("chzzkOAuthState");

        if(code == null || state == null || !state.equals(loginState)) {
            log.warn("치지직 로그인 실패: state 불일치 혹은 code 없음");
            return redirectToFront("/?login=fail");
        }

        ChzzkAuthResponse.TokenRequest token;
        ChzzkAuthResponse.UsersMe user;
        try{
            // 토큰 발급
            token = chzzkClient.requestToken(code, state);

            // 내 정보 조회 -> 서비스로
            user = chzzkClient.getMe(token.accessToken());

            // 세션 정보 저장
            request.changeSessionId();
            session.setAttribute("channelId", user.channelId());
            session.setAttribute("channelName", user.channelName());
            session.setAttribute("accessToken", token.accessToken());
            session.setAttribute("refreshToken", token.refreshToken());
            session.setAttribute("tokenExpiresAt", Instant.now().plusSeconds(Long.parseLong(token.expiresIn())));

            return redirectToFront("/?login=ok");

        } catch(ChzzkApiException e ){
            log.warn("치지직 로그인 실패: {}", e.getMessage());
            return redirectToFront("/?login=fail");
        }
    }

    private ResponseEntity<Void> redirectToFront(String path) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(frontUrl + path))
                .build();
    }

    // 사용자 정보 조회 (헤더 사용)
    @GetMapping("/me")
    public Map<String, Object> me(HttpSession session) {

        String name = (String) session.getAttribute("channelName");

        return name == null
                ? Map.of("loggedIn", false)
                : Map.of("loggedIn", true, "channelName", name);
    }

    // 사용자 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
