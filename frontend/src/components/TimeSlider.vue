<template>
  <div class="time-slider">
    <div class="section-label">
      <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
        <circle cx="7" cy="7" r="6" stroke="var(--cyan)" stroke-width="1" opacity="0.5"/>
        <path d="M7 3v4l3 2" stroke="var(--cyan)" stroke-width="1.2" stroke-linecap="round"/>
      </svg>
      <span>TIME CONTROL</span>
    </div>
    <div class="time-display-row">
      <div class="year-box" @click="cycleYear(-1)">
        <span class="arrow">◄</span>
        <span class="val">{{ year }}</span>
        <span class="arrow" @click.stop="cycleYear(1)">►</span>
      </div>
      <div class="month-grid">
        <div class="month-cell" v-for="m in 12" :key="m"
             :class="{ active: m === month, disabled: year === 2025 && m > 7 }"
             @click="selectMonth(m)">
          {{ m }}
        </div>
      </div>
    </div>
    <div class="slider-row">
      <button class="play-btn" :class="{ playing: store.isPlaying }" @click="togglePlay">
        <svg v-if="!store.isPlaying" width="14" height="14" viewBox="0 0 14 14" fill="none">
          <path d="M3 1.5l9 5.5-9 5.5V1.5z" fill="currentColor"/>
        </svg>
        <svg v-else width="14" height="14" viewBox="0 0 14 14" fill="none">
          <rect x="2" y="1.5" width="3.5" height="11" rx="1" fill="currentColor"/>
          <rect x="8.5" y="1.5" width="3.5" height="11" rx="1" fill="currentColor"/>
        </svg>
      </button>
      <div class="slider-track">
        <el-slider v-model="sliderVal" :min="0" :max="maxSlider" :format-tooltip="formatTip"
                   size="small" @change="onSlide" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onUnmounted } from 'vue'
import { useAppStore } from '@/stores/app'

const store = useAppStore()
const years = Array.from({ length: 16 }, (_, i) => 2010 + i)
const year = ref(store.currentYear)
const month = ref(store.currentMonth)
const maxSlider = computed(() => (2025 - 2010) * 12 + 6)
const sliderVal = ref((year.value - 2010) * 12 + month.value - 1)
let playTimer = null

function cycleYear(dir) {
  const newY = year.value + dir
  if (newY >= 2010 && newY <= 2025) {
    year.value = newY
    if (year.value === 2025 && month.value > 7) month.value = 7
    syncTime()
  }
}

function selectMonth(m) {
  if (year.value === 2025 && m > 7) return
  month.value = m
  syncTime()
}

function syncTime() {
  store.setTime(year.value, month.value)
  sliderVal.value = (year.value - 2010) * 12 + month.value - 1
}

function onSlide(val) {
  const y = 2010 + Math.floor(val / 12)
  const m = (val % 12) + 1
  year.value = y
  month.value = m
  store.setTime(y, m)
}

function formatTip(val) {
  const y = 2010 + Math.floor(val / 12)
  const m = (val % 12) + 1
  return `${y}年${m}月`
}

function togglePlay() {
  if (store.isPlaying) {
    clearInterval(playTimer)
    store.isPlaying = false
  } else {
    store.isPlaying = true
    playTimer = setInterval(() => {
      if (sliderVal.value >= maxSlider.value) {
        clearInterval(playTimer)
        store.isPlaying = false
        return
      }
      sliderVal.value++
      onSlide(sliderVal.value)
    }, 400)
  }
}

onUnmounted(() => clearInterval(playTimer))
</script>

<style scoped lang="scss">
.section-label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  span {
    font-family: var(--font-display);
    font-size: 10px;
    letter-spacing: 2.5px;
    color: var(--cyan);
    opacity: 0.7;
  }
}

.time-display-row {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 12px;
}

.year-box {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 8px;
  background: var(--bg-card);
  border: 1px solid var(--border-dim);
  border-radius: var(--radius-sm);

  .val {
    font-family: var(--font-mono);
    font-size: 22px;
    font-weight: 500;
    color: var(--gold);
    text-shadow: 0 0 12px rgba(255, 215, 0, 0.25);
    min-width: 60px;
    text-align: center;
  }
  .arrow {
    cursor: pointer;
    color: var(--text-muted);
    font-size: 12px;
    padding: 4px 8px;
    border-radius: 4px;
    transition: all 0.2s;
    user-select: none;
    &:hover {
      color: var(--cyan);
      background: var(--cyan-dim);
    }
  }
}

.month-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 4px;

  .month-cell {
    text-align: center;
    font-family: var(--font-mono);
    font-size: 12px;
    padding: 6px 0;
    border-radius: 6px;
    cursor: pointer;
    color: var(--text-muted);
    border: 1px solid transparent;
    transition: all 0.2s;

    &:hover:not(.disabled) {
      color: var(--text-bright);
      border-color: var(--border-glow);
      background: var(--cyan-dim);
    }

    &.active {
      color: var(--bg-void);
      background: var(--cyan);
      border-color: var(--cyan);
      font-weight: 500;
      box-shadow: 0 0 12px rgba(0, 240, 255, 0.3);
    }

    &.disabled {
      opacity: 0.2;
      cursor: not-allowed;
    }
  }
}

.slider-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.play-btn {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  border: 1px solid var(--border-dim);
  background: var(--bg-card);
  color: var(--text-light);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  flex-shrink: 0;
  transition: all 0.3s;

  &:hover {
    border-color: var(--cyan);
    color: var(--cyan);
    box-shadow: var(--cyan-glow);
  }

  &.playing {
    border-color: var(--magenta);
    color: var(--magenta);
    box-shadow: 0 0 16px rgba(255, 45, 120, 0.3);
    animation: pulse-glow 1.5s ease-in-out infinite;
  }
}

.slider-track { flex: 1; }
</style>
