<!--
  AppToast.vue — 잠깐 뜨는 안내 토스트

  props:
    message: string              — 안내 문구
    type: 'success' | 'error'   — 종류 (점 색으로 구분)

  emits:
    hide — 2.5초 후 사라질 때 부모에게 알림 (부모가 v-if로 제거)

  사용 예:
    <AppToast v-if="visible" message="저장됐습니다." type="success" @hide="visible = false" />
-->
<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

defineProps<{
  message: string
  type: 'success' | 'error'
}>()

const emit = defineEmits<{ hide: [] }>()

const internalVisible = ref(false)
let hideTimer: ReturnType<typeof setTimeout>
let emitTimer: ReturnType<typeof setTimeout>

onMounted(() => {
  internalVisible.value = true
  // 2.5초 뒤 페이드아웃 시작
  hideTimer = setTimeout(() => {
    internalVisible.value = false
    // 페이드아웃(0.3s) 완료 후 부모에게 hide 알림
    emitTimer = setTimeout(() => emit('hide'), 300)
  }, 2500)
})

onUnmounted(() => {
  clearTimeout(hideTimer)
  clearTimeout(emitTimer)
})
</script>

<template>
  <Teleport to="body">
    <Transition name="toast">
      <div v-if="internalVisible" class="toast" :class="`toast--${type}`" role="status" aria-live="polite">
        <span class="toast-dot" :class="`toast-dot--${type}`" aria-hidden="true"></span>
        <span class="toast-message">{{ message }}</span>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
.toast {
  position: fixed;
  bottom: var(--space-4);
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-4);
  background: var(--color-toast-bg);
  color: var(--color-toast-text);
  border-radius: var(--radius-card);
  font-size: var(--font-size-small);
  white-space: nowrap;
  z-index: 300;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.18);
}

.toast-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}

.toast-dot--success {
  background: var(--color-success);
}

.toast-dot--error {
  background: var(--color-error);
}

.toast-message {
  line-height: var(--line-height-body);
}

/* 전환 애니메이션 */
.toast-enter-active,
.toast-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.toast-enter-from {
  opacity: 0;
  transform: translateX(-50%) translateY(8px);
}

.toast-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(4px);
}
</style>
