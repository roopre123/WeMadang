package org.example.wemadang.game;

import org.example.wemadang.game.controller.GameController;
import org.example.wemadang.game.domain.GameCategory;
import org.example.wemadang.game.domain.GameSort;
import org.example.wemadang.game.dto.GameListResponse;
import org.example.wemadang.game.dto.GameRequest;
import org.example.wemadang.game.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    // 가짜 요청을 보낼 도구
    @Autowired
    MockMvc mockMvc;

    // 가짜 서비스를 만들어서 미리 정한 값만 돌려주고 어떤 값으로 불렸는지 기록함
    @MockitoBean
    GameService gameService;

    @Test
    @DisplayName("요청 값을 GameRequest로 묶어 서비스에 넘겨보기")
    void getGameList() throws Exception {
        // Given: 서비스가 불리면 빈 목록을 돌려주게 정함.
        // given(A).willReturn(B):
        //      A처럼 불리면 B처럼 응답. B는 게임0개 2페이지, 전체 0페이지인 빈 결과. 내용은 의미 없이 null 아니면 됨
        // any(A): 내용이 중요하지 않아 아무 A여도 됨.
        given(gameService.getGameList(any(GameRequest.class)))
                .willReturn(new GameListResponse(List.of(), 2, 0));

        // When: GET /game?category=QUIZ&sort=POPULAR&keyword= 롤&page=2
        // perform: 요청을 실제로 보낸다. 스프링 안에서 바로 처리 됨
        mockMvc.perform(get("/games")
                        .param("category", "QUIZ")
                        .param("sort", "POPULAR")
                        .param("keyword", " 롤")
                        .param("page","2"))
                .andExpect(status().isOk());

        // Then: 서비스가 이 값으로 불렸는지 (검색어 공백 지워졌는지)
        verify(gameService).getGameList(new GameRequest(GameCategory.QUIZ, GameSort.POPULAR, "롤", 2));
    }
}
