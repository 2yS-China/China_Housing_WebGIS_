<template>
  <div class="cyber-panel">
    <div class="panel-header">
      <span class="panel-icon">📈</span>
      <span class="panel-label">CITY TREND</span>
    </div>
    <div v-if="!store.selectedCity" class="empty-state">
      <div class="empty-icon">◎</div>
      <div class="empty-text">点击地图上的城市</div>
    </div>
    <div v-else>
      <div class="city-badge">
        <span class="badge-dot"></span>
        <span class="badge-name">{{ store.selectedCity.cityName }}</span>
        <span class="badge-prov">{{ store.selectedCity.provinceName }}</span>
      </div>
      <v-chart :option="chartOption" autoresize style="height:340px" />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, DataZoomComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { useAppStore } from '@/stores/app'
import { getCityTrend } from '@/api/city'

use([LineChart, GridComponent, TooltipComponent, DataZoomComponent, CanvasRenderer])

const store = useAppStore()
const chartOption = ref({})

watch(() => store.selectedCity, async (city) => {
  if (!city?.cityCode) return
  const data = await getCityTrend(city.cityCode)
  if (!data) return
  const xData = data.points.map(p => `${p.year}-${String(p.month).padStart(2, '0')}`)
  const yData = data.points.map(p => p.price)

  chartOption.value = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(10,14,39,0.9)',
      borderColor: 'rgba(0,240,255,0.3)',
      textStyle: { color: '#c0c8e8', fontFamily: 'Noto Sans SC', fontSize: 12 },
      formatter: '{b}<br/>均价：<span style="color:#ffd700;font-weight:bold">{c}</span> 元/㎡'
    },
    grid: { top: 20, right: 15, bottom: 60, left: 55 },
    xAxis: {
      type: 'category', data: xData, boundaryGap: false,
      axisLabel: { rotate: 45, fontSize: 10, color: '#5a6590', fontFamily: 'JetBrains Mono' },
      axisLine: { lineStyle: { color: 'rgba(0,240,255,0.15)' } },
      splitLine: { show: false }
    },
    yAxis: {
      type: 'value',
      axisLabel: { fontSize: 10, color: '#5a6590', fontFamily: 'JetBrains Mono' },
      splitLine: { lineStyle: { color: 'rgba(80,100,160,0.1)' } },
      axisLine: { show: false }
    },
    dataZoom: [
      { type: 'inside' },
      { type: 'slider', height: 18, bottom: 6, borderColor: 'transparent',
        backgroundColor: 'rgba(80,100,160,0.1)', fillerColor: 'rgba(0,240,255,0.1)',
        handleStyle: { color: '#00f0ff', borderColor: '#00f0ff' },
        textStyle: { color: '#5a6590', fontSize: 10 } }
    ],
    series: [{
      type: 'line', data: yData, smooth: true, symbol: 'none',
      lineStyle: { color: '#00f0ff', width: 2, shadowColor: 'rgba(0,240,255,0.4)', shadowBlur: 8 },
      areaStyle: {
        color: {
          type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(0,240,255,0.2)' },
            { offset: 0.6, color: 'rgba(0,240,255,0.05)' },
            { offset: 1, color: 'transparent' }
          ]
        }
      }
    }]
  }
}, { immediate: true })
</script>

<style scoped lang="scss">
.cyber-panel {
  animation: float-in 0.4s ease-out;
}
.panel-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 14px;
  .panel-icon { font-size: 16px; }
  .panel-label {
    font-family: var(--font-display);
    font-size: 11px;
    letter-spacing: 2px;
    color: var(--cyan);
  }
}
.empty-state {
  text-align: center;
  padding: 50px 0;
  .empty-icon {
    font-size: 36px;
    color: var(--text-muted);
    opacity: 0.3;
    animation: pulse-glow 3s ease-in-out infinite;
  }
  .empty-text { color: var(--text-muted); font-size: 13px; margin-top: 10px; }
}
.city-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: var(--bg-card);
  border: 1px solid var(--border-dim);
  border-radius: var(--radius-sm);
  margin-bottom: 12px;
  .badge-dot {
    width: 8px; height: 8px; border-radius: 50%;
    background: var(--cyan);
    box-shadow: 0 0 8px var(--cyan);
  }
  .badge-name {
    font-weight: 500;
    color: var(--text-bright);
    font-size: 14px;
  }
  .badge-prov { color: var(--text-muted); font-size: 12px; margin-left: auto; }
}
</style>
