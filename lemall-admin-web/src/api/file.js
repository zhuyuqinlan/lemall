import request from '@/utils/request'
import SparkMD5 from 'spark-md5'

async function uploadToLocal(file) {
  // 获取 access code
  const { data: accessCode } = await request.get('/api/lemall-common/file/local/access-code')

  // 计算文件 MD5（前端秒传关键）
  const fileMd5 = await calcFileMd5(file)

  // 检查文件是否已存在（秒传）
  const res = await request.post('/api/lemall-common/file/local/check', null, {
    params: { md5: fileMd5, accessCode },
  })

  if (res.data.exist) {
    // 秒传成功，直接返回文件信息
    console.log('秒传成功：', res.data)
    return res
  }

  // 文件不存在，执行上传
  const formData = new FormData()
  formData.append('file', file)
  formData.append('uploadCode', res.data.uploadCode)
  return await request.post('/api/lemall-common/file/local/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

// minio 上传
async function uploadToMinio(file) {
  const formData = new FormData();
  formData.append('file', file);
  return request.post('/api/minio/upload', formData)
}

// 阿里云oss上传
async function uploadToOSS(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/aliyun/oss/policy', formData)
}

// 根据 localStorage 自动选择上传方式
export function uploadFile(file) {
  if (!file || !(file instanceof Blob)) {
    return reject(new Error('传入的不是有效文件'))
  }
  const type = localStorage.getItem('uploadType') || 'local'

  switch (type) {
    case 'oss':
      return uploadToOSS(file)
    case 'local':
      return uploadToLocal(file)
    case 'minio':
      return uploadToMinio(file)
    default:
      return Promise.reject(new Error('未知上传方式'))
  }
}

function calcFileMd5(file) {
  return new Promise((resolve, reject) => {
    const chunkSize = 2 * 1024 * 1024 // 每片2MB
    const chunks = Math.ceil(file.size / chunkSize)
    let currentChunk = 0
    const spark = new SparkMD5.ArrayBuffer()
    const fileReader = new FileReader()

    fileReader.onload = e => {
      spark.append(e.target.result)
      currentChunk++
      if (currentChunk < chunks) {
        loadNext()
      } else {
        resolve(spark.end()) // 返回最终 MD5 字符串
      }
    }

    fileReader.onerror = e => {
      console.error('FileReader 错误', e)
      reject('读取文件失败')
    }

    function loadNext() {
      const start = currentChunk * chunkSize
      const end = Math.min(start + chunkSize, file.size)
      fileReader.readAsArrayBuffer(file.slice(start, end))
    }

    loadNext()
  })
}
