<!--
  GameSelectView.vue — 게임 목록 화면 (조작 화면)
  screens/game-select.md, screens/broadcast-panel.md

  props:
    games: Game[]          — 지금 페이지의 게임 (최대 9개)
    category: string       — 지금 분류 ('' = 전체)
    sort: string           — 지금 정렬 ('default' = 기본순, 'popular' = 인기순)
    keyword: string        — 지금 검색어
    page: number           — 지금 페이지 (1부터)
    totalPages: number     — 전체 페이지 수
    loggedIn: boolean      — 로그인 여부 (방송 패널 표시)
    panelOpen: boolean     — 방송 패널 펼침 여부
    overlayUrl: string     — (방송 패널) 표출 URL
    connected: boolean     — (방송 패널) OBS 연결 여부

  slots:
    preview                — (방송 패널) 미리보기 칸

  emits:
    select-game(id)        — [시작하기] 클릭 (로그인 안 했으면 부모가 로그인으로 보냄)
    change-category(value) — 분류 버튼 클릭 ('' | 'quiz' | 'debate' | 'interview')
    change-sort(value)     — 정렬 버튼 클릭 ('default' | 'popular')
    search(keyword)        — 검색칸에서 Enter
    change-page(page)      — 페이지 번호 클릭
    toggle-panel           — (방송 패널) 접기 / 펼치기
    copy-url               — (방송 패널) [복사] 클릭

  game-select.md의 props 표에는 overlayUrl, connected, preview 슬롯, toggle-panel / copy-url 이 없다.
  방송 패널(broadcast-panel.md)에 넘겨주기 위해 추가했다.
-->
<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import {
  IconChevronLeft,
  IconChevronRight,
  IconChevronsLeft,
  IconChevronsRight,
  IconSearch,
} from '@tabler/icons-vue'
import BroadcastPanel from '@/components/BroadcastPanel.vue'
import GameIcon from '@/components/GameIcon.vue'
import { categoryLabels, categoryTabs, sortTabs } from '@/types/game'
import type { Game } from '@/types/game'
import { pageWindow } from '@/utils/pagination'

const props = withDefaults(
  defineProps<{
    games?: Game[]
    category?: string
    sort?: string
    keyword?: string
    page?: number
    totalPages?: number
    loggedIn?: boolean
    panelOpen?: boolean
    overlayUrl?: string
    connected?: boolean
  }>(),
  {
    games: () => [],
    category: '',
    sort: 'default',
    keyword: '',
    page: 1,
    totalPages: 1,
    loggedIn: false,
    panelOpen: true,
    overlayUrl: '',
    connected: false,
  },
)

const emit = defineEmits<{
  'select-game': [id: number]
  'change-category': [category: string]
  'change-sort': [sort: string]
  search: [keyword: string]
  'change-page': [page: number]
  'toggle-panel': []
  'copy-url': []
}>()

// 검색칸은 Enter를 눌러야 검색하므로 입력 중인 글자는 따로 들고 있고, 부모가 바꾼 검색어만 따라간다
const searchText = ref(props.keyword)
watch(
  () => props.keyword,
  (value) => {
    searchText.value = value
  },
)

function onSearchEnter(event: KeyboardEvent) {
  // 한글 입력 중 글자를 확정하는 Enter는 검색으로 치지 않는다
  if (event.isComposing) return
  emit('search', searchText.value)
}

const withPanel = computed(() => props.loggedIn && props.panelOpen)

const pageNumbers = computed(() => pageWindow(props.page, props.totalPages))

const isFirstPage = computed(() => props.page <= 1)
const isLastPage = computed(() => props.page >= props.totalPages)
</script>

