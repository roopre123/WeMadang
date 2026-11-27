<script setup lang="ts">
import { ref, onMounted, type Component } from 'vue'
import { fetchGame } from '@/api/games'
import type { Game } from '@/types/game'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

import ErrorView from '@/views/ErrorView.vue'
import GamePlayView from '@/views/GamePlayView.vue'
import OverlayGameReady from '@/components/overlay/OverlayGameReady.vue'
import OverlayPreviewBox from '@/components/overlay/OverlayPreviewBox.vue'
import BalanceDebateScreen from '@/components/games/BalanceDebateScreen.vue'
import InterviewScreen from '@/components/games/InterviewScreen.vue'

const auth = useAuthStore()

const router = useRouter()
const route = useRoute()

const game = ref<Game | null>(null)
const status = ref<'loading' | 'ok' | 'notFound' | 'error'>('loading')

const screens: Record<string, Component> = {
  'balance-debate': BalanceDebateScreen,
  interview: InterviewScreen,
}

async function load() {
  status.value = 'loading'
  try {
    const result = await fetchGame(String(route.params.id))

    if (result === null || !screens[result.screen]) status.value = 'notFound'
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
  <game-play-view
    v-if="status === 'loading' || status === 'ok'"
    :game="game"
    @back="router.push('/games')"
  >
    <Component v-if="status === 'ok' &&  game"
         :is="screens[game.screen]"/>

    <template #preview>
      <OverlayPreviewBox v-if="game">
        <overlay-game-ready :game="game" />
      </OverlayPreviewBox>
    </template>
  </game-play-view>
  <error-view
    v-else
    :type="status === 'notFound' ? 'not-found-game' : 'load-failed'"
    :logged-in="auth.loggedIn"
    @go-games="router.push('/games')"
    @retry="load"
  />
</template>
