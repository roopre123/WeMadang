package org.example.wemadang.game.controller;

import org.example.wemadang.game.dto.GameListResponse;
import org.example.wemadang.game.dto.GameRequest;
import org.example.wemadang.game.dto.GameResponse;
import org.example.wemadang.game.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // 게임 목록 조회
    @GetMapping("/games")
    public GameListResponse getGameList(GameRequest request) {
        return gameService.getGameList(request);
    }

    // 게임 단건 정보 조회
    @GetMapping("/games/{id}")
    public GameResponse getGameDetail(@PathVariable Long id) {
        return gameService.getGame(id);
    }
}
