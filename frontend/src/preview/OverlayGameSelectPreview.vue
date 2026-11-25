<!--
  OverlayGameSelectPreview.vue — 송출 화면(게임 목록) 미리보기
  접속: /preview/overlay-game-select

  확인 항목:
    - 1920×1080 그대로 / 0.3배(576×324)로 줄인 것 (방송 패널 미리보기 자리에 넣은 모양)
    - 라이트 / 다크 (OS 설정과 별개로 강제 전환)
    - 기본 9개 / 마지막 페이지 3개 / 페이지 1개(번호 숨김) / 페이지 많음(번호 이동) / 검색 결과 없음
    - 분류, 정렬 선택 표시 (긴 게임 이름은 예시 데이터에 포함)
-->
<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import BroadcastPanel from '@/components/BroadcastPanel.vue'
import OverlayGameSelect from '@/components/overlay/OverlayGameSelect.vue'
import OverlayPreviewBox from '@/components/overlay/OverlayPreviewBox.vue'
import { categoryTabs, sortTabs } from '@/types/game'
import { makeGames } from './sampleGames'

const PAGE_SIZE = 9

const size = ref<'both' | 'full' | 'small'>('both')
const theme = ref<'light' | 'dark'>('light')
const data = ref<'basic' | 'last' | 'one' | 'many' | 'empty'>('basic')
const category = ref('')
const sort = ref('default')

const sizes = [['both', '둘 다'], ['full', '1920×1080'], ['small', '0.3배 (576×324)']] as const
const themes = [['light', '라이트'], ['dark', '다크']] as const
const datas = [
  ['basic', '기본 9개 (2/3페이지)'],
  ['last', '마지막 페이지 3개'],
  ['one', '페이지 1개'],
  ['many', '페이지 많음 (7/12)'],
  ['empty', '검색 결과 없음'],
] as const

const scenario = computed(() => {
  switch (data.value) {
    case 'last': return { total: 21, page: 3 }
    case 'one': return { total: 6, page: 1 }
    case 'many': return { total: 108, page: 7 }
    case 'empty': return { total: 0, page: 1 }
    default: return { total: 27, page: 2 }
  }
})

const allGames = computed(() =>
  makeGames(scenario.value.total).filter((g) => !category.value || g.category === category.value),
)
const totalPages = computed(() => Math.max(Math.ceil(allGames.value.length / PAGE_SIZE), 1))
const page = computed(() => Math.min(scenario.value.page, totalPages.value))
const games = computed(() => allGames.value.slice((page.value - 1) * PAGE_SIZE, page.value * PAGE_SIZE))

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
          v-for="[v, label] in datas" :key="v"
          class="btn btn-sm" :class="data === v ? 'btn-primary' : 'btn-secondary'"
          @click="data = v"
        >{{ label }}</button>
      </div>
      <div class="preset-buttons">
        <button
          v-for="item in categoryTabs" :key="item.value"
          class="btn btn-sm" :class="category === item.value ? 'btn-primary' : 'btn-secondary'"
          @click="category = item.value"
        >{{ item.label }}</button>
      </div>
      <div class="preset-buttons">
        <button
          v-for="item in sortTabs" :key="item.value"
          class="btn btn-sm" :class="sort === item.value ? 'btn-primary' : 'btn-secondary'"
          @click="sort = item.value"
        >{{ item.label }}</button>
      </div>
    </div>

    <div class="preview-stage">
      <section v-if="size !== 'small'" class="preview-block">
        <h2 class="text-subheading">1920×1080</h2>
        <div class="preview-full">
          <OverlayGameSelect :games="games" :category="category" :sort="sort" :page="page" :total-pages="totalPages" />
        </div>
      </section>

      <section v-if="size !== 'full'" class="preview-block">
        <h2 class="text-subheading">0.3배 (방송 패널 미리보기 자리)</h2>
        <div class="preview-panel">
          <BroadcastPanel :open="true" overlay-url="https://wemadang.example.com/overlay/xxxxxxxxxxxx" :connected="true">
            <OverlayPreviewBox>
              <OverlayGameSelect :games="games" :category="category" :sort="sort" :page="page" :total-pages="totalPages" />
            </OverlayPreviewBox>
          </BroadcastPanel>
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

.preview-panel {
  width: var(--panel-width);
}
</style>
