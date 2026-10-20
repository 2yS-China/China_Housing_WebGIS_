import http from './index'

export const getProvinceList = () =>
  http.get('/province/list')

export const getProvinceStats = (provinceName, year, month) =>
  http.get('/province/stats', { params: { provinceName, year, month } })
