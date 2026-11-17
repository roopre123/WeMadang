<!--
  AppModal.vue — 공통 모달

  props:
    open: boolean — 열림 여부

  emits:
    close — 뒤 배경 클릭 또는 X 버튼 클릭

  사용 예:
    <AppModal :open="modalOpen" @close="modalOpen = false">
      <h3>제목</h3>
      <p>내용</p>
    </AppModal>
-->
<script setup lang="ts">
defineProps<{ open: boolean }>()
const emit = defineEmits<{ close: [] }>()
</script>

<template>
  <Teleport to="body">
    <Transition name="modal">
      <div
        v-if="open"
        class="modal-overlay"
        role="dialog"
        aria-modal="true"
        @click.self="emit('close')"
      >
        <div class="modal-box">
          <button class="modal-close" aria-label="닫기" @click="emit('close')">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
              <line x1="18" y1="6" x2="6" y2="18"/>
              <line x1="6" y1="6" x2="18" y2="18"/>
            </svg>
          </button>
          <slot />
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 200;
  padding: var(--space-4);
}

.modal-box {
  position: relative;
  background: var(--color-background);
  border-radius: var(--radius-card);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.18);
  padding: var(--space-5);
  width: 100%;
  max-width: 520px;
}

.modal-close {
  position: absolute;
  top: var(--space-3);
  right: var(--space-3);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: none;
  color: var(--color-text-sub);
  cursor: pointer;
  transition: background 0.15s, color 0.15s;
  border: none;
}

.modal-close:hover {
  background: var(--color-surface);
  color: var(--color-text);
}

/* 반응형 */
@media (max-width: 1279px) {
  .modal-box {
    max-width: 90vw;
  }
}

/* 전환 애니메이션 */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.2s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-active :deep(.modal-box),
.modal-leave-active :deep(.modal-box) {
  transition: transform 0.2s ease;
}

.modal-enter-from :deep(.modal-box),
.modal-leave-to :deep(.modal-box) {
  transform: scale(0.96);
}
</style>
