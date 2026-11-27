# 디자인 자료 사용법

## 폴더
- `design-rules.md` : 기본 디자인 규칙 (화면 크기, 반응형, 색, 글꼴, 버튼)
- `screens/` : 화면별 설명 (요소, 상태, emit, props, 문구)
  - `common-frame.md` : 헤더, 푸터, 토스트, 모달 (모든 조작 화면 공통)
  - `start.md` : 시작 화면
  - `delay.md` : 딜레이 보정 (조작 화면 모달 + 표출 화면)
  - `login-confirm.md` : 로그인 확인 모달 (치지직 연동 안내)
  - `game-select.md` : 게임 목록 화면 (스프린트 2)
  - `broadcast-panel.md` : 방송 패널 (송출용 화면 미리보기, 로그인 후 게임 목록·게임 화면 공통)
  - `overlay-game-select.md` : 송출 화면 - 게임 목록
  - `overlay-game-ready.md` : 송출 화면 - 게임 첫 화면 (준비 중)
  - `game-play.md` : 게임 화면 공통 틀 + 게임별 빈 화면 (스프린트 3)
  - `error.md` : 공통 에러 화면 (없는 주소, 없는 게임, 불러오기 실패) (스프린트 3)
- `wireframes/` : 화면별 와이어프레임 이미지 (`screens/`의 파일명과 같은 이름, 데스크톱 한 장. 상태가 여러 개면 `-guest`, `-login`처럼 뒤에 붙임)
- 와이어프레임은 구성(무엇이 어디에)만 보고, 크기·간격 숫자는 `design-rules.md`와 `screens/` 문서를 따른다

## 새 화면 추가 순서
1. 데스크톱 와이어프레임 그리기 (피그마 → PNG 2x로 내보내기)
2. `wireframes/`에 이미지 넣기 (파일 이름은 소문자, 하이픈)
3. `screens/`에 화면 설명 파일 만들기 (기존 파일 복사해서 수정)
4. Claude Code에 요청하기 (좁은 화면은 design-rules.md 반응형 규칙으로 자동)

## Claude Code 요청 예시
```
docs/design/screens/start.md와 docs/design/wireframes/start.png를 기준으로
시작 화면을 만들어줘. frontend/CLAUDE.md 규칙을 따르고,
미리보기 페이지에서 1920, 1024 너비를 둘 다 확인할 수 있게 해줘.
```

## 수정 요청 예시
```
StartView.vue에서 로그인 버튼만 더 크게 바꿔줘. 다른 부분은 건드리지 마.
```
