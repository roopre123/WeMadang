<!--
  AppFooter.vue — 공통 푸터 (조작 화면 전용)

  props:
    wide: boolean — 넓게 보기 적용 여부 (default: false). true면 안쪽 폭 1776 (화면 조건은 CSS가 판단)
  emits: 없음
-->
<script setup lang="ts">
withDefaults(defineProps<{
  wide?: boolean
}>(), {
  wide: false,
})
</script>

<template>
  <footer class="app-footer">
    <div class="footer-inner container" :class="{ 'is-wide': wide }">

      <!-- 왼쪽: 로고 + 제휴 안내 -->
      <div class="footer-left">
        <img src="@/assets/logo.svg" width="24" height="24" alt="" aria-hidden="true">
        <span class="footer-notice text-small">위마당은 치지직, 네이버와 제휴하지 않습니다.</span>
      </div>

      <!-- 오른쪽: 링크 + 저작권 -->
      <div class="footer-right">
        <a href="#" class="footer-link text-small">이용약관</a>
        <a href="#" class="footer-link text-small">개인정보처리방침</a>
        <span class="footer-copy text-small">&copy;2026 wemadang</span>
      </div>

    </div>
  </footer>
</template>

<style scoped>
.app-footer {
  border-top: 1px solid var(--color-border);
  padding: var(--space-4) 0;
}

.footer-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--space-4);
}

/* 왼쪽 */
.footer-left {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  flex-shrink: 0;
}


.footer-notice {
  color: var(--color-text-sub);
}

/* 오른쪽 */
.footer-right {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  flex-shrink: 0;
}

.footer-link {
  color: var(--color-text-sub);
  text-decoration: none;
  border: 1px solid var(--color-border);
  padding: 4px var(--space-2);
  border-radius: var(--radius-btn);
  transition: color 0.15s, border-color 0.15s;
}

.footer-link:hover {
  color: var(--color-text);
  border-color: var(--color-text-sub);
  text-decoration: none;
}

.footer-copy {
  color: var(--color-text-sub);
}

/* 반응형 */
@media (max-width: 1279px) {
  .footer-inner {
    gap: var(--space-3);
  }
}
</style>
