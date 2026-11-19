package org.example.wemadang.auth;

import org.example.wemadang.chzzk.client.ChzzkClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// [구조] AuthController + 스프링 MVC만 띄움
@WebMvcTest(controllers = AuthController.class,
        properties = "app.front-url=http://localhost:5173")
class AuthControllerTest {

    // [구조] 가짜 요청을 보내는 도구
    @Autowired
    MockMvc mockMvc;

    // [구조] 컨트롤러 생성자에 필요한 빈자리 채우기 (가짜)
    @MockitoBean
    ChzzkClient chzzkClient;

    // 약속 ①② : 로그아웃하면 세션이 지워지고 204
    @Test
    void 로그아웃하면_세션이_무효화된다() throws Exception {
        // Given: 로그인된 세션
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("channelName", "테스트채널");
        session.setAttribute("accessToken", "test-token");

        // When: 로그아웃 요청
        mockMvc.perform(post("/auth/logout").session(session))
                // Then: 204
                .andExpect(status().isNoContent());

        // Then: 세션이 지워짐
        assertThat(session.isInvalid()).isTrue();
    }

    // 약속 ③ : 로그인 안 해도 에러 없음
    @Test
    void 로그인_안한_상태에서도_로그아웃은_성공한다() throws Exception {
        // Given: 없음 (세션 없이)
        // When
        mockMvc.perform(post("/auth/logout"))
                // Then
                .andExpect(status().isNoContent());
    }

    // 약속 ④ : GET은 막힘
    @Test
    void 로그아웃은_GET으로_호출할_수_없다() throws Exception {
        // When
        mockMvc.perform(get("/auth/logout"))
                // Then: 405 (허용 안 된 메서드)
                .andExpect(status().isMethodNotAllowed());
    }
}