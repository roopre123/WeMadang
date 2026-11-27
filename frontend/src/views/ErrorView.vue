<!--
  ErrorView.vue — 공통 에러 화면 (조작 화면)
  기준: docs/design/screens/error.md, docs/design/screens/broadcast-panel.md
  헤더/푸터는 공통 프레임이 그린다. 송출 화면에는 영향 없음.

  props:
    type: 'not-found-page' | 'not-found-game' | 'load-failed'  — 에러 종류
    loggedIn: boolean      — 로그인 여부 (true면 방송 패널 표시, 접기 없음)
    overlayUrl: string     — (방송 패널) 표출 URL
    connected: boolean     — (방송 패널) OBS 연결 여부
    wide: boolean          — 넓게 보기 선택 여부 (default: false). 로그인 후에만 의미 있음 (856 / 24 / 896). 화면 조건은 CSS가 판단

  slots:
    preview                — (방송 패널) 미리보기 칸. 비어 있으면 검은 상자

  emits:
    go-home                — [홈으로] 클릭
    go-games               — [게임 목록으로] 클릭
    retry                  — [다시 시도] 클릭
    copy-url               — (방송 패널) [복사] 클릭
    toggle-wide            — (방송 패널) [넓게 보기] / [기본 크기] 클릭

  이동과 다시 불러오기는 부모가 처리한다.
-->
<script setup lang="ts">
import { computed } from 'vue'
import { IconCloudOff, IconMapQuestion, IconMoodEmpty } from '@tabler/icons-vue'
import BroadcastPanel from '@/components/BroadcastPanel.vue'

type ErrorType = 'not-found-page' | 'not-found-game' | 'load-failed'

const props = withDefaults(defineProps<{
  type: ErrorType
  loggedIn?: boolean
  overlayUrl?: string
  connected?: boolean
  wide?: boolean
}>(), {
  loggedIn: false,
  overlayUrl: '',
  connected: false,
  wide: false,
})

const emit = defineEmits<{
  'go-home': []
  'go-games': []
  retry: []
  'copy-url': []
  'toggle-wide': []
}>()

const contents = {
  'not-found-page': {
    icon: IconMapQuestion,
    title: '페이지를 찾을 수 없어요',
    desc: '주소를 다시 확인해 주세요.',
  },
  'not-found-game': {
    icon: IconMoodEmpty,
    title: '게임을 찾을 수 없어요',
    desc: '주소가 바뀌었거나 지금은 열 수 없는 게임이에요.',
  },
  'load-failed': {
    icon: IconCloudOff,
    title: '게임 정보를 불러오지 못했어요',
    desc: '잠시 후 다시 시도해 주세요.',
  },
}

const content = computed(() => contents[props.type])
</script>

<template>
  <section class="error-view">
    <div class="container" :class="{ 'panel-layout': loggedIn, 'is-wide': loggedIn && wide }">

      <div class="error-main">
        <div class="error-box">
          <component :is="content.icon" class="error-icon" size="48" stroke="2" />
          <h1 class="text-subheading error-title">{{ content.title }}</h1>
          <p class="text-small error-desc">{{ content.desc }}</p>

          <div class="error-actions">
            <template v-if="type === 'not-found-page'">
              <button class="btn btn-secondary btn-md" @click="emit('go-home')">홈으로</button>
            </template>
            <template v-else-if="type === 'not-found-game'">
              <button class="btn btn-secondary btn-md" @click="emit('go-games')">게임 목록으로</button>
            </template>
            <template v-else>
              <button class="btn btn-primary btn-md" @click="emit('retry')">다시 시도</button>
              <button class="btn btn-secondary btn-md" @click="emit('go-games')">게임 목록으로</button>
            </template>
          </div>
        </div>
      </div>

      <BroadcastPanel
        v-if="loggedIn"
        :open="true"
        :collapsible="false"
        :overlay-url="overlayUrl"
        :connected="connected"
        :wide="wide"
        @toggle-wide="emit('toggle-wide')"
        @copy-url="emit('copy-url')"
      >
        <slot name="preview" />
      </BroadcastPanel>

    </div>
  </section>
</template>

<style scoped>
/* 본문 위 여백 48 (방송 패널 기준). 에러 내용 묶음은 본문 맨 위에서 120 (= 48 + 72) */
.error-view {
  padding: var(--space-6) 0;
}

.error-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: calc(var(--error-top-gap) - var(--space-6));
  text-align: center;
}

.error-icon {
  color: var(--color-text-sub);
}

.error-title {
  margin-top: var(--space-3);
}

.error-desc {
  margin-top: var(--space-2);
  color: var(--color-text-sub);
}

.error-actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: var(--space-2);
  margin-top: var(--space-4);
}
</style>
