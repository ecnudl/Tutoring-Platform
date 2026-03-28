<template>
  <div>
    <section class="banner" style="background:linear-gradient(135deg,#409eff,#67c23a);color:#fff;padding:80px 20px;text-align:center">
      <h1 style="font-size:42px;margin-bottom:16px">家教在线平台</h1>
      <p style="font-size:18px;margin-bottom:24px">找家教、做家教，一站式服务平台</p>
      <div style="display:flex;gap:12px;justify-content:center">
        <NuxtLink to="/tutor"><el-button type="warning" size="large">找教员</el-button></NuxtLink>
        <NuxtLink to="/register"><el-button size="large">我要做家教</el-button></NuxtLink>
      </div>
    </section>
    <div class="container" style="padding:40px 20px">
      <h2 class="page-title">平台流程</h2>
      <div style="display:grid;grid-template-columns:repeat(4,1fr);gap:20px;margin-bottom:40px">
        <el-card v-for="(s,i) in steps" :key="i" shadow="hover" style="text-align:center;padding:20px">
          <div style="font-size:36px;color:#409eff;margin-bottom:12px">{{ i+1 }}</div>
          <h3>{{ s.title }}</h3><p style="color:#999;margin-top:8px">{{ s.desc }}</p>
        </el-card>
      </div>

      <h2 class="page-title">热门教员</h2>
      <div v-if="tutors.length" class="card-grid" style="margin-bottom:32px">
        <el-card v-for="t in tutors" :key="t.id" shadow="hover" style="cursor:pointer" @click="$router.push('/tutor/'+t.id)">
          <div style="display:flex;gap:12px">
            <el-avatar :size="50" :src="t.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <div>
              <h4>{{ t.realName || '教员' }}</h4>
              <p style="color:#999;font-size:12px">{{ t.university || '' }}</p>
              <p style="color:#e6a23c;font-size:13px" v-if="t.priceMin">{{ t.priceMin }}-{{ t.priceMax }}元/时</p>
            </div>
          </div>
        </el-card>
      </div>
      <p v-else style="color:#999;margin-bottom:32px">暂无教员数据，<NuxtLink to="/tutor" style="color:#409eff">浏览教员库</NuxtLink></p>

      <h2 class="page-title">最新需求</h2>
      <div v-if="requirements.length" class="card-grid">
        <el-card v-for="r in requirements" :key="r.id" shadow="hover" style="cursor:pointer" @click="$router.push('/requirement/'+r.id)">
          <h4>{{ r.title }}</h4>
          <p style="color:#e6a23c;font-size:13px" v-if="r.budgetMin">预算：{{ r.budgetMin }}-{{ r.budgetMax }}元/时</p>
        </el-card>
      </div>
      <p v-else style="color:#999">暂无需求数据，<NuxtLink to="/requirement" style="color:#409eff">浏览需求列表</NuxtLink></p>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'

const { post } = useApi()
const tutors = ref([])
const requirements = ref([])

const steps = [
  { title: '注册账号', desc: '选择教员或学员角色注册' },
  { title: '完善资料', desc: '填写个人信息和教学信息' },
  { title: '搜索匹配', desc: '浏览教员库或发布找老师需求' },
  { title: '开始辅导', desc: '预约确认后开始家教服务' }
]

onMounted(async () => {
  try {
    const [tRes, rRes] = await Promise.all([
      post('/user/api/tutor/search', { pageCurrent: 1, pageSize: 6 }),
      post('/user/api/requirement/search', { pageCurrent: 1, pageSize: 6 })
    ])
    if (tRes.code === 200 && tRes.data) tutors.value = tRes.data.list || []
    if (rRes.code === 200 && rRes.data) requirements.value = rRes.data.list || []
  } catch (e) { console.error(e) }
})
</script>
