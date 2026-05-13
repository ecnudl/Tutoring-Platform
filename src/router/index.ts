import { createRouter, createWebHistory } from 'vue-router'
const Layout = () => import('@/components/layout/AdminLayout.vue')
const routes = [
  { path: '/login', component: () => import('@/views/Login.vue') },
  { path: '/', component: Layout, redirect: '/dashboard', children: [
    { path: 'dashboard', component: () => import('@/views/Dashboard.vue') },
    { path: 'tutor/audit', component: () => import('@/views/tutor/TutorAudit.vue') },
    { path: 'cert/audit', component: () => import('@/views/cert/CertAudit.vue') },
    { path: 'tutor/list', component: () => import('@/views/tutor/TutorList.vue') },
    { path: 'tutor/recent-edited', component: () => import('@/views/tutor/RecentEdited.vue') },
    { path: 'tutor/detail/:id', component: () => import('@/views/tutor/TutorDetail.vue') },
    { path: 'requirement/audit', component: () => import('@/views/requirement/RequirementAudit.vue') },
    { path: 'requirement/list', component: () => import('@/views/requirement/RequirementList.vue') },
    { path: 'reservation/list', component: () => import('@/views/reservation/ReservationList.vue') },
    { path: 'student/list', component: () => import('@/views/student/StudentList.vue') },
    { path: 'feedback/list', component: () => import('@/views/feedback/FeedbackList.vue') },
    { path: 'content/banner', component: () => import('@/views/content/BannerList.vue') },
    { path: 'content/tutor-group-qr', component: () => import('@/views/content/TutorGroupQr.vue') },
    { path: 'content/announcement', component: () => import('@/views/content/AnnouncementList.vue') },
    { path: 'content/faq', component: () => import('@/views/content/FaqList.vue') },
    { path: 'content/friend-links', component: () => import('@/views/content/FriendLinks.vue') },
    { path: 'content/footer-menus', component: () => import('@/views/content/FooterMenus.vue') },
  ]}
]
const router = createRouter({ history: createWebHistory('/admin/'), routes })
router.beforeEach((to) => { if (to.path !== '/login' && !localStorage.getItem('admin_token')) return '/login' })
export default router
