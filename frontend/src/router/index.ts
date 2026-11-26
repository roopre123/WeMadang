import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

// meta.noFrame: true → App.vue가 공통 프레임(헤더, 푸터, 토스트)을 씌우지 않는다 (미리보기 전용)
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('../views/StartView.vue'),
    },
    {
      path: '/games',
      component: () => import('../views/GameListPage.vue'),
    },
    {
      path: '/games/:id',
      component: () => import('../views/GamePlayPage.vue'),
      meta: {requiresAuth: true},
    },
    {
      path: '/preview/common-frame',
      component: () => import('../preview/CommonFramePreview.vue'),
      meta: { noFrame: true },
    },
    {
      path: '/preview/start',
      component: () => import('../preview/StartPreview.vue'),
      meta: { noFrame: true },
    },
    {
      path: '/preview/start-frame',
      component: () => import('../preview/StartFramePreview.vue'),
      meta: { noFrame: true },
    },
    {
      path: '/preview/game-select-frame',
      component: () => import('../preview/GameSelectFramePreview.vue'),
      meta: { noFrame: true },
    },
    {
      path: '/preview/game-select-guest',
      component: () => import('../preview/GameSelectPreview.vue'),
      meta: { noFrame: true, state: 'guest' },
    },
    {
      path: '/preview/game-select-open',
      component: () => import('../preview/GameSelectPreview.vue'),
      meta: { noFrame: true, state: 'open' },
    },
    {
      path: '/preview/game-select-closed',
      component: () => import('../preview/GameSelectPreview.vue'),
      meta: { noFrame: true, state: 'closed' },
    },
    {
      path: '/preview/game-select-empty',
      component: () => import('../preview/GameSelectPreview.vue'),
      meta: { noFrame: true, state: 'empty' },
    },
    {
      path: '/preview/overlay-game-select',
      component: () => import('../preview/OverlayGameSelectPreview.vue'),
      meta: { noFrame: true },
    },
    {
      path: '/preview/overlay-game-ready',
      component: () => import('../preview/OverlayGameReadyPreview.vue'),
      meta: { noFrame: true },
    },
  ],
})

router.beforeEach(async (to) => {
  if( !to.meta.requiresAuth ) return
  const auth = useAuthStore()
  if( !auth.loggedIn ) await auth.fetchMe()
  if( !auth.loggedIn) {
    auth.login(to.fullPath)
    return false
  }
})

export default router
