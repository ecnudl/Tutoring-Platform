<template>
  <div class="contact-page">
    <h2 class="page-title">联系客服</h2>
    <el-card class="contact-card">
      <div class="contact-header">
        <el-icon :size="48" color="#409eff"><Service /></el-icon>
        <h3>我们随时为您服务</h3>
        <p>如有任何疑问，请通过以下方式联系我们的客服团队</p>
      </div>

      <el-divider />

      <div class="contact-grid">
        <div class="contact-item">
          <el-icon :size="28" color="#409eff"><Phone /></el-icon>
          <div>
            <h4>客服电话</h4>
            <p class="contact-value">400-888-0000</p>
            <p class="contact-note">工作日 9:00 - 18:00</p>
          </div>
        </div>

        <div class="contact-item">
          <el-icon :size="28" color="#67c23a"><ChatDotRound /></el-icon>
          <div>
            <h4>微信客服</h4>
            <p class="contact-value">jiajiao_kefu</p>
            <p class="contact-note">扫码或搜索微信号添加</p>
          </div>
        </div>

        <div class="contact-item">
          <el-icon :size="28" color="#e6a23c"><Message /></el-icon>
          <div>
            <h4>客服邮箱</h4>
            <p class="contact-value">kefu@jiajiao.com</p>
            <p class="contact-note">24小时内回复</p>
          </div>
        </div>

        <div class="contact-item">
          <el-icon :size="28" color="#f56c6c"><Clock /></el-icon>
          <div>
            <h4>服务时间</h4>
            <p class="contact-value">周一至周日</p>
            <p class="contact-note">9:00 - 21:00（节假日正常服务）</p>
          </div>
        </div>
      </div>

      <el-divider />

      <div class="faq-section">
        <h3>常见问题</h3>
        <el-collapse>
          <el-collapse-item title="如何成为平台教员？" name="1">
            注册时选择"教员"身份，完善教员资料并提交审核。审核通过后即可在教员库中展示，并申请学员需求。
          </el-collapse-item>
          <el-collapse-item title="如何发布找老师需求？" name="2">
            登录后进入个人中心，点击"发布需求"，填写科目、年级、上课时间等信息提交。平台审核通过后，教员即可看到并申请。
          </el-collapse-item>
          <el-collapse-item title="如何开通VIP？" name="3">
            目前VIP开通请联系客服人工处理。VIP会员可享受每日不限次数申请需求等专属权益。
          </el-collapse-item>
          <el-collapse-item title="预约教员后多久会有回复？" name="4">
            教员预约后，平台客服将在24小时内致电确认详情。如未收到回复，请通过上方联系方式咨询。
          </el-collapse-item>
          <el-collapse-item title="如何修改个人资料？" name="5">
            登录后进入个人中心，点击"个人资料"或"教员资料"即可修改。修改教员资料后需重新提交审核。
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-card>
  </div>
</template>
<script setup>
definePageMeta({
  layout: 'center',
  middleware: 'auth'
})

import { Phone, ChatDotRound, Message, Clock, Service } from '@element-plus/icons-vue'
</script>
<style scoped>
.contact-page { max-width: 700px; margin: 0 auto; }
.page-title { font-size: 20px; font-weight: 600; margin-bottom: 16px; }
.contact-card { padding: 8px; }
.contact-header { text-align: center; padding: 20px 0 10px; }
.contact-header h3 { font-size: 20px; margin: 12px 0 8px; }
.contact-header p { color: #666; font-size: 14px; }
.contact-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; }
.contact-item { display: flex; gap: 16px; align-items: flex-start; padding: 16px; background: #f9f9fb; border-radius: 8px; }
.contact-item h4 { margin: 0 0 4px; font-size: 15px; }
.contact-value { font-size: 16px; font-weight: 600; color: #333; margin: 0 0 2px; }
.contact-note { font-size: 12px; color: #999; margin: 0; }
.faq-section h3 { font-size: 18px; margin-bottom: 12px; }
@media (max-width: 768px) {
  .contact-grid { grid-template-columns: 1fr; gap: 12px; }
  .contact-item { padding: 12px; }
}
</style>
