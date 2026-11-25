package org.example.wemadang.game.dto;

import org.example.wemadang.game.domain.Game;
import org.example.wemadang.game.domain.GameCategory;
import org.example.wemadang.game.domain.GameColor;

public record GameResponse (Long id,
                            String name,
                            String description,
                            GameCategory category,
                            String screen,
                            String icon,
                            GameColor color) {

    public static GameResponse from(Game game){
        return new GameResponse(
                game.getId(),
                game.getName(),
                game.getDescription(),
                game.getCategory(),
                game.getScreen(),
                game.getIcon(),
                game.getColor());
    }
}
