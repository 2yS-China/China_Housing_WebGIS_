<template>
  <div class="panel-card">
    <div class="panel-title">省份房价统计</div>
    <el-select v-model="selected" filterable placeholder="选择省份" size="small" @change="loadData">
      <el-option v-for="p in provinces" :key="p.provinceName" :label="p.provinceName" :value="p.provinceName" />
    </el-select>
    <div v-if="stats" class="stats-grid">
      <div class="stat-item"><span class="label">城市数</span><span class="val">{{ stats.cityCount }}</span></div>
      <div class="stat-item"><span class="label">均价</span><span class="val price">{{ stats.avgPrice }}</span></div>
      <div class="stat-item"><span class="label">最低价</span><span class="val">{{ stats.minPrice }}</span></div>
      <div class="stat-item"><span class="label">最高价</span><span class="val">{{ stats.maxPrice }}</span></div>
      <div class="stat-item"><span class="label">中位数</span><span class="val">{{ stats.medianPrice }}</span></div>
      <div class="stat-item"><span class="label">标准差</span><span class="val">{{ stats.stddevPrice }}</span></div>
    </div>
    <v-chart v-if="barOption.series" :option="barOption" autoresize style="height:260px;margin-top:10px" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { useAppStore } from '@/stores/app'
import { getProvinceList, getProvinceStats } from '@/api/province'
import { getChoropleth } from '@/api/map'

use([BarChart, GridComponent, TooltipComponent, CanvasRenderer])

const store = useAppStore()
const provinces = ref([])
const selected = ref('')
const stats = ref(null)
const barOption = ref({})

onMounted(async () => {
  provinces.value = await getProvinceList()
})

async function loadData() {
  if (!selected.value) return
  stats.value = await getProvinceStats(selected.value, store.currentYear, store.currentMonth)

  const allData = await getChoropleth(store.currentYear, store.currentMonth)
  const provCities = allData.filter(d => d.provinceName === selected.value).sort((a, b) => b.price - a.price)

  barOption.value = {
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(10,14,39,0.9)', borderColor: 'rgba(0,240,255,0.3)', textStyle: { color: '#c0c8e8', fontSize: 12 } },
    grid: { top: 10, right: 10, bottom: 30, left: 60 },
    xAxis: { type: 'value', axisLabel: { fontSize: 10, color: '#5a6590', fontFamily: 'JetBrains Mono' }, splitLine: { lineStyle: { color: 'rgba(80,100,160,0.1)' } } },
    yAxis: { type: 'category', data: provCities.map(c => c.cityName).reverse(), axisLabel: { fontSize: 11, color: '#c0c8e8' } },
    series: [{ type: 'bar', data: provCities.map(c => c.price).reverse(), barMaxWidth: 18,
      itemStyle: { color: { type: 'linear', x: 0, y: 0, x2: 1, y2: 0, colorStops: [{ offset: 0, color: 'rgba(0,240,255,0.6)' }, { offset: 1, color: 'rgba(255,45,120,0.6)' }] } }
    }]
  }
}
</script>

<style scoped lang="scss">
.panel-card {
  animation: float-in 0.4s ease-out;
  .panel-title {
    font-family: var(--font-display); font-size: 11px; letter-spacing: 2px;
    color: var(--cyan); margin-bottom: 14px;
    &::before { content: '🗺️ '; font-size: 14px; }
  }
  .stats-grid {
    display: grid; grid-template-columns: repeat(2, 1fr); gap: 8px; margin-top: 14px;
    .stat-item {
      background: var(--bg-card); border: 1px solid var(--border-dim);
      border-radius: var(--radius-sm); padding: 12px;
      display: flex; flex-direction: column; gap: 4px;
      transition: all 0.25s;
      &:hover { border-color: var(--border-glow); box-shadow: 0 0 12px rgba(0,240,255,0.08); }
      .label { font-size: 10px; color: var(--text-muted); text-transform: uppercase; letter-spacing: 1px; }
      .val {
        font-family: var(--font-mono); font-size: 17px; font-weight: 500; color: var(--text-bright);
        &.price { color: var(--gold); text-shadow: 0 0 8px rgba(255,215,0,0.2); }
      }
    }
  }
}
</style>
