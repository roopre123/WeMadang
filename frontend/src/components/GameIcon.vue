<!--
  GameIcon.vue — 게임 아이콘 (색 상자 + Tabler 아이콘)
  design-rules.md "게임 아이콘". 조작 화면 카드, 송출 화면 타일, 게임 첫 화면에서 같이 쓴다.

  props:
    name: string                          — Tabler 아이콘 이름 (games.icon). 없거나 모르는 이름이면 분류 기본 아이콘 + gray
    color: string                         — 색 이름 (games.color: purple|amber|teal|pink|green|gray)
    category: 'quiz'|'debate'|'interview' — 분류 (기본 아이콘 선택용)
    size: 'card'|'tile'|'hero'            — 상자 40 / 72 / 96 (안의 아이콘 24 / 40 / 56)

  emits: 없음

  아이콘은 이 파일의 icons 목록에 있는 이름만 그려진다 (사용하는 것만 번들에 넣기 위해).
  games.icon에 새 이름을 쓰려면 import 한 줄과 icons 항목을 함께 추가한다.
-->
<script setup lang="ts">
import { computed } from 'vue'
import type { Component } from 'vue'
import {
  IconBulb,
  IconDeviceGamepad2,
  IconFlag,
  IconMessages,
  IconMovie,
  IconMusic,
  IconQuestionMark,
  IconScale,
  IconTrophy,
  IconWorld,
} from '@tabler/icons-vue'
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

const icons: Record<string, Component> = {
  bulb: IconBulb,
  'device-gamepad-2': IconDeviceGamepad2,
  flag: IconFlag,
  messages: IconMessages,
  movie: IconMovie,
  music: IconMusic,
  'question-mark': IconQuestionMark,
  scale: IconScale,
  trophy: IconTrophy,
  world: IconWorld,
}

const defaultIcons: Record<GameCategory, Component> = {
  quiz: IconQuestionMark,
  debate: IconScale,
  interview: IconMessages,
}

const colors = ['purple', 'amber', 'teal', 'pink', 'green', 'gray']
const iconSizes = { card: 24, tile: 40, hero: 56 }

const icon = computed(() => icons[props.name])
const iconComponent = computed(() => icon.value ?? defaultIcons[props.category])
const colorName = computed(() => (icon.value && colors.includes(props.color) ? props.color : 'gray'))
</script>

<template>
  <div class="game-icon" :class="[`game-icon-${size}`, `game-icon-${colorName}`]" aria-hidden="true">
    <component :is="iconComponent" :size="iconSizes[size]" stroke="2" />
  </div>
</template>
