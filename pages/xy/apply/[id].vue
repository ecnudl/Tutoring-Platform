<template>
<div class="ap-page">
  <Head>
    <Title>申请订单 S{{ displayNoNum }} - 591家教网</Title>
    <Meta name="description" content="教员在线申请家教订单。" />
  </Head>

  <div class="container ap-crumb">
    <NuxtLink to="/">首页</NuxtLink>
    <span class="sep">›</span>
    <NuxtLink to="/xy">学员库</NuxtLink>
    <span class="sep">›</span>
    <NuxtLink :to="`/xy/s${displayNoNum}`">S{{ displayNoNum }}</NuxtLink>
    <span class="sep">›</span>
    <span class="cur">申请订单</span>
  </div>

  <div class="container ap-wrap">

    <header class="ap-head">
      <div class="ap-eyebrow">
        <span class="ap-rule"></span>591 家教 · 在线申请
      </div>
      <h1 class="ap-h">申请订单 S{{ displayNoNum }}</h1>
      <p class="ap-lead">提交您的优势说明，客服会根据"先到先得，择优推荐"原则审核，1-2 个工作日内致电与您匹配。</p>
    </header>

    <div class="ap-grid">
      <!-- 表单 -->
      <section class="ap-form-card">
        <div class="ap-form">
          <label class="ap-label">您接这单的优势 <span class="req">*</span></label>
          <textarea
            v-model="text"
            class="ap-textarea"
            rows="8"
            maxlength="1000"
            placeholder="请简短说明您的教学水平、相关经验，并确认时间、地址、报酬都可接受。"
          ></textarea>
          <div class="ap-counter">{{ text.length }} / 1000</div>

          <label class="ap-label">联系手机号 <span class="req">*</span></label>
          <input v-model="mobile" class="ap-input" maxlength="11" placeholder="11 位手机号, 客服会拨打此号码与您联系" />
          <div v-if="mobile && !isValidMobile" class="ap-hint err">请输入正确的 11 位手机号</div>
          <div v-else-if="prefilled && mobile === userStore.mobile" class="ap-hint">已自动填入您的注册手机号, 如需更改请直接编辑</div>

          <label class="ap-label">微信号（选填）</label>
          <input v-model="wx" class="ap-input" maxlength="50" placeholder="方便客服快速联系" />

          <button class="ap-submit" :disabled="!canSubmit || submitting" @click="submit">
            <span v-if="submitting">提交中...</span>
            <span v-else>提交申请</span>
          </button>
        </div>
      </section>

      <!-- 注意事项 -->
      <aside class="ap-tips-card">
        <div class="tips-eyebrow">特别提醒</div>
        <p class="tips-lead"><strong>591家教网</strong>将以"<em>先到先得、择优推荐</em>"原则受理申请（受理 ≠ 确定接单）。</p>

        <ul class="tips-block">
          <li><strong>A. 确认路程时间：</strong>请明确接单的路程距离与可上课时段。接单后因路程或时间不符不能执教的，中介费将不予退还。</li>
          <li><strong>B. 确认教学经验：</strong>请如实评估自身相关教学经验。家长反馈无家教经验的，平台将扣除部分中介费。</li>
          <li><strong>C. 预约超时：</strong>预约后超过约定时间未办理接单手续，平台将暂停为您介绍订单。</li>
        </ul>

        <ol class="tips-list">
          <li>请认真核对学员要求（专业 / 性别 / 时间 / 地点 / 报酬）后再申请，并在说明中针对自身优势<em>简短说明</em>。</li>
          <li>承诺向 591 家教网提供真实有效证件（身份证 / 学生证 / 教师资格证 / 工作证等）。<em>提供虚假材料</em>由教员承担一切后果。</li>
          <li>学员若以任何理由让您<em>先支付押金 / 借款</em>，请立即提高警惕，第一时间联系客服。</li>
          <li>请提前与家长说明：<em>课时费一次一结</em>。</li>
          <li><em>禁止</em>让他人代课。代课纠纷由当事教员自行承担。</li>
          <li><em>禁止</em>向学员或家长推销任何商品。由此产生的纠纷由教员承担全部责任。</li>
        </ol>
      </aside>
    </div>
  </div>
</div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '~/stores/user'

