<template>
  <div class="cyber-panel">
    <div class="panel-header">
      <span class="panel-icon">🏆</span>
      <span class="panel-label">PRICE RANK</span>
    </div>
    <div class="rank-controls">
      <el-radio-group v-model="order" size="small" @change="loadData">
        <el-radio-button value="desc">TOP</el-radio-button>
        <el-radio-button value="asc">LOW</el-radio-button>
      </el-radio-group>
      <el-input-number v-model="limit" :min="5" :max="50" size="small" controls-position="right" @change="loadData" />
    </div>
    <div class="rank-list">
      <div class="rank-item" v-for="(item, i) in list" :key="item.cityCode"
           :style="{ animationDelay: i * 25 + 'ms' }" @click="onClickCity(item)">
        <span class="rank-num" :class="'rank-' + (i < 3 ? i : 'n')">{{ item.rank }}</span>
        <div class="rank-info">
          <span class="city-name">{{ item.cityName }}</span>
          <span class="province">{{ item.provinceName }}</span>
        </div>
        <div class="rank-bar-wrap">
          <div class="rank-bar" :style="{ width: barWidth(item.price) }"></div>
        </div>
        <span class="price">{{ item.price }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed } from 'vue'
import { useAppStore } from '@/stores/app'
import { getPriceRank } from '@/api/rank'

const store = useAppStore()
const order = ref('desc')
const limit = ref(20)
const list = ref([])

const maxPrice = computed(() => list.value.length ? Math.max(...list.value.map(i => i.price || 0)) : 1)
function barWidth(price) {
  return ((price || 0) / maxPrice.value * 100) + '%'
}

async function loadData() {
  list.value = await getPriceRank(store.currentYear, store.currentMonth, order.value, limit.value)
}

function onClickCity(item) { store.selectCity(item) }

watch(() => [store.currentYear, store.currentMonth], loadData)
onMounted(loadData)
</script>

<style scoped lang="scss">
.cyber-panel { animation: float-in 0.4s ease-out; }
.panel-header {
  display: flex; align-items: center; gap: 8px; margin-bottom: 14px;
  .panel-icon { font-size: 16px; }
  .panel-label { font-family: var(--font-display); font-size: 11px; letter-spacing: 2px; color: var(--gold); }
}
.rank-controls { display: flex; gap: 10px; align-items: center; margin-bottom: 12px; }
.rank-list { max-height: 520px; overflow-y: auto; }

.rank-item {
  display: flex; align-items: center; gap: 10px;
  padding: 9px 8px; cursor: pointer; font-size: 13px;
  border-bottom: 1px solid rgba(80,100,160,0.08);
  transition: all 0.2s;
  animation: float-in 0.35s ease-out both;

  &:hover {
    background: var(--cyan-dim);
    .city-name { color: var(--cyan); }
  }

  .rank-num {
    width: 26px; height: 26px; border-radius: 8px; display: flex; align-items: center;
    justify-content: center; font-family: var(--font-mono); font-size: 12px; font-weight: 500;
    flex-shrink: 0; border: 1px solid var(--border-dim);
    background: var(--bg-card); color: var(--text-muted);

    &.rank-0 {
      background: linear-gradient(135deg, #ffd700, #ff8c00);
      border-color: #ffd700; color: var(--bg-void);
      box-shadow: 0 0 12px rgba(255, 215, 0, 0.3);
    }
    &.rank-1 {
      background: linear-gradient(135deg, #c0c0c0, #8a8a8a);
      border-color: #c0c0c0; color: var(--bg-void);
    }
    &.rank-2 {
      background: linear-gradient(135deg, #cd7f32, #a0522d);
      border-color: #cd7f32; color: var(--bg-void);
    }
  }

  .rank-info { display: flex; flex-direction: column; gap: 1px; min-width: 70px; }
  .city-name { font-weight: 500; color: var(--text-bright); transition: color 0.2s; font-size: 13px; }
  .province { color: var(--text-muted); font-size: 11px; }

  .rank-bar-wrap {
    flex: 1; height: 4px; background: rgba(80,100,160,0.1); border-radius: 2px; overflow: hidden;
    .rank-bar {
      height: 100%;
      background: linear-gradient(90deg, var(--cyan), var(--magenta));
      border-radius: 2px;
      transition: width 0.6s ease-out;
    }
  }

  .price {
    font-family: var(--font-mono); font-size: 13px; font-weight: 500;
    color: var(--gold); text-shadow: 0 0 6px rgba(255,215,0,0.2);
    min-width: 55px; text-align: right;
  }
}
</style>