<template>
  <section class="game-select">
    <div class="container game-select-body" :class="{ 'panel-layout': withPanel }">
      <div class="game-select-main">
        <div class="select-title">
          <h1 class="text-heading">게임 목록</h1>
          <p class="text-body select-guide">진행할 게임을 골라주세요.</p>
        </div>

        <div class="select-toolbar">
          <div class="filter-buttons">
            <div class="category-buttons">
              <button
                v-for="item in categoryTabs"
                :key="item.value"
                class="btn btn-sm btn-option"
                :class="{ selected: category === item.value }"
                @click="emit('change-category', item.value)"
              >
                {{ item.label }}
              </button>
            </div>

            <div class="sort-buttons" role="group" aria-label="정렬">
              <button
                v-for="item in sortTabs"
                :key="item.value"
                class="btn btn-sm btn-option"
                :class="{ selected: sort === item.value }"
                :aria-pressed="sort === item.value"
                @click="emit('change-sort', item.value)"
              >
                {{ item.label }}
              </button>
            </div>
          </div>

          <label class="search-box">
            <IconSearch class="search-icon" size="16" stroke="2" />
            <input
              v-model="searchText"
              class="input search-input"
              type="text"
              placeholder="게임 이름으로 검색"
              aria-label="게임 이름으로 검색"
              @keydown.enter="onSearchEnter"
            />
          </label>
        </div>

        <ul v-if="games.length > 0" class="game-grid">
          <li v-for="game in games" :key="game.id" class="game-grid-item">
            <article class="card game-card">
              <div class="game-card-head">
                <GameIcon :name="game.icon" :color="game.color" :category="game.category" />
                <div class="game-card-title">
                  <span class="category-pill">{{ categoryLabels[game.category] }}</span>
                  <h2 class="text-subheading game-name">{{ game.name }}</h2>
                </div>
              </div>

              <p class="text-small game-desc">{{ game.description }}</p>

              <div class="game-card-foot">
                <button class="btn btn-secondary btn-sm" @click="emit('select-game', game.id)">
                  시작하기
                </button>
              </div>
            </article>
          </li>
        </ul>

        <div v-else class="empty-result">
          <p class="text-body">찾는 게임이 없어요.</p>
          <p class="text-small">다른 이름으로 검색해 보세요.</p>
        </div>

        <nav v-if="totalPages > 1" class="pagination" aria-label="페이지">
          <button
            class="btn btn-sm btn-option page-btn"
            :disabled="isFirstPage"
            aria-label="맨 앞"
            @click="emit('change-page', 1)"
          >
            <IconChevronsLeft size="16" stroke="2" />
          </button>
          <button
            class="btn btn-sm btn-option page-btn"
            :disabled="isFirstPage"
            aria-label="이전"
            @click="emit('change-page', page - 1)"
          >
            <IconChevronLeft size="16" stroke="2" />
          </button>
          <button
            v-for="n in pageNumbers"
            :key="n"
            class="btn btn-sm btn-option page-btn"
            :class="{ selected: n === page }"
            :aria-current="n === page ? 'page' : undefined"
            @click="emit('change-page', n)"
          >
            {{ n }}
          </button>
          <button
            class="btn btn-sm btn-option page-btn"
            :disabled="isLastPage"
            aria-label="다음"
            @click="emit('change-page', page + 1)"
          >
            <IconChevronRight size="16" stroke="2" />
          </button>
          <button
            class="btn btn-sm btn-option page-btn"
            :disabled="isLastPage"
            aria-label="맨 뒤"
            @click="emit('change-page', totalPages)"
          >
            <IconChevronsRight size="16" stroke="2" />
          </button>
        </nav>
      </div>

      <BroadcastPanel
        v-if="loggedIn"
        :open="panelOpen"
        :overlay-url="overlayUrl"
        :connected="connected"
        @toggle="emit('toggle-panel')"
        @copy-url="emit('copy-url')"
      >
        <slot name="preview" />
      </BroadcastPanel>
    </div>
  </section>
</template>

<style scoped>
/* 접힘 퀵메뉴 버튼의 기준 (layout.css .broadcast-quick-wrap) */
.game-select {
  position: relative;
  padding: var(--space-6) 0;
}

/* 분류 + 정렬 (정렬은 분류 묶음과 사이 32) */
.filter-buttons {
  display: flex;
  align-items: center;
  gap: var(--space-5);
}

/* 정렬: 두 칸이 붙은 버튼 묶음 */
.sort-buttons {
  display: flex;
  flex-shrink: 0;
}

.sort-buttons .btn {
  border-radius: 0;
}

.sort-buttons .btn:first-child {
  border-radius: var(--radius-btn) 0 0 var(--radius-btn);
}

.sort-buttons .btn:last-child {
  margin-left: -1px;
  border-radius: 0 var(--radius-btn) var(--radius-btn) 0;
}

.sort-buttons .btn.selected {
  position: relative;
}

.select-guide {
  color: var(--color-text-sub);
}

.select-toolbar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: var(--space-2);
  margin-top: var(--space-4);
}

.category-buttons {
  display: flex;
  gap: var(--space-2);
}

/* 검색칸 */
.search-box {
  position: relative;
  display: block;
  width: var(--search-width);
}

.search-icon {
  position: absolute;
  top: 50%;
  left: var(--space-panel);
  transform: translateY(-50%);
  color: var(--color-text-sub);
  pointer-events: none;
}

.search-input {
  padding-left: var(--space-5);
}

/* 게임 카드 (3열 → 패널 펼침 2열 → 좁은 화면 1열) */
.game-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: var(--space-4);
  margin-top: var(--space-5);
}

.panel-layout .game-grid {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.game-grid-item {
  display: flex;
}

.game-card {
  display: flex;
  flex: 1;
  flex-direction: column;
  gap: var(--space-3);
  min-width: 0;
}

.game-card-head {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.game-card-title {
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: flex-start;
  min-width: 0;
}

.category-pill {
  padding: 0 var(--space-pill-x);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-pill);
  color: var(--color-text-sub);
  font-size: var(--font-size-small);
  line-height: var(--line-height-body);
}

.game-name {
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.game-desc {
  display: -webkit-box;
  overflow: hidden;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  line-clamp: 2;
}

.game-card-foot {
  display: flex;
  justify-content: flex-end;
  margin-top: auto;
}

/* 검색 결과 없음 */
.empty-result {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: var(--card-min-height);
  margin-top: var(--space-5);
  text-align: center;
}

/* 페이지 번호 */
.pagination {
  display: flex;
  justify-content: center;
  gap: var(--space-2);
  margin-top: var(--space-6);
}

.page-btn {
  width: var(--btn-height-sm);
  padding: 0;
}

/* 패널 펼침(왼쪽 568): 첫 줄 분류 + 정렬, 둘째 줄 검색칸 (폭 전체), 두 줄 사이 16 */
@media (min-width: 1280px) {
  .panel-layout .select-toolbar {
    flex-direction: column;
    align-items: stretch;
    gap: var(--space-3);
  }

  .panel-layout .search-box {
    width: auto;
  }
}

@media (max-width: 1279px) {
  .game-grid,
  .panel-layout .game-grid {
    grid-template-columns: minmax(0, 1fr);
  }
}
</style>
