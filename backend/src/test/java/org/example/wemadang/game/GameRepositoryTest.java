package org.example.wemadang.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
public class GameRepositoryTest {

    @Autowired
    GameRepository gameRepository;


    @Test
    @DisplayName("Flyway V1 처음 넣은 게임 2개가 조회된다.")
    void findAll_returnSeedGames() {
        List<Game> games = gameRepository.findAll();

        assertThat(games).hasSize(2);
    }

    @Test
    @DisplayName("게임 분류와 색이 enum으로 읽힌다.")
    void enumColumns_areMapped() {
        Game debate = gameRepository.findAll().stream()
                .filter(game -> game.getName().equals("밸런스 토론"))
                .findFirst()
                .orElseThrow();

        assertThat(debate.getCategory()).isEqualTo(GameCategory.DEBATE);
        assertThat(debate.getColor()).isEqualTo(GameColor.PURPLE);
        assertThat(debate.getIcon()).isEqualTo("scale");
    }


    @Test
    @DisplayName("sort order 순으로 정렬")
    void sortOrder() {
        List<Game> games = gameRepository.findAll(Sort.by("sortOrder"));

        assertThat(games)
                .extracting(Game::getName)
                .containsExactly("밸런스 토론", "1:1 면담 (진실의 방)");
    }
}
