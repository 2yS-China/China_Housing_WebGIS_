<template>
  <div class="mode-switch">
    <div class="section-label">
      <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
        <rect x="1" y="1" width="5" height="5" rx="1" stroke="var(--cyan)" stroke-width="1" opacity="0.5"/>
        <rect x="8" y="1" width="5" height="5" rx="1" stroke="var(--cyan)" stroke-width="1" opacity="0.5"/>
        <rect x="1" y="8" width="5" height="5" rx="1" stroke="var(--cyan)" stroke-width="1" opacity="0.5"/>
        <rect x="8" y="8" width="5" height="5" rx="1" stroke="var(--cyan)" stroke-width="1" opacity="0.5"/>
      </svg>
      <span>MAP LAYER</span>
    </div>
    <div class="mode-grid">
      <button v-for="m in modes" :key="m.key"
              class="mode-btn" :class="{ active: store.mapMode === m.key }"
              @click="store.setMapMode(m.key)">
        <span class="mode-icon" v-html="m.icon"></span>
        <span class="mode-name">{{ m.name }}</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { useAppStore } from '@/stores/app'
const store = useAppStore()

const modes = [
  { key: 'choropleth', name: '设色', icon: '◆' },
  { key: 'bubble', name: '气泡', icon: '◉' },
  { key: 'province', name: '省级', icon: '⬡' },
  { key: 'mom', name: '环比', icon: '↕' },
  { key: 'yoy', name: '同比', icon: '⇅' },
  { key: 'moran', name: 'LISA', icon: '✦' },
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
    color: var(--cyan);
    opacity: 0.7;
  }
}

.mode-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 6px;
}

.mode-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 10px 4px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-dim);
  background: var(--bg-card);
  color: var(--text-muted);
  cursor: pointer;
  transition: all 0.25s;
  font-family: var(--font-body);

  .mode-icon { font-size: 16px; transition: all 0.25s; }
  .mode-name { font-size: 11px; }

  &:hover {
    border-color: var(--border-glow);
    color: var(--text-light);
    background: rgba(0, 240, 255, 0.06);
    .mode-icon { color: var(--cyan); }
  }

  &.active {
    border-color: var(--cyan);
    background: var(--cyan-dim);
    color: var(--cyan);
    box-shadow: 0 0 16px rgba(0, 240, 255, 0.15), inset 0 0 12px rgba(0, 240, 255, 0.05);
    .mode-icon {
      color: var(--cyan);
      text-shadow: 0 0 8px rgba(0, 240, 255, 0.6);
    }
    .mode-name { font-weight: 500; }
  }
}
</style>
