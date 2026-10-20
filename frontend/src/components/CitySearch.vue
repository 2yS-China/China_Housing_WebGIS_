<template>
  <div class="city-search">
    <div class="search-box">
      <svg class="search-icon" width="16" height="16" viewBox="0 0 16 16" fill="none">
        <circle cx="7" cy="7" r="5.5" stroke="currentColor" stroke-width="1.2"/>
        <path d="M11 11l3.5 3.5" stroke="currentColor" stroke-width="1.2" stroke-linecap="round"/>
      </svg>
      <input
        v-model="keyword"
        placeholder="搜索城市 / 区划代码 ..."
        @input="onSearch"
        class="search-input"
      />
      <div class="search-shortcut" v-if="!keyword">⌘K</div>
    </div>
    <transition name="dropdown">
      <div class="search-results" v-if="results.length">
        <div class="result-item" v-for="(c, i) in results" :key="c.cityCode"
             :style="{ animationDelay: i * 30 + 'ms' }" @click="onSelect(c)">
          <div class="result-dot"></div>
          <div class="result-info">
            <span class="city-name">{{ c.cityName }}</span>
            <span class="province-name">{{ c.provinceName }}</span>
          </div>
          <span class="city-code">{{ c.cityCode }}</span>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { searchCity } from '@/api/city'
import { useAppStore } from '@/stores/app'

const store = useAppStore()
const keyword = ref('')
const results = ref([])
let timer = null

function onSearch() {
  clearTimeout(timer)
  if (!keyword.value.trim()) { results.value = []; return }
  timer = setTimeout(async () => {
    results.value = await searchCity(keyword.value.trim())
  }, 300)
}

function onSelect(city) {
  store.selectCity(city)
  results.value = []
  keyword.value = city.cityName
}
</script>

<style scoped lang="scss">
.city-search { position: relative; }

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(10, 14, 39, 0.7);
  border: 1px solid var(--border-dim);
  border-radius: var(--radius-sm);
  padding: 0 12px;
  height: 38px;
  transition: all 0.3s;

  &:focus-within {
    border-color: var(--cyan);
    box-shadow: 0 0 16px rgba(0, 240, 255, 0.12), inset 0 0 20px rgba(0, 240, 255, 0.03);
  }

  .search-icon { color: var(--text-muted); flex-shrink: 0; }
  .search-input {
    flex: 1;
    background: none;
    border: none;
    outline: none;
    color: var(--text-bright);
    font-family: var(--font-body);
    font-size: 13px;
    &::placeholder { color: var(--text-muted); }
  }
  .search-shortcut {
    font-family: var(--font-mono);
    font-size: 10px;
    color: var(--text-muted);
    padding: 2px 6px;
    border: 1px solid var(--border-dim);
    border-radius: 4px;
  }
}

.search-results {
  position: absolute;
  top: 44px;
  left: 0;
  right: 0;
  z-index: 200;
  background: var(--bg-deep);
  border: 1px solid var(--border-glow);
  border-radius: var(--radius-sm);
  max-height: 280px;
  overflow-y: auto;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.5);
}

.result-item {
  padding: 10px 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  animation: float-in 0.3s ease-out both;
  transition: all 0.2s;
  border-bottom: 1px solid rgba(80, 100, 160, 0.08);

  &:hover {
    background: var(--cyan-dim);
    .result-dot { background: var(--cyan); box-shadow: 0 0 8px var(--cyan); }
    .city-name { color: var(--cyan); }
  }

  .result-dot {
    width: 6px; height: 6px; border-radius: 50%;
    background: var(--text-muted);
    flex-shrink: 0;
    transition: all 0.2s;
  }
  .result-info { display: flex; flex-direction: column; gap: 2px; }
  .city-name { font-weight: 500; color: var(--text-bright); transition: color 0.2s; }
  .province-name { color: var(--text-muted); font-size: 11px; }
  .city-code {
    margin-left: auto;
    font-family: var(--font-mono);
    font-size: 11px;
    color: var(--text-muted);
  }
}

.dropdown-enter-active { animation: float-in 0.25s ease-out; }
.dropdown-leave-active { animation: float-in 0.15s ease-in reverse; }
</style>
