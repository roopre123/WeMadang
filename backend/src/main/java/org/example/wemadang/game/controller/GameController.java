package org.example.wemadang.game.controller;

import org.example.wemadang.game.dto.GameListResponse;
import org.example.wemadang.game.dto.GameRequest;
import org.example.wemadang.game.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    public GameListResponse getGameList(GameRequest request) {
        return gameService.getGameList(request);
    }
}
