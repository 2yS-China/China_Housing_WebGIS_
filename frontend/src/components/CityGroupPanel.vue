<template>
  <div class="panel-card">
    <div class="panel-title">城市群房价对比</div>
    <el-select v-model="selectedGroup" placeholder="选择城市群（空=全部）" size="small"
               clearable @change="loadData">
      <el-option v-for="g in groups" :key="g.groupId" :label="g.groupName" :value="g.groupId" />
    </el-select>
    <div class="group-list" v-if="statsList.length">
      <div class="group-card" v-for="s in statsList" :key="s.groupId">
        <div class="group-name">{{ s.groupName }}</div>
        <div class="group-stats">
          <span>城市数 <b>{{ s.cityCount }}</b></span>
          <span>均价 <b class="price">{{ s.avgPrice }}</b></span>
          <span>最低 {{ s.minPrice }}</span>
          <span>最高 {{ s.maxPrice }}</span>
        </div>
      </div>
    </div>
    <v-chart v-if="chartOption.series" :option="chartOption" autoresize style="height:280px;margin-top:10px" />
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
import { getCityGroupList, getCityGroupStats } from '@/api/citygroup'

use([BarChart, GridComponent, TooltipComponent, CanvasRenderer])

const store = useAppStore()
const groups = ref([])
const selectedGroup = ref(null)
const statsList = ref([])
const chartOption = ref({})

onMounted(async () => {
  groups.value = await getCityGroupList()
  loadData()
})

async function loadData() {
  statsList.value = await getCityGroupStats(store.currentYear, store.currentMonth, selectedGroup.value)
  if (!statsList.value.length) return

  chartOption.value = {
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(10,14,39,0.9)', borderColor: 'rgba(0,240,255,0.3)', textStyle: { color: '#c0c8e8', fontSize: 12 } },
    grid: { top: 10, right: 10, bottom: 30, left: 80 },
    xAxis: { type: 'value', axisLabel: { fontSize: 10, color: '#5a6590', fontFamily: 'JetBrains Mono' }, splitLine: { lineStyle: { color: 'rgba(80,100,160,0.1)' } } },
    yAxis: { type: 'category', data: statsList.value.map(s => s.groupName).reverse(), axisLabel: { fontSize: 11, color: '#c0c8e8' } },
    series: [{ type: 'bar', data: statsList.value.map(s => s.avgPrice).reverse(), barMaxWidth: 24,
      itemStyle: { color: { type: 'linear', x: 0, y: 0, x2: 1, y2: 0, colorStops: [{ offset: 0, color: 'rgba(0,255,136,0.6)' }, { offset: 1, color: 'rgba(0,240,255,0.6)' }] } }
    }]
  }
}
</script>

<style scoped lang="scss">
.panel-card {
  animation: float-in 0.4s ease-out;
  .panel-title {
    font-family: var(--font-display); font-size: 11px; letter-spacing: 2px;
    color: var(--green); margin-bottom: 14px;
    &::before { content: '🏙️ '; font-size: 14px; }
  }
  .group-list { margin-top: 14px; display: flex; flex-direction: column; gap: 8px; }
  .group-card {
    background: var(--bg-card); border: 1px solid var(--border-dim);
    border-radius: var(--radius-sm); padding: 12px;
    transition: all 0.25s;
    &:hover { border-color: var(--border-glow); box-shadow: 0 0 12px rgba(0,240,255,0.08); }
    .group-name { font-weight: 500; color: var(--text-bright); margin-bottom: 6px; }
    .group-stats {
      display: flex; flex-wrap: wrap; gap: 10px; font-size: 12px; color: var(--text-muted);
      b { color: var(--text-bright); font-family: var(--font-mono); }
      .price { color: var(--gold); text-shadow: 0 0 6px rgba(255,215,0,0.2); }
    }
  }
}
</style>
