<!--
  GamePlayFramePreview.vue — 게임 화면 미리보기용 내부 페이지 (GamePlayPreview의 iframe 안에서 열림)
  접속: /preview/game-play-frame?game=normal|loading|long|noicon&screen=debate|interview&obs=on|off&theme=light|dark
  공통 프레임(헤더, 푸터)을 직접 그린다 (route meta noFrame).
-->
<script setup lang="ts">
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import GamePlayView from '@/views/GamePlayView.vue'
import BalanceDebateScreen from '@/components/games/BalanceDebateScreen.vue'
import InterviewScreen from '@/components/games/InterviewScreen.vue'
import OverlayGameReady from '@/components/overlay/OverlayGameReady.vue'
import OverlayPreviewBox from '@/components/overlay/OverlayPreviewBox.vue'
import type { Game } from '@/types/game'

const query = useRoute().query

const games: Record<string, Game> = {
  normal: { id: 1, name: '밸런스 토론', description: '', category: 'debate', screen: 'debate', icon: 'scale', color: 'purple' },
  long: { id: 2, name: '유튜브 랭킹 퀴즈 시즌2 — 조회수 순위를 맞혀 보는 대규모 퀴즈 대결', description: '', category: 'quiz', screen: 'quiz', icon: 'trophy', color: 'pink' },
  noicon: { id: 3, name: '아이콘 없는 면담', description: '', category: 'interview', screen: 'interview', icon: '', color: '' },
}
const key = String(query.game ?? 'normal')
const game: Game | null = key === 'loading' ? null : (games[key] ?? games.normal!)
const screenIsInterview = query.screen === 'interview'

onMounted(() => {
  const theme = query.theme
  if (theme === 'light' || theme === 'dark') {
    document.documentElement.dataset.theme = theme
  }
})
</script>

<template>
  <div class="app-layout">
    <AppHeader :logged-in="true" nickname="스트리머" active-menu="게임 목록" />
    <main class="app-main">
      <GamePlayView
        :game="game"
        overlay-url="https://wemadang.example.com/overlay/xxxxxxxxxxxx"
        :connected="query.obs !== 'off'"
      >
        <template v-if="game">
          <InterviewScreen v-if="screenIsInterview" />
          <BalanceDebateScreen v-else />
        </template>
        <template v-if="game" #preview>
          <OverlayPreviewBox>
            <OverlayGameReady :game="game" />
          </OverlayPreviewBox>
        </template>
      </GamePlayView>
    </main>
    <AppFooter />
  </div>
</template>
