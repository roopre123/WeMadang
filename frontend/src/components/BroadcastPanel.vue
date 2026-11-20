<!--
  BroadcastPanel.vue — 방송 패널 (송출용 화면 미리보기). 로그인 후에만 쓴다
  screens/broadcast-panel.md

  props:
    open: boolean         — 펼침 여부 (default: true)
    overlayUrl: string    — 표출 URL (default: '')
    connected: boolean    — OBS 연결 여부 (default: false)
    collapsible: boolean  — 접기 가능 여부. false면 항상 펼침 + [미리보기 접기] 숨김 (default: true)

  slots:
    default               — 미리보기 칸 (576×324, 송출 화면을 0.3배로 넣는다). 비어 있으면 검은 상자

  emits:
    toggle                — [미리보기 접기] / 퀵메뉴 버튼 클릭
    copy-url              — [복사] 클릭

  접힘(퀵메뉴 버튼)일 때는 부모가 position: relative 이고 화면 폭 전체를 차지해야 한다 (layout.css .broadcast-quick-wrap).
  펼침일 때는 .panel-layout 의 오른쪽 칸에 넣는다.
-->
<script setup lang="ts">
import { computed } from 'vue'
import { IconBroadcast, IconChevronLeft, IconChevronRight } from '@tabler/icons-vue'

const props = withDefaults(defineProps<{
  open?: boolean
  overlayUrl?: string
  connected?: boolean
  collapsible?: boolean
}>(), {
  open: true,
  overlayUrl: '',
  connected: false,
  collapsible: true,
})

const emit = defineEmits<{
  toggle: []
  'copy-url': []
}>()

const expanded = computed(() => props.open || !props.collapsible)
const statusText = computed(() => (props.connected ? 'OBS 연결됨' : 'OBS 연결 안 됨'))
</script>

<template>
  <aside v-if="expanded" class="broadcast-panel panel-sticky">
    <div class="panel-head">
      <h2 class="text-subheading">송출용 화면 미리보기</h2>
      <button v-if="collapsible" class="btn btn-secondary btn-sm" @click="emit('toggle')">
        미리보기 접기
        <IconChevronRight size="16" stroke="2" />
      </button>
    </div>

    <div class="panel-preview">
      <slot />
    </div>

    <p class="panel-status text-small">
      <span class="status-dot" :class="connected ? 'status-on' : 'status-off'" />
      <span class="status-text">{{ statusText }}</span>
    </p>

    <div class="panel-url-row">
      <input class="input url-input" type="text" readonly :value="overlayUrl" aria-label="표출 URL">
      <button class="btn btn-secondary btn-sm" @click="emit('copy-url')">복사</button>
    </div>

    <p class="text-small">OBS 브라우저 소스에 한 번만 넣으면 돼요.</p>
  </aside>

  <div v-else class="broadcast-quick-wrap">
    <button class="broadcast-quick" aria-label="송출용 화면 미리보기 펼치기" @click="emit('toggle')">
      <span class="status-dot" :class="connected ? 'status-on' : 'status-off'" />
      <IconBroadcast size="24" stroke="2" />
      <span class="quick-label">미리보기</span>
      <IconChevronLeft size="16" stroke="2" />
    </button>
  </div>
</template>

<style scoped>
.broadcast-panel {
  display: flex;
  flex-direction: column;
  gap: var(--space-panel);
  padding: var(--space-3);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-card);
}

.panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--space-2);
}

/* 송출 화면(1920×1080)의 0.3배. 연결 전에는 검은 상자 */
.panel-preview {
  width: var(--preview-width);
  max-width: 100%;
  height: var(--preview-height);
  margin: 0 auto;
  overflow: hidden;
  background: var(--color-preview-bg);
  border-radius: var(--radius-btn);
}

.panel-status {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  color: var(--color-text);
}

.status-dot {
  width: 8px;
  height: 8px;
  flex-shrink: 0;
  border-radius: 50%;
}

.status-on { background: var(--color-success); }
.status-off { background: var(--color-error); }

.panel-url-row {
  display: flex;
  gap: var(--space-2);
}

.url-input {
  flex: 1;
  min-width: 0;
  border-style: dotted;
  text-overflow: ellipsis;
}

/* 접힘 퀵메뉴 버튼 (위치는 layout.css .broadcast-quick-wrap) */
.broadcast-quick {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-2);
  width: var(--quick-menu-width);
  padding: var(--space-panel);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-card);
  color: var(--color-text);
}

.broadcast-quick:hover {
  filter: brightness(0.95);
}

.quick-label {
  font-size: var(--btn-font-sm);
  line-height: 1.3;
}
</style>
