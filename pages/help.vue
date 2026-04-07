<template>
  <div class="help-wrapper">
    <div class="breadcrumb-bar">
      <div class="container">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>帮助中心</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>

    <div class="container help-body">
      <aside class="help-sidebar">
        <div class="sidebar-title">帮助中心</div>
        <div class="sidebar-menu">
          <div
            v-for="(cat, idx) in categories"
            :key="idx"
            class="menu-item"
            :class="{ active: activeCategory === idx }"
            @click="activeCategory = idx"
          >
            <span class="menu-icon">{{ cat.icon }}</span>
            <span>{{ cat.name }}</span>
          </div>
        </div>
      </aside>

      <main class="help-main">
        <div class="main-header">
          <h2>{{ categories[activeCategory].name }}</h2>
        </div>

        <div class="faq-list">
          <div
            v-for="(item, idx) in categories[activeCategory].items"
            :key="idx"
            class="faq-card"
            :class="{ open: item.open }"
          >
            <div class="faq-q" @click="item.open = !item.open">
              <span class="q-badge">Q</span>
              <span class="q-text">{{ item.q }}</span>
              <span class="q-arrow" :class="{ rotated: item.open }">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="6 9 12 15 18 9"/></svg>
              </span>
            </div>
            <transition name="slide">
              <div class="faq-a" v-show="item.open">
                <div class="a-text" v-html="item.a"></div>
              </div>
            </transition>
          </div>
        </div>

        <div v-if="activeCategory === categories.length - 1" class="contact-cards">
          <div class="contact-card">
            <div class="c-title">客服热线</div>
            <div class="c-value">400-000-0000</div>
            <div class="c-desc">周一至周日 9:00-21:00</div>
          </div>
          <div class="contact-card">
            <div class="c-title">客服邮箱</div>
            <div class="c-value">service@51jiajiao.com</div>
            <div class="c-desc">24小时内回复</div>
          </div>
          <div class="contact-card">
            <div class="c-title">在线客服</div>
            <div class="c-value">微信客服</div>
            <div class="c-desc">扫码添加客服微信</div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'

const activeCategory = ref(0)

const categories = reactive([
  {
    name: '家长/学员指南',
    icon: '👨‍👩‍👧',
    items: [
      { q: '如何发布家教需求？', a: '<ul><li>注册并登录账号</li><li>点击"请家教"进入需求发布页面</li><li>填写辅导科目、年级、时间、地点等信息</li><li>提交需求后，平台会为您推荐合适的教员</li></ul>', open: false },
      { q: '如何联系教员？', a: '<ul><li>在教员库中浏览教员信息</li><li>点击"预约试讲"按钮</li><li>填写预约信息并提交</li><li>教员会在24小时内联系您</li></ul>', open: false },
      { q: '平台如何保障教学质量？', a: '<ul><li>严格审核教员资质，要求提供学历证明</li><li>建立教员评价体系，家长可查看历史评价</li><li>提供试讲服务，满意后再正式开始</li><li>7天内不满意可免费更换教员</li></ul>', open: false },
      { q: '试讲是否收费？', a: '<p>首次试讲通常为免费或象征性收费（如半小时30-50元），具体由教员和家长协商。试讲满意后再签订正式辅导协议。</p>', open: false }
    ]
  },
  {
    name: '教员指南',
    icon: '👨‍🏫',
    items: [
      { q: '如何注册成为教员？', a: '<ul><li>点击页面右上角"注册"按钮</li><li>选择"我是教员"身份</li><li>填写手机号、密码等基本信息</li><li>完善个人资料和教学信息</li><li>等待平台审核通过（1-3个工作日）</li></ul>', open: false },
      { q: '如何成为优质教员？', a: '<ul><li>完善个人资料，上传真实照片和证书</li><li>及时响应家长预约请求（24小时内）</li><li>认真备课，提供高质量教学服务</li><li>积累好评，提升平台信誉度</li><li>定期更新可授课时间和科目</li></ul>', open: false },
      { q: '教员需要提供哪些资料？', a: '<ul><li>身份证照片（用于实名认证）</li><li>学历证书或学生证</li><li>教师资格证（在职教师需提供）</li><li>个人简历和教学经历</li><li>近期生活照</li></ul>', open: false },
      { q: '如何接单和管理订单？', a: '<p>登录个人中心后，可以在"需求管理"中查看家长发布的需求，主动申请感兴趣的订单。也可以等待平台根据您的资料推荐给家长。</p>', open: false }
    ]
  },
  {
    name: '收费标准',
    icon: '💰',
    items: [
      { q: '大学生家教收费是多少？', a: '<p>大学生家教参考价格为 <strong>50-100元/小时</strong>。在校大学生学习成绩优秀，有辅导经验，性价比高。</p>', open: false },
      { q: '专职教员收费是多少？', a: '<p>专职教员参考价格为 <strong>100-150元/小时</strong>。全职家教老师教学经验丰富，有专业教学方法，效果显著。</p>', open: false },
      { q: '在职教师收费是多少？', a: '<p>在职教师参考价格为 <strong>150-300元/小时</strong>。学校在职教师深谙考试重点，权威教学经验，提分快速。</p>', open: false },
      { q: '费用如何支付？', a: '<ul><li>以上价格仅供参考，实际费用由教员和家长协商确定</li><li>价格会根据科目难度、年级、上课地点等因素调整</li><li>课时费一般按课时结算，由家长直接支付给教员</li></ul>', open: false }
    ]
  },
  {
    name: '账号相关',
    icon: '🔒',
    items: [
      { q: '忘记密码怎么办？', a: '<ul><li>点击登录页面的"找回密码"</li><li>输入注册时使用的手机号</li><li>获取短信验证码并验证</li><li>设置新密码即可</li></ul>', open: false },
      { q: '如何修改个人信息？', a: '<ul><li>登录后进入"个人中心"</li><li>点击"个人资料"菜单</li><li>修改需要变更的信息</li><li>点击保存即可</li></ul>', open: false },
      { q: '如何注销账号？', a: '<p>如需注销账号，请联系客服热线 400-000-0000，客服人员会在确认身份后为您处理。注销后账号数据将无法恢复。</p>', open: false }
    ]
  },
  {
    name: '联系我们',
    icon: '📞',
    items: [
      { q: '平台客服工作时间？', a: '<p>客服热线工作时间为 <strong>周一至周日 9:00-21:00</strong>，节假日照常服务。</p>', open: false },
      { q: '如何投诉或建议？', a: '<ul><li>拨打客服热线：400-000-0000</li><li>发送邮件至：service@51jiajiao.com</li><li>通过微信客服进行反馈</li><li>我们会在24小时内处理您的反馈</li></ul>', open: false }
    ]
  }
])
</script>

