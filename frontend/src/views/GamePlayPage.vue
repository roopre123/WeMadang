<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { fetchGame } from '@/api/games'
import type { Game } from '@/types/game'
import { useRoute, useRouter } from 'vue-router'

const router = useRouter()
const route = useRoute()

const game = ref<Game | null>(null)
const status = ref<'loading' | 'ok' | 'notFound' | 'error'>('loading')

async function load() {
  status.value = 'loading'
  try {
    const result = await fetchGame(String(route.params.id))

    if (result === null) status.value = 'notFound'
    else {
      game.value = result
      status.value = 'ok'
    }
  } catch {
    status.value = 'error'
  }
}

onMounted(load)
</script>
<template>
  <div v-if="status === 'loading'">게임 불러오는 중..</div>
  <div v-else-if="status === 'ok'">{{ game?.name }}</div>
  <div v-else-if="status === 'notFound'">
    게임을 찾을 수 없어요
    <button @click="router.push('/games')">게임 목록</button>
  </div>
  <div v-else>
    불러오지 못했어요
    <button @click="load">다시시도</button>
    <button @click="router.push('/games')">게임 목록</button>
  </div>
</template>
