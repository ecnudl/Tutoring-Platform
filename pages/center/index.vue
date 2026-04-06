<template>
<div class="center-home">
  <div class="welcome-box">
    <h2>欢迎回来，{{ userStore.mobile }}</h2>
    <p>{{ userStore.userType === 1 ? '教员账户' : '学员/家长账户' }}</p>
  </div>

  <!-- 快捷功能 -->
  <div class="quick-actions">
    <div class="action-card" @click="$router.push('/center/profile')">
      <div class="action-icon" style="background:#ecf5ff;color:#409eff">👤</div>
      <div class="action-info">
        <h3>个人资料</h3>
        <p>完善个人信息</p>
      </div>
    </div>

    <div class="action-card" @click="$router.push('/center/favorites')">
      <div class="action-icon" style="background:#fef0f0;color:#f56c6c">⭐</div>
      <div class="action-info">
        <h3>备选教员</h3>
        <p>查看收藏的教员</p>
      </div>
    </div>

    <div class="action-card" @click="$router.push('/center/requirements')">
      <div class="action-icon" style="background:#f4f4f5;color:#909399">📄</div>
      <div class="action-info">
        <h3>我的需求</h3>
        <p>管理发布的需求</p>
      </div>
    </div>

    <div class="action-card" @click="$router.push('/qjj')">
      <div class="action-icon" style="background:#f0f9ff;color:#67c23a">➕</div>
      <div class="action-info">
        <h3>发布需求</h3>
        <p>快速找到合适教员</p>
      </div>
    </div>
  </div>

  <!-- 统计信息 -->
  <div class="stats-box">
    <div class="stat-item">
      <div class="stat-value">{{ stats.favorites }}</div>
      <div class="stat-label">备选教员</div>
    </div>
    <div class="stat-item">
      <div class="stat-value">{{ stats.requirements }}</div>
      <div class="stat-label">我的需求</div>
    </div>
    <div class="stat-item">
      <div class="stat-value">{{ stats.orders }}</div>
      <div class="stat-label">订单数</div>
    </div>
  </div>

  <!-- 最近活动 -->
  <div class="recent-box">
    <h3 class="box-title">最近活动</h3>
    <div class="activity-list">
      <div class="activity-item">
        <div class="activity-icon">📌</div>
        <div class="activity-content">
          <div class="activity-text">您收藏了一位教员</div>
          <div class="activity-time">2小时前</div>
        </div>
      </div>
      <div class="activity-item">
        <div class="activity-icon">📝</div>
        <div class="activity-content">
          <div class="activity-text">发布了新的需求</div>
          <div class="activity-time">1天前</div>
        </div>
      </div>
    </div>
  </div>
</div>
</template>

<script setup>
definePageMeta({
  layout: 'center',
  middleware: 'auth'
})
import { ref, onMounted } from 'vue'
import { useUserStore } from '~/stores/user'

definePageMeta({ layout: 'center' })

const userStore = useUserStore()
const { post } = useApi()

const stats = ref({
  favorites: 0,
  requirements: 0,
  orders: 0
})

onMounted(async () => {
  try {
    // 加载统计数据
    const [favRes, reqRes] = await Promise.all([
      post('/user/auth/favorite/list', { targetType: 1 }),
      post('/user/api/requirement/search', { pageCurrent: 1, pageSize: 1 })
    ])
    if (favRes.code === 200 && favRes.data) {
      stats.value.favorites = favRes.data.length || 0
    }
    if (reqRes.code === 200 && reqRes.data) {
      stats.value.requirements = reqRes.data.totalCount || 0
    }
  } catch (e) {
    console.error(e)
  }
})
</script>

<style scoped>
.center-home {
  max-width: 900px;
}

.welcome-box {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 32px;
  border-radius: 8px;
  margin-bottom: 24px;
}

.welcome-box h2 {
  font-size: 24px;
  margin-bottom: 8px;
}

.welcome-box p {
  font-size: 14px;
  opacity: 0.9;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.action-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.action-card:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64,158,255,0.2);
  transform: translateY(-2px);
}

.action-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
}

.action-info h3 {
  font-size: 16px;
  color: #333;
  margin-bottom: 4px;
}

.action-info p {
  font-size: 13px;
  color: #999;
}

.stats-box {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-item {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #409eff;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.recent-box {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 20px;
}

.box-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 6px;
}

.activity-icon {
  font-size: 20px;
  width: 36px;
  height: 36px;
  background: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.activity-content {
  flex: 1;
}

.activity-text {
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}

.activity-time {
  font-size: 12px;
  color: #999;
}

@media (max-width: 768px) {
  .quick-actions {
    grid-template-columns: 1fr;
  }

  .stats-box {
    grid-template-columns: 1fr;
  }

  .welcome-box {
    padding: 24px;
  }

  .welcome-box h2 {
    font-size: 20px;
  }
}
</style>
