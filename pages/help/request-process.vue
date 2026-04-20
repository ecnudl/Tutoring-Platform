<template>
  <div class="hp-page">
    <Head>
      <Title>请家教流程 - 591家教网</Title>
      <Meta name="description" content="591家教网请家教流程详解：发布需求、平台匹配、电话沟通、免费试讲、确认合作、开始上课。全程免费，教员实名认证，7天无理由换人。" />
    </Head>

    <div class="container hp-wrapper">
      <!-- 面包屑 -->
      <div class="hp-crumbs">
        <NuxtLink to="/">首页</NuxtLink>
        <span class="sep">›</span>
        <span>请家教流程</span>
      </div>

      <!-- 主体：左内容 + 右侧栏 -->
      <div class="hp-body">
        <article class="hp-article">
          <!-- admin 配置的富文本 -->
          <div v-if="overrideHtml" class="hp-content hp-html" v-html="overrideHtml"></div>

          <!-- 默认兜底版本（admin 还没编辑时显示）-->
          <div v-else class="hp-content">
            <h1 class="hp-title">请家教流程</h1>
            <p class="hp-lead">591家教网提供 <strong>免费、快捷、专业</strong> 的家教匹配服务。只需 5 步，即可找到合适的家教老师。</p>

            <div class="steps">
              <div class="step" v-for="(s, i) in defaultSteps" :key="i">
                <div class="step-num">{{ i + 1 }}</div>
                <div class="step-body">
                  <h3>{{ s.title }}</h3>
                  <p>{{ s.desc }}</p>
                </div>
              </div>
            </div>

            <h2 class="hp-h2">服务保障</h2>
            <ul class="hp-list">
              <li><strong>实名认证</strong>：所有教员均需上传学生证 / 教师资格证，平台审核通过方可接单。</li>
              <li><strong>价格透明</strong>：课时费由家长与教员直接结算，平台 <strong>不收取任何中介费</strong>。</li>
              <li><strong>免费换教员</strong>：首次正式上课 7 天内不满意可免费更换。</li>
              <li><strong>全程可反馈</strong>：家长可随时通过客服或平台反馈教学情况。</li>
            </ul>

            <h2 class="hp-h2">常见问题</h2>
            <dl class="hp-qa">
              <dt>Q：试讲真的是免费的吗？</dt>
              <dd>首次试讲 30 分钟免费。部分特殊科目（如艺术类、乐器、海归外教）可能收取少量试讲费，具体与教员协商。</dd>
              <dt>Q：课时费如何支付？</dt>
              <dd>由家长直接支付给教员，平台不经手。建议按课时或按周结算，避免纠纷。</dd>
              <dt>Q：教员迟到或缺课怎么办？</dt>
              <dd>请第一时间拨打客服热线 <strong>{{ hotline }}</strong> 反馈，我们会协助处理或推荐新教员。</dd>
              <dt>Q：可以指定教员的性别/学校吗？</dt>
              <dd>可以。在"请家教"页面表单里填写您的偏好，我们会优先匹配符合条件的教员。</dd>
            </dl>

            <div class="hp-cta">
              <p>有任何疑问？</p>
              <p>客服热线 <strong class="tel">{{ hotline }}</strong>（{{ workTime }}）</p>
              <p v-if="csEmail">邮箱：{{ csEmail }}</p>
            </div>

            <div class="hp-actions">
              <NuxtLink to="/qjj" class="btn-primary">立即发布需求</NuxtLink>
              <NuxtLink to="/jy" class="btn-secondary">浏览教员库</NuxtLink>
            </div>
          </div>
        </article>

        <!-- 右侧 -->
        <aside class="hp-side">
          <div class="side-card">
            <div class="side-title">其它帮助</div>
            <NuxtLink to="/zf" class="side-link">请家教收费标准</NuxtLink>
            <NuxtLink to="/help" class="side-link">请家教常见问题</NuxtLink>
            <NuxtLink to="/help" class="side-link">怎样快速找到老师</NuxtLink>
            <NuxtLink to="/register/teacher" class="side-link">成为家教老师</NuxtLink>
          </div>

          <div class="side-card side-card--blue">
            <div class="side-title light">客服联系</div>
            <div class="side-phone">{{ hotline }}</div>
            <div class="side-time">{{ workTime }}</div>
            <NuxtLink to="/qjj" class="side-btn">提交家教需求 →</NuxtLink>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useSiteConfig } from '~/composables/useSiteConfig'

