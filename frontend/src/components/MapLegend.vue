<template>
  <transition name="legend-fade">
    <div class="map-legend glass-panel" v-if="items.length">
      <div class="legend-header">
        <span class="legend-dot"></span>
        <span class="legend-title">{{ title }}</span>
      </div>
      <div class="legend-body">
        <div class="legend-item" v-for="(it, i) in items" :key="i"
             :style="{ animationDelay: i * 30 + 'ms' }">
          <span class="legend-color" :style="{ background: it.color, boxShadow: '0 0 6px ' + it.color + '80' }"></span>
          <span class="legend-label">{{ it.label }}</span>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { computed } from 'vue'
import { useAppStore } from '@/stores/app'
import { getPriceLegend, getChangeLegend } from '@/utils/colorScale'

const store = useAppStore()

const title = computed(() => {
  const m = store.mapMode
  if (m === 'choropleth' || m === 'bubble' || m === 'province') return 'PRICE ¥/㎡'
  if (m === 'mom' || m === 'yoy') return 'CHANGE %'
  if (m === 'moran') return 'LISA CLUSTER'
  return ''
})

const items = computed(() => {
  const m = store.mapMode
  if (m === 'choropleth' || m === 'bubble' || m === 'province') return getPriceLegend()
  if (m === 'mom' || m === 'yoy') return getChangeLegend()
  if (m === 'moran') {
    return [
      { color: '#d73027', label: 'HH 高高聚类' },
      { color: '#2b83ba', label: 'LL 低低聚类' },
      { color: '#fdae61', label: 'HL 高低异常' },
      { color: '#abd9e9', label: 'LH 低高异常' },
      { color: '#666666', label: 'NS 不显著' }
    ]
  }
  return []
})
</script>

<style scoped lang="scss">
.map-legend {
  position: absolute;
  bottom: 24px;
  left: 24px;
  z-index: 1000;
  padding: 14px 16px;
  min-width: 150px;
  animation: float-in 0.4s ease-out;
}

.legend-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--border-dim);
}

.legend-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--cyan);
  box-shadow: 0 0 6px var(--cyan);
  animation: pulse-glow 2s ease-in-out infinite;
}

.legend-title {
  font-family: var(--font-display);
  font-size: 10px;
  letter-spacing: 2px;
  color: var(--cyan);
  opacity: 0.8;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 3px 0;
  animation: float-in 0.3s ease-out both;
}

.legend-color {
  width: 18px;
  height: 10px;
  border-radius: 3px;
  flex-shrink: 0;
}

.legend-label {
  font-family: var(--font-mono);
  font-size: 11px;
  color: #ffffff;
}

.legend-fade-enter-active { animation: float-in 0.3s ease-out; }
.legend-fade-leave-active { animation: float-in 0.2s ease-in reverse; }
</style>
