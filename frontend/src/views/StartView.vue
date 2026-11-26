<!--
  StartView.vue — 시작 화면 (조작 화면)
  기준: docs/design/screens/start.md, docs/design/wireframes/start.png
  헤더/푸터는 공통 프레임(App.vue)이 그리므로 여기서는 본문만 다룬다.

  props:
    visitCount: number   — 방문 카운트 (default: 0)
    playCount: number    — 게임 플레이 카운트 (default: 0)
    viewerCount: number  — 참여 시청자 누적 카운트 (default: 0)
    loggedIn: boolean    — 로그인 여부 (default: false). true면 [치지직으로 로그인]을 숨기고 [게임 둘러보기]만 보여줌

  emits:
    login        — [치지직으로 로그인] 클릭
    show-games   — [게임 둘러보기] 클릭

  slots:
    demo         — 토론 화면 예시 (이미지 또는 움직이는 이미지). 비어 있으면 빈 자리 표시
-->
<script setup lang="ts">
withDefaults(defineProps<{
  visitCount?: number
  playCount?: number
  viewerCount?: number
  loggedIn?: boolean
}>(), {
  visitCount: 0,
  playCount: 0,
  viewerCount: 0,
  loggedIn: false,
})

const emit = defineEmits<{
  login: []
  'show-games': []
}>()

const formatCount = (n: number) => n.toLocaleString('ko-KR')

// 기능 소개 카드: 내용은 나중에 채움 (와이어프레임 자리 문구)
const features = [
  { title: '채팅으로 참여', desc: '시청자는 가입 없이 치지직 채팅으로 투표하고 참가해요' },
  { title: '채팅으로 참여', desc: '시청자는 가입 없이 치지직 채팅으로 투표하고 참가해요' },
  { title: '채팅으로 참여', desc: '시청자는 가입 없이 치지직 채팅으로 투표하고 참가해요' },
]
</script>

<template>
  <div class="container start-view">

    <section class="start-hero col-2">

      <!-- 왼쪽: 토론 화면 예시 -->
      <div class="demo-area">
        <div class="demo-frame">
          <slot name="demo">
            <span class="demo-empty text-subheading">토론 화면 예시</span>
          </slot>
        </div>
        <p class="demo-caption text-small">밸런스 토론 진행 화면</p>
      </div>

      <!-- 오른쪽: 소개 -->
      <div class="intro-area">
        <h1 class="text-heading-xl">판을 열면, 채팅이 <span class="intro-accent">모인다</span></h1>
        <p class="text-body intro-text">
          시청자는 치지직 채팅만 치면 돼요.<br>
          투표하고, 편 나눠 토론하고, 결과까지 방송 화면에 바로 보여줘요.
        </p>

        <div class="intro-actions">
          <button v-if="!loggedIn" class="btn btn-primary btn-lg" @click="emit('login')">치지직으로 로그인</button>
          <button class="btn btn-secondary btn-lg" @click="emit('show-games')">게임 둘러보기</button>
        </div>

        <div class="count-row">
          <div class="count-box card">
            <span class="text-small">방문</span>
            <span class="count-value text-subheading">{{ formatCount(visitCount) }}</span>
          </div>
          <div class="count-box card">
            <span class="text-small">게임 플레이</span>
            <span class="count-value text-subheading">{{ formatCount(playCount) }}</span>
          </div>
          <div class="count-box card">
            <span class="text-small">참여 시청자</span>
            <span class="count-value text-subheading">{{ formatCount(viewerCount) }}명</span>
          </div>
        </div>
      </div>

    </section>

    <section class="feature-section">
      <h2 class="text-subheading feature-title">기능 소개</h2>
      <div class="feature-row card-row">
        <div v-for="(f, i) in features" :key="i" class="feature-card">
          <h3 class="text-subheading">{{ f.title }}</h3>
          <p class="text-small">{{ f.desc }}</p>
        </div>
      </div>
    </section>

  </div>
</template>

<style scoped>
.start-view {
  padding-top: var(--space-7);
  padding-bottom: var(--space-7);
}

.start-hero {
  display: flex;
  align-items: stretch;
  gap: var(--space-6);
}

.demo-area,
.intro-area {
  flex: 1;
  min-width: 0;
}

.demo-frame {
  aspect-ratio: 16 / 9;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-card);
}

.demo-frame :slotted(img),
.demo-frame :slotted(video) {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.demo-empty {
  color: var(--color-text-sub);
}

.demo-caption {
  margin-top: var(--space-2);
  text-align: center;
}

.intro-accent {
  color: var(--color-accent);
}

.intro-text {
  margin-top: var(--space-4);
}

.intro-actions {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
  margin-top: var(--space-5);
}

.count-row {
  display: flex;
  gap: var(--space-3);
  margin-top: var(--space-5);
}

.count-box {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
  padding: var(--space-3);
}

.count-value {
  overflow-wrap: anywhere;
}

.feature-section {
  margin-top: var(--space-6);
  padding-top: var(--space-6);
  border-top: 1px solid var(--color-border);
}

.feature-title {
  margin-bottom: var(--space-4);
}

.feature-row {
  display: flex;
  gap: var(--space-4);
}

.feature-card {
  flex: 1;
  min-width: 0;
  min-height: 160px;
  padding: var(--space-4);
  border: 1px dashed var(--color-border);
  border-radius: var(--radius-card);
}

.feature-card .text-small {
  margin-top: var(--space-2);
}
</style>
