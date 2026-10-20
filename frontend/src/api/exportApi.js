import axios from 'axios'
import { saveAs } from 'file-saver'
import { ElMessage } from 'element-plus'

export const exportExcel = async (param) => {
  try {
    const res = await axios.post('/api/export/excel', param, {
      responseType: 'blob',
      timeout: 60000
    })
    const blob = new Blob([res.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    saveAs(blob, '房价筛选结果.xlsx')
    ElMessage.success('导出成功')
  } catch {
    ElMessage.error('导出失败')
  }
}
