<template>
  <el-dialog :model-value="visible" title="审核操作" width="500px" @close="$emit('update:visible', false)">
    <el-form label-width="80px">
      <el-form-item label="审核结果"><el-radio-group v-model="action"><el-radio label="approve">通过</el-radio><el-radio label="reject">拒绝</el-radio></el-radio-group></el-form-item>
      <el-form-item label="备注"><el-input v-model="remark" type="textarea" :rows="3" placeholder="请输入审核备注" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('update:visible', false)">取消</el-button>
      <el-button type="primary" @click="$emit('confirm', { action, remark })">确认</el-button>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref } from 'vue'
defineProps<{ visible: boolean }>()
defineEmits(['update:visible', 'confirm'])
const action = ref('approve')
const remark = ref('')
</script>
