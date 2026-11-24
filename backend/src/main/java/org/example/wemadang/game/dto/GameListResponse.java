package org.example.wemadang.game.dto;

import java.util.List;

public record GameListResponse(List<GameResponse> games,
                               int page,
                               int totalPages) {
}