const { config, load } = useSiteConfig()

const overrideHtml = computed(() => (config.value.helpRequestProcessHtml || '').trim())
const hotline = computed(() => config.value.siteHotline || '13795420591')
const workTime = computed(() => config.value.siteWorkTime || '周一至周日 9:00-21:00')
const csEmail = computed(() => config.value.siteCsEmail || '')

const defaultSteps = [
  { title: '发布家教需求', desc: '登录后点击"请家教"入口，填写辅导科目、年级、上课时间、地点、预算等信息；也可直接拨打客服热线由专员为您登记。' },
  { title: '平台智能匹配', desc: '顾问会在 24 小时内为您推荐 3-5 位合适的教员（含简历、授课经验、评价）供您挑选。' },
  { title: '电话/微信沟通', desc: '直接与推荐教员电话或微信沟通，了解对方的教学风格、时间安排，确认是否合适。' },
  { title: '免费试讲', desc: '首次试讲 30 分钟免费（特殊科目可协商），家长可现场观摩；不满意随时换人，不收任何费用。' },
  { title: '确认合作，开始上课', desc: '双方确定合作后签订《家教辅导协议》，约定上课频率、课时数、课时费；教员按约上门或在线授课。' }
]

onMounted(() => { load() })
</script>

<style scoped>
.hp-page {
  background: var(--color-bg, #f5f6f8);
  min-height: 100vh;
  padding: 30px 0 60px;
}

.hp-wrapper { max-width: var(--content-width, 1200px); margin: 0 auto; padding: 0 20px; }

/* 面包屑 */
.hp-crumbs {
  font-size: 13px;
  color: #64748b;
  margin-bottom: 18px;
}
.hp-crumbs a { color: #64748b; text-decoration: none; }
.hp-crumbs a:hover { color: var(--color-primary); }
.hp-crumbs .sep { margin: 0 6px; color: #cbd5e1; }

/* 两栏布局 */
.hp-body {
  display: grid;
  grid-template-columns: 1fr 280px;
  gap: 24px;
  align-items: start;
}

.hp-article {
  background: #fff;
  border-radius: 12px;
  padding: 48px 56px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.hp-title {
  font-size: 30px;
  font-weight: 700;
  color: #111827;
  margin: 0 0 16px;
  padding-bottom: 20px;
  border-bottom: 2px solid var(--color-primary, #163B6B);
  width: fit-content;
  letter-spacing: 1px;
}

.hp-lead {
  font-size: 16px;
  line-height: 1.8;
  color: #374151;
  margin: 24px 0 36px;
}
.hp-lead strong { color: var(--color-primary, #163B6B); }

/* 步骤 */
.steps {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 40px;
}
.step {
  display: flex;
  gap: 20px;
  background: #f8fafc;
  border-left: 3px solid var(--color-primary, #163B6B);
  border-radius: 6px;
  padding: 20px 24px;
  transition: box-shadow 0.2s;
}
.step:hover { box-shadow: 0 3px 14px rgba(22, 59, 107, 0.08); }
.step-num {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--color-primary, #163B6B);
  color: #fff;
  font-size: 18px;
  font-weight: 700;
  font-family: Georgia, serif;
  display: flex;
  align-items: center;
  justify-content: center;
}
.step-body { flex: 1; }
.step-body h3 {
  font-size: 17px;
  font-weight: 600;
  color: #111827;
  margin: 0 0 8px;
}
.step-body p {
  font-size: 14px;
  line-height: 1.75;
  color: #475569;
  margin: 0;
}

/* H2 与段落 */
.hp-h2 {
  font-size: 22px;
  font-weight: 700;
  color: #111827;
  margin: 36px 0 18px;
  padding-left: 12px;
  border-left: 4px solid var(--color-primary, #163B6B);
  line-height: 1;
}
.hp-list {
  list-style: none;
  padding: 0;
  margin: 16px 0 28px;
}
.hp-list li {
  position: relative;
  padding: 8px 0 8px 22px;
  color: #475569;
  font-size: 14px;
  line-height: 1.75;
}
.hp-list li::before {
  content: '✓';
  position: absolute;
  left: 0;
  color: var(--color-primary, #163B6B);
  font-weight: 700;
}
.hp-list strong { color: #111827; }

/* Q&A */
.hp-qa { margin: 16px 0 28px; }
.hp-qa dt {
  font-size: 15px;
  font-weight: 600;
  color: #111827;
  margin-top: 18px;
}
.hp-qa dt:first-child { margin-top: 0; }
.hp-qa dd {
  font-size: 14px;
  color: #475569;
  line-height: 1.75;
  margin: 6px 0 0 0;
  padding-left: 22px;
  border-left: 2px solid #e5e7eb;
}

/* CTA + 按钮 */
.hp-cta {
  background: linear-gradient(135deg, #f0f6fd, #e8f0fa);
  border-radius: 8px;
  padding: 20px 24px;
  margin: 30px 0;
  text-align: center;
  color: #334155;
  font-size: 14px;
  line-height: 1.9;
}
.hp-cta .tel { color: var(--color-primary, #163B6B); font-family: Georgia, serif; font-size: 17px; }

.hp-actions {
  display: flex;
  gap: 16px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px dashed #e5e7eb;
}
.btn-primary, .btn-secondary {
  flex: 1;
  text-align: center;
  padding: 12px 20px;
  border-radius: 6px;
  font-size: 15px;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.2s;
}
.btn-primary {
  background: var(--color-primary, #163B6B);
  color: #fff;
}
.btn-primary:hover { background: #0f2a4d; transform: translateY(-1px); }
.btn-secondary {
  background: transparent;
  color: var(--color-primary, #163B6B);
  border: 1px solid var(--color-primary, #163B6B);
}
.btn-secondary:hover { background: var(--color-primary-lighter); }

/* 右侧栏 */
.hp-side {
  position: sticky;
  top: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.side-card {
  background: #fff;
  border-radius: 10px;
  padding: 20px 22px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.04);
}
.side-title {
  font-size: 15px;
  font-weight: 700;
  color: #111827;
  margin-bottom: 14px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f1f5f9;
}
.side-title.light { color: #fff; border-bottom-color: rgba(255,255,255,0.15); }
.side-link {
  display: block;
  font-size: 14px;
  color: #475569;
  padding: 8px 0;
  text-decoration: none;
  transition: color 0.2s;
}
.side-link:hover { color: var(--color-primary); }

.side-card--blue {
  background: var(--color-primary, #163B6B);
  color: #fff;
  text-align: center;
}
.side-phone {
  font-family: Georgia, serif;
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 1px;
  margin: 10px 0 4px;
}
.side-time {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.65);
  margin-bottom: 14px;
}
.side-btn {
  display: inline-block;
  padding: 8px 22px;
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
  border-radius: 20px;
  font-size: 13px;
  text-decoration: none;
  transition: background 0.2s;
}
.side-btn:hover { background: rgba(255, 255, 255, 0.25); }

/* 富文本（admin 编辑时）通用排版 */
.hp-html :deep(h1) { font-size: 28px; font-weight: 700; color: #111827; margin: 0 0 20px; }
.hp-html :deep(h2) { font-size: 20px; font-weight: 700; color: #111827; margin: 28px 0 12px; }
.hp-html :deep(h3) { font-size: 16px; font-weight: 600; color: #111827; margin: 18px 0 8px; }
.hp-html :deep(p) { font-size: 14px; line-height: 1.8; color: #475569; margin: 8px 0; }
.hp-html :deep(ul), .hp-html :deep(ol) { padding-left: 24px; }
.hp-html :deep(li) { font-size: 14px; line-height: 1.8; color: #475569; margin: 4px 0; }
.hp-html :deep(strong) { color: #111827; }

@media (max-width: 768px) {
  .hp-body { grid-template-columns: 1fr; }
  .hp-article { padding: 28px 20px; }
  .hp-title { font-size: 24px; }
  .step { flex-direction: column; gap: 10px; }
  .hp-actions { flex-direction: column; }
  .hp-side { position: static; }
}
</style>
