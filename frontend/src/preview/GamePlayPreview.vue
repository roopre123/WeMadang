<!--
  GamePlayPreview.vue — 게임 화면(공통 틀) 미리보기
  접속: /preview/game-play

  확인 항목:
    - 너비 1920 / 1024 (iframe 안에서 실제 미디어 쿼리와 sticky가 적용됨), 높이 1000 / 600
    - 라이트 / 다크 (OS 설정과 별개로 강제 전환)
    - 게임 정보 있음 / 불러오는 중(null) / 긴 이름 / 아이콘 없는 게임
    - 슬롯: BalanceDebateScreen / InterviewScreen
    - 방송 패널 미리보기: OverlayGameReady 0.3배 (접기 버튼 없음), OBS 연결됨 / 안 됨
-->
<script setup lang="ts">
import { computed, ref } from 'vue'

const width = ref(1920)
const height = ref(1000)
const theme = ref<'light' | 'dark'>('light')
const game = ref<'normal' | 'loading' | 'long' | 'noicon'>('normal')
const screen = ref<'debate' | 'interview'>('debate')
const obs = ref<'on' | 'off'>('on')

const src = computed(
  () =>
    `/preview/game-play-frame?game=${game.value}&screen=${screen.value}&obs=${obs.value}&theme=${theme.value}`,
)

const widths = [1920, 1024]
const heights = [1000, 600]
const themes = [['light', '라이트'], ['dark', '다크']] as const
const gameStates = [
  ['normal', '게임 정보 있음'],
  ['loading', '불러오는 중 (null)'],
  ['long', '긴 이름'],
  ['noicon', '아이콘 없는 게임'],
] as const
const screens = [['debate', 'BalanceDebateScreen'], ['interview', 'InterviewScreen']] as const
const obsStates = [['on', 'OBS 연결됨'], ['off', 'OBS 연결 안 됨']] as const
</script>

<template>
  <div class="game-play-preview">
    <div class="preview-controls">
      <div class="preset-buttons">
        <button
          v-for="w in widths" :key="w"
          class="btn btn-sm" :class="width === w ? 'btn-primary' : 'btn-secondary'"
          @click="width = w"
        >{{ w }}</button>
      </div>
      <div class="preset-buttons">
        <button
          v-for="h in heights" :key="h"
          class="btn btn-sm" :class="height === h ? 'btn-primary' : 'btn-secondary'"
          @click="height = h"
        >높이 {{ h }}</button>
      </div>
      <div class="preset-buttons">
        <button
          v-for="[v, label] in themes" :key="v"
          class="btn btn-sm" :class="theme === v ? 'btn-primary' : 'btn-secondary'"
          @click="theme = v"
        >{{ label }}</button>
      </div>
      <div class="preset-buttons">
        <button
          v-for="[v, label] in gameStates" :key="v"
          class="btn btn-sm" :class="game === v ? 'btn-primary' : 'btn-secondary'"
          @click="game = v"
        >{{ label }}</button>
      </div>
      <div class="preset-buttons">
        <button
          v-for="[v, label] in screens" :key="v"
          class="btn btn-sm" :class="screen === v ? 'btn-primary' : 'btn-secondary'"
          @click="screen = v"
        >{{ label }}</button>
      </div>
      <div class="preset-buttons">
        <button
          v-for="[v, label] in obsStates" :key="v"
          class="btn btn-sm" :class="obs === v ? 'btn-primary' : 'btn-secondary'"
          @click="obs = v"
        >{{ label }}</button>
      </div>
    </div>

    <div class="preview-stage">
      <iframe :key="src" class="preview-frame" :src="src" :width="width" :height="height" title="게임 화면 미리보기" />
    </div>
  </div>
</template>

<style scoped>
.game-play-preview {
  padding: var(--space-3);
}

.preview-controls {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-4);
  margin-bottom: var(--space-3);
}

.preset-buttons {
  display: flex;
  gap: var(--space-2);
}

.preview-stage {
  overflow-x: auto;
}

.preview-frame {
  display: block;
  border: 1px solid var(--color-border);
  background: var(--color-background);
}
</style>
