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
    { path: 'application/list', component: () => import('@/views/application/ApplicationList.vue') },
    { path: 'student/list', component: () => import('@/views/student/StudentList.vue') },
    { path: 'feedback/list', component: () => import('@/views/feedback/FeedbackList.vue') },
    { path: 'dict/subject', component: () => import('@/views/dict/SubjectList.vue') },
    { path: 'dict/grade', component: () => import('@/views/dict/GradeList.vue') },
    { path: 'dict/tag', component: () => import('@/views/dict/TagList.vue') },
    { path: 'content/banner', component: () => import('@/views/content/BannerList.vue') },
    { path: 'content/tutor-group-qr', component: () => import('@/views/content/TutorGroupQr.vue') },
    { path: 'content/article', component: () => import('@/views/content/ArticleList.vue') },
    { path: 'content/announcement', component: () => import('@/views/content/AnnouncementList.vue') },
    { path: 'content/faq', component: () => import('@/views/content/FaqList.vue') },
    // 地理配置
    { path: 'city/list', component: () => import('@/views/city/CityList.vue') },
    { path: 'district/list', component: () => import('@/views/city/DistrictList.vue') },
    { path: 'university/list', component: () => import('@/views/city/UniversityList.vue') },
    // 科目分类
    { path: 'subject-category/list', component: () => import('@/views/dict/SubjectCategoryList.vue') },
    // 内容配置
    { path: 'homepage/config', component: () => import('@/views/content/HomepageConfig.vue') },
    { path: 'price-reference/list', component: () => import('@/views/content/PriceReferenceList.vue') },
    // 审核日志
    { path: 'audit-log/list', component: () => import('@/views/system/AuditLogList.vue') },
    // 系统管理
  ]}
]
const router = createRouter({ history: createWebHistory('/admin/'), routes })
router.beforeEach((to) => { if (to.path !== '/login' && !localStorage.getItem('admin_token')) return '/login' })
export default router
