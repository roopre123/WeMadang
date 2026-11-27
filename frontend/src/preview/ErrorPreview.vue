<!--
  ErrorPreview.vue — 공통 에러 화면 미리보기
  접속: /preview/error

  확인 항목:
    - 너비 1920 / 1024 (iframe 안에서 실제 미디어 쿼리가 적용됨), 높이 1000 / 600
    - 라이트 / 다크 (OS 설정과 별개로 강제 전환)
    - type 3가지 (없는 주소 / 없는 게임 / 불러오기 실패) × 로그인 전 / 로그인 후
    - 로그인 후: 방송 패널 (접기 없음, 미리보기 칸은 비어 있어 검은 상자), OBS 연결됨 / 안 됨
    - 넓게 보기: 켜기 / 끄기 (로그인 후에만 적용). 1920×1000에서 856/896 배치, 너비 1823 / 높이 799에서는 기본 모양
-->
<script setup lang="ts">
import { computed, ref } from 'vue'

const width = ref(1920)
const height = ref(1000)
const wide = ref<'off' | 'on'>('on')
const theme = ref<'light' | 'dark'>('light')
const type = ref<'not-found-page' | 'not-found-game' | 'load-failed'>('not-found-game')
const login = ref<'off' | 'on'>('on')
const obs = ref<'on' | 'off'>('on')

const src = computed(
  () =>
    `/preview/error-frame?type=${type.value}&login=${login.value}&obs=${obs.value}&wide=${wide.value}&theme=${theme.value}`,
)

const widths = [1920, 1823, 1024]
const heights = [1000, 799, 600]
const wides = [['on', '넓게 켜기'], ['off', '넓게 끄기']] as const
const themes = [['light', '라이트'], ['dark', '다크']] as const
const types = [
  ['not-found-page', '없는 주소'],
  ['not-found-game', '없는 게임'],
  ['load-failed', '불러오기 실패'],
] as const
const logins = [['off', '로그인 전'], ['on', '로그인 후']] as const
const obsStates = [['on', 'OBS 연결됨'], ['off', 'OBS 연결 안 됨']] as const
</script>

<template>
  <div class="error-preview">
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
          v-for="[v, label] in types" :key="v"
          class="btn btn-sm" :class="type === v ? 'btn-primary' : 'btn-secondary'"
          @click="type = v"
        >{{ label }}</button>
      </div>
      <div class="preset-buttons">
        <button
          v-for="[v, label] in logins" :key="v"
          class="btn btn-sm" :class="login === v ? 'btn-primary' : 'btn-secondary'"
          @click="login = v"
        >{{ label }}</button>
      </div>
      <div v-if="login === 'on'" class="preset-buttons">
        <button
          v-for="[v, label] in obsStates" :key="v"
          class="btn btn-sm" :class="obs === v ? 'btn-primary' : 'btn-secondary'"
          @click="obs = v"
        >{{ label }}</button>
      </div>
      <div class="preset-buttons">
        <button
          v-for="[v, label] in wides" :key="v"
          class="btn btn-sm" :class="wide === v ? 'btn-primary' : 'btn-secondary'"
          @click="wide = v"
        >{{ label }}</button>
      </div>
    </div>

    <div class="preview-stage">
      <iframe :key="src" class="preview-frame" :src="src" :width="width" :height="height" title="에러 화면 미리보기" />
    </div>
  </div>
</template>

<style scoped>
.error-preview {
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
