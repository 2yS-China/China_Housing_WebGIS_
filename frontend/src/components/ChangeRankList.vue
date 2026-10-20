<template>
  <div class="panel-card">
    <div class="panel-title">房价涨跌幅排行</div>
    <div class="controls">
      <div class="time-range">
        <el-date-picker v-model="startDate" type="month" placeholder="起始" size="small"
                        value-format="YYYY-MM" style="width:120px" @change="loadData" />
        <span>→</span>
        <el-date-picker v-model="endDate" type="month" placeholder="结束" size="small"
                        value-format="YYYY-MM" style="width:120px" @change="loadData" />
      </div>
      <el-radio-group v-model="order" size="small" @change="loadData">
        <el-radio-button value="desc">涨幅最大</el-radio-button>
        <el-radio-button value="asc">跌幅最大</el-radio-button>
      </el-radio-group>
    </div>
    <div class="rank-list">
      <div class="rank-item" v-for="(item, i) in list" :key="item.cityCode">
        <span class="rank-num" :class="{ top3: i < 3 }">{{ i + 1 }}</span>
        <div class="info">
          <span class="city-name">{{ item.cityName }}</span>
          <span class="detail">{{ item.startPrice }} → {{ item.endPrice }}</span>
        </div>
        <span class="change" :class="item.changePct >= 0 ? 'up' : 'down'">
          {{ item.changePct >= 0 ? '+' : '' }}{{ item.changePct }}%
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getChangeRank } from '@/api/rank'

const startDate = ref('2020-01')
const endDate = ref('2024-06')
const order = ref('desc')
const list = ref([])

async function loadData() {
  if (!startDate.value || !endDate.value) return
  const [sy, sm] = startDate.value.split('-').map(Number)
  const [ey, em] = endDate.value.split('-').map(Number)
  list.value = await getChangeRank(sy, sm, ey, em, order.value, 20)
}

onMounted(loadData)
</script>

<style scoped lang="scss">
.panel-card {
  animation: float-in 0.4s ease-out;
  .panel-title {
    font-family: var(--font-display); font-size: 11px; letter-spacing: 2px;
    color: var(--magenta); margin-bottom: 14px;
    &::before { content: '📊 '; font-size: 14px; }
  }
  .controls { display: flex; flex-direction: column; gap: 8px; margin-bottom: 12px; }
  .time-range { display: flex; align-items: center; gap: 6px; font-size: 13px; color: var(--text-muted); }
  .rank-list { max-height: 420px; overflow-y: auto; }
  .rank-item {
    display: flex; align-items: center; gap: 8px;
    padding: 9px 8px; border-bottom: 1px solid rgba(80,100,160,0.08); font-size: 13px;
    animation: float-in 0.35s ease-out both; transition: all 0.2s;
    &:hover { background: var(--cyan-dim); }
    .rank-num {
      width: 26px; height: 26px; border-radius: 8px; display: flex; align-items: center;
      justify-content: center; font-family: var(--font-mono); font-size: 12px; font-weight: 500;
      background: var(--bg-card); border: 1px solid var(--border-dim); color: var(--text-muted); flex-shrink: 0;
      &.top3 {
        background: linear-gradient(135deg, #ffd700, #ff8c00);
        border-color: #ffd700; color: var(--bg-void);
        box-shadow: 0 0 10px rgba(255,215,0,0.25);
      }
    }
    .info { display: flex; flex-direction: column; gap: 2px; }
    .city-name { font-weight: 500; color: var(--text-bright); }
    .detail { font-family: var(--font-mono); font-size: 11px; color: var(--text-muted); }
    .change {
      margin-left: auto; font-family: var(--font-mono); font-weight: 500;
      &.up { color: var(--red); text-shadow: 0 0 6px rgba(255,71,87,0.3); }
      &.down { color: var(--green); text-shadow: 0 0 6px rgba(0,255,136,0.3); }
    }
  }
}
</style>
