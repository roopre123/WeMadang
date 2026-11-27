<!--
  ErrorFramePreview.vue — 에러 화면 미리보기용 내부 페이지 (ErrorPreview의 iframe 안에서 열림)
  접속: /preview/error-frame?type=not-found-page|not-found-game|load-failed&login=on|off&obs=on|off&theme=light|dark
  공통 프레임(헤더, 푸터)을 직접 그린다 (route meta noFrame).
-->
<script setup lang="ts">
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import ErrorView from '@/views/ErrorView.vue'

const query = useRoute().query
const loggedIn = query.login === 'on'
const types = ['not-found-page', 'not-found-game', 'load-failed'] as const
const type = types.find((t) => t === query.type) ?? 'not-found-game'

onMounted(() => {
  const theme = query.theme
  if (theme === 'light' || theme === 'dark') {
    document.documentElement.dataset.theme = theme
  }
})
</script>

<template>
  <div class="app-layout">
    <AppHeader :logged-in="loggedIn" nickname="스트리머" active-menu="" />
    <main class="app-main">
      <ErrorView
        :type="type"
        :logged-in="loggedIn"
        overlay-url="https://wemadang.example.com/overlay/xxxxxxxxxxxx"
        :connected="query.obs !== 'off'"
      />
    </main>
    <AppFooter />
  </div>
</template>
