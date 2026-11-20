<!--
  OverlayGameReadyPreview.vue — 송출 화면(게임 첫 화면) 미리보기
  접속: /preview/overlay-game-ready

  확인 항목:
    - 1920×1080 그대로 / 0.3배(576×324)로 줄인 것
    - 라이트 / 다크 (OS 설정과 별개로 강제 전환)
    - 게임 이름: 보통 / 한 줄 넘는 긴 이름(2줄) / 두 줄 넘는 이름(...) / 아이콘 없는 게임
-->
<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import OverlayGameReady from '@/components/overlay/OverlayGameReady.vue'
import OverlayPreviewBox from '@/components/overlay/OverlayPreviewBox.vue'
import type { Game } from '@/types/game'

const size = ref<'both' | 'full' | 'small'>('both')
const theme = ref<'light' | 'dark'>('light')
const sample = ref<'normal' | 'long' | 'longer' | 'noicon'>('normal')

const sizes = [['both', '둘 다'], ['full', '1920×1080'], ['small', '0.3배 (576×324)']] as const
const themes = [['light', '라이트'], ['dark', '다크']] as const
const samples = [
  ['normal', '보통 이름'],
  ['long', '긴 이름 (2줄)'],
  ['longer', '아주 긴 이름 (...)'],
  ['noicon', '아이콘 없는 게임'],
] as const

const games: Record<typeof sample.value, Game> = {
  normal: { id: 1, name: '밸런스 토론', description: '', category: 'debate', screen: 'debate', icon: 'scale', color: 'purple' },
  long: { id: 2, name: '유튜브 랭킹 퀴즈 시즌2 — 조회수 순위를 맞혀 보는 대규모 퀴즈 대결', description: '', category: 'quiz', screen: 'quiz', icon: 'trophy', color: 'pink' },
  longer: { id: 3, name: '유튜브 랭킹 퀴즈 시즌2 — 조회수 순위를 맞혀 보는 대규모 퀴즈 대결 특별 편 영상 속 장면과 노래와 인물까지 모두 맞혀 보는 최종 결승전 이야기', description: '', category: 'quiz', screen: 'quiz', icon: 'trophy', color: 'pink' },
  noicon: { id: 4, name: '아이콘 없는 면담', description: '', category: 'interview', screen: 'interview', icon: '', color: '' },
}

const game = computed(() => games[sample.value])

function applyTheme() {
  document.documentElement.dataset.theme = theme.value
}
onMounted(applyTheme)
watch(theme, applyTheme)
</script>

<template>
  <div class="overlay-preview">
    <div class="preview-controls">
      <div class="preset-buttons">
        <button
          v-for="[v, label] in sizes" :key="v"
          class="btn btn-sm" :class="size === v ? 'btn-primary' : 'btn-secondary'"
          @click="size = v"
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
          v-for="[v, label] in samples" :key="v"
          class="btn btn-sm" :class="sample === v ? 'btn-primary' : 'btn-secondary'"
          @click="sample = v"
        >{{ label }}</button>
      </div>
    </div>

    <div class="preview-stage">
      <section v-if="size !== 'small'" class="preview-block">
        <h2 class="text-subheading">1920×1080</h2>
        <div class="preview-full">
          <OverlayGameReady :game="game" />
        </div>
      </section>

      <section v-if="size !== 'full'" class="preview-block">
        <h2 class="text-subheading">0.3배 (576×324)</h2>
        <div class="preview-small">
          <OverlayPreviewBox>
            <OverlayGameReady :game="game" />
          </OverlayPreviewBox>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
.overlay-preview {
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
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
  overflow-x: auto;
}

.preview-block {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.preview-full {
  width: var(--overlay-width);
  border: 1px solid var(--color-border);
}

.preview-small {
  width: var(--preview-width);
  border: 1px solid var(--color-border);
}
</style>
