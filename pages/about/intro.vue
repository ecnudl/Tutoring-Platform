<template>
<div class="about-page">
<Head>
  <Title>{{ siteBrand }} - 公司简介</Title>
  <Meta name="description" :content="'关于 ' + siteBrand + ' - 公司简介'" />
</Head>

<div class="container about-wrapper">
  <div class="crumbs">
    <NuxtLink to="/">首页</NuxtLink>
    <span class="sep">›</span>
    <span>公司简介</span>
  </div>

  <article class="about-card">
    <!-- admin 富文本优先 -->
    <div v-if="overrideHtml" class="about-body about-html" v-html="overrideHtml"></div>

    <!-- 默认模板（admin 未填写时显示） -->
    <div v-else class="about-body">
      <h1 class="about-title">公司简介</h1>

      <p class="about-lead">
        {{ siteBrand }} 是一家专注于<strong>一对一家教信息撮合</strong>的互联网服务平台，致力于为家长/学员和优质教员提供高效、可信、售后有保障的对接服务。
      </p>

      <h2>我们的服务</h2>
      <ul>
        <li>海量经过实名与学历审核的大学生、在职教师、专职教员资源</li>
        <li>按城市、区域、学段、科目多维度精准匹配</li>
        <li>家长发布需求后，系统自动推送符合条件的教员</li>
      </ul>

      <h2>我们的承诺</h2>
      <ul>
        <li><strong>透明中介费</strong>：一次性收取，规则公开（家长 100 / 教员首单课时费），续单 100% 归教员</li>
        <li><strong>真实资料</strong>：教员需提交学生证/教师资格证等证件，审核通过才能上线</li>
        <li><strong>响应迅速</strong>：常规需求发布后 30 分钟内匹配推荐</li>
        <li><strong>隐私保护</strong>：联系方式仅在双方确认意向后互相可见</li>
      </ul>

      <h2>联系我们</h2>
      <div class="contact">
        <div><strong>客服热线：</strong>{{ hotline }}</div>
        <div v-if="csEmail"><strong>客服邮箱：</strong>{{ csEmail }}</div>
        <div v-if="csWechat"><strong>客服微信：</strong>{{ csWechat }}</div>
        <div v-if="workTime"><strong>工作时间：</strong>{{ workTime }}</div>
      </div>

      <p class="about-footnote">
        本页内容由网站管理员维护，可在 admin 后台修改 <code>siteAboutIntroHtml</code> 字段为 HTML 内容以覆盖本默认模板。
      </p>
    </div>
  </article>
</div>
</div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useSiteConfig } from '~/composables/useSiteConfig'
const { config, load } = useSiteConfig()

const overrideHtml = computed(() => (config.value.siteAboutIntroHtml || '').trim())
const siteBrand = computed(() => config.value.siteBrandName || '591家教网')
const hotline = computed(() => config.value.siteHotline || '客服热线：待配置')
const csEmail = computed(() => config.value.siteCsEmail || '')
const csWechat = computed(() => config.value.siteCsWechat || '')
const workTime = computed(() => config.value.siteWorkTime || '')

onMounted(() => { load() })
</script>

<style scoped>
.about-page { min-height: 100vh; background: #f5f7fa; padding: 32px 0 60px; }
.about-wrapper { max-width: 820px; margin: 0 auto; padding: 0 20px; }
.crumbs { font-size: 13px; color: #64748b; margin-bottom: 16px; }
.crumbs a { color: #64748b; text-decoration: none; }
.crumbs a:hover { color: var(--color-primary); }
.crumbs .sep { margin: 0 6px; color: #cbd5e1; }

.about-card {
  background: #fff;
  border: 1px solid #f1f5f9;
  border-radius: 12px;
  padding: 40px 48px 48px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.03);
}
.about-title {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  margin: 0 0 20px;
  text-align: center;
  letter-spacing: 2px;
}
.about-body h2 {
  font-size: 18px;
  font-weight: 700;
  color: var(--color-primary, #163B6B);
  margin: 28px 0 12px;
  padding-left: 10px;
  border-left: 4px solid var(--color-primary, #163B6B);
}
.about-body p, .about-body li { color: #334155; line-height: 1.9; font-size: 15px; }
.about-lead {
  background: #f8fafc;
  padding: 18px 22px;
  border-radius: 8px;
  font-size: 15px !important;
}
.about-lead strong { color: #f97316; }
.about-body ul { padding-left: 20px; margin: 10px 0; }
.about-body li { margin-bottom: 8px; }
.contact {
  background: #f8fafc;
  padding: 16px 22px;
  border-radius: 8px;
  line-height: 2;
}
.about-footnote {
  margin-top: 40px;
  font-size: 12px;
  color: #94a3b8;
  border-top: 1px dashed #e2e8f0;
  padding-top: 12px;
}
.about-footnote code {
  background: #f1f5f9;
  padding: 1px 6px;
  border-radius: 3px;
  font-family: ui-monospace, monospace;
}
.about-html :deep(h1), .about-html :deep(h2), .about-html :deep(h3) { color: var(--color-primary, #163B6B); }
.about-html :deep(p) { line-height: 1.9; }

@media (max-width: 640px) {
  .about-card { padding: 24px 16px; }
  .about-title { font-size: 22px; }
}
</style>
