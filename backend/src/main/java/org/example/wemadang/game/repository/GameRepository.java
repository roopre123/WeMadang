package org.example.wemadang.game.repository;

import org.example.wemadang.game.domain.Game;
import org.example.wemadang.game.domain.GameCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    // findBy: 조건으로 찾는다
    // VisibleTrue: visible = true
    // NameContaining: name LIKE '%값%'
    // IgnoreCase: UPPER(name) LIKE UPPER(...) 위 조건을 꾸며주는 문장
    // OrderBySortOrderAsc: ORDER BY sort_order ASC
    // List<Game> findByVisibleTrueAndNameContainingIgnoreCaseOrderBySortOrderAsc(String keyword);


    @Query("""
    SELECT g 
    FROM Game g
    WHERE   g.visible = true
    AND     (:category IS NULL OR g.category= :category)
    AND     LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    ORDER BY g.sortOrder asc
""")
    Page<Game> findVisibleGames(@Param("category") GameCategory category,
                                @Param("keyword") String keyword,
                                Pageable pageable);

    @Query(value = """
        SELECT  g
        FROM    Game g
        LEft JOIN GamePlay p 
            ON  p.game = g
        WHERE   g.visible = true
        AND     (:category IS NULL OR g.category = :category)
        AND     LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        GROUP BY g
        ORDER BY COUNT(p) DESC, g.sortOrder ASC
    """,
    countQuery = """
        SELECT COUNT(g)
        FROM Game g
        WHERE g.visible = true
        AND     (:category IS NULL OR g.category = :category)
        AND     LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Game> findVisibleGamesByPopular(@Param("category") GameCategory category,
                                         @Param("keyword") String keyword,
                                         Pageable pageable);
}
