import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAppStore = defineStore('app', () => {
  const currentYear = ref(2024)
  const currentMonth = ref(6)
  const mapMode = ref('choropleth')
  const selectedCity = ref(null)
  const compareCodes = ref([])
  const rightPanel = ref('')
  const isPlaying = ref(false)

  const yearMonth = computed(() => `${currentYear.value}-${String(currentMonth.value).padStart(2, '0')}`)

  function setTime(year, month) {
    currentYear.value = year
    currentMonth.value = month
  }

  function setMapMode(mode) {
    mapMode.value = mode
  }

  function selectCity(city) {
    selectedCity.value = city
    rightPanel.value = 'trend'
  }

  function toggleCompareCity(code) {
    const idx = compareCodes.value.indexOf(code)
    if (idx >= 0) {
      compareCodes.value.splice(idx, 1)
    } else if (compareCodes.value.length < 5) {
      compareCodes.value.push(code)
    }
  }

  function showPanel(panel) {
    rightPanel.value = rightPanel.value === panel ? '' : panel
  }

  return {
    currentYear, currentMonth, mapMode, selectedCity,
    compareCodes, rightPanel, isPlaying, yearMonth,
    setTime, setMapMode, selectCity, toggleCompareCity, showPanel
  }
})
