import http from './index'

export const getCityGroupList = () =>
  http.get('/citygroup/list')

export const getCityGroupStats = (year, month, groupId) =>
  http.get('/citygroup/stats', { params: { year, month, groupId } })
