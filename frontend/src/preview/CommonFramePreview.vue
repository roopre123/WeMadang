<!--
  CommonFramePreview.vue — 공통 프레임 미리보기
  접속: /preview/common-frame

  확인 항목:
    - 헤더: 로그인 전 / 후, 딜레이 미설정·설정, 알림 켜짐·꺼짐, 긴 닉네임
    - 토스트: 성공 / 오류
    - 모달: 열기·닫기
    - 푸터
    - 라이트·다크 모드 (OS 설정으로 전환)
-->
<script setup lang="ts">
import { ref, nextTick } from 'vue'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import AppToast from '@/components/AppToast.vue'
import AppModal from '@/components/AppModal.vue'

/* ---- 헤더 상태 ---- */
const loggedIn = ref(false)
const nickname = ref('스트리머')
const soundOn = ref(true)
const activeMenu = ref('게임 목록')
const delayValue = ref<string | null>(null)
// 넓게 보기: 헤더, 푸터 안쪽 폭 1776 (안쪽 폭 1824 이상, 높이 800 이상 화면에서만 보임)
const wide = ref(false)

type HeaderPreset = {
  label: string
  loggedIn: boolean
  nickname: string
  soundOn: boolean
  activeMenu: string
  delayValue: string | null
}

const presets: HeaderPreset[] = [
  { label: '로그인 전', loggedIn: false, nickname: '', soundOn: true, activeMenu: '게임 목록', delayValue: null },
  { label: '로그인 후 — 딜레이 미설정', loggedIn: true, nickname: '스트리머', soundOn: true, activeMenu: '게임 목록', delayValue: null },
  { label: '로그인 후 — 딜레이 3.2초', loggedIn: true, nickname: '스트리머', soundOn: true, activeMenu: '게임 목록', delayValue: '3.2초' },
  { label: '긴 닉네임', loggedIn: true, nickname: '아주길고엄청긴닉네임테스트입니다', soundOn: true, activeMenu: '커뮤니티', delayValue: '3.2초' },
  { label: '알림 꺼짐', loggedIn: true, nickname: '스트리머', soundOn: false, activeMenu: '게임 목록', delayValue: null },
]
const activePreset = ref(0)

function applyPreset(index: number) {
  const p = presets[index]
  activePreset.value = index
  loggedIn.value = p.loggedIn
  nickname.value = p.nickname
  soundOn.value = p.soundOn
  activeMenu.value = p.activeMenu
  delayValue.value = p.delayValue
}

/* ---- 토스트 ---- */
const toastVisible = ref(false)
const toastMessage = ref('')
const toastType = ref<'success' | 'error'>('success')

async function triggerToast(msg: string, type: 'success' | 'error') {
  toastVisible.value = false
  await nextTick()
  toastMessage.value = msg
  toastType.value = type
  toastVisible.value = true
}

/* ---- 모달 ---- */
const modalOpen = ref(false)
</script>

