<template>
  <div class="panel-card">
    <div class="panel-title">Moran's I 空间自相关</div>
    <el-button size="small" type="primary" @click="loadData" :loading="loading">查询分析结果</el-button>
    <div v-if="result" class="moran-result">
      <div class="global-stats">
        <div class="stat-item"><span class="label">Global Moran's I</span><span class="val">{{ result.globalMoranI ?? result.globalMorani }}</span></div>
        <div class="stat-item"><span class="label">Z-Score</span><span class="val">{{ result.zScore ?? result.zscore }}</span></div>
        <div class="stat-item"><span class="label">P-Value</span><span class="val">{{ result.pValue ?? result.pvalue }}</span></div>
      </div>
      <div class="lisa-summary">
        <div class="label">LISA 聚类分布</div>
        <div class="summary-row" v-for="t in lisaSummary" :key="t.type">
          <span class="dot" :style="{ background: t.color }"></span>
          <span>{{ t.label }}</span>
          <span class="count">{{ t.count }} 个城市</span>
        </div>
      </div>
    </div>
    <div v-if="!result && !loading" class="empty-tip">暂无该时间段的分析结果</div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useAppStore } from '@/stores/app'
import { getMoran } from '@/api/analysis'
import { getLisaColor } from '@/utils/colorScale'

const store = useAppStore()
const result = ref(null)
const loading = ref(false)

const lisaSummary = computed(() => {
  if (!result.value?.lisaItems) return []
  const map = {}
  const items = result.value.lisaItems || result.value.lisaitems || []
  items.forEach(item => {
    map[item.clusterType || item.clustertype] = (map[item.clusterType || item.clustertype || 0] || 0) + 1
  })
  const labels = { HH: '高高聚类', LL: '低低聚类', HL: '高低异常', LH: '低高异常', NS: '不显著' }
  return Object.entries(map).map(([type, count]) => ({
    type, count, color: getLisaColor(type), label: labels[type] || type
  }))
})

async function tryGetMoran(year, month) {
  try {
    return await getMoran(year, month)
  } catch {
    return null
  }
}

async function loadData() {
  loading.value = true
  try {
    result.value = await tryGetMoran(store.currentYear, store.currentMonth)
    if (result.value) {
      store.setMapMode('moran')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.panel-card {
  animation: float-in 0.4s ease-out;
  .panel-title {
    font-family: var(--font-display); font-size: 11px; letter-spacing: 2px;
    color: var(--magenta); margin-bottom: 14px;
    &::before { content: '🔬 '; font-size: 14px; }
  }
  .empty-tip {
    color: var(--text-muted); font-size: 13px; text-align: center; padding: 30px 0;
  }
  .moran-result { margin-top: 14px; }
  .global-stats {
    display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; margin-bottom: 16px;
    .stat-item {
      background: var(--bg-card); border: 1px solid var(--border-dim);
      border-radius: var(--radius-sm); padding: 10px; text-align: center;
      transition: all 0.25s;
      &:hover { border-color: var(--border-glow); }
      .label { font-size: 9px; color: var(--text-muted); display: block; text-transform: uppercase; letter-spacing: 1px; }
      .val {
        font-family: var(--font-mono); font-size: 15px; font-weight: 500;
        display: block; margin-top: 6px; color: var(--text-bright);
      }
    }
  }
  .lisa-summary {
    .label {
      font-family: var(--font-display); font-size: 10px; letter-spacing: 2px;
      color: var(--text-muted); margin-bottom: 10px;
    }
    .summary-row {
      display: flex; align-items: center; gap: 10px; padding: 6px 0; font-size: 13px;
      border-bottom: 1px solid rgba(80,100,160,0.08);
      .dot {
        width: 14px; height: 14px; border-radius: 4px; flex-shrink: 0;
        box-shadow: 0 0 8px currentColor;
      }
      .count {
        margin-left: auto; font-family: var(--font-mono); font-size: 12px;
        color: var(--text-muted);
      }
    }
  }
}
</style>
