<template>
<div class="tg-page">
  <Head><Title>接单群 - 591家教网</Title></Head>

  <div class="tg-card">
    <div class="tg-header">
      <div class="tg-eyebrow">591家教网 · 教员通道</div>
      <h2 class="tg-title">接单群</h2>
      <p class="tg-sub">扫码加入下方微信群，第一时间接收上海家长的真实订单。</p>
    </div>

    <div class="tg-qr-wrap">
      <div class="tg-qr-frame" v-if="qrUrl">
        <div class="tg-corner tl"></div>
        <div class="tg-corner tr"></div>
        <div class="tg-corner bl"></div>
        <div class="tg-corner br"></div>
        <img :src="qrUrl" alt="接单群微信二维码" class="tg-qr-img" />
      </div>
      <div class="tg-qr-empty" v-else>
        <div class="tg-empty-icon">
          <svg viewBox="0 0 64 64" width="56" height="56" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="6" y="6" width="22" height="22" rx="2"/>
            <rect x="36" y="6" width="22" height="22" rx="2"/>
            <rect x="6" y="36" width="22" height="22" rx="2"/>
            <rect x="14" y="14" width="6" height="6" fill="currentColor"/>
            <rect x="44" y="14" width="6" height="6" fill="currentColor"/>
            <rect x="14" y="44" width="6" height="6" fill="currentColor"/>
            <rect x="38" y="38" width="6" height="6"/>
            <rect x="50" y="38" width="6" height="6"/>
            <rect x="38" y="50" width="6" height="6"/>
            <rect x="50" y="50" width="6" height="6"/>
          </svg>
        </div>
        <div class="tg-empty-title">二维码暂未发布</div>
        <div class="tg-empty-desc">请稍后再来，或直接联系客服 {{ hotline }} 加入。</div>
      </div>

      <div class="tg-qr-caption">
        <span class="tg-tag">微信扫一扫</span>
        加入「591家教网 · 上海教员接单群」
      </div>
    </div>

    <div class="tg-tips">
      <h4 class="tg-tips-title">入群须知</h4>
      <ol class="tg-tips-list">
        <li>群内仅发布<strong>已审核</strong>的家长订单，不发广告。</li>
        <li>看到合适订单可在群内回复或在站内"申请"，群内按客服指引报名。</li>
        <li>请<strong>实名修改群昵称</strong>为「姓+科目+教龄」，例：李数学5年。</li>
        <li>禁止在群内私加家长、绕过平台联系，违规将被移出并下线账号。</li>
        <li>群二维码定期更新，过期请回到本页重新扫码。</li>
      </ol>
    </div>
  </div>
</div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '~/stores/user'
import { useSiteConfig } from '~/composables/useSiteConfig'

definePageMeta({ layout: 'center', middleware: 'auth' })

const userStore = useUserStore()
const router = useRouter()
const { config, load: loadSiteConfig } = useSiteConfig()
const { get } = useApi()

// 直接访问 URL 时也要拦非教员
if (process.client && userStore.isLoggedIn && !userStore.isTutor) {
  ElMessage.warning('您不是教员，无法进入')
  router.replace('/center')
}

const qrUrl = ref('')
const hotline = computed(() => config.value.siteHotline || '13795420591')

const loadQr = async () => {
  if (!userStore.isTutor) return
  try {
    const res = await get('/user/auth/tutor-group/qr')
    if (res?.code === 200) qrUrl.value = res.data?.qrUrl || ''
  } catch (e) { /* anon/student 应该已经被前置 redirect 拦下来, 兜底无需提示 */ }
}

onMounted(() => { loadSiteConfig(); loadQr() })
</script>

<style scoped>
.tg-page { padding: 8px 0 32px; }

.tg-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border-light);
  border-radius: 14px;
  padding: 36px 40px;
  box-shadow: var(--shadow-md);
}

.tg-header { text-align: center; margin-bottom: 28px; }
.tg-eyebrow {
  display: inline-block;
  font-size: 12px;
  letter-spacing: 4px;
  color: var(--color-primary);
  background: var(--color-primary-lighter);
  padding: 4px 12px;
  border-radius: 999px;
  margin-bottom: 14px;
}
.tg-title {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-text);
  letter-spacing: 4px;
  margin: 0 0 8px;
}
.tg-sub {
  font-size: 14px;
  color: var(--color-text-secondary);
  margin: 0;
  letter-spacing: 0.5px;
}

.tg-qr-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 24px 0 32px;
}

.tg-qr-frame {
  position: relative;
  width: 280px;
  height: 280px;
  padding: 14px;
  background: linear-gradient(135deg, #fff 0%, #f8fafc 100%);
  border: 1px solid var(--color-border);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 12px 28px -16px rgba(31, 78, 140, 0.35);
}
.tg-qr-img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 8px;
}
.tg-corner {
  position: absolute;
  width: 22px; height: 22px;
  border: 3px solid var(--color-accent);
}
.tg-corner.tl { top: -2px; left: -2px; border-right: none; border-bottom: none; border-radius: 8px 0 0 0; }
.tg-corner.tr { top: -2px; right: -2px; border-left: none; border-bottom: none; border-radius: 0 8px 0 0; }
.tg-corner.bl { bottom: -2px; left: -2px; border-right: none; border-top: none; border-radius: 0 0 0 8px; }
.tg-corner.br { bottom: -2px; right: -2px; border-left: none; border-top: none; border-radius: 0 0 8px 0; }

.tg-qr-empty {
  width: 280px; height: 280px;
  background: var(--color-bg);
  border: 2px dashed var(--color-border);
  border-radius: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: var(--color-text-muted);
}
.tg-empty-icon { color: var(--color-border); }
.tg-empty-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-text-secondary);
  letter-spacing: 1px;
}
.tg-empty-desc {
  font-size: 12.5px;
  color: var(--color-text-muted);
  text-align: center;
  padding: 0 30px;
  line-height: 1.6;
}

.tg-qr-caption {
  margin-top: 18px;
  font-size: 13.5px;
  color: var(--color-text-secondary);
  display: flex;
  align-items: center;
  gap: 8px;
}
.tg-tag {
  background: var(--color-primary);
  color: #fff;
  font-size: 11px;
  letter-spacing: 1px;
  padding: 3px 8px;
  border-radius: 4px;
  font-weight: 600;
}

.tg-tips {
  background: var(--color-primary-lighter);
  border-radius: 10px;
  padding: 18px 24px;
  border-left: 3px solid var(--color-primary);
}
.tg-tips-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-primary-dark);
  margin: 0 0 10px;
  letter-spacing: 1px;
}
.tg-tips-list {
  margin: 0;
  padding-left: 20px;
  font-size: 13px;
  color: var(--color-text-secondary);
  line-height: 2;
}
.tg-tips-list strong {
  color: var(--color-primary-dark);
  font-weight: 600;
}

@media (max-width: 560px) {
  .tg-card { padding: 24px 18px; }
  .tg-qr-frame, .tg-qr-empty { width: 240px; height: 240px; }
}
</style>
