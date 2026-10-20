import axios from 'axios'
import { ElMessage } from 'element-plus'

const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE,
  timeout: 30000
})

http.interceptors.response.use(
  (res) => {
    if (res.data.code === 200) {
      return res.data.data
    }
    ElMessage.error(res.data.msg || '请求失败')
    return Promise.reject(res.data)
  },
  (err) => {
    ElMessage.error(err.message || '网络异常')
    return Promise.reject(err)
  }
)

export default http
