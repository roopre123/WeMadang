<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { RouterView, useRoute, useRouter } from 'vue-router'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import AppToast from '@/components/AppToast.vue'

import { useAuthStore } from '@/stores/auth'
const auth = useAuthStore()

const route = useRoute()
const router = useRouter()

// 임시 값: API 연결 전이라 로그인 전 상태와 카운트 0으로 고정. 연결 시 실제 값으로 교체
const loggedIn = false
const visitCount = 0
const playCount = 0
const viewerCount = 0

// 임시 값: 토스트는 아직 띄울 일이 없어 숨김. 연결 시 실제 메시지로 교체
const toastVisible = ref(false)
const toastMessage = ref('')
const toastType = ref<'success' | 'error'>('success')

// App 마운트 시 세션으로 사용자 정보 조회
onMounted(() => auth.fetchMe())

function showToast(message: string, type: 'success' | 'error') {
  toastMessage.value = message
  toastVisible.value = true
  toastType.value = type
}

// 로그인 상태 확인하는 부분인데
// 함수에 싸서 넣는거랑 바로 route.query.login 넣는거랑 해석이 다름
// 함수에 싸서 넣으면 사용 방법을 알려주는거고 바로 넣으면 사용해서 주는거라고 보면 편한듯
function getLoginQuery() {
  return route.query.login
}

function onLoginQueryChange(v: unknown) {
  if (v === 'ok') showToast('로그인했어요', 'success')
  if (v === 'fail') showToast('로그인에 실패했어요', 'error')
  if (v) router.replace({ query: {} })
}

watch(getLoginQuery, onLoginQueryChange, { immediate: true })

async function onLogout() {
  const ok = await auth.logout()
  if (ok) showToast('로그아웃했어요.', 'success')
  else showToast('로그아웃에 실패했어요.', 'error')
}

function onNavigate(menu: string) {
  if (menu === '홈') router.push('/')
  if (menu === '게임 목록') router.push('/games')
  // if (menu === '커뮤니티') "";
}
</script>

<template>
  <!-- 미리보기 경로(meta.noFrame)는 자체 프레임을 쓰므로 그대로 출력 -->
  <RouterView v-if="route.meta.noFrame" />

  <div v-else class="app-layout">
    <AppHeader
      :logged-in="auth.loggedIn"
      :nickname="auth.channelName"
      :active-menu="route.path === '/games' ? '게임 목록' : ''"
      @login="auth.login"
      @logout="onLogout"
      @navigate="onNavigate"
    />

    <main class="app-main">
      <RouterView v-slot="{ Component }">
        <component
          :is="Component"
          :visit-count="visitCount"
          :play-count="playCount"
          :viewer-count="viewerCount"
          @login="auth.login"
          @show-games="router.push('/games')"
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
