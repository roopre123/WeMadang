# 디자인 자료 사용법

## 폴더
- `design-rules.md` : 기본 디자인 규칙 (화면 크기, 색, 글꼴, 버튼)
- `screens/` : 화면별 설명 (요소, 상태, emit, props, 문구)
  - `common-frame.md` : 헤더, 푸터, 토스트, 모달 (모든 조작 화면 공통)
- `wireframes/` : 화면별 와이어프레임 이미지 (`screens/`의 파일명과 같은 이름)
  - 데스크톱 : `화면명.png`
  - 태블릿 : `화면명-tablet.png`

## 새 화면 추가 순서
1. 데스크톱 와이어프레임 그리기 (개발자)
2. 태블릿 시안 만들기 (Claude 채팅에 데스크톱 와이어프레임을 주고 요청)
3. `wireframes/`에 두 이미지 넣기
4. `screens/`에 화면 설명 파일 만들기 (기존 파일 복사해서 수정)
5. Claude Code에 요청하기

## Claude Code 요청 예시
```
docs/design/screens/start.md와
docs/design/wireframes/start.png, start-tablet.png를 기준으로
시작 화면을 만들어줘. frontend/CLAUDE.md 규칙을 따르고,
미리보기 페이지에서 데스크톱과 태블릿 너비를 둘 다 확인할 수 있게 해줘.
```

## 수정 요청 예시
```
StartView.vue에서 로그인 버튼만 더 크게 바꿔줘. 다른 부분은 건드리지 마.
```
