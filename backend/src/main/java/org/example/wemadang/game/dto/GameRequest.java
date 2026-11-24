package org.example.wemadang.game.dto;

import org.example.wemadang.game.domain.GameCategory;
import org.example.wemadang.game.domain.GameSort;

public record GameRequest(GameCategory category,
                          GameSort sort,
                          String keyword,
                          Integer page) {
    public GameRequest {
        if(sort == null) sort = GameSort.DEFAULT;

        keyword = (keyword == null) ? "" : keyword.strip();

        if(page == null || page < 1) page = 1;
    }
}
