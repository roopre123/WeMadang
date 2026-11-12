# 공통 프레임 (헤더, 푸터, 토스트, 모달)

- 파일: `src/components/AppHeader.vue`, `src/components/AppFooter.vue`, `src/components/AppToast.vue`, `src/components/AppModal.vue`
- 와이어프레임: `../wireframes/common-frame.png` (데스크톱), `../wireframes/common-frame-tablet.png` (태블릿)
- 조작 화면(일반 웹 화면)에서만 사용. 표출 화면(OBS)에는 헤더, 푸터 없음

## 배치
- 헤더: 화면을 스크롤해도 위에 고정, 높이 64px
- 본문
- 푸터: 화면에 고정하지 않고 본문 아래에 붙음

## 헤더 (로그인 전)
- 왼쪽: (로고) 위마당
- 가운데: 게임 목록
- 오른쪽: [알림 on/off 아이콘] [로그인]

## 헤더 (로그인 후)
- 왼쪽: (로고 아이콘) 위마당
- 가운데: 게임 목록, 커뮤니티
- 오른쪽: [딜레이 보정] [알림 on/off 아이콘] 닉네임님 [로그아웃 아이콘]
- 로그인 후 닉네임은 치지직 닉네임

## 헤더 공통
- 현재 보고 있는 메뉴 강조 (포인트 브릿지 밑줄, design-rules.md 참고)
- 알림 on/off 버튼은 불필요하면 나중에 삭제 가능
- 태블릿 배치는 태블릿 시안을 따름

## 헤더 정할 것
- 로그인 전 헤더에도 커뮤니티 메뉴를 보여줄지
- 헤더에 딜레이 값 표시 여부 (`딜레이 미설정` / `딜레이 3.2초`)

## 푸터
- (로고) + design-rules.md "하단 표기 문구"

## 토스트
- 잠깐 떴다 사라지는 안내 (로그인 성공·실패, 알림, 저장 완료)
- 앞에 작은 점으로 종류 구분: 성공(success 색) / 오류(error 색)
- 위치: 화면 하단 가운데, 2~3초 뒤 사라짐

## 모달
- 사용자가 작업해야 하는 기능 (딜레이 보정 등)
- 뒤 배경을 살짝 흐리게(어둡게)

## 동작 (emit)
| 동작 | emit |
|---|---|
| 로그인 클릭 | `login` |
| 로그아웃 클릭 | `logout` |
| 알림 on/off 클릭 | `toggle-sound` |
| 딜레이 보정 클릭 | `open-delay` |
| 메뉴 클릭 | `navigate` (메뉴 이름 전달) |
| 모달 닫기 | `close` |

## props
| 컴포넌트 | 이름 | 타입 | 설명 |
|---|---|---|---|
| AppHeader | loggedIn | boolean | 로그인 여부 |
| AppHeader | nickname | string | 치지직 닉네임 |
| AppHeader | soundOn | boolean | 알림 켜짐 여부 |
| AppHeader | activeMenu | string | 강조할 메뉴 |
| AppToast | message | string | 안내 문구 |
| AppToast | type | 'success' \| 'error' | 종류 |
| AppModal | open | boolean | 열림 여부 |

## 깨지기 쉬운 경우
- 긴 닉네임
- 라이트 / 다크 모드 둘 다
- 태블릿 너비 (768 ~ 1279)
