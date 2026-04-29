<template>
  <div class="zf-page">
    <Head>
      <Title>{{ cityStore.cityName }}家教价格 - 591家教网</Title>
      <Meta name="description" :content="`${cityStore.cityName}家教价格参考：文化类 / 才艺运动 / 语言留学 / 大学课程 四大类，覆盖大学生、专职教员、在职教师、海归外教多个教员等级。`" />
    </Head>

    <div class="container zf-wrapper">
      <!-- 面包屑 -->
      <div class="crumbs">
        <NuxtLink to="/">首页</NuxtLink>
        <span class="sep">›</span>
        <span>家教价格</span>
      </div>

      <!-- 顶部 hero -->
      <div class="zf-hero">
        <h1>{{ cityStore.cityName }}家教价格</h1>
        <div class="sub">名校名师 · 优质家教平台 · 价格参考</div>
      </div>

      <div class="zf-body">
        <article class="zf-main">
          <!-- 说明 -->
          <div class="intro-box">
            <ol>
              <li v-for="(p, i) in introPoints" :key="i" v-html="p"></li>
            </ol>
          </div>

          <!-- 各大类价格表 -->
          <section v-for="(cat, ci) in categories" :key="ci" class="price-section">
            <h2 class="sec-title">
              <span class="sec-num">{{ ci + 1 }}</span>
              {{ cityStore.cityName }}{{ cat.title }}
              <small v-if="cat.unit">（{{ cat.unit }}）</small>
            </h2>

            <div v-for="(tbl, ti) in cat.tables" :key="ti" class="zf-table-wrap">
              <table class="zf-table">
                <thead>
                  <tr>
                    <th v-for="h in tbl.headers" :key="h" :class="{ first: !h || h === '教员类型' }">{{ h || '—' }}</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(row, ri) in tbl.rows" :key="ri">
                    <td v-for="(c, j) in row" :key="j" :class="{ 'row-head': j === 0 }">{{ c }}</td>
                  </tr>
                </tbody>
              </table>
            </div>

            <ul v-if="cat.notes && cat.notes.length" class="sec-notes">
              <li v-for="(n, ni) in cat.notes" :key="ni">{{ n }}</li>
            </ul>
          </section>

          <div v-if="footnote" class="footnote">{{ footnote }}</div>

          <!-- 价格说明 -->
          <div v-if="displayedNotes.length" class="notes-section">
            <h3>价格说明</h3>
            <ul class="notes-list">
              <li v-for="(n, i) in displayedNotes" :key="i" v-html="n"></li>
            </ul>
          </div>

          <!-- CTA -->
          <div class="cta-section">
            <h3>需要找家教？</h3>
            <div class="cta-buttons">
              <NuxtLink to="/qjj"><el-button type="primary" size="large">免费提交需求</el-button></NuxtLink>
              <NuxtLink to="/jy"><el-button size="large">浏览教员库</el-button></NuxtLink>
            </div>
          </div>

          <!-- 中介费政策 -->
          <section class="brokerage-card">
            <h2 class="bk-title">中介费政策</h2>
            <p class="bk-lead">591家教网对成功撮合的订单收取<strong>一次性中介费</strong>，覆盖匹配、审核、售后；<strong>续单</strong>课时费由家长直接支付给教员，平台不再收取。</p>
            <ul class="bk-list">
              <li><span class="bk-tag tag-parent">家长</span>一次性 <strong>100 元</strong> 中介费</li>
              <li><span class="bk-tag tag-tutor">教员</span>中介费 = <strong>首次上课课时费金额</strong>（如 2 小时 350 元 → 中介费 350 元；首单收益归平台，<strong>续单 100% 归教员</strong>）</li>
              <li><span class="bk-tag tag-once">仅 1 次</span>若学员只上 1 次课，中介费<strong>减半退还</strong></li>
              <li><span class="bk-tag tag-stay">2 次起</span>上 2 次及以上，中介费<strong>不退</strong></li>
              <li><span class="bk-tag tag-swap">售后</span>对教员不满意，可联系客服<strong>免费推荐其他教员</strong>，直到满意为止（家长侧不再额外付中介费）</li>
              <li><span class="bk-tag tag-flow">流程</span>客服联系教员说明缴费金额 → 教员撮合时为家长代收 100 元中介费 → 双方中介费随首单一并上缴客服 → 客服将家长电话给教员 → 教员联系家长定时间地点</li>
            </ul>
          </section>
        </article>

        <aside class="zf-side">
          <div class="side-card">
            <div class="side-title">相关帮助</div>
            <NuxtLink v-for="l in sideLinks" :key="l.href" :to="l.href" class="side-link">
              <span class="arr">›</span>{{ l.label }}
            </NuxtLink>
          </div>

          <div class="side-card side-card--blue">
            <div class="side-title light">客服咨询</div>
            <div class="side-phone">{{ hotline }}</div>
            <div class="side-time">{{ workTime }}</div>
            <NuxtLink to="/qjj" class="side-btn">立即提交需求 →</NuxtLink>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useCityStore } from '~/stores/city'
