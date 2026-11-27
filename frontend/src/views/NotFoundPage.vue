<script setup lang="ts">
import ErrorView from '@/views/ErrorView.vue'

import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const auth = useAuthStore()
</script>

<template>
  <error-view type="not-found-page"
  :logged-in="auth.loggedIn"
  @go-home="router.push('/')"/>
</template>
