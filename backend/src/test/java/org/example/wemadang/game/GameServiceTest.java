package org.example.wemadang.game;

import org.example.wemadang.game.domain.GameCategory;
import org.example.wemadang.game.domain.GameSort;
import org.example.wemadang.game.dto.GameListResponse;
import org.example.wemadang.game.dto.GameRequest;
import org.example.wemadang.game.dto.GameResponse;
import org.example.wemadang.game.repository.GameRepository;
import org.example.wemadang.game.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
@Sql("/sql/game-list-test-data.sql")
public class GameServiceTest {
    /*
    * 1. 트랜잭션 시작
    * 2. SQL 파일 실행 -> 테스트 게임 12개와 플레이 기록이 생김
    * 3. 테스트 실행 ( 서비스 조회 -> 확인 )
    * 4. 트랜잭션 롤백 -> 2번에서 넣은 데이터 삭제
    * */

    @Autowired
    GameService gameService;

    @Test
    @DisplayName("숨긴 게임은 목록에 안나온다")
    void hiddenGame_isExcluded () {
        GameRequest request = new GameRequest(null, GameSort.DEFAULT, "테스트", 1);

        GameListResponse res = gameService.getGameList(request);

        assertThat(res.games()).extracting(GameResponse::name).doesNotContain("테스트 숨긴 퀴즈");
    }

    @Test
    @DisplayName("분류를 고르면 그 분류의 게임만 나온다")
    void category_filters() {
        GameRequest request = new GameRequest(GameCategory.QUIZ, GameSort.DEFAULT, null, 1);
        GameListResponse list = gameService.getGameList(request);

        assertThat(list.games()).isNotEmpty();
        assertThat(list.games()).allMatch(game -> game.category().equals(GameCategory.QUIZ));
    }

    @Test
    @DisplayName("검색어 이름이 들어간 게임만 나온다.")
    void keyword_filters() {
        GameRequest request = new GameRequest(null, null, "롤", 1);
        GameListResponse list = gameService.getGameList(request);

        assertThat(list.games()).isNotEmpty();
        assertThat(list.games()).hasSize(3);
        assertThat(list.games()).allMatch(game-> game.name().contains("롤"));
    }

    @Test
    @DisplayName("페이징 처리가 되어서 나온다.")
    void page_filters() {
        GameRequest request = new GameRequest(null, null, null, 1);
        GameListResponse list = gameService.getGameList(request);

        assertThat(list.games()).hasSize(9);
        assertThat(list.page()).isEqualTo(1);
        assertThat(list.totalPages()).isEqualTo(2);

        GameRequest request2 = new GameRequest(null, null, null, 2);
        GameListResponse list2 = gameService.getGameList(request2);

        assertThat(list2.games()).hasSize(4);
        assertThat(list2.page()).isEqualTo(2);
        assertThat(list2.totalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("플레이순으로 게임이 표출된다.")
    void popular_sortByPlays() {
        GameRequest request = new GameRequest(null, GameSort.POPULAR, "테스트", 1);
        GameListResponse list = gameService.getGameList(request);

        assertThat(list.games()).extracting(GameResponse::name).startsWith("테스트 영화 퀴즈 4","테스트 애니 퀴즈 2");

    }
}
