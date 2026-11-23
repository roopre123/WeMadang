<!--
  GameIcon.vue — 게임 아이콘 (색 상자 + Tabler 아이콘)
  design-rules.md "게임 아이콘". 조작 화면 카드, 송출 화면 타일, 게임 첫 화면에서 같이 쓴다.

  props:
    name: string                          — Tabler 아이콘 이름 (games.icon, 예: 'ball-football'). 없거나 틀리면 분류 기본 아이콘 + gray
    color: string                         — 색 이름 (games.color: purple|amber|teal|pink|green|gray)
    category: 'quiz'|'debate'|'interview' — 분류 (기본 아이콘 선택용)
    size: 'card'|'tile'|'hero'            — 상자 40 / 72 / 96 (안의 아이콘 24 / 40 / 56)

  emits: 없음

  아이콘은 이름으로 그때그때 불러온다 (Tabler 아이콘 이름이면 무엇이든, 필요한 것만 따로 불러와서 앱이 무거워지지 않음).
  불러오는 동안은 빈 색 상자. 이름이 비었거나 틀리면 분류 기본 아이콘 + gray.
-->
<script setup lang="ts">
import { computed, ref, shallowRef, watch } from 'vue'
import type { Component } from 'vue'
import { IconMessages, IconQuestionMark, IconScale } from '@tabler/icons-vue'
import type { GameCategory } from '@/types/game'

const props = withDefaults(defineProps<{
  name?: string
  color?: string
  category: GameCategory
  size?: 'card' | 'tile' | 'hero'
}>(), {
  name: '',
  color: '',
  size: 'card',
})

// Tabler 아이콘 파일마다 따로 불러오는 함수 (실제로 쓰는 아이콘만 그때 받아옴)
const loaders = import.meta.glob<Component>(
  '../../node_modules/@tabler/icons-vue/dist/esm/icons/Icon*.mjs',
  { import: 'default' },
)

const defaultIcons: Record<GameCategory, Component> = {
  quiz: IconQuestionMark,
  debate: IconScale,
  interview: IconMessages,
}

const colors = ['purple', 'amber', 'teal', 'pink', 'green', 'gray']
const iconSizes = { card: 24, tile: 40, hero: 56 }

// 'ball-football' → 'IconBallFootball'
function toLoaderKey(name: string) {
  const pascal = name
    .split('-')
    .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
    .join('')
  return `../../node_modules/@tabler/icons-vue/dist/esm/icons/Icon${pascal}.mjs`
}

// loading: 불러오는 중 (빈 색 상자), ready: 이름의 아이콘, fallback: 이름이 없거나 틀림
const status = ref<'loading' | 'ready' | 'fallback'>('fallback')
const loaded = shallowRef<Component | null>(null)

watch(
  () => props.name,
  async (name) => {
    const load = /^[a-z0-9-]+$/.test(name) ? loaders[toLoaderKey(name)] : undefined
    if (!load) {
      loaded.value = null
      status.value = 'fallback'
      return
    }
    status.value = 'loading'
    try {
      const component = await load()
      if (props.name !== name) return // 그새 이름이 바뀌었으면 버림
      loaded.value = component
      status.value = 'ready'
    } catch {
      if (props.name !== name) return
      loaded.value = null
      status.value = 'fallback'
    }
  },
  { immediate: true },
)

const iconComponent = computed(() =>
  status.value === 'ready' ? loaded.value : status.value === 'fallback' ? defaultIcons[props.category] : null,
)
const colorName = computed(() => (status.value !== 'fallback' && colors.includes(props.color) ? props.color : 'gray'))
</script>

<template>
  <div class="game-icon" :class="[`game-icon-${size}`, `game-icon-${colorName}`]" aria-hidden="true">
    <component :is="iconComponent" v-if="iconComponent" :size="iconSizes[size]" stroke="2" />
  </div>
</template>
