import http from './index'

export const getPriceRank = (year, month, order = 'desc', limit = 20) =>
  http.get('/rank/price', { params: { year, month, order, limit } })

export const getChangeRank = (startYear, startMonth, endYear, endMonth, order = 'desc', limit = 20) =>
  http.get('/rank/change', { params: { startYear, startMonth, endYear, endMonth, order, limit } })
