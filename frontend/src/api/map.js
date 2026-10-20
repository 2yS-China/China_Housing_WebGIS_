import http from './index'

export const getChoropleth = (year, month) =>
  http.get('/map/choropleth', { params: { year, month } })

export const getProvinceAggregation = (year, month) =>
  http.get('/map/province', { params: { year, month } })

export const getBubble = (year, month) =>
  http.get('/map/bubble', { params: { year, month } })

export const getMomChange = (year, month) =>
  http.get('/map/change/mom', { params: { year, month } })

export const getYoyChange = (year) =>
  http.get('/map/change/yoy', { params: { year } })
