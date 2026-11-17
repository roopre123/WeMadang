<!--
  AppHeader.vue — 공통 헤더 (조작 화면 전용)

  props:
    loggedIn: boolean         — 로그인 여부 (default: false)
    nickname: string          — 치지직 닉네임 (default: '')
    soundOn: boolean          — 알림 켜짐 여부 (default: true)
    activeMenu: string        — 강조할 메뉴 이름 (default: '')
    delayValue: string | null — 딜레이 보정 값. null이면 '딜레이 미설정' (default: null)
                                (wireframe 기준 추가; props 표 미기재)

  emits:
    login               — 로그인 클릭
    logout              — 로그아웃 클릭
    toggle-sound        — 알림 on/off 클릭
    open-delay          — 딜레이 보정 클릭
    navigate(menu)      — 메뉴 클릭 (메뉴 이름 전달)
-->
<script setup lang="ts">
withDefaults(defineProps<{
  loggedIn?: boolean
  nickname?: string
  soundOn?: boolean
  activeMenu?: string
  delayValue?: string | null
}>(), {
  loggedIn: false,
  nickname: '',
  soundOn: true,
  activeMenu: '',
  delayValue: null,
})

const emit = defineEmits<{
  login: []
  logout: []
  'toggle-sound': []
  'open-delay': []
  navigate: [menu: string]
}>()

const menus = ['게임 목록', '커뮤니티']
</script>

<template>
  <header class="app-header">
    <div class="header-inner container">

      <!-- 로고 -->
      <a href="/" class="header-logo" @click.prevent="emit('navigate', '홈')">
        <img src="@/assets/logo.svg" width="28" height="28" alt="" aria-hidden="true">
        <span class="site-name">위마당</span>
      </a>

      <!-- 네비게이션 -->
      <nav class="header-nav">
        <button
          v-for="menu in menus"
          :key="menu"
          class="nav-item"
          :class="{ active: activeMenu === menu }"
          @click="emit('navigate', menu)"
        >
          {{ menu }}
        </button>
      </nav>

      <!-- 로그인 전 -->
      <div v-if="!loggedIn" class="header-actions">
        <button
          class="sound-btn"
          :aria-label="soundOn ? '알림 끄기' : '알림 켜기'"
          @click="emit('toggle-sound')"
        >
          <!-- 알림 켜짐 (Feather bell) -->
          <svg v-if="soundOn" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/>
            <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
          </svg>
          <!-- 알림 꺼짐 (Feather bell-off) -->
          <svg v-else xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
            <path d="M18.63 13A17.89 17.89 0 0 1 18 8"/>
            <path d="M6.26 6.26A5.86 5.86 0 0 0 6 8c0 7-3 9-3 9h14"/>
            <path d="M18 8a6 6 0 0 0-9.33-5"/>
            <line x1="1" y1="1" x2="23" y2="23"/>
          </svg>
        </button>

        <button class="btn btn-secondary btn-sm" @click="emit('login')">로그인</button>
      </div>

      <!-- 로그인 후 -->
      <div v-else class="header-actions">
        <div class="delay-group">
          <button class="btn btn-secondary btn-sm" @click="emit('open-delay')">딜레이 보정</button>
          <span class="delay-value" :class="{ 'delay-unset': delayValue === null }">
            {{ delayValue !== null ? `딜레이 ${delayValue}` : '딜레이 미설정' }}
          </span>
        </div>

        <button
          class="sound-btn"
          :aria-label="soundOn ? '알림 끄기' : '알림 켜기'"
          @click="emit('toggle-sound')"
        >
          <svg v-if="soundOn" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/>
            <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
          </svg>
          <svg v-else xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
            <path d="M18.63 13A17.89 17.89 0 0 1 18 8"/>
            <path d="M6.26 6.26A5.86 5.86 0 0 0 6 8c0 7-3 9-3 9h14"/>
            <path d="M18 8a6 6 0 0 0-9.33-5"/>
            <line x1="1" y1="1" x2="23" y2="23"/>
          </svg>
        </button>

        <span class="nickname text-small">{{ nickname }}님 안녕하세요.</span>

        <button class="btn btn-secondary btn-sm" @click="emit('logout')">로그아웃</button>
      </div>

    </div>
  </header>
</template>

<style scoped>
.app-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: var(--header-height);
  background: var(--color-background);
  border-bottom: 1px solid var(--color-border);
  z-index: 100;
}

.header-inner {
  display: flex;
  align-items: center;
  height: 100%;
  gap: var(--space-5);
}

/* 로고 */
.header-logo {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  text-decoration: none;
  flex-shrink: 0;
}

.site-name {
  font-size: var(--font-size-subheading);
  font-weight: var(--font-weight-bold);
  color: var(--color-text);
}

/* 네비게이션 */
.header-nav {
  flex: 1;
  display: flex;
  align-items: center;
  gap: var(--space-5);
}

.nav-item {
  position: relative;
  padding-bottom: var(--space-1);
  font-size: var(--font-size-body);
  font-weight: var(--font-weight-normal);
  color: var(--color-text-sub);
  background: none;
  border: none;
  cursor: pointer;
  transition: color 0.15s;
}

.nav-item:hover {
  color: var(--color-text);
}

.nav-item.active {
  font-weight: var(--font-weight-bold);
  color: var(--color-text);
}

.nav-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: var(--gradient-bridge);
  border-radius: 1px;
}

/* 오른쪽 액션 영역 */
.header-actions {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  flex-shrink: 0;
}

/* 딜레이 그룹 */
.delay-group {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.delay-value {
  font-size: var(--font-size-small);
  color: var(--color-text);
  white-space: nowrap;
}

.delay-unset {
  color: var(--color-text-sub);
}

/* 알림 버튼 */
.sound-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--color-surface);
  border: none;
  cursor: pointer;
  color: var(--color-text);
  flex-shrink: 0;
  transition: filter 0.15s;
}

.sound-btn:hover {
  filter: brightness(0.93);
}

/* 닉네임 */
.nickname {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 160px;
}

/* 반응형: 768 ~ 1279 */
@media (max-width: 1279px) {
  .header-inner {
    gap: var(--space-3);
  }

  .header-nav {
    gap: var(--space-3);
  }

  .header-actions {
    gap: var(--space-2);
  }

  .nickname {
    max-width: 100px;
  }

  .delay-group {
    gap: var(--space-1);
  }
}
</style>
