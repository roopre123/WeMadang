package org.example.wemadang.game.dto;

import org.example.wemadang.game.domain.GameCategory;
import org.example.wemadang.game.domain.GameColor;

public record GameResponse (Long id,
                            String name,
                            String description,
                            GameCategory category,
                            String screen,
                            String icon,
                            GameColor color) {
}
