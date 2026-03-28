import { createRouter, createWebHistory } from 'vue-router'
const Layout = () => import('@/components/layout/AdminLayout.vue')
const routes = [
  { path: '/login', component: () => import('@/views/Login.vue') },
  { path: '/', component: Layout, redirect: '/dashboard', children: [
    { path: 'dashboard', component: () => import('@/views/Dashboard.vue') },
    { path: 'tutor/audit', component: () => import('@/views/tutor/TutorAudit.vue') },
    { path: 'tutor/list', component: () => import('@/views/tutor/TutorList.vue') },
    { path: 'tutor/detail/:id', component: () => import('@/views/tutor/TutorDetail.vue') },
    { path: 'requirement/audit', component: () => import('@/views/requirement/RequirementAudit.vue') },
    { path: 'requirement/list', component: () => import('@/views/requirement/RequirementList.vue') },
    { path: 'reservation/list', component: () => import('@/views/reservation/ReservationList.vue') },
    { path: 'application/list', component: () => import('@/views/application/ApplicationList.vue') },
    { path: 'student/list', component: () => import('@/views/student/StudentList.vue') },
    { path: 'feedback/list', component: () => import('@/views/feedback/FeedbackList.vue') },
    { path: 'vip/list', component: () => import('@/views/vip/VipList.vue') },
    { path: 'dict/subject', component: () => import('@/views/dict/SubjectList.vue') },
    { path: 'dict/grade', component: () => import('@/views/dict/GradeList.vue') },
    { path: 'dict/tag', component: () => import('@/views/dict/TagList.vue') },
    { path: 'content/banner', component: () => import('@/views/content/BannerList.vue') },
    { path: 'content/article', component: () => import('@/views/content/ArticleList.vue') },
    { path: 'system/user', component: () => import('@/views/system/UserList.vue') },
    { path: 'system/role', component: () => import('@/views/system/RoleList.vue') },
    { path: 'system/menu', component: () => import('@/views/system/MenuList.vue') },
  ]}
]
const router = createRouter({ history: createWebHistory('/admin/'), routes })
router.beforeEach((to) => { if (to.path !== '/login' && !localStorage.getItem('admin_token')) return '/login' })
export default router