<style scoped>
.help-wrapper {
  min-height: 100vh;
  background: var(--color-bg);
}

.breadcrumb-bar {
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
  padding: 14px 0;
}
.breadcrumb-bar .container {
  max-width: var(--content-width);
  margin: 0 auto;
  padding: 0 var(--space-xl);
}

.help-body {
  max-width: var(--content-width);
  margin: 0 auto;
  padding: var(--space-2xl) var(--space-xl);
  display: flex;
  gap: var(--space-2xl);
  align-items: flex-start;
}

.help-sidebar {
  width: var(--sidebar-width);
  flex-shrink: 0;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  overflow: hidden;
  position: sticky;
  top: 72px;
}

.sidebar-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  padding: 18px var(--space-xl);
  border-bottom: 1px solid var(--color-border-light);
}

.sidebar-menu { padding: var(--space-sm) 0; }

.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: var(--space-md) var(--space-xl);
  cursor: pointer;
  font-size: var(--font-size-base);
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
  border-left: 3px solid transparent;
}
.menu-item:hover { background: var(--color-bg); color: var(--color-primary); }
.menu-item.active { background: var(--color-primary-lighter); color: var(--color-primary); font-weight: var(--font-weight-medium); border-left-color: var(--color-primary); }
.menu-icon { font-size: var(--font-size-xl); }

.help-main { flex: 1; min-width: 0; }

.main-header { margin-bottom: var(--space-xl); }
.main-header h2 {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin: 0;
  padding-bottom: var(--space-md);
  border-bottom: 2px solid var(--color-primary);
  display: inline-block;
}

.faq-list { display: flex; flex-direction: column; gap: var(--space-md); }

.faq-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  overflow: hidden;
  transition: border-color var(--transition-fast);
}
.faq-card:hover { border-color: var(--color-primary); }

.faq-q {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  padding: var(--space-lg) var(--space-xl);
  cursor: pointer;
  transition: background var(--transition-fast);
}
.faq-q:hover { background: var(--color-bg); }

.q-badge {
  width: 24px;
  height: 24px;
  background: var(--color-primary);
  color: white;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-bold);
  flex-shrink: 0;
}
.q-text { flex: 1; font-size: var(--font-size-md); font-weight: var(--font-weight-medium); color: var(--color-text); }
.q-arrow { color: var(--color-text-muted); transition: transform 0.2s; display: flex; align-items: center; }
.q-arrow.rotated { transform: rotate(180deg); }

.faq-a { padding: 0 var(--space-xl) var(--space-lg) 56px; color: var(--color-text-secondary); font-size: var(--font-size-base); line-height: var(--line-height-relaxed); }

.a-text :deep(ul) { margin: 0; padding: 0; list-style: none; }
.a-text :deep(li) { position: relative; padding-left: var(--space-lg); margin: 6px 0; }
.a-text :deep(li):before { content: ''; position: absolute; left: 0; top: 10px; width: 6px; height: 6px; background: var(--color-primary); border-radius: 50%; }
.a-text :deep(p) { margin: 0; }
.a-text :deep(strong) { color: var(--color-primary); }

.slide-enter-active, .slide-leave-active { transition: all 0.2s ease; overflow: hidden; }
.slide-enter-from, .slide-leave-to { opacity: 0; max-height: 0; padding-top: 0; padding-bottom: 0; }

.contact-cards { display: grid; grid-template-columns: repeat(3, 1fr); gap: var(--space-lg); margin-top: var(--space-2xl); }
.contact-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-2xl);
  text-align: center;
  transition: border-color var(--transition-fast);
}
.contact-card:hover { border-color: var(--color-primary); }
.c-title { font-size: var(--font-size-md); font-weight: var(--font-weight-semibold); color: var(--color-text); margin-bottom: var(--space-sm); }
.c-value { font-size: var(--font-size-lg); font-weight: var(--font-weight-semibold); color: var(--color-primary); margin-bottom: 4px; }
.c-desc { font-size: var(--font-size-xs); color: var(--color-text-muted); }

@media (max-width: 768px) {
  .help-body { flex-direction: column; padding: var(--space-lg) var(--space-md); }
  .help-sidebar { width: 100%; position: static; }
  .sidebar-menu { display: flex; overflow-x: auto; padding: var(--space-sm) var(--space-md); gap: 4px; }
  .menu-item { white-space: nowrap; padding: var(--space-sm) var(--space-md); border-radius: var(--radius-lg); border-left: none; font-size: var(--font-size-sm); }
  .menu-item.active { background: var(--color-primary); color: white; border-left: none; }
  .sidebar-title { display: none; }
  .contact-cards { grid-template-columns: 1fr; }
  .faq-a { padding-left: var(--space-xl); }
}
</style>
