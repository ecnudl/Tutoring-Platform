<script setup>
// 旧路径已迁移至 /jy/t<displayNo>
definePageMeta({
  middleware: [(to) => navigateTo('/jy', { redirectCode: 301 })]
})
</script>
<template><div /></template>
