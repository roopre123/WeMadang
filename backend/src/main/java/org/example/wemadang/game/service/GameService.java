package org.example.wemadang.game.service;

import org.example.wemadang.game.dto.GameListResponse;
import org.example.wemadang.game.dto.GameRequest;
import org.example.wemadang.game.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameListResponse getGameList(GameRequest gameRequest) {
        return null;
    }
}
