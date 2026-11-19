import { ref } from 'vue'
import { defineStore } from 'pinia'
export const useAuthStore = defineStore('auth', () =>{
  const loggedIn = ref(false)
  const channelName = ref('')

  // 채널명 조회
  async function fetchMe() {
    const res = await fetch('/api/auth/me')
    const data = await res.json()

    loggedIn.value = data.loggedIn
    channelName.value = data.channelName ?? ''
  }

  // 로그인
  function login() {
    window.location.href = '/api/auth/chzzk/login'
  }

  // 로그아웃
  async function logout(): Promise<boolean> {
    try{
      const res = await fetch('/api/auth/logout', { method: 'POST' })
      if(!res.ok) return false
    } catch {
      return false
    }
    loggedIn.value = false
    channelName.value = ''
    return true
  }

  return {
    loggedIn,
    channelName,
    login,
    logout,
    fetchMe
  }
})
