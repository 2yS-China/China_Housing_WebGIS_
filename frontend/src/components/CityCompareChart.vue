<template>
  <div class="cyber-panel">
    <div class="panel-header">
      <span class="panel-icon">⚡</span>
      <span class="panel-label">COMPARE</span>
    </div>
    <div class="compare-input">
      <el-select v-model="selectedCodes" multiple filterable remote :remote-method="remoteSearch"
                 placeholder="输入城市名搜索（最多5个）" size="small" :multiple-limit="5" @change="onSelectChange">
        <el-option v-for="c in mergedOptions" :key="c.cityCode" :label="c.cityName" :value="c.cityCode" />
      </el-select>
    </div>
    <v-chart v-if="chartOption.series" :option="chartOption" autoresize style="height:340px" />
    <div v-else class="empty-state">
      <div class="empty-icon">⚡</div>
      <div class="empty-text">选择城市开始对比</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, DataZoomComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { searchCity, compareCities } from '@/api/city'

use([LineChart, GridComponent, TooltipComponent, LegendComponent, DataZoomComponent, CanvasRenderer])

const COLORS = ['#00f0ff', '#ff2d78', '#ffd700', '#00ff88', '#b06cff']
const selectedCodes = ref([])
const searchOptions = ref([])
const selectedCities = ref([])
const chartOption = ref({})

const mergedOptions = computed(() => {
  const map = new Map()
  selectedCities.value.forEach(c => map.set(c.cityCode, c))
  searchOptions.value.forEach(c => map.set(c.cityCode, c))
  return Array.from(map.values())
})

async function remoteSearch(query) {
  if (!query) { searchOptions.value = []; return }
  searchOptions.value = await searchCity(query)
}

function onSelectChange() {
  selectedCities.value = mergedOptions.value.filter(c => selectedCodes.value.includes(c.cityCode))
  loadData()
}

async function loadData() {
  if (!selectedCodes.value.length) { chartOption.value = {}; return }
  const trends = await compareCities(selectedCodes.value)
  if (!trends.length) return

  const xData = trends[0].points.map(p => `${p.year}-${String(p.month).padStart(2, '0')}`)
  const series = trends.map((t, i) => ({
    name: t.cityName, type: 'line', data: t.points.map(p => p.price),
    smooth: true, symbol: 'none',
    lineStyle: { width: 2, color: COLORS[i % 5], shadowColor: COLORS[i % 5] + '60', shadowBlur: 6 },
    itemStyle: { color: COLORS[i % 5] }
  }))

  chartOption.value = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(10,14,39,0.9)',
      borderColor: 'rgba(0,240,255,0.3)',
      textStyle: { color: '#c0c8e8', fontFamily: 'Noto Sans SC', fontSize: 12 }
    },
    legend: { top: 0, textStyle: { color: '#5a6590', fontSize: 11, fontFamily: 'Noto Sans SC' } },
    grid: { top: 30, right: 15, bottom: 60, left: 55 },
    xAxis: {
      type: 'category', data: xData, boundaryGap: false,
      axisLabel: { rotate: 45, fontSize: 10, color: '#5a6590', fontFamily: 'JetBrains Mono' },
      axisLine: { lineStyle: { color: 'rgba(0,240,255,0.15)' } }
    },
    yAxis: {
      type: 'value',
      axisLabel: { fontSize: 10, color: '#5a6590', fontFamily: 'JetBrains Mono' },
      splitLine: { lineStyle: { color: 'rgba(80,100,160,0.1)' } }
    },
    dataZoom: [
      { type: 'inside' },
      { type: 'slider', height: 18, bottom: 6, borderColor: 'transparent',
        backgroundColor: 'rgba(80,100,160,0.1)', fillerColor: 'rgba(0,240,255,0.1)',
        handleStyle: { color: '#00f0ff' }, textStyle: { color: '#5a6590', fontSize: 10 } }
    ],
    series
  }
}
</script>

<style scoped lang="scss">
.cyber-panel { animation: float-in 0.4s ease-out; }
.panel-header {
  display: flex; align-items: center; gap: 8px; margin-bottom: 14px;
  .panel-icon { font-size: 16px; }
  .panel-label { font-family: var(--font-display); font-size: 11px; letter-spacing: 2px; color: var(--gold); }
}
.compare-input { margin-bottom: 12px; }
.empty-state {
  text-align: center; padding: 50px 0;
  .empty-icon { font-size: 36px; opacity: 0.3; }
  .empty-text { color: var(--text-muted); font-size: 13px; margin-top: 10px; }
}
</style>
