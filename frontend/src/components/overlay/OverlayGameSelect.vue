<!--
  OverlayGameSelect.vue — 송출 화면: 게임 목록 (표출 화면, 1920×1080 고정)
  screens/overlay-game-select.md. 버튼, 마우스 올림 효과 없음 (보여주기만)

  props:
    games: Game[]        — 지금 페이지의 게임 (최대 9개)
    category: string     — 지금 분류 ('' = 전체)
    sort: string         — 지금 정렬 ('default' | 'popular'). 지금 정렬 하나만 표시
    page: number         — 지금 페이지 (1부터)
    totalPages: number   — 전체 페이지 수

  emits: 없음
-->
<script setup lang="ts">
import { computed } from 'vue'
import { IconMessageCircle } from '@tabler/icons-vue'
import GameIcon from '@/components/GameIcon.vue'
import { categoryLabels, categoryTabs, sortTabs } from '@/types/game'
import type { Game } from '@/types/game'
import { pageWindow } from '@/utils/pagination'

const props = withDefaults(defineProps<{
  games?: Game[]
  category?: string
  sort?: string
  page?: number
  totalPages?: number
}>(), {
  games: () => [],
  category: '',
  sort: 'default',
  page: 1,
  totalPages: 1,
})

const pageNumbers = computed(() => pageWindow(props.page, props.totalPages))
const currentSort = computed(() => sortTabs.find((item) => item.value === props.sort) ?? sortTabs[0]!)
</script>

<template>
  <div class="overlay-screen">
    <div class="overlay-content">
      <div class="overlay-logo">
        <img src="@/assets/logo.svg" width="56" height="56" alt="">
        <span>위마당</span>
      </div>

      <h1 class="overlay-title">게임을 고르고 있어요</h1>

      <div class="overlay-categories">
        <span
          v-for="item in categoryTabs"
          :key="item.value"
          class="overlay-chip overlay-category"
          :class="{ selected: category === item.value }"
        >
          {{ item.label }}
        </span>
        <span class="overlay-chip overlay-category overlay-sort selected">{{ currentSort.label }}</span>
      </div>

      <ul v-if="games.length > 0" class="overlay-tiles">
        <li v-for="game in games" :key="game.id" class="overlay-tile">
          <GameIcon :name="game.icon" :color="game.color" :category="game.category" size="tile" />
          <div class="overlay-tile-text">
            <span class="overlay-pill">{{ categoryLabels[game.category] }}</span>
            <span class="overlay-game-name">{{ game.name }}</span>
          </div>
        </li>
      </ul>
      <p v-else class="overlay-empty">찾는 게임이 없어요.</p>

      <div class="overlay-guide">
        <IconMessageCircle size="24" stroke="2" />
        <span>치지직 채팅으로 참여해요</span>
      </div>

      <div v-if="totalPages > 1" class="overlay-pages">
        <span
          v-for="n in pageNumbers"
          :key="n"
          class="overlay-chip overlay-page-cell"
          :class="{ selected: n === page }"
        >
          {{ n }}
        </span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.overlay-title {
  margin-top: var(--space-4);
  font-size: var(--font-size-overlay-title);
  text-align: center;
}

.overlay-categories {
  display: flex;
  justify-content: center;
  gap: var(--space-3);
  margin-top: var(--space-4);
}

/* 분류, 페이지 번호 칸: 선택 표시 규칙 (design-rules.md "선택 표시"), 누를 수 없음 */
.overlay-chip {
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-btn);
  color: var(--color-text-sub);
  font-size: var(--font-size-overlay-small);
  line-height: 1;
}

.overlay-chip.selected {
  background: var(--color-primary);
  border-color: var(--color-primary-border);
  color: var(--color-on-primary);
  font-weight: var(--font-weight-bold);
}

.overlay-category {
  height: var(--overlay-cell);
  padding: 0 var(--space-4);
}

/* 지금 정렬 하나: 분류 묶음 바로 오른쪽, 분류 묶음과 사이 32 (칸 사이 gap 16 포함) */
.overlay-sort {
  margin-left: calc(var(--space-5) - var(--space-3));
}

.overlay-tiles {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-auto-rows: var(--overlay-tile-height);
  gap: var(--space-4);
  margin-top: var(--space-5);
}

.overlay-tile {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  min-width: 0;
  padding: var(--space-4);
  background: var(--color-surface);
  border-radius: var(--radius-bubble);
}

.overlay-tile-text {
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: flex-start;
  gap: var(--space-2);
  min-width: 0;
}

.overlay-pill {
  padding: 0 var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-pill);
  color: var(--color-text-sub);
  font-size: var(--font-size-overlay-small);
  line-height: var(--line-height-heading);
}

.overlay-game-name {
  max-width: 100%;
  overflow: hidden;
  font-size: var(--font-size-overlay-sub);
  font-weight: var(--font-weight-bold);
  line-height: var(--line-height-heading);
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 검색 결과 없음: 타일 3줄 자리 가운데 */
.overlay-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  height: calc(var(--overlay-tile-height) * 3 + var(--space-4) * 2);
  margin-top: var(--space-5);
  font-size: var(--font-size-overlay-sub);
}

.overlay-pages {
  position: absolute;
  right: 0;
  bottom: 0;
  display: flex;
  gap: var(--space-2);
}

.overlay-page-cell {
  width: var(--overlay-cell);
  height: var(--overlay-cell);
}
</style>
