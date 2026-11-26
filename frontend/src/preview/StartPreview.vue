<!--
  StartPreview.vue — 시작 화면 미리보기
  접속: /preview/start

  확인 항목:
    - 너비 1920 / 1024 (iframe 안에서 실제 미디어 쿼리가 적용됨)
    - 라이트 / 다크 (OS 설정과 별개로 강제 전환)
    - 로그인 전 / 로그인 후 (버튼이 하나만 남음) / 로그인 확인 모달 (AppModal 안에 열린 상태)
    - 카운트: 0 / 아주 큰 숫자(쉼표 표시)
    - 토론 화면 예시 이미지: 없음(빈 자리) / 있음
-->
<script setup lang="ts">
import { computed, ref } from 'vue'

const width = ref(1920)
const theme = ref<'light' | 'dark'>('light')
const state = ref<'guest' | 'login' | 'modal'>('guest')
const data = ref<'basic' | 'large'>('basic')
const image = ref<'off' | 'on'>('off')

const src = computed(
  () => `/preview/start-frame?state=${state.value}&theme=${theme.value}&data=${data.value}&image=${image.value}`,
)

const widths = [1920, 1024]
const states = [['guest', '로그인 전'], ['login', '로그인 후'], ['modal', '로그인 확인 모달']] as const
const themes = [['light', '라이트'], ['dark', '다크']] as const
const datas = [['basic', '카운트 0'], ['large', '큰 숫자']] as const
const images = [['off', '이미지 없음'], ['on', '이미지 있음']] as const
</script>

<template>
  <div class="start-preview">
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
          v-for="[v, label] in states" :key="v"
          class="btn btn-sm" :class="state === v ? 'btn-primary' : 'btn-secondary'"
          @click="state = v"
        >{{ label }}</button>
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
          v-for="[v, label] in datas" :key="v"
          class="btn btn-sm" :class="data === v ? 'btn-primary' : 'btn-secondary'"
          @click="data = v"
        >{{ label }}</button>
      </div>
      <div class="preset-buttons">
        <button
          v-for="[v, label] in images" :key="v"
          class="btn btn-sm" :class="image === v ? 'btn-primary' : 'btn-secondary'"
          @click="image = v"
        >{{ label }}</button>
      </div>
    </div>

    <div class="preview-stage">
      <iframe :key="src" class="preview-frame" :src="src" :width="width" height="1000" title="시작 화면 미리보기" />
    </div>
  </div>
</template>

<style scoped>
.start-preview {
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
