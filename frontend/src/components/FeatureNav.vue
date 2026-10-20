<template>
  <div class="feature-nav">
    <div class="section-label">
      <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
        <path d="M2 4h10M2 7h10M2 10h7" stroke="var(--magenta)" stroke-width="1.2" stroke-linecap="round" opacity="0.6"/>
      </svg>
      <span>ANALYTICS</span>
    </div>
    <div class="nav-list">
      <div class="nav-item" v-for="(f, i) in features" :key="f.key"
           :class="{ active: store.rightPanel === f.key }"
           :style="{ animationDelay: i * 40 + 'ms' }"
           @click="store.showPanel(f.key)">
        <span class="nav-icon" :style="{ color: f.color }">{{ f.icon }}</span>
        <span class="nav-name">{{ f.name }}</span>
        <span class="nav-arrow">›</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAppStore } from '@/stores/app'
const store = useAppStore()

const features = [
  { key: 'trend',         name: '城市趋势',   icon: '📈', color: 'var(--cyan)' },
  { key: 'compare',       name: '多城对比',   icon: '⚡', color: 'var(--gold)' },
  { key: 'rank',          name: '房价排行',   icon: '🏆', color: 'var(--gold)' },
  { key: 'changeRank',    name: '涨跌排行',   icon: '📊', color: 'var(--magenta)' },
  { key: 'provinceStats', name: '省份统计',   icon: '🗺️', color: 'var(--cyan)' },
  { key: 'cityGroup',     name: '城市群分析', icon: '🏙️', color: 'var(--green)' },
  { key: 'moran',         name: 'Moran 分析', icon: '🔬', color: 'var(--magenta)' },
  { key: 'cluster',       name: '空间聚类',   icon: '🎯', color: 'var(--cyan)' },
  { key: 'filter',        name: '筛选导出',   icon: '📥', color: 'var(--green)' }
]
</script>

<style scoped lang="scss">
.section-label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
  span {
    font-family: var(--font-display);
    font-size: 10px;
    letter-spacing: 2.5px;
    color: var(--magenta);
    opacity: 0.7;
  }
}

.nav-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: var(--radius-sm);
  cursor: pointer;
  font-size: 13px;
  border: 1px solid transparent;
  transition: all 0.25s;
  animation: float-in 0.4s ease-out both;

  .nav-icon { font-size: 16px; flex-shrink: 0; }
  .nav-name { flex: 1; color: var(--text-light); }
  .nav-arrow {
    font-size: 16px;
    color: var(--text-muted);
    transition: all 0.25s;
    opacity: 0;
    transform: translateX(-4px);
  }

  &:hover {
    background: rgba(255, 255, 255, 0.03);
    border-color: var(--border-dim);
    .nav-arrow { opacity: 1; transform: translateX(0); color: var(--cyan); }
  }

  &.active {
    background: linear-gradient(135deg, var(--cyan-dim), var(--magenta-dim));
    border-color: var(--border-glow);
    box-shadow: 0 0 20px rgba(0, 240, 255, 0.08);
    .nav-name { color: var(--text-bright); font-weight: 500; }
    .nav-arrow { opacity: 1; transform: translateX(0); color: var(--magenta); }
  }
}
</style>
