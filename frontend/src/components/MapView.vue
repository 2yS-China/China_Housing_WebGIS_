<template>
  <div ref="mapContainer" class="map-container"></div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import L from 'leaflet'
import { useAppStore } from '@/stores/app'
import { getChoropleth, getBubble, getMomChange, getYoyChange, getProvinceAggregation } from '@/api/map'
import { getMoran } from '@/api/analysis'
import { getCluster } from '@/api/analysis'
import { getPriceColor, getPriceRadius, getChangeColor, getLisaColor, getClusterColor } from '@/utils/colorScale'
import { pricePopup, changePopup } from '@/utils/bindPopup'

const TDT_KEY = import.meta.env.VITE_TIANDITU_KEY
const store = useAppStore()
const mapContainer = ref(null)
let map = null
let dataLayer = null
const popupOpts = { className: 'cyber-popup-wrap', maxWidth: 280 }

onMounted(() => {
  map = L.map(mapContainer.value, {
    center: [35.86, 104.19],
    zoom: 5,
    minZoom: 3,
    maxZoom: 18,
    zoomControl: false
  })

  L.control.zoom({ position: 'topright' }).addTo(map)

  L.tileLayer(
    `http://t{s}.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL={x}&TILEROW={y}&TILEMATRIX={z}&tk=${TDT_KEY}`,
    { subdomains: ['0','1','2','3','4','5','6','7'], maxZoom: 18 }
  ).addTo(map)

  L.tileLayer(
    `http://t{s}.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL={x}&TILEROW={y}&TILEMATRIX={z}&tk=${TDT_KEY}`,
    { subdomains: ['0','1','2','3','4','5','6','7'], maxZoom: 18 }
  ).addTo(map)

  loadMapData()
})

watch(() => [store.currentYear, store.currentMonth, store.mapMode], () => {
  loadMapData()
})

async function loadMapData() {
  if (!map) return
  clearDataLayer()

  const year = store.currentYear
  const month = store.currentMonth
  const mode = store.mapMode

  try {
    if (mode === 'choropleth') {
      const data = await getChoropleth(year, month)
      renderChoropleth(data)
    } else if (mode === 'bubble') {
      const data = await getBubble(year, month)
      renderBubble(data)
    } else if (mode === 'province') {
      const data = await getProvinceAggregation(year, month)
      renderProvinceMarkers(data)
    } else if (mode === 'mom') {
      const data = await getMomChange(year, month)
      renderChangeMap(data)
    } else if (mode === 'yoy') {
      const data = await getYoyChange(year)
      renderChangeMap(data)
    } else if (mode === 'moran') {
      const result = await getMoran(year, month)
      if (result) renderMoran(result.lisaItems)
    } else if (mode === 'cluster') {
      // cluster mode is handled by ClusterPanel setting data
    }
  } catch (e) {
    console.error('地图数据加载失败', e)
  }
}

function clearDataLayer() {
  if (dataLayer) {
    map.removeLayer(dataLayer)
    dataLayer = null
  }
}

function renderChoropleth(data) {
  dataLayer = L.layerGroup()
  data.forEach(item => {
    if (item.lon && item.lat) {
      const marker = L.circleMarker([item.lat, item.lon], {
        radius: 6,
        fillColor: getPriceColor(item.price),
        color: 'rgba(0,240,255,0.4)',
        weight: 1,
        fillOpacity: 0.85
      }).bindPopup(pricePopup(item), popupOpts)
      marker.on('click', () => store.selectCity(item))
      dataLayer.addLayer(marker)
    }
  })
  dataLayer.addTo(map)
}

function renderBubble(data) {
  dataLayer = L.layerGroup()
  data.forEach(item => {
    if (item.lon && item.lat) {
      const marker = L.circleMarker([item.lat, item.lon], {
        radius: getPriceRadius(item.price),
        fillColor: getPriceColor(item.price),
        color: 'rgba(0,240,255,0.3)',
        weight: 1,
        fillOpacity: 0.65
      }).bindPopup(pricePopup(item), popupOpts)
      marker.on('click', () => store.selectCity(item))
      dataLayer.addLayer(marker)
    }
  })
  dataLayer.addTo(map)
}

function renderProvinceMarkers(data) {
  dataLayer = L.layerGroup()
  data.forEach(item => {
    if (item.centerLon && item.centerLat) {
      const marker = L.circleMarker([item.centerLat, item.centerLon], {
        radius: 14,
        fillColor: getPriceColor(item.avgPrice),
        color: 'rgba(0,240,255,0.5)',
        weight: 2,
        fillOpacity: 0.8
      }).bindPopup(`
        <div class="cyber-popup">
          <div class="popup-title">${item.provinceName}</div>
          <div class="popup-row">城市数 <b>${item.cityCount}</b></div>
          <div class="popup-row">均价 <b class="gold">${item.avgPrice}</b> ¥/㎡</div>
          <div class="popup-row">最低 ${item.minPrice} → 最高 ${item.maxPrice}</div>
          <div class="popup-row">中位数 ${item.medianPrice}</div>
        </div>`, popupOpts)
      dataLayer.addLayer(marker)
    }
  })
  dataLayer.addTo(map)
}

