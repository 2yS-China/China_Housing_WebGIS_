<template>
  <div class="panel-card">
    <div class="panel-title">多维筛选与导出</div>
    <el-form label-position="top" size="small">
      <el-form-item label="省份">
        <el-select v-model="form.provinces" multiple filterable collapse-tags placeholder="不限" style="width:100%">
          <el-option v-for="p in provinceOptions" :key="p" :label="p" :value="p" />
        </el-select>
      </el-form-item>
      <el-form-item label="时间">
        <div style="display:flex;gap:8px;align-items:center">
          <el-date-picker v-model="startDate" type="month" placeholder="起始" value-format="YYYY-MM" style="width:120px" />
          <span>→</span>
          <el-date-picker v-model="endDate" type="month" placeholder="结束" value-format="YYYY-MM" style="width:120px" />
        </div>
      </el-form-item>
      <el-form-item label="房价区间 (元/㎡)">
        <div style="display:flex;gap:8px">
          <el-input-number v-model="form.minPrice" :min="0" controls-position="right" placeholder="最低" style="width:50%" />
          <el-input-number v-model="form.maxPrice" :min="0" controls-position="right" placeholder="最高" style="width:50%" />
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onFilter" style="width:100%">筛选</el-button>
      </el-form-item>
    </el-form>

    <div v-if="resultCount !== null" class="result-info">
      筛选结果：<b>{{ resultCount }}</b> 个城市
      <el-button size="small" type="success" @click="onExport" style="margin-left:10px">导出Excel</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getProvinceList } from '@/api/province'
import { filterCities } from '@/api/city'
import { exportExcel } from '@/api/exportApi'

const provinceOptions = ref([])
const resultCount = ref(null)

const form = reactive({
  provinces: [],
  minPrice: null,
  maxPrice: null
})
const startDate = ref(null)
const endDate = ref(null)

onMounted(async () => {
  const list = await getProvinceList()
  provinceOptions.value = list.map(p => p.provinceName)
})

function buildParam() {
  const param = { ...form }
  if (startDate.value) {
    const [y, m] = startDate.value.split('-').map(Number)
    param.startYear = y; param.startMonth = m
  }
  if (endDate.value) {
    const [y, m] = endDate.value.split('-').map(Number)
    param.endYear = y; param.endMonth = m
  }
  return param
}

async function onFilter() {
  const param = buildParam()
  const data = await filterCities(param)
  resultCount.value = data.length
}

async function onExport() {
  const param = buildParam()
  await exportExcel(param)
}
</script>

<style scoped lang="scss">
.panel-card {
  animation: float-in 0.4s ease-out;
  .panel-title {
    font-family: var(--font-display); font-size: 11px; letter-spacing: 2px;
    color: var(--green); margin-bottom: 14px;
    &::before { content: '📥 '; font-size: 14px; }
  }
  .result-info {
    margin-top: 14px; padding: 12px;
    background: var(--bg-card); border: 1px solid var(--border-dim);
    border-radius: var(--radius-sm); font-size: 13px;
    animation: data-pulse 2s ease-in-out infinite;
    b { color: var(--cyan); font-family: var(--font-mono); font-size: 18px; }
  }
}
</style>
