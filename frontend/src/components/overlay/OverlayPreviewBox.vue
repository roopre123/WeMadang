<!--
  OverlayPreviewBox.vue — 송출 화면(1920×1080)을 0.3배(576×324)로 줄여 보여주는 상자
  방송 패널의 미리보기 칸(BroadcastPanel 기본 슬롯)에 넣어 쓴다

  props:
    wide: boolean — 넓게 보기 여부 (default: false). true면 0.45배 (864×486), 아니면 0.3배 (576×324). 화면 조건은 CSS가 판단
  slots: default — 송출 화면 컴포넌트 (OverlayGameSelect 등)
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
  <div class="overlay-preview-box" :class="{ 'is-wide': wide }">
    <div class="overlay-preview-inner">
      <slot />
    </div>
  </div>
</template>

<style scoped>
.overlay-preview-box {
  width: var(--preview-w);
  height: var(--preview-h);
  overflow: hidden;
}

.overlay-preview-inner {
  width: var(--overlay-width);
  height: var(--overlay-height);
  transform: scale(var(--preview-scale));
  transform-origin: top left;
  pointer-events: none;
}
</style>
