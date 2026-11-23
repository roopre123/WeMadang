-- ========================================
-- 게임 목록
-- ========================================
CREATE TABLE games(
    id          BIGSERIAL       PRIMARY KEY,
    name        VARCHAR(50)     NOT NULL UNIQUE,
    description VARCHAR(200)    NOT NULL,
    category    varchar(20)     NOT NULL
        CHECK ( category IN ('QUIZ', 'DEBATE', 'INTERVIEW')),
    screen      varchar(50)     NOT NULL,
    icon        varchar(50),
    color       VARCHAR(20)     NOT NULL
        CHECK ( color IN ('PURPLE', 'AMBER', 'TEAL', 'PINK', 'GREEN', 'GRAY')),
    sort_order  INT             not null,
    visible     BOOLEAN         NOT NULL default true,
    created_at  timestamptz     not null default now(),
    updated_at  timestamptz     not null default now()
);

COMMENT ON TABLE  games             IS '게임 목록. 게임 목록 화면과 게임 화면 연결에 사용';
COMMENT ON COLUMN games.id          IS '게임 ID';
COMMENT ON COLUMN games.name        IS '게임 이름. 카드와 송출 화면에 표시, 중복 불가';
COMMENT ON COLUMN games.description IS '게임 설명. 카드에 최대 2줄로 표시';
COMMENT ON COLUMN games.category    IS '분류. QUIZ(퀴즈형), DEBATE(토론형), INTERVIEW(면담형). 분류 표시와 검색에 사용';
COMMENT ON COLUMN games.screen      IS '화면 키. 게임을 시작하면 이동할 화면 (예: balance-debate). 같은 퀴즈 화면을 여러 게임이 같이 쓸 수 있음';
COMMENT ON COLUMN games.icon        IS 'Tabler 아이콘 이름 (예: scale). 없거나 틀리면 분류 기본 아이콘으로 표시';
COMMENT ON COLUMN games.color       IS '아이콘 상자 색 이름. PURPLE, AMBER, TEAL, PINK, GREEN, GRAY. 실제 색은 화면 CSS 변수(--icon-{이름}-bg/fg)';
COMMENT ON COLUMN games.sort_order  IS '목록 순서. 작은 값이 먼저. 인기순 정렬에서 플레이 수가 같을 때도 사용';
COMMENT ON COLUMN games.visible     IS '노출 여부. FALSE면 목록에 안 보임';
COMMENT ON COLUMN games.created_at  IS '등록 시각';
COMMENT ON COLUMN games.updated_at  IS '수정 시각';


-- ========================================
-- 한 판 기록
-- ========================================
create table game_plays(
    id                  bigserial       primary key,
    game_id             bigint          not null references games (id),
    started_at          timestamptz     not null default now(),
    ended_at            timestamptz,
    participant_count   int             not null default 0
);
CREATE INDEX idx_game_plays_game_id ON game_plays (game_id);

COMMENT ON TABLE  game_plays                   IS '한 판 기록. 시작 화면 게임 플레이 카운트와 인기순에 사용';
COMMENT ON COLUMN game_plays.id                IS '플레이 ID';
COMMENT ON COLUMN game_plays.game_id           IS '플레이한 게임 (games.id)';
COMMENT ON COLUMN game_plays.started_at        IS '게임 시작 시각';
COMMENT ON COLUMN game_plays.ended_at          IS '게임 종료 시각. 진행 중이면 NULL';
COMMENT ON COLUMN game_plays.participant_count IS '참여한 시청자 수';


-- =========================================
-- 처음 게임
-- =========================================
INSERT INTO games (name, description, category, screen, icon, color, sort_order)
VALUES
    ('밸런스 토론', 'A vs B 투표로 팀을 나누고 채팅으로 토론해요', 'DEBATE', 'balance-debate', 'scale', 'PURPLE', 1),
    ('1:1 면담 (진실의 방)', '뽑힌 시청자와 익명으로 1:1 대화를 나눠요', 'INTERVIEW', 'interview', 'messages', 'TEAL', 2);


-- =========================================
-- 날짜별 방문 수
-- =========================================
CREATE TABLE daily_visits (
    visit_date  DATE    PRIMARY KEY,
    count       BIGINT  NOT NULL DEFAULT 0
);

COMMENT ON TABLE  daily_visits            IS '날짜별 방문 수. 시작 화면 방문 카운트에 사용';
COMMENT ON COLUMN daily_visits.visit_date IS '방문 날짜 (Asia/Seoul 기준). 하루에 한 줄';
COMMENT ON COLUMN daily_visits.count      IS '그날 방문 수';