const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { post, get } = useApi()

const displayNo = route.params.id || route.params.displayNo
const displayNoNum = computed(() => String(displayNo || '').replace(/^S/i, ''))

const text = ref('')
// 自动 pre-fill 登录教员的手机号 (大多数情况这就是教员的联系方式), 教员可手动改
const mobile = ref(userStore.mobile || '')
const prefilled = ref(!!userStore.mobile)
const wx = ref('')
const submitting = ref(false)
const requirementId = ref(null)

const isValidMobile = computed(() => /^1[3-9]\d{9}$/.test(mobile.value.trim()))
const canSubmit = computed(() => text.value.trim().length >= 10 && isValidMobile.value)

// 教员鉴权
if (process.client) {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录教员账号')
    router.replace('/login?redirect=' + encodeURIComponent(`/xy/apply/S${displayNoNum.value}`))
  } else if (!userStore.isTutor) {
    ElMessage.warning('该入口仅教员可用')
    router.replace('/xy')
  }
}

const loadReqId = async () => {
  try {
    const r = await get('/user/api/requirement/view', { displayNo: 'S' + displayNoNum.value })
    if (r.code === 200 && r.data) {
      requirementId.value = r.data.id
    } else {
      ElMessage.error(r.msg || '需求不存在')
      router.replace('/xy')
    }
  } catch (e) {
    ElMessage.error('加载失败，请稍后重试')
  }
}

const submit = async () => {
  if (!text.value.trim() || text.value.trim().length < 10) {
    ElMessage.warning('请填写至少 10 字的优势说明')
    return
  }
  if (!isValidMobile.value) {
    ElMessage.warning('请填写正确的 11 位手机号')
    return
  }
  if (submitting.value) return
  if (!requirementId.value) {
    ElMessage.warning('需求加载中，请稍候')
    return
  }

  let message = text.value.trim()
  if (wx.value && wx.value.trim()) {
    message += `　【微信号：${wx.value.trim()}】`
  }

  submitting.value = true
  try {
    const r = await post('/user/auth/application/apply', {
      requirementId: requirementId.value,
      applyMessage: message,
      mobile: mobile.value.trim()
    })
    if (r.code === 200) {
      await ElMessageBox.alert(
        '<div style="font-size:15px;line-height:1.7"><p style="color:#d97706;font-weight:600">您的申请已收到</p><p>请保持电话畅通，若条件匹配，客服会在 1-2 个工作日内与您联系。</p><p>可在「<strong>个人中心 → 我的申请</strong>」查看进度。</p></div>',
        '申请成功',
        { dangerouslyUseHTMLString: true, confirmButtonText: '我知道了' }
      ).catch(() => {})
      router.push('/center/applications')
    } else {
      ElMessage.error(r.msg || '申请失败')
    }
  } catch (e) {
    ElMessage.error('提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  if (process.client && userStore.isLoggedIn && userStore.isTutor) {
    loadReqId()
  }
})
</script>

