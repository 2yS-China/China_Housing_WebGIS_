<template>
  <div class="panel-card">
    <div class="panel-title">空间聚类分析</div>
    <el-select v-model="selectedTask" placeholder="选择聚类任务" size="small" @change="loadData">
      <el-option v-for="t in tasks" :key="t" :label="t" :value="t" />
    </el-select>
    <div v-if="clusterData.length" class="cluster-result">
      <div class="cluster-group" v-for="g in groupedData" :key="g.label">
        <div class="group-header">
          <span class="dot" :style="{ background: g.color }"></span>
          <span class="desc">{{ g.desc || '第' + g.label + '类' }}</span>
          <span class="count">{{ g.cities.length }} 个城市</span>
        </div>
        <div class="city-tags">
          <el-tag v-for="c in g.cities.slice(0, 10)" :key="c.cityCode" size="small" type="info">
            {{ c.cityName }}
          </el-tag>
          <el-tag v-if="g.cities.length > 10" size="small" type="info">+{{ g.cities.length - 10 }}</el-tag>
        </div>
      </div>
    </div>
    <div v-else class="empty-tip">请选择聚类任务</div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, inject } from 'vue'
import { getClusterTasks, getCluster } from '@/api/analysis'
import { getClusterColor } from '@/utils/colorScale'

const mapView = inject('mapView')
const tasks = ref([])
const selectedTask = ref('')
const clusterData = ref([])

const groupedData = computed(() => {
  const map = {}
  clusterData.value.forEach(item => {
    if (!map[item.clusterLabel]) {
      map[item.clusterLabel] = {
        label: item.clusterLabel,
        desc: item.clusterDesc,
        color: getClusterColor(item.clusterLabel),
        cities: []
      }
    }
    map[item.clusterLabel].cities.push(item)
  })
  return Object.values(map).sort((a, b) => a.label - b.label)
})

onMounted(async () => {
  tasks.value = await getClusterTasks()
})

async function loadData() {
  if (!selectedTask.value) return
  clusterData.value = await getCluster(selectedTask.value)
  if (clusterData.value.length && mapView?.value) {
    mapView.value.renderClusterOnMap(clusterData.value)
  }
}
</script>

<style scoped lang="scss">
.panel-card {
  animation: float-in 0.4s ease-out;
  .panel-title {
    font-family: var(--font-display); font-size: 11px; letter-spacing: 2px;
    color: var(--cyan); margin-bottom: 14px;
    &::before { content: '🎯 '; font-size: 14px; }
  }
  .empty-tip { color: var(--text-muted); font-size: 13px; text-align: center; padding: 30px 0; }
  .cluster-result { margin-top: 14px; display: flex; flex-direction: column; gap: 14px; }
  .cluster-group {
    .group-header {
      display: flex; align-items: center; gap: 10px; margin-bottom: 8px; font-size: 13px;
      .dot {
        width: 14px; height: 14px; border-radius: 4px; flex-shrink: 0;
        box-shadow: 0 0 8px currentColor;
      }
      .desc { font-weight: 500; color: var(--text-bright); }
      .count { margin-left: auto; font-family: var(--font-mono); color: var(--text-muted); font-size: 12px; }
    }
    .city-tags { display: flex; flex-wrap: wrap; gap: 4px; }
  }
}
</style>
