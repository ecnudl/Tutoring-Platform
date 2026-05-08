<template>
  <span class="tutor-icons" v-if="anyVisible">
    <!-- 明星教员: 橙色实心五角星 -->
    <span v-if="tutor && tutor.isStar === 1" class="tutor-icon" title="明星教员" aria-label="明星教员">
      <svg viewBox="0 0 24 24" :width="size" :height="size" xmlns="http://www.w3.org/2000/svg">
        <path d="M12 2.3l3.04 6.95 7.46.66-5.66 4.95 1.7 7.34L12 18.3l-6.54 3.9 1.7-7.34L1.5 9.91l7.46-.66z"
              fill="#f59e0b" stroke="#d97706" stroke-width="0.5" stroke-linejoin="round"/>
      </svg>
    </span>

    <!-- 证件已认证: 绿色圆 + 白色对勾 -->
    <span v-if="tutor && tutor.isVerified === 1" class="tutor-icon" title="证件已认证" aria-label="证件已认证">
      <svg viewBox="0 0 24 24" :width="size" :height="size" xmlns="http://www.w3.org/2000/svg">
        <circle cx="12" cy="12" r="11" fill="#10b981"/>
        <path d="M6.5 12.4l3.5 3.5L17.5 8.4" stroke="#fff" stroke-width="2.6"
              fill="none" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
    </span>

    <!-- 可网络授课: 橙色圆 + 白色摄像头 (镜头 + 底座) -->
    <span v-if="tutor && (tutor.teachingMethod === 3 || tutor.teachingMethod === 4)" class="tutor-icon" title="可网络授课" aria-label="可网络授课">
      <svg viewBox="0 0 24 24" :width="size" :height="size" xmlns="http://www.w3.org/2000/svg">
        <circle cx="12" cy="12" r="11" fill="#f97316"/>
        <!-- 摄像头主体 (镜头外圈) -->
        <circle cx="12" cy="10.5" r="4" fill="none" stroke="#fff" stroke-width="1.6"/>
        <!-- 镜头 (内圆点) -->
        <circle cx="12" cy="10.5" r="1.4" fill="#fff"/>
        <!-- 底座颈部 -->
        <rect x="11.4" y="14.4" width="1.2" height="1.5" fill="#fff"/>
        <!-- 底座 -->
        <rect x="8.6" y="15.9" width="6.8" height="1.4" rx="0.5" fill="#fff"/>
      </svg>
    </span>
  </span>
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({
  tutor: { type: Object, required: true },
  size: { type: [Number, String], default: 16 }
})
const anyVisible = computed(() => {
  const t = props.tutor
  if (!t) return false
  return t.isStar === 1 || t.isVerified === 1 || t.teachingMethod === 3 || t.teachingMethod === 4
})
</script>

<style scoped>
.tutor-icons {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  vertical-align: middle;
  line-height: 1;
}
.tutor-icon {
  display: inline-flex;
  align-items: center;
  line-height: 1;
}
.tutor-icon svg { display: block; }
</style>
