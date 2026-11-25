package org.example.wemadang.game.service;

import org.example.wemadang.game.domain.Game;
import org.example.wemadang.game.domain.GameSort;
import org.example.wemadang.game.dto.GameListResponse;
import org.example.wemadang.game.dto.GameRequest;
import org.example.wemadang.game.dto.GameResponse;
import org.example.wemadang.game.repository.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameListResponse getGameList(GameRequest request) {
        Pageable pageable = PageRequest.of(request.page() -1 , 9);
        Page<Game> games = (request.sort() == GameSort.POPULAR)
                ? gameRepository.findVisibleGamesByPopular(request.category(), request.keyword(), pageable)
                : gameRepository.findVisibleGames(request.category(), request.keyword(), pageable);
        return new GameListResponse(
                games.stream().map(GameResponse::from).toList(),
                request.page(),
                games.getTotalPages());
    }
}