<style scoped>
.ap-page { background: var(--color-bg, #f5f6f8); min-height: 80vh; padding-bottom: 80px; }

/* breadcrumb */
.ap-crumb { padding-top: 18px; font-size: 13px; color: #64748b; }
.ap-crumb a { color: #64748b; text-decoration: none; transition: color .15s; }
.ap-crumb a:hover { color: var(--color-primary, #163B6B); }
.ap-crumb .sep { margin: 0 6px; color: #cbd5e1; }
.ap-crumb .cur { color: var(--color-primary, #163B6B); font-weight: 600; }

.ap-wrap { max-width: var(--content-width, 1200px); margin: 20px auto 0; padding: 0 20px; }

/* head */
.ap-head {
  background: #fff;
  border-radius: 12px;
  padding: 30px 40px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
  position: relative;
  overflow: hidden;
}
.ap-head::before {
  content: '';
  position: absolute; top: 0; left: 0; right: 0; height: 3px;
  background: linear-gradient(to right, var(--color-primary, #163B6B), #264e82, #d97706);
}
.ap-eyebrow {
  display: flex; align-items: center; gap: 12px;
  font-size: 12px; letter-spacing: 4px; font-weight: 600;
  color: var(--color-primary, #163B6B);
  margin-bottom: 12px;
}
.ap-rule {
  width: 30px; height: 2px; background: var(--color-primary, #163B6B);
}
.ap-h {
  font-size: 24px; font-weight: 700; letter-spacing: 4px;
  color: var(--color-primary, #163B6B);
  margin: 0 0 12px;
}
.ap-lead {
  font-size: 13.5px; color: #475569; line-height: 1.8; margin: 0;
  max-width: 760px;
}

/* grid */
.ap-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 380px;
  gap: 20px;
  align-items: start;
}

/* form card */
.ap-form-card {
  background: #fff;
  border-radius: 12px;
  padding: 28px 32px 32px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}
.ap-form { display: flex; flex-direction: column; gap: 8px; }
.ap-label {
  font-size: 13.5px; font-weight: 600; color: #111827;
  letter-spacing: 0.5px; margin-top: 14px;
}
.ap-label:first-child { margin-top: 0; }
.ap-label .req { color: #d97706; margin-left: 4px; }

.ap-textarea, .ap-input {
  width: 100%; box-sizing: border-box;
  padding: 12px 14px;
  font-size: 14px; line-height: 1.7; color: #111827;
  background: #f8fafc;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  transition: border-color .15s, background .15s;
  font-family: inherit;
  resize: vertical;
  min-height: 0;
}
.ap-textarea:focus, .ap-input:focus {
  outline: none;
  border-color: var(--color-primary, #163B6B);
  background: #fff;
}
.ap-counter {
  text-align: right; font-size: 12px; color: #94a3b8;
  margin-top: 4px; letter-spacing: 0.3px;
}
.ap-hint { font-size: 12px; margin-top: 6px; color: #94a3b8; }
.ap-hint.err { color: #dc2626; }
.ap-submit {
  margin-top: 24px;
  background: var(--color-primary, #163B6B);
  color: #fff;
  border: none; border-radius: 8px;
  padding: 14px 0;
  font-size: 15px; font-weight: 600; letter-spacing: 6px;
  cursor: pointer;
  transition: background .15s, transform .15s, box-shadow .15s;
  box-shadow: 0 6px 20px -10px rgba(31, 78, 140, 0.55);
}
.ap-submit:hover:not(:disabled) {
  background: #264e82;
  transform: translateY(-1px);
  box-shadow: 0 12px 30px -12px rgba(31, 78, 140, 0.65);
}
.ap-submit:disabled {
  background: #cbd5e1;
  cursor: not-allowed;
  box-shadow: none;
}

/* tips card */
.ap-tips-card {
  background: #fff;
  border-radius: 12px;
  padding: 26px 28px 28px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  border-left: 3px solid #d97706;
}
.tips-eyebrow {
  font-size: 12px; letter-spacing: 4px; font-weight: 700;
  color: #d97706;
  margin-bottom: 12px;
}
.tips-lead {
  font-size: 13px; color: #475569; line-height: 1.75;
  margin: 0 0 16px;
  padding-bottom: 14px; border-bottom: 1px dashed #e5e7eb;
}
.tips-lead strong { color: #111827; font-weight: 600; }
.tips-lead em { color: #d97706; font-style: normal; font-weight: 600; }

.tips-block {
  list-style: none; padding: 0; margin: 0 0 14px;
  display: grid; gap: 10px;
}
.tips-block li {
  font-size: 13px; color: #475569; line-height: 1.7;
  padding: 10px 12px;
  background: #f8fafc; border-radius: 4px;
  border-left: 2px solid var(--color-primary, #163B6B);
}
.tips-block strong { color: #111827; font-weight: 600; margin-right: 4px; }

.tips-list {
  list-style: decimal; padding-left: 20px; margin: 0;
  font-size: 12.5px; color: #64748b; line-height: 1.85;
}
.tips-list li { padding: 2px 0; }
.tips-list em { color: #d97706; font-style: normal; font-weight: 600; }

/* responsive */
@media (max-width: 1024px) {
  .ap-grid { grid-template-columns: 1fr; }
}
@media (max-width: 768px) {
  .ap-head { padding: 22px 20px; }
  .ap-h { font-size: 20px; letter-spacing: 2px; }
  .ap-form-card, .ap-tips-card { padding: 22px 18px; }
  .ap-submit { letter-spacing: 4px; }
}
</style>