<template>
  <div class="app-layout">

    <!-- 헤더 (실제 fixed 위치에 표시) -->
    <AppHeader
      :logged-in="loggedIn"
      :nickname="nickname"
      :sound-on="soundOn"
      :active-menu="activeMenu"
      :delay-value="delayValue"
      :wide="wide"
      @login="loggedIn = true"
      @logout="loggedIn = false"
      @toggle-sound="soundOn = !soundOn"
      @open-delay="modalOpen = true"
      @navigate="(menu) => activeMenu = menu"
    />

    <main class="app-main preview-main">
      <div class="container preview-container">

        <h1 class="text-heading preview-title">공통 프레임 미리보기</h1>

        <!-- 헤더 상태 전환 -->
        <section class="preview-section">
          <h2 class="text-subheading preview-section-title">헤더 상태 전환</h2>
          <p class="text-small preview-desc">버튼을 눌러 위 헤더의 상태를 바꿉니다.</p>
          <div class="preset-buttons">
            <button
              v-for="(preset, i) in presets"
              :key="i"
              class="btn btn-sm"
              :class="activePreset === i ? 'btn-primary' : 'btn-secondary'"
              @click="applyPreset(i)"
            >
              {{ preset.label }}
            </button>
          </div>
          <div class="preset-buttons wide-buttons">
            <button
              class="btn btn-sm"
              :class="wide ? 'btn-primary' : 'btn-secondary'"
              @click="wide = !wide"
            >
              넓게 보기 {{ wide ? '끄기' : '켜기' }} (헤더·푸터 안쪽 1776)
            </button>
          </div>
          <div class="state-info card">
            <p class="text-small"><strong>loggedIn:</strong> {{ loggedIn }}</p>
            <p class="text-small"><strong>nickname:</strong> {{ nickname || '(없음)' }}</p>
            <p class="text-small"><strong>soundOn:</strong> {{ soundOn }}</p>
            <p class="text-small"><strong>activeMenu:</strong> {{ activeMenu }}</p>
            <p class="text-small"><strong>delayValue:</strong> {{ delayValue ?? 'null' }}</p>
          </div>
        </section>

        <!-- 토스트 -->
        <section class="preview-section">
          <h2 class="text-subheading preview-section-title">토스트</h2>
          <p class="text-small preview-desc">버튼을 누르면 화면 하단 가운데에 2.5초간 표시됩니다.</p>
          <div class="preset-buttons">
            <button class="btn btn-secondary btn-md" @click="triggerToast('저장이 완료됐습니다.', 'success')">
              성공 토스트
            </button>
            <button class="btn btn-secondary btn-md" @click="triggerToast('오류가 발생했습니다. 다시 시도해주세요.', 'error')">
              오류 토스트
            </button>
          </div>
        </section>

        <!-- 모달 -->
        <section class="preview-section">
          <h2 class="text-subheading preview-section-title">모달</h2>
          <p class="text-small preview-desc">뒤 배경 클릭 또는 X 버튼으로 닫힙니다.</p>
          <div class="preset-buttons">
            <button class="btn btn-secondary btn-md" @click="modalOpen = true">
              모달 열기
            </button>
          </div>
        </section>

        <!-- 깨지기 쉬운 경우 안내 -->
        <section class="preview-section">
          <h2 class="text-subheading preview-section-title">깨지기 쉬운 경우</h2>
          <ul class="text-small breakage-list">
            <li>긴 닉네임 → '긴 닉네임' 프리셋으로 확인</li>
            <li>라이트·다크 모드 → OS 설정에서 전환</li>
            <li>좁은 너비 → 브라우저 창을 768px ~ 1279px으로 조절</li>
          </ul>
        </section>

      </div>
    </main>

    <!-- 푸터 -->
    <AppFooter :wide="wide" />

    <!-- 토스트 -->
    <AppToast
      v-if="toastVisible"
      :message="toastMessage"
      :type="toastType"
      @hide="toastVisible = false"
    />

    <!-- 모달 -->
    <AppModal :open="modalOpen" @close="modalOpen = false">
      <h3 class="text-subheading modal-demo-title">딜레이 보정</h3>
      <p class="text-small modal-demo-desc">딜레이 보정 모달 예시 내용입니다. 실제 기능은 개발자가 연결합니다.</p>
      <div class="modal-demo-actions">
        <button class="btn btn-secondary btn-md" @click="modalOpen = false">취소</button>
        <button class="btn btn-primary btn-md" @click="modalOpen = false">저장</button>
      </div>
    </AppModal>

  </div>
</template>

<style scoped>
.preview-main {
  padding-bottom: var(--space-7);
}

.preview-container {
  padding-top: var(--space-6);
}

.preview-title {
  margin-bottom: var(--space-6);
}

.preview-section {
  margin-bottom: var(--space-6);
  padding-bottom: var(--space-6);
  border-bottom: 1px solid var(--color-border);
}

.preview-section:last-child {
  border-bottom: none;
}

.preview-section-title {
  margin-bottom: var(--space-2);
}

.preview-desc {
  margin-bottom: var(--space-3);
}

.preset-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.wide-buttons {
  margin-top: var(--space-2);
}

.state-info {
  margin-top: var(--space-3);
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.breakage-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
  list-style: disc;
  padding-left: var(--space-4);
}

/* 모달 내부 */
.modal-demo-title {
  margin-bottom: var(--space-2);
  padding-right: var(--space-5);
}

.modal-demo-desc {
  margin-bottom: var(--space-5);
}

.modal-demo-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-2);
}
</style>
