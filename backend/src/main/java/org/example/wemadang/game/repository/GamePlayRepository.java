package org.example.wemadang.game.repository;

import org.example.wemadang.game.domain.GamePlay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayRepository extends JpaRepository<GamePlay, Long> {
}
