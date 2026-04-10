<template>
  <div class="announcement-float">
    <!-- 悬浮球 -->
    <div class="float-ball" @click="visible = true">
      <span class="ball-icon">📢</span>
      <span class="ball-badge" v-if="unreadCount > 0">{{ unreadCount }}</span>
    </div>

    <!-- 公告弹窗 -->
    <el-dialog
      v-model="visible"
      title="网站公告"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="announcement-list">
        <div
          v-for="item in announcements"
          :key="item.id"
          class="announcement-item"
          @click="showDetail(item)"
        >
          <div class="item-header">
            <h3 class="item-title">
              {{ item.title }}
              <el-tag v-if="isNew(item)" type="danger" size="small" effect="dark">NEW</el-tag>
            </h3>
            <span class="item-time">{{ formatDate(item.gmtCreate) }}</span>
          </div>
          <div class="item-preview">{{ item.content.substring(0, 50) }}...</div>
        </div>

        <el-empty v-if="!announcements.length" description="暂无公告" />
      </div>
    </el-dialog>

    <!-- 公告详情弹窗 -->
    <el-dialog
      v-model="detailVisible"
      :title="currentAnnouncement?.title"
      width="600px"
    >
      <div class="announcement-detail">
        <div class="detail-time">发布时间：{{ formatDate(currentAnnouncement?.gmtCreate) }}</div>
        <div class="detail-content" v-html="currentAnnouncement?.content"></div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const visible = ref(false)
const detailVisible = ref(false)
const currentAnnouncement = ref(null)
const announcements = ref([])
const readIds = ref([])

// 模拟数据（后续可以从后端API获取）
const mockAnnouncements = [
  {
    id: 1,
    title: '欢迎使用591家教网',
    content: '591家教网致力于为学员和家长提供优质的家教服务，我们拥有大量经过认证的优秀教员，覆盖小学、初中、高中各个年级和科目。',
    gmtCreate: new Date().toISOString(),
    status: 1
  },
  {
    id: 2,
    title: '平台服务升级通知',
    content: '为了给您提供更好的服务体验，我们将在本周末进行系统升级维护，届时部分功能可能暂时无法使用，预计维护时间为2小时，给您带来的不便敬请谅解。',
    gmtCreate: new Date(Date.now() - 86400000).toISOString(),
    status: 1
  },
  {
    id: 3,
    title: '教员认证流程优化',
    content: '为了提高教员认证效率，我们优化了认证流程，现在只需要上传学生证或教师资格证即可快速完成认证，审核时间缩短至24小时内。',
    gmtCreate: new Date(Date.now() - 172800000).toISOString(),
    status: 1
  }
]

const unreadCount = computed(() => {
  return announcements.value.filter(a => !readIds.value.includes(a.id)).length
})

const isNew = (item) => {
  const threeDaysAgo = Date.now() - 3 * 24 * 60 * 60 * 1000
  return new Date(item.gmtCreate).getTime() > threeDaysAgo
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

const showDetail = (item) => {
  currentAnnouncement.value = item
  detailVisible.value = true

  // 标记为已读
  if (!readIds.value.includes(item.id)) {
    readIds.value.push(item.id)
    localStorage.setItem('announcement_read_ids', JSON.stringify(readIds.value))
  }
}

onMounted(() => {
  // 加载公告数据
  const stored = localStorage.getItem('announcements')
  if (stored) {
    try {
      announcements.value = JSON.parse(stored)
    } catch (e) {
      announcements.value = mockAnnouncements
    }
  } else {
    announcements.value = mockAnnouncements
    // 初始化到localStorage
    localStorage.setItem('announcements', JSON.stringify(mockAnnouncements))
  }

  // 加载已读记录
  const readStored = localStorage.getItem('announcement_read_ids')
  if (readStored) {
    try {
      readIds.value = JSON.parse(readStored)
    } catch (e) {
      readIds.value = []
    }
  }

  // 监听localStorage变化（管理员发布新公告时自动更新）
  window.addEventListener('storage', (e) => {
    if (e.key === 'announcements' && e.newValue) {
      try {
        announcements.value = JSON.parse(e.newValue)
      } catch (err) {
        console.error(err)
      }
    }
  })
})
</script>

<style scoped>
.announcement-float {
  position: fixed;
  right: 30px;
  bottom: 100px;
  z-index: 999;
}

.float-ball {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
  transition: all 0.3s;
  position: relative;
  animation: float 3s ease-in-out infinite;
}

.float-ball:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.6);
}

.ball-icon {
  font-size: 24px;
}

.ball-badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background: #f56c6c;
  color: #fff;
  font-size: 12px;
  font-weight: 600;
  min-width: 20px;
  height: 20px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 6px;
  border: 2px solid #fff;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.announcement-list {
  max-height: 500px;
  overflow-y: auto;
}

.announcement-item {
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background 0.2s;
}

.announcement-item:hover {
  background: #f5f7fa;
}

.announcement-item:last-child {
  border-bottom: none;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.item-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.item-time {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
}

.item-preview {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.announcement-detail {
  padding: 16px 0;
}

.detail-time {
  font-size: 13px;
  color: #999;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.detail-content {
  font-size: 14px;
  color: #333;
  line-height: 1.8;
  white-space: pre-wrap;
}

@media (max-width: 768px) {
  .announcement-float {
    right: 20px;
    bottom: 80px;
  }

  .float-ball {
    width: 48px;
    height: 48px;
  }

  .ball-icon {
    font-size: 20px;
  }
}
</style>
