import { createRouter, createWebHistory } from 'vue-router'

// meta.noFrame: true → App.vue가 공통 프레임(헤더, 푸터, 토스트)을 씌우지 않는다 (미리보기 전용)
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('../views/StartView.vue'),
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
  ],
})

export default router
