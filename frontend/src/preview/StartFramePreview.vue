<!--
  StartFramePreview.vue — 시작 화면 미리보기용 내부 페이지 (StartPreview의 iframe 안에서 열림)
  접속: /preview/start-frame?theme=light|dark&data=basic|large&image=on|off
  공통 프레임(헤더, 푸터)을 직접 그린다 (route meta noFrame).
-->
<script setup lang="ts">
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import StartView from '@/views/StartView.vue'
import logo from '@/assets/logo.svg'

const query = useRoute().query
const large = query.data === 'large'
const hasImage = query.image === 'on'

onMounted(() => {
  const theme = query.theme
  if (theme === 'light' || theme === 'dark') {
    document.documentElement.dataset.theme = theme
  }
})
</script>

<template>
  <div class="app-layout">
    <AppHeader :logged-in="false" active-menu="" />
    <main class="app-main">
      <StartView
        :visit-count="large ? 23123234 : 0"
        :play-count="large ? 23456789 : 0"
        :viewer-count="large ? 23456789 : 0"
      >
        <template v-if="hasImage" #demo>
          <img :src="logo" alt="토론 화면 예시 이미지 (임시)">
        </template>
      </StartView>
    </main>
    <AppFooter />
  </div>
</template>
