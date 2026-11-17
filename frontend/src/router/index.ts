import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/preview/common-frame',
      component: () => import('../preview/CommonFramePreview.vue'),
    },
  ],
})

export default router
