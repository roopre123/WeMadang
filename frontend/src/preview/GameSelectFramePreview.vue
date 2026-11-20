<!--
  GameSelectFramePreview.vue — 게임 목록 화면 미리보기용 내부 페이지 (GameSelectPreview의 iframe 안에서 열림)
  접속: /preview/game-select-frame?state=guest|open|closed|empty&theme=light|dark&data=basic|one|many&obs=on|off&url=short|long
  공통 프레임(헤더, 푸터)을 직접 그린다 (route meta noFrame).
  가짜 데이터와, 분류·검색·페이지·패널 접기를 눌러 볼 수 있게 하는 임시 처리만 여기에 있다 (실제 동작은 개발자가 연결).
-->
<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import GameSelectView from '@/views/GameSelectView.vue'
import type { Game } from '@/types/game'

const query = useRoute().query
const state = String(query.state ?? 'guest')
const dataSize = String(query.data ?? 'basic')

const PAGE_SIZE = 9

// 화면 설명 예시 데이터 + 긴 이름 + 긴 설명 + 아이콘 없는 게임 포함
const baseGames: Omit<Game, 'id'>[] = [
  { name: '밸런스 토론', description: '편 나눠 투표하고 토론해요.', category: 'debate', screen: 'debate', icon: 'scale', color: 'purple' },
  { name: '1:1 면담 (진실의 방)', description: '시청자 한 명을 뽑아 대화해요.', category: 'interview', screen: 'interview', icon: 'messages', color: 'amber' },
  { name: '스무고개', description: '질문하며 정답을 좁혀요.', category: 'quiz', screen: 'quiz', icon: 'question-mark', color: 'teal' },
  { name: '유튜브 랭킹 퀴즈 시즌2', description: '순위를 맞혀요.', category: 'quiz', screen: 'quiz', icon: 'trophy', color: 'pink' },
  { name: '국기 퀴즈', description: '국기를 보고 나라 이름을 맞혀요. 채팅으로 정답을 입력하면 가장 빨리 맞힌 시청자가 점수를 얻어요. 설명이 아주 길어서 두 줄을 넘어가는 경우도 확인해요.', category: 'quiz', screen: 'quiz', icon: 'flag', color: 'green' },
  { name: '노래 퀴즈', description: '앞부분만 듣고 제목을 맞혀요.', category: 'quiz', screen: 'quiz', icon: 'music', color: 'gray' },
  { name: '영화 장면 퀴즈', description: '장면을 보고 영화를 맞혀요.', category: 'quiz', screen: 'quiz', icon: 'movie', color: 'purple' },
  { name: '아이콘 없는 토론', description: '아이콘이 없으면 분류 기본 아이콘과 gray 색을 써요.', category: 'debate', screen: 'debate', icon: '', color: '' },
  { name: '상식 퀴즈', description: '상식 문제를 풀어요.', category: 'quiz', screen: 'quiz', icon: 'bulb', color: 'amber' },
  { name: '세계 지도 퀴즈', description: '지도에서 나라를 찾아요.', category: 'quiz', screen: 'quiz', icon: 'world', color: 'teal' },
  { name: '게임 이름 퀴즈', description: '게임 장면을 보고 이름을 맞혀요.', category: 'quiz', screen: 'quiz', icon: 'device-gamepad-2', color: 'pink' },
  { name: '찬반 토론', description: '한 주제로 찬성과 반대를 나눠요.', category: 'debate', screen: 'debate', icon: 'scale', color: 'green' },
  { name: '시청자 면담', description: '시청자와 차례로 대화해요.', category: 'interview', screen: 'interview', icon: 'messages', color: 'purple' },
]

const total = dataSize === 'one' ? 6 : dataSize === 'many' ? 100 : 21
const allGames: Game[] = Array.from({ length: total }, (_, i) => {
  const base = baseGames[i % baseGames.length]!
  const round = Math.floor(i / baseGames.length)
  return { ...base, id: i + 1, name: round > 0 ? `${base.name} ${round + 1}` : base.name }
})

const category = ref('')
const keyword = ref(state === 'empty' ? '없는 게임 이름' : '')
const page = ref(1)
const panelOpen = ref(state !== 'closed')

const filtered = computed(() =>
  allGames.filter(
    (g) => (!category.value || g.category === category.value) && g.name.includes(keyword.value),
  ),
)
const totalPages = computed(() => Math.max(Math.ceil(filtered.value.length / PAGE_SIZE), 1))
const games = computed(() => filtered.value.slice((page.value - 1) * PAGE_SIZE, page.value * PAGE_SIZE))

function onChangeCategory(value: string) {
  category.value = value
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
        :keyword="keyword"
        :page="page"
        :total-pages="totalPages"
        :logged-in="state !== 'guest'"
        :panel-open="panelOpen"
        :overlay-url="overlayUrl"
        :connected="query.obs !== 'off'"
        @change-category="onChangeCategory"
        @search="onSearch"
        @change-page="page = $event"
        @toggle-panel="panelOpen = !panelOpen"
      />
    </main>
    <AppFooter />
  </div>
</template>
