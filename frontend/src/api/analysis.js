import http from './index'

export const getMoran = (year, month) =>
  http.get('/analysis/moran', { params: { year, month } })

export const getClusterTasks = () =>
  http.get('/analysis/cluster/tasks')

export const getCluster = (taskName) =>
  http.get('/analysis/cluster', { params: { taskName } })