function renderChangeMap(data) {
  dataLayer = L.layerGroup()
  data.forEach(item => {
    if (item.lon && item.lat && item.changePct != null) {
      const marker = L.circleMarker([item.lat, item.lon], {
        radius: 6,
        fillColor: getChangeColor(item.changePct),
        color: 'rgba(255,45,120,0.3)',
        weight: 1,
        fillOpacity: 0.85
      }).bindPopup(changePopup(item), popupOpts)
      marker.on('click', () => store.selectCity(item))
      dataLayer.addLayer(marker)
    }
  })
  dataLayer.addTo(map)
}

function renderMoran(lisaItems) {
  if (!lisaItems) return
  dataLayer = L.layerGroup()
  lisaItems.forEach(item => {
    if (item.lon && item.lat) {
      const marker = L.circleMarker([item.lat, item.lon], {
        radius: 7,
        fillColor: getLisaColor(item.clusterType),
        color: 'rgba(0,240,255,0.3)',
        weight: 1,
        fillOpacity: 0.85
      }).bindPopup(`
        <div class="cyber-popup">
          <div class="popup-title">${item.cityName}</div>
          <div class="popup-row">LISA → <b>${item.clusterType}</b></div>
          <div class="popup-row">Local I: ${item.localI ?? item.locali}</div>
          <div class="popup-row">P-value: ${item.pValue ?? item.pvalue}</div>
        </div>`, popupOpts)
      dataLayer.addLayer(marker)
    }
  })
  dataLayer.addTo(map)
}

// 供外部组件调用
function renderClusterOnMap(items) {
  clearDataLayer()
  dataLayer = L.layerGroup()
  items.forEach(item => {
    if (item.lon && item.lat) {
      const marker = L.circleMarker([item.lat, item.lon], {
        radius: 7,
        fillColor: getClusterColor(item.clusterLabel),
        color: 'rgba(0,240,255,0.3)',
        weight: 1,
        fillOpacity: 0.85
      }).bindPopup(`
        <div class="cyber-popup">
          <div class="popup-title">${item.cityName} <span style="opacity:0.5">${item.provinceName}</span></div>
          <div class="popup-row">聚类 → ${item.clusterDesc || '第' + item.clusterLabel + '类'}</div>
        </div>`, popupOpts)
      dataLayer.addLayer(marker)
    }
  })
  dataLayer.addTo(map)
}

function flyToCity(lon, lat) {
  if (map && lon && lat) {
    map.flyTo([lat, lon], 10, { duration: 1.2 })
  }
}

defineExpose({ flyToCity, renderClusterOnMap, loadMapData })
</script>

<style scoped>
.map-container {
  width: 100%;
  height: 100%;
  background: var(--bg-void);
}
</style>

<style>
/* Leaflet popup cyberpunk overrides */
.leaflet-popup-content-wrapper {
  background: rgba(10, 14, 39, 0.92) !important;
  backdrop-filter: blur(16px);
  border: 1px solid rgba(0, 240, 255, 0.25) !important;
  border-radius: 10px !important;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.5), 0 0 20px rgba(0, 240, 255, 0.08) !important;
  color: #c0c8e8 !important;
}
.leaflet-popup-tip {
  background: rgba(10, 14, 39, 0.92) !important;
  border: 1px solid rgba(0, 240, 255, 0.25) !important;
  border-top: none !important;
  border-left: none !important;
}
.leaflet-popup-close-button {
  color: #5a6590 !important;
  &:hover { color: #ff2d78 !important; }
}
.cyber-popup {
  font-family: 'Noto Sans SC', sans-serif;
  font-size: 13px;
  line-height: 1.8;
  min-width: 160px;
  .popup-title {
    font-weight: 600;
    font-size: 14px;
    color: #00f0ff;
    border-bottom: 1px solid rgba(0,240,255,0.15);
    padding-bottom: 4px;
    margin-bottom: 4px;
  }
  .popup-row {
    color: #8a90b0;
    b { color: #f0f4ff; font-family: 'JetBrains Mono', monospace; }
    .gold { color: #ffd700; }
  }
}

/* Leaflet controls */
.leaflet-bar {
  background: rgba(10,14,39,0.8) !important;
  border: 1px solid rgba(0,240,255,0.2) !important;
  border-radius: 8px !important;
  box-shadow: 0 4px 16px rgba(0,0,0,0.3) !important;
}
.leaflet-bar a {
  background: transparent !important;
  color: #5a6590 !important;
  border-bottom-color: rgba(80,100,160,0.2) !important;
  &:hover { color: #00f0ff !important; background: rgba(0,240,255,0.1) !important; }
}
</style>
