<!--
  GameSelectPreview.vue — 게임 목록 화면 미리보기
  접속:
    /preview/game-select-guest   : 로그인 전
    /preview/game-select-open    : 로그인 후, 방송 패널 펼침
    /preview/game-select-closed  : 로그인 후, 방송 패널 접힘 (오른쪽 여백 퀵메뉴 버튼)
    /preview/game-select-empty   : 검색 결과 없음

  확인 항목:
    - 너비 1920 / 1024, 높이 1000 / 600 (iframe 안에서 실제 미디어 쿼리와 sticky가 적용됨)
    - 라이트 / 다크 (OS 설정과 별개로 강제 전환)
    - 예시 데이터: 21개 3페이지(마지막 페이지 3개) / 6개 1페이지(페이지 번호 숨김) / 100개 12페이지(번호 이동)
    - OBS 연결됨 / 안 됨, 표출 URL 짧음 / 김 (로그인 후)
    - 분류, 정렬, 검색(Enter), 페이지, 패널 접기·펼치기는 화면 안에서 직접 눌러 볼 수 있다
-->
<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'

const state = String(useRoute().meta.state ?? 'guest')

const width = ref(1920)
const height = ref(1000)
const theme = ref<'light' | 'dark'>('light')
const data = ref<'basic' | 'one' | 'many'>('basic')
const obs = ref<'on' | 'off'>('on')
const url = ref<'short' | 'long'>('short')

const src = computed(
  () =>
    `/preview/game-select-frame?state=${state}&theme=${theme.value}&data=${data.value}&obs=${obs.value}&url=${url.value}`,
)

const widths = [1920, 1024]
const heights = [1000, 600]
const themes = [['light', '라이트'], ['dark', '다크']] as const
const datas = [['basic', '21개 (3페이지)'], ['one', '6개 (1페이지)'], ['many', '100개 (12페이지)']] as const
const obsStates = [['on', 'OBS 연결됨'], ['off', 'OBS 연결 안 됨']] as const
const urls = [['short', 'URL 짧음'], ['long', 'URL 김']] as const
const loggedIn = state !== 'guest'
</script>

<template>
  <div class="game-select-preview">
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
      <div v-if="state !== 'empty'" class="preset-buttons">
        <button
          v-for="[v, label] in datas" :key="v"
          class="btn btn-sm" :class="data === v ? 'btn-primary' : 'btn-secondary'"
          @click="data = v"
        >{{ label }}</button>
      </div>
      <template v-if="loggedIn">
        <div class="preset-buttons">
          <button
            v-for="[v, label] in obsStates" :key="v"
            class="btn btn-sm" :class="obs === v ? 'btn-primary' : 'btn-secondary'"
            @click="obs = v"
          >{{ label }}</button>
        </div>
        <div class="preset-buttons">
          <button
            v-for="[v, label] in urls" :key="v"
            class="btn btn-sm" :class="url === v ? 'btn-primary' : 'btn-secondary'"
            @click="url = v"
          >{{ label }}</button>
        </div>
      </template>
    </div>

    <div class="preview-stage">
      <iframe :key="src" class="preview-frame" :src="src" :width="width" :height="height" title="게임 목록 화면 미리보기" />
    </div>
  </div>
</template>

<style scoped>
.game-select-preview {
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
