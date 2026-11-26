<script setup lang="ts">
import { ref, onMounted } from 'vue'
import GameSelectView from '@/views/GameSelectView.vue'
import { fetchGames } from '@/api/games'
import type { Game } from '@/types/game'
import OverlayGameSelect from '@/components/overlay/OverlayGameSelect.vue'
import OverlayPreviewBox from '@/components/overlay/OverlayPreviewBox.vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const router = useRouter()

const auth = useAuthStore()
const panelOpen = ref(true)

const category = ref('')
const sort = ref('default')
const keyword = ref('')
const page = ref(1)
const games = ref<Game[]>([])
const totalPages = ref(0)

async function loadGaems() {
  try {
    const result = await fetchGames({
      category: category.value,
      sort: sort.value,
      keyword: keyword.value,
      page: page.value,
    })
    games.value = result.games
    totalPages.value = result.totalPages
  } catch (e) {
    console.error(e)
  }
}

onMounted(loadGaems)

function onChangeCategory(value: string) {
  category.value = value
  page.value = 1
  loadGaems()
}

function onChangeSort(value: string) {
  sort.value = value
  page.value = 1
  loadGaems()
}

function onSearch(value: string) {
  keyword.value = value
  page.value = 1
  loadGaems()
}

function onChangePage(value: number) {
  page.value = value
  loadGaems()
}

function onSelectGame(id: number) {
  router.push('/games/' + id)
}
</script>
<template>
  <GameSelectView
    :games="games"
    :category="category"
    :sort="sort"
    :keyword="keyword"
    :page="page"
    :total-pages="totalPages"
    :logged-in="auth.loggedIn"
    @change-category="onChangeCategory"
    @change-sort="onChangeSort"
    @search="onSearch"
    @change-page="onChangePage"
    @select-game="onSelectGame"
    :panel-open="panelOpen"
    @toggle-panel="panelOpen = !panelOpen"
  >
    <template #preview>
      <OverlayPreviewBox>
        <OverlayGameSelect
          :games="games"
          :category="category"
          :sort="sort"
          :page="page"
          :total-pages="totalPages"
        />
      </OverlayPreviewBox>
    </template>
  </GameSelectView>
</template>
