<!--
  GamePlayView.vue — 게임 화면 공통 틀 (조작 화면, 로그인 후에만)
  기준: docs/design/screens/game-play.md, docs/design/screens/broadcast-panel.md
  헤더/푸터는 공통 프레임이 그린다. 가운데 "게임별 화면"은 기본 슬롯으로 받는다.

  props:
    game: Game | null      — 게임 정보. null이면 불러오는 중 모양 (같은 틀, 회색 상자)
    overlayUrl: string     — (방송 패널) 표출 URL
    connected: boolean     — (방송 패널) OBS 연결 여부

  slots:
    default                — 게임별 화면 (부모가 game.screen 값으로 골라 넣음)
    preview                — (방송 패널) 미리보기 칸

  emits:
    back                   — [← 게임 목록으로] 클릭
    copy-url               — (방송 패널) [복사] 클릭
-->
<script setup lang="ts">
import { IconArrowLeft } from '@tabler/icons-vue'
import BroadcastPanel from '@/components/BroadcastPanel.vue'
import GameIcon from '@/components/GameIcon.vue'
import { categoryLabels } from '@/types/game'
import type { Game } from '@/types/game'

withDefaults(defineProps<{
  game: Game | null
  overlayUrl?: string
  connected?: boolean
}>(), {
  overlayUrl: '',
  connected: false,
})

const emit = defineEmits<{
  back: []
  'copy-url': []
}>()
</script>

<template>
  <section class="game-play">
    <div class="container panel-layout">

      <div class="game-play-main">
        <div class="game-info-row">
          <template v-if="game">
            <div class="game-info">
              <GameIcon :name="game.icon" :color="game.color" :category="game.category" />
              <div class="game-info-title">
                <span class="category-pill">{{ categoryLabels[game.category] }}</span>
                <h1 class="text-heading game-info-name">{{ game.name }}</h1>
              </div>
            </div>
          </template>
          <div v-else class="game-info" aria-busy="true">
            <div class="skeleton-icon" />
            <div class="skeleton-name" />
          </div>

          <button class="btn btn-secondary btn-sm back-button" @click="emit('back')">
            <IconArrowLeft size="16" stroke="2" />
            게임 목록으로
          </button>
        </div>

        <div class="game-screen-slot">
          <slot />
        </div>
      </div>

      <BroadcastPanel
        :open="true"
        :collapsible="false"
        :overlay-url="overlayUrl"
        :connected="connected"
        @copy-url="emit('copy-url')"
      >
        <slot name="preview" />
      </BroadcastPanel>

    </div>
  </section>
</template>

<style scoped>
.game-play {
  padding: var(--space-6) 0;
}

/* 게임 정보 줄: 정보 묶음 + (사이 24) 돌아가기 버튼. 높이는 불러오는 중에도 같게 아이콘 40에 맞춤 */
.game-info-row {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  min-height: var(--game-icon-card);
}

.game-info {
  display: flex;
  flex: 0 1 auto;
  align-items: center;
  gap: var(--space-3);
  min-width: 0;
}

.game-info-title {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  min-width: 0;
}

.game-info-name {
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.back-button {
  flex-shrink: 0;
}

/* 불러오는 중: 아이콘 40×40, 이름 200×28 */
.skeleton-icon {
  flex-shrink: 0;
  width: var(--game-icon-card);
  height: var(--game-icon-card);
  background: var(--color-surface);
  border-radius: var(--radius-btn);
}

.skeleton-name {
  width: 200px;
  height: 28px;
  background: var(--color-surface);
  border-radius: var(--radius-btn);
}

.game-screen-slot {
  margin-top: var(--space-4);
}
</style>
