<script setup lang="ts">
import { ref } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import AppToast from '@/components/AppToast.vue'

const route = useRoute()

// 숫자/문자열(앞자리 0 포함)을 숫자로 바꾼다. 숫자가 아니면 0
const toCount = (v: number | string) => {
  const n = Number(v)
  return Number.isFinite(n) ? n : 0
}

// 임시 값: API 연결 전이라 로그인 전 상태와 카운트 임시 숫자로 고정. 연결 시 실제 값으로 교체
const loggedIn = false
const visitCount = toCount('0234567890')
const playCount = toCount('034567890')
const viewerCount = toCount(123456789)

// 임시 값: 토스트는 아직 띄울 일이 없어 숨김. 연결 시 실제 메시지로 교체
const toastVisible = ref(false)
const toastMessage = ref('')
const toastType = ref<'success' | 'error'>('success')
</script>

<template>
  <!-- 미리보기 경로(meta.noFrame)는 자체 프레임을 쓰므로 그대로 출력 -->
  <RouterView v-if="route.meta.noFrame" />

  <div v-else class="app-layout">
    <AppHeader :logged-in="loggedIn" />

    <main class="app-main">
      <RouterView v-slot="{ Component }">
        <component
          :is="Component"
          :visit-count="visitCount"
          :play-count="playCount"
          :viewer-count="viewerCount"
        />
      </RouterView>
    </main>

    <AppFooter />

    <AppToast
      v-if="toastVisible"
      :message="toastMessage"
      :type="toastType"
      @hide="toastVisible = false"
    />
  </div>
</template>
