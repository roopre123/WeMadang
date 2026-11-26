<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
</script>
<template>
  <div>게임 #{{ route.params.id }} 준비 중</div>
</template>
