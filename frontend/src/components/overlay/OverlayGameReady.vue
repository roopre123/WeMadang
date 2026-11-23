<!--
  OverlayGameReady.vue — 송출 화면: 게임 첫 화면 (준비 중) (표출 화면, 1920×1080 고정)
  screens/overlay-game-ready.md. 모든 게임이 같은 화면을 쓰고 게임 정보만 바뀐다

  props:
    game: Game           — 고른 게임 (이름, 아이콘, 색)

  emits: 없음
-->
<script setup lang="ts">
import { IconMessageCircle } from '@tabler/icons-vue'
import GameIcon from '@/components/GameIcon.vue'
import type { Game } from '@/types/game'

defineProps<{
  game: Game
}>()
</script>

<template>
  <div class="overlay-screen">
    <div class="overlay-content">
      <div class="overlay-logo">
        <img src="@/assets/logo.svg" width="56" height="56" alt="">
        <span>위마당</span>
      </div>

      <div class="ready-center">
        <div class="ready-game">
          <GameIcon :name="game.icon" :color="game.color" :category="game.category" size="hero" />
          <h1 class="ready-name">{{ game.name }}</h1>
        </div>
        <p class="ready-guide">
          게임을 준비 중이에요.<br>
          잠시만 기다려주세요.
        </p>
      </div>

      <div class="overlay-guide">
        <IconMessageCircle size="24" stroke="2" />
        <span>치지직 채팅으로 참여해요</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 내용 영역 전체의 가로·세로 가운데 */
.ready-center {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.ready-game {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  max-width: 100%;
}

/* 한 줄을 넘으면 두 줄까지, 그 뒤는 ... (아이콘 96 + 간격 24를 뺀 1080) */
.ready-name {
  display: -webkit-box;
  max-width: calc(var(--container-width) - var(--game-icon-hero) - var(--space-4));
  overflow: hidden;
  font-size: var(--font-size-overlay-hero);
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  line-clamp: 2;
}

.ready-guide {
  margin-top: var(--space-5);
  color: var(--color-text-sub);
  font-size: var(--font-size-overlay-sub);
  line-height: var(--line-height-body);
  text-align: center;
}
</style>