import { useSiteConfig } from '~/composables/useSiteConfig'

const cityStore = useCityStore()
const { config, load, priceNotes } = useSiteConfig()

const hotline = computed(() => config.value.siteHotline || '13795420591')
const workTime = computed(() => config.value.siteWorkTime || '周一至周日 9:00-21:00')

// 默认兜底（万一 sys_config 没配置）
const DEFAULT_DATA = {
  introPoints: [
    '591家教网从数万教员库择优为您服务。',
    '教员执教前，平台会进行严格的实名+学历认证。',
    '对教员的每一份家教都有详细记录，供家长/学员参考。',
    '平台对成功撮合的订单收取一次性中介费 (家长 100 / 教员首单课时费)，<b>续单 100% 归教员</b>，规则见下方说明。',
    '如有任何疑问，请拨打客服热线。'
  ],
  categories: [
    {
      title: '文化类家教价格',
      unit: '元/每小时',
      tables: [{
        headers: ['教员类型', '学前/小学', '初中', '高中'],
        rows: [
          ['大学生', '80-120', '100-150', '120-200'],
          ['研究生', '100-150', '120-180', '180-250'],
          ['专职教员', '120-180', '150-220', '200-300'],
          ['在职教师', '150-250', '200-300', '300-500']
        ]
      }],
      notes: ['英文版教材：在以上基础上价格有增加。', '网络课程：相比上门辅导，费用降低 20%-30%，具体可商议。']
    }
  ],
  footnote: '★ 明星教员价格对应以上会适当上浮。'
}

const pricing = computed(() => {
  const raw = config.value.sitePricingData
  if (!raw) return DEFAULT_DATA
  try { return JSON.parse(raw) } catch { return DEFAULT_DATA }
})

const introPoints = computed(() => pricing.value.introPoints || [])
const categories = computed(() => pricing.value.categories || [])
const footnote = computed(() => pricing.value.footnote || '')

// 已有的"价格说明"列表（来自 sitePriceNotesJson）
const DEFAULT_BOTTOM_NOTES = [
  '以上价格为上门一对一辅导参考价格，在线辅导一般可享 <strong>8-9折</strong> 优惠。',
  '艺术类（钢琴、美术、书法等）价格另议，通常高于文化课 20%-50%。',
  '首次上课可安排免费试讲30分钟（需与教员协商）。',
  '<strong>课时费由教员自定</strong>。续单课时费由家长直接结算给教员（不经平台）；首单课时费会作为教员侧中介费上缴客服，规则见下方说明。',
  '以上价格仅供参考，具体价格请与教员沟通确认。'
]
const displayedNotes = computed(() => priceNotes.value.length ? priceNotes.value : DEFAULT_BOTTOM_NOTES)

const sideLinks = [
  { label: '请家教流程', href: '/help/request-process' },
  { label: '请家教常见问题', href: '/help/request-faq' },
  { label: '怎样快速找到老师', href: '/help/find-tutor' },
  { label: '做家教收费标准', href: '/help/tutor-pricing' },
  { label: '收费方式', href: '/help/payment-method' }
]

onMounted(() => { load() })
</script>

