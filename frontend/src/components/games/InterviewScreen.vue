<!--
  InterviewScreen.vue — 게임별 화면: 면담 (지금은 빈 화면, 나중에 채움)
  screens/game-play.md "게임별 빈 화면". GamePlayView의 기본 슬롯에 들어간다

  props: 없음
  emits: 없음
-->
<template>
  <div class="game-empty">
    <p class="text-body">게임을 준비하고 있어요</p>
    <p class="text-small game-empty-sub">진행 화면은 곧 추가돼요.</p>
  </div>
</template>

<style scoped>
.game-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: var(--game-screen-min-height);
  background: var(--color-surface);
  border-radius: var(--radius-card);
  text-align: center;
}

.game-empty-sub {
  color: var(--color-text-sub);
}
</style>
