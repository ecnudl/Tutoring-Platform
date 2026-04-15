<template>
  <div class="notice-page">
    <div class="container">
      <el-breadcrumb separator="/" style="margin-bottom:24px">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>公告详情</el-breadcrumb-item>
      </el-breadcrumb>

      <div class="notice-card" v-if="notice">
        <Head>
          <Title>{{ notice.title }} - 591家教网</Title>
        </Head>
        <h1 class="notice-title">{{ notice.title }}</h1>
        <div class="notice-meta">
          <span>{{ notice.date }}</span>
          <el-tag size="small" :type="notice.tagType">{{ notice.tag }}</el-tag>
        </div>
        <el-divider />
        <div class="notice-content" v-html="notice.content"></div>
      </div>

      <div v-else class="notice-card" style="text-align:center;padding:60px">
        <el-empty description="公告不存在" />
        <NuxtLink to="/">
          <el-button type="primary" style="margin-top:16px">返回首页</el-button>
        </NuxtLink>
      </div>
    </div>
  </div>
</template>

<script setup>
const route = useRoute()
const id = route.params.id

const noticeMap = {
  'service-upgrade': {
    title: '平台服务升级维护通知',
    date: '2025-04-10',
    tag: '网站公告',
    tagType: '',
    content: `
      <p>尊敬的用户：</p>
      <p>为了给您提供更好的服务体验，591家教网将于近期进行系统升级维护，届时部分功能可能暂时不可用。</p>
      <h3>升级内容</h3>
      <ul>
        <li>优化教员搜索算法，提升匹配精准度</li>
        <li>提升网站访问速度和稳定性</li>
        <li>修复已知问题，改善用户体验</li>
      </ul>
      <h3>维护时间</h3>
      <p>预计维护时间约 30 分钟，期间给您带来的不便敬请谅解。</p>
      <p>如有任何疑问，请联系客服热线：<strong>13795420591</strong></p>
    `
  },
  'holiday-schedule': {
    title: '五一假期客服时间调整',
    date: '2025-04-25',
    tag: '网站公告',
    tagType: '',
    content: `
      <p>尊敬的用户：</p>
      <p>五一劳动节假期期间（5月1日至5月5日），客服工作时间调整如下：</p>
      <ul>
        <li><strong>电话客服</strong>：每日 10:00 - 18:00（缩短服务时间）</li>
        <li><strong>在线客服</strong>：正常服务，可能响应稍有延迟</li>
        <li><strong>邮件回复</strong>：48小时内回复</li>
      </ul>
      <p>5月6日起恢复正常工作时间（9:00 - 21:00）。假期期间教员和家长可正常使用平台功能。</p>
      <p>祝大家假期愉快！</p>
    `
  },
  'cert-optimization': {
    title: '教员认证流程优化公告',
    date: '2025-04-15',
    tag: '网站公告',
    tagType: '',
    content: `
      <p>尊敬的教员：</p>
      <p>为提升教员认证效率和用户信任度，平台对认证流程进行了以下优化：</p>
      <h3>新增功能</h3>
      <ul>
        <li><strong>注册即可上传证书</strong>：教员注册时即可选择上传教师资格证、学生证、学历证等资质证明</li>
        <li><strong>已认证标识</strong>：通过审核的教员将在头像上显示蓝色认证徽章，提升家长信任度</li>
        <li><strong>个人中心管理</strong>：随时在"个人中心 > 资质证书"中补充或更新证书</li>
      </ul>
      <h3>认证流程</h3>
      <ol>
        <li>注册时上传证书（或注册后在个人中心补充）</li>
        <li>提交审核，平台在 1-3 个工作日内完成审核</li>
        <li>审核通过后自动获得认证标识</li>
      </ol>
      <p>认证教员将获得更多曝光机会，建议尽快完成认证。</p>
    `
  },
  'new-cities': {
    title: '新增多个城市站点上线',
    date: '2025-04-15',
    tag: '网站公告',
    tagType: '',
    content: `
      <p>591家教网持续拓展服务范围，现已新增以下城市站点：</p>
      <ul>
        <li><strong>郑州站</strong> — zhengzhou.591jiajiao.cn</li>
        <li><strong>重庆站</strong> — chongqing.591jiajiao.cn</li>
        <li><strong>西安站</strong> — xian.591jiajiao.cn</li>
        <li><strong>成都站</strong> — chengdu.591jiajiao.cn</li>
        <li><strong>长沙站</strong> — changsha.591jiajiao.cn</li>
      </ul>
      <p>此前已开通的城市包括：上海、北京、广州、南京、苏州、杭州、合肥、福州、南昌、济南、天津、武汉。</p>
      <p>欢迎各城市的家长和教员注册使用！</p>
    `
  },
  'zhongkao-policy': {
    title: '2025年上海中考政策解读',
    date: '2025-03-20',
    tag: '教育资讯',
    tagType: 'success',
    content: `
      <p>2025年上海中考政策有以下重要变化，家长和学生需要关注：</p>
      <h3>主要变化</h3>
      <ul>
        <li>名额分配综合评价录取方案进一步细化</li>
        <li>自主招生计划比例保持稳定</li>
        <li>体育考试评分标准微调</li>
        <li>综合素质评价在录取中权重提升</li>
      </ul>
      <h3>备考建议</h3>
      <ul>
        <li>重视综合素质评价材料准备</li>
        <li>合理规划各科复习时间</li>
        <li>注重体育锻炼，保证体育考试成绩</li>
        <li>如需专科辅导，可在平台预约专业教员</li>
      </ul>
    `
  },
  'math-training': {
    title: '小学数学思维训练方法分享',
    date: '2025-03-15',
    tag: '教育资讯',
    tagType: 'success',
    content: `
      <p>培养孩子的数学思维能力，比单纯刷题更重要。以下是几种有效的训练方法：</p>
      <h3>1. 问题拆解法</h3>
      <p>引导孩子将复杂问题拆解为简单步骤，培养逻辑推理能力。</p>
      <h3>2. 生活数学化</h3>
      <p>在日常生活中融入数学元素，如购物时计算折扣、旅行时计算路程时间等。</p>
      <h3>3. 数形结合</h3>
      <p>利用图形帮助理解抽象概念，如用线段图理解分数和百分数。</p>
      <h3>4. 错题反思</h3>
      <p>建立错题本，定期回顾分析错误原因，避免重复犯错。</p>
      <p>如需专业指导，可在平台预约数学家教老师进行一对一辅导。</p>
    `
  },
  'english-listening': {
    title: '初中英语听力提分技巧',
    date: '2025-03-10',
    tag: '教育资讯',
    tagType: 'success',
    content: `
      <p>英语听力是很多同学的薄弱环节，以下技巧助你快速提分：</p>
      <ul>
        <li><strong>提前审题</strong>：利用播放录音前的时间快速浏览题目和选项</li>
        <li><strong>关键词捕捉</strong>：重点关注时间、地点、数字、人名等关键信息</li>
        <li><strong>磨耳朵练习</strong>：每天坚持听 15-20 分钟英语材料</li>
        <li><strong>跟读模仿</strong>：模仿标准发音，提升辨音能力</li>
        <li><strong>做笔记</strong>：听较长对话时简要记录要点</li>
      </ul>
      <p>坚持每天练习，听力成绩一定会有明显提升。</p>
    `
  },
  'gaokao-guide': {
    title: '高考志愿填报注意事项',
    date: '2025-03-05',
    tag: '教育资讯',
    tagType: 'success',
    content: `
      <p>高考志愿填报是人生重要选择，以下事项需注意：</p>
      <ul>
        <li><strong>了解政策</strong>：掌握所在省份的录取批次和投档规则</li>
        <li><strong>合理定位</strong>：根据模考成绩和位次合理定位目标院校</li>
        <li><strong>梯度填报</strong>：按"冲、稳、保"原则拉开梯度</li>
        <li><strong>了解专业</strong>：深入了解专业课程内容和就业方向</li>
        <li><strong>关注细节</strong>：注意单科成绩要求、体检限报等条件</li>
      </ul>
      <p>平台也提供高考志愿填报指导服务，可预约专业老师咨询。</p>
    `
  },
  'free-trial': {
    title: '首次试讲免费，满意后再上课',
    date: '2025-01-01',
    tag: '教/学员须知',
    tagType: 'warning',
    content: `
      <p>为保障家长和学员的权益，591家教网推出首次试讲服务：</p>
      <ul>
        <li>首次试讲通常为免费或象征性收费（30分钟）</li>
        <li>试讲期间可全面了解教员教学风格和水平</li>
        <li>试讲满意后再正式确定辅导关系</li>
        <li>不满意可免费更换教员</li>
      </ul>
      <p>建议家长在试讲时关注：教员的教学方法、与孩子的互动、知识讲解的清晰度等方面。</p>
    `
  },
  'payment-policy': {
    title: '课时费由家长与教员直接结算',
    date: '2025-01-01',
    tag: '教/学员须知',
    tagType: 'warning',
    content: `
      <p>关于课时费结算说明：</p>
      <ul>
        <li>课时费由家长与教员直接协商确定</li>
        <li>费用由家长直接支付给教员，平台不经手课时费</li>
        <li>建议按课时结算，每次上课后当面支付</li>
        <li>教员和家长可自行约定付款方式（现金、转账等）</li>
      </ul>
      <p>如对费用有任何疑问，可联系客服协调。</p>
    `
  },
  'cert-required': {
    title: '教员需通过实名认证方可接单',
    date: '2025-01-01',
    tag: '教/学员须知',
    tagType: 'warning',
    content: `
      <p>为保障家长和学员的安全与权益，平台要求所有教员必须通过实名认证：</p>
      <h3>认证要求</h3>
      <ul>
        <li>提交真实个人信息（姓名、联系方式等）</li>
        <li>上传有效证件（身份证、学生证或教师资格证）</li>
        <li>通过平台审核（1-3个工作日）</li>
      </ul>
      <h3>认证优势</h3>
      <ul>
        <li>获得蓝色认证徽章，提升个人信誉</li>
        <li>优先展示在教员库中</li>
        <li>获得更多家长的信任和预约</li>
      </ul>
    `
  },
  'contact-support': {
    title: '如遇问题请拨打客服热线反馈',
    date: '2025-01-01',
    tag: '教/学员须知',
    tagType: 'warning',
    content: `
      <p>如您在使用平台过程中遇到任何问题，可通过以下方式联系我们：</p>
      <ul>
        <li><strong>客服热线</strong>：13795420591（周一至周日 9:00-21:00）</li>
        <li><strong>客服邮箱</strong>：service@591jiajiao.com（24小时内回复）</li>
        <li><strong>微信客服</strong>：扫描首页二维码添加</li>
      </ul>
      <p>我们会尽快处理您的反馈，感谢您的支持与信任！</p>
    `
  }
}

const notice = noticeMap[id] || null
</script>

<style scoped>
.notice-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: var(--space-xl) 0 var(--space-4xl);
}

.container {
  max-width: var(--content-width);
  margin: 0 auto;
  padding: 0 var(--space-xl);
}

.notice-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-3xl) var(--space-4xl);
}

.notice-title {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin: 0 0 var(--space-md) 0;
}

.notice-meta {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
}

.notice-content {
  font-size: var(--font-size-base);
  color: var(--color-text);
  line-height: 1.8;
}

.notice-content :deep(h3) {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  margin: var(--space-xl) 0 var(--space-md);
  color: var(--color-text);
}

.notice-content :deep(ul),
.notice-content :deep(ol) {
  padding-left: var(--space-xl);
  margin: var(--space-md) 0;
}

.notice-content :deep(li) {
  margin: var(--space-sm) 0;
}

.notice-content :deep(p) {
  margin: var(--space-md) 0;
}

.notice-content :deep(strong) {
  color: var(--color-primary);
}

@media (max-width: 768px) {
  .notice-card {
    padding: var(--space-xl) var(--space-lg);
  }
  .notice-title {
    font-size: var(--font-size-xl);
  }
}
</style>