<style scoped>
.zf-page { background: var(--color-bg, #f5f6f8); min-height: 100vh; padding: 30px 0 60px; }
.zf-wrapper { max-width: var(--content-width, 1200px); margin: 0 auto; padding: 0 20px; }

.crumbs { font-size: 13px; color: #64748b; margin-bottom: 18px; }
.crumbs a { color: #64748b; text-decoration: none; }
.crumbs a:hover { color: var(--color-primary); }
.crumbs .sep { margin: 0 6px; color: #cbd5e1; }

/* Hero */
.zf-hero {
  background: linear-gradient(135deg, var(--color-primary, #163B6B), #264e82);
  color: #fff;
  padding: 36px 40px;
  border-radius: 12px;
  text-align: center;
  margin-bottom: 20px;
}
.zf-hero h1 { font-size: 28px; font-weight: 700; letter-spacing: 3px; margin: 0; }
.zf-hero .sub { margin-top: 6px; font-size: 14px; opacity: 0.82; letter-spacing: 1px; }

/* 两栏 */
.zf-body { display: grid; grid-template-columns: 1fr 280px; gap: 24px; align-items: start; }
.zf-main {
  background: #fff; border-radius: 12px; padding: 36px 44px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

/* 简介条 */
.intro-box {
  background: #f8fafc; border-left: 3px solid var(--color-primary, #163B6B);
  border-radius: 6px; padding: 18px 24px 18px 28px; margin-bottom: 36px;
}
.intro-box ol { margin: 0; padding-left: 20px; }
.intro-box li {
  font-size: 14px; line-height: 1.8; color: #475569; margin: 4px 0;
}
.intro-box :deep(b) { color: var(--color-primary, #163B6B); font-weight: 600; }

/* 分类 */
.price-section { margin-bottom: 40px; }
.sec-title {
  display: flex; align-items: center; gap: 10px;
  font-size: 18px; font-weight: 700; color: #111827;
  margin: 0 0 16px; padding-bottom: 10px; border-bottom: 2px solid var(--color-primary, #163B6B);
}
.sec-title small {
  font-size: 13px; font-weight: 400; color: #64748b; margin-left: auto;
}
.sec-num {
  display: inline-flex; align-items: center; justify-content: center;
  width: 26px; height: 26px; border-radius: 50%;
  background: var(--color-primary, #163B6B); color: #fff;
  font-size: 14px; font-weight: 700; font-family: Georgia, serif;
}

/* 表格 */
.zf-table-wrap { overflow-x: auto; margin-bottom: 14px; }
.zf-table {
  width: 100%; border-collapse: collapse;
  font-size: 14px; background: #fff;
}
.zf-table thead th {
  padding: 12px 14px; background: var(--color-primary, #163B6B); color: #fff;
  font-weight: 600; text-align: center; border: 1px solid var(--color-primary, #163B6B);
}
.zf-table thead th.first { background: #264e82; }
.zf-table tbody td {
  padding: 10px 14px; border: 1px solid #e5e7eb;
  text-align: center; color: #475569;
}
.zf-table tbody td.row-head {
  background: #f8fafc; font-weight: 600; color: #111827;
}
.zf-table tbody tr:hover td { background: #f0f6fd; }
.zf-table tbody tr:hover td.row-head { background: #e8f0fa; }

.sec-notes {
  list-style: none; padding: 0; margin: 10px 0 0;
  font-size: 13px; color: #64748b;
}
.sec-notes li {
  padding: 4px 0 4px 22px; position: relative; line-height: 1.7;
}
.sec-notes li::before {
  content: '·'; position: absolute; left: 10px;
  color: var(--color-primary); font-weight: 700; font-size: 18px;
  top: -2px;
}

.footnote {
  font-size: 14px; color: #b45309; background: #fffbeb;
  border: 1px dashed #fbbf24; border-radius: 6px;
  padding: 12px 18px; margin: 24px 0 0; letter-spacing: 1px;
}

.notes-section {
  margin-top: 40px; padding-top: 24px; border-top: 1px dashed #e5e7eb;
}
.notes-section h3 { font-size: 16px; margin: 0 0 12px; color: #111827; }
.notes-list { padding: 0; list-style: none; }
.notes-list li {
  padding: 6px 0 6px 22px; position: relative;
  font-size: 14px; color: #475569; line-height: 1.75;
}
.notes-list li::before {
  content: '✓'; position: absolute; left: 0;
  color: var(--color-primary, #163B6B); font-weight: 700;
}
.notes-list :deep(strong) { color: var(--color-primary, #163B6B); }

.cta-section {
  margin-top: 36px; padding: 30px;
  background: linear-gradient(135deg, #f0f6fd, #e8f0fa);
  border-radius: 8px; text-align: center;
}
.cta-section h3 { font-size: 20px; color: #111827; margin: 0 0 16px; }
.cta-buttons { display: flex; gap: 16px; justify-content: center; }

/* 侧栏 */
.zf-side { position: sticky; top: 20px; display: flex; flex-direction: column; gap: 16px; }
.side-card { background: #fff; border-radius: 10px; padding: 20px 22px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.04); }
.side-title { font-size: 15px; font-weight: 700; color: #111827; margin-bottom: 14px; padding-bottom: 10px; border-bottom: 1px solid #f1f5f9; }
.side-title.light { color: #fff; border-bottom-color: rgba(255,255,255,0.15); }
.side-link { display: flex; align-items: center; gap: 4px; font-size: 14px; color: #475569; padding: 8px 0; text-decoration: none; transition: color 0.2s; }
.side-link:hover { color: var(--color-primary); }
.side-link .arr { color: #cbd5e1; transition: color 0.2s; }
.side-link:hover .arr { color: var(--color-primary); }

.side-card--blue { background: var(--color-primary, #163B6B); color: #fff; text-align: center; }
.side-phone { font-family: Georgia, serif; font-size: 22px; font-weight: 700; letter-spacing: 1px; margin: 10px 0 4px; }
.side-time { font-size: 12px; color: rgba(255, 255, 255, 0.65); margin-bottom: 14px; }
.side-btn { display: inline-block; padding: 8px 22px; background: rgba(255, 255, 255, 0.15); color: #fff; border-radius: 20px; font-size: 13px; text-decoration: none; transition: background 0.2s; }
.side-btn:hover { background: rgba(255, 255, 255, 0.25); }

@media (max-width: 768px) {
  .zf-body { grid-template-columns: 1fr; }
  .zf-main { padding: 24px 18px; }
  .zf-hero h1 { font-size: 22px; }
  .cta-buttons { flex-direction: column; }
  .zf-side { position: static; }
}

.brokerage-card {
  background: linear-gradient(155deg, #fff 0%, var(--color-primary-lighter) 100%);
  border: 1px solid var(--color-border-light);
  border-radius: 14px;
  padding: 28px 32px 30px;
  margin-top: 28px;
  position: relative;
  overflow: hidden;
}
.brokerage-card::before {
  content: "";
  position: absolute;
  inset: 0 0 auto 0;
  height: 4px;
  background: linear-gradient(to right, var(--color-primary), var(--color-accent));
}
.bk-title {
  font-size: 22px; font-weight: 700; letter-spacing: 4px;
  color: var(--color-primary-dark); margin: 0 0 12px;
}
.bk-lead {
  font-size: 14px; color: var(--color-text-secondary);
  line-height: 1.85; margin: 0 0 18px;
}
.bk-lead strong { color: var(--color-primary-dark); font-weight: 600; }
.bk-list { list-style: none; padding: 0; margin: 0; display: grid; gap: 14px; }
.bk-list li {
  display: grid; grid-template-columns: 92px 1fr; align-items: start; gap: 14px;
  padding: 12px 14px; background: #fff; border-radius: 8px;
  border-left: 3px solid var(--color-primary);
  font-size: 14px; line-height: 1.75; color: var(--color-text);
}
.bk-list li strong { color: var(--color-accent-dark); font-weight: 700; }
.bk-tag {
  display: inline-block; padding: 4px 10px;
  font-size: 12px; letter-spacing: 1px; font-weight: 600;
  border-radius: 4px; text-align: center; align-self: start;
}
.tag-parent { background: var(--color-primary); color: #fff; }
.tag-tutor { background: var(--color-accent); color: #fff; }
.tag-once { background: rgba(46, 125, 50, 0.12); color: var(--color-success); }
.tag-stay { background: rgba(198, 40, 40, 0.10); color: var(--color-error); }
.tag-swap { background: rgba(2, 136, 209, 0.12); color: var(--color-info, #0277bd); }
.tag-flow { background: var(--color-text-muted); color: #fff; }
</style>
