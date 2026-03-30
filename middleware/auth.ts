export default defineNuxtRouteMiddleware((to) => {
  if (typeof localStorage !== 'undefined' && !localStorage.getItem('token')) return navigateTo('/login')
})
