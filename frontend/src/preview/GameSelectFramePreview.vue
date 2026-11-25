<!--
  GameSelectFramePreview.vue — 게임 목록 화면 미리보기용 내부 페이지 (GameSelectPreview의 iframe 안에서 열림)
  접속: /preview/game-select-frame?state=guest|open|closed|empty&theme=light|dark&data=basic|one|many&obs=on|off&url=short|long
  공통 프레임(헤더, 푸터)을 직접 그린다 (route meta noFrame).
  가짜 데이터와, 분류·정렬·검색·페이지·패널 접기를 눌러 볼 수 있게 하는 임시 처리만 여기에 있다 (실제 동작은 개발자가 연결).
-->
<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import GameSelectView from '@/views/GameSelectView.vue'
import OverlayGameSelect from '@/components/overlay/OverlayGameSelect.vue'
import OverlayPreviewBox from '@/components/overlay/OverlayPreviewBox.vue'
import { makeGames } from './sampleGames'

const query = useRoute().query
const state = String(query.state ?? 'guest')
const dataSize = String(query.data ?? 'basic')

const PAGE_SIZE = 9

const total = dataSize === 'one' ? 6 : dataSize === 'many' ? 100 : 21
const allGames = makeGames(total)

const category = ref('')
const sort = ref('default')
const keyword = ref(state === 'empty' ? '없는 게임 이름' : '')
const page = ref(1)
const panelOpen = ref(state !== 'closed')

// 인기순용 가짜 플레이 수 (같으면 정해 둔 순서)
const plays = (id: number) => (id * 7) % 13

const filtered = computed(() => {
  const list = allGames.filter(
    (g) => (!category.value || g.category === category.value) && g.name.includes(keyword.value),
  )
  return sort.value === 'popular' ? [...list].sort((a, b) => plays(b.id) - plays(a.id) || a.id - b.id) : list
})
const totalPages = computed(() => Math.max(Math.ceil(filtered.value.length / PAGE_SIZE), 1))
const games = computed(() => filtered.value.slice((page.value - 1) * PAGE_SIZE, page.value * PAGE_SIZE))

function onChangeCategory(value: string) {
  category.value = value
  page.value = 1
}

function onChangeSort(value: string) {
  sort.value = value
  page.value = 1
}

function onSearch(value: string) {
  keyword.value = value
  page.value = 1
}

const overlayUrl = query.url === 'long'
  ? 'https://wemadang.example.com/overlay/aB3dE5fG7hJ9kL1mN3pQ5rS7tU9vW1xY3zA5bC7dE9fG1hJ3kL5mN7pQ9rS'
  : 'https://wemadang.example.com/overlay/xxxxxxxxxxxx'

onMounted(() => {
  const theme = query.theme
  if (theme === 'light' || theme === 'dark') {
    document.documentElement.dataset.theme = theme
  }
})
</script>

<template>
  <div class="app-layout">
    <AppHeader
      :logged-in="state !== 'guest'"
      nickname="스트리머"
      active-menu="게임 목록"
    />
    <main class="app-main">
      <GameSelectView
        :games="games"
        :category="category"
        :sort="sort"
        :keyword="keyword"
        :page="page"
        :total-pages="totalPages"
        :logged-in="state !== 'guest'"
        :panel-open="panelOpen"
        :overlay-url="overlayUrl"
        :connected="query.obs !== 'off'"
        @change-category="onChangeCategory"
        @change-sort="onChangeSort"
        @search="onSearch"
        @change-page="page = $event"
        @toggle-panel="panelOpen = !panelOpen"
      >
        <template #preview>
          <OverlayPreviewBox>
            <OverlayGameSelect :games="games" :category="category" :sort="sort" :page="page" :total-pages="totalPages" />
          </OverlayPreviewBox>
        </template>
      </GameSelectView>
    </main>
    <AppFooter />
  </div>
</template>
