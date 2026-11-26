<!--
  LoginConfirmModal.vue — 로그인 확인 모달 내용 (공통 프레임의 AppModal 안에 넣어 쓴다)
  기준: docs/design/screens/login-confirm.md (모양은 delay.md #1 안내 모달과 같음)
  모달 열고 닫기, 뒤 배경·X 버튼(AppModal의 close)은 부모가 처리한다.

  props: 없음

  emits:
    cancel    — [취소] 클릭
    confirm   — [확인] 클릭

  사용 예:
    <AppModal :open="open" @close="open = false">
      <LoginConfirmModal @cancel="open = false" @confirm="..." />
    </AppModal>
-->
<script setup lang="ts">
const emit = defineEmits<{
  cancel: []
  confirm: []
}>()
</script>

<template>
  <div class="login-confirm">
    <h3 class="text-subheading login-confirm-title">치지직 로그인</h3>
    <p class="text-body login-confirm-desc">치지직 계정과 연동하여 로그인합니다.</p>
    <div class="login-confirm-actions">
      <button class="btn btn-secondary btn-md" @click="emit('cancel')">취소</button>
      <button class="btn btn-primary btn-md" @click="emit('confirm')">확인</button>
    </div>
  </div>
</template>

<style scoped>
/* 제목은 AppModal의 X 버튼과 겹치지 않게 오른쪽을 비운다 */
.login-confirm-title {
  margin-bottom: var(--space-2);
  padding-right: var(--space-5);
}

.login-confirm-desc {
  margin-bottom: var(--space-5);
}

.login-confirm-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-2);
}
</style>
