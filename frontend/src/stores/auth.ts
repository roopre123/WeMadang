import { ref } from 'vue'
import { defineStore } from 'pinia'
export const useAuthStore = defineStore('auth', () =>{
  const loggedIn = ref(false)
  const channelName = ref('')
  const loginConfirmOpen = ref(false)
  let pendingReturnPath = '/'

  // 채널명 조회
  async function fetchMe() {
    const res = await fetch('/api/auth/me')
    const data = await res.json()

    loggedIn.value = data.loggedIn
    channelName.value = data.channelName ?? ''
  }

  // 로그인: 바로 가지 않고 확인 모달을 연다
  function login(returnPath: string = window.location.pathname + window.location.search) {
    pendingReturnPath = returnPath
    loginConfirmOpen.value = true
  }

  // 모달 [확인]: 치지직 로그인 이동
  function confirmLogin() {
    loginConfirmOpen.value = false
    window.location.href = '/api/auth/chzzk/login?returnPath=' + encodeURIComponent(pendingReturnPath)
  }

  // 모달 [취소] / 배경/ x: 닫기만
  function cancelLogin() {
    loginConfirmOpen.value = false
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
    fetchMe,
    confirmLogin,
    cancelLogin,
    loginConfirmOpen
  }
})
