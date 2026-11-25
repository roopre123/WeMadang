-- 게임 목록 조회 테스트용 데이터. 테스트가 끝나면 롤백됨
INSERT INTO games (name, description, category, screen, icon, color, sort_order, visible)
VALUES
    ('테스트 숨긴 퀴즈', '숨김', 'QUIZ', 'quiz', 'book', 'GRAY', 100, FALSE),
    ('테스트 롤 퀴즈 1', '설명', 'QUIZ', 'quiz', 'book', 'AMBER', 101, TRUE),
    ('테스트 롤 퀴즈 2', '설명', 'QUIZ', 'quiz', 'book', 'AMBER', 102, TRUE),
    ('테스트 롤 퀴즈 3', '설명', 'QUIZ', 'quiz', 'book', 'AMBER', 103, TRUE),
    ('테스트 애니 퀴즈 1', '설명', 'QUIZ', 'quiz', 'book', 'PINK', 104, TRUE),
    ('테스트 애니 퀴즈 2', '설명', 'QUIZ', 'quiz', 'book', 'PINK', 105, TRUE),
    ('테스트 애니 퀴즈 3', '설명', 'QUIZ', 'quiz', 'book', 'PINK', 106, TRUE),
    ('테스트 영화 퀴즈 1', '설명', 'QUIZ', 'quiz', 'book', 'GREEN', 107, TRUE),
    ('테스트 영화 퀴즈 2', '설명', 'QUIZ', 'quiz', 'book', 'GREEN', 108, TRUE),
    ('테스트 영화 퀴즈 3', '설명', 'QUIZ', 'quiz', 'book', 'GREEN', 109, TRUE),
    ('테스트 영화 퀴즈 4', '설명', 'QUIZ', 'quiz', 'book', 'GREEN', 110, TRUE),
    ('테스트 토론', '설명', 'DEBATE', 'balance-debate', 'scale', 'PURPLE', 111, TRUE);

-- 플레이 기록: 영화 퀴즈 4 = 3번, 애니 퀴즈 2 = 2번, 롤 퀴즈 1 = 1번
INSERT INTO game_plays (game_id)
SELECT id FROM games WHERE name = '테스트 영화 퀴즈 4'
UNION ALL SELECT id FROM games WHERE name = '테스트 영화 퀴즈 4'
UNION ALL SELECT id FROM games WHERE name = '테스트 영화 퀴즈 4'
UNION ALL SELECT id FROM games WHERE name = '테스트 애니 퀴즈 2'
UNION ALL SELECT id FROM games WHERE name = '테스트 애니 퀴즈 2'
UNION ALL SELECT id FROM games WHERE name = '테스트 롤 퀴즈 1';