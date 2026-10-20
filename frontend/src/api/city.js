import http from './index'

export const searchCity = (keyword) =>
  http.get('/city/search', { params: { keyword } })

export const getCityDetail = (cityCode) =>
  http.get(`/city/${cityCode}`)

export const getCityTrend = (cityCode) =>
  http.get(`/city/${cityCode}/trend`)

export const compareCities = (codes) =>
  http.get('/city/compare', { params: { codes } , paramsSerializer: { indexes: null } })

export const filterCities = (param) =>
  http.post('/city/filter', param)
