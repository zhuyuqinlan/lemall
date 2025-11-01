import request from '@/utils/request'
import SparkMD5 from 'spark-md5'

async function uploadToLocal(file) {
  // 获取 access code
  const {data: accessCode} = await request.get('/api/lemall-common/file/local/access-code')

  // 计算文件 MD5（前端秒传关键）
  const fileMd5 = await calcFileMd5(file)

  // 检查文件是否已存在（秒传）
  const res = await request.post('/api/lemall-common/file/local/check', null, {
    params: {md5: fileMd5, accessCode},
  })

  if (res.data.exist) {
    // 秒传成功，直接返回文件信息
    console.log('秒传成功：', res.data)
    return res
  }

  // 文件不存在，执行上传
  const formData = new FormData()
  formData.append('file', file)
  formData.append('uploadId', res.data.uploadId)
  return await request.post('/api/lemall-common/file/local/upload', formData, {
    headers: {'Content-Type': 'multipart/form-data'},
  })
}

async function uploadToMinio(file) {
  // 1. 获取 access code
  const {data: accessCode} = await request.get('/api/lemall-common/file/minio-public/access-code')

  // 2. 计算 MD5
  const fileMd5 = await calcFileMd5(file)
  const {name: fileName, type: contentType} = file

  // 3. 获取上传凭证
  const res = await request.post('/api/lemall-common/file/minio-public/upload-url', null, {
    params: {md5: fileMd5, accessCode, fileName, contentType}
  })

  // 4. 秒传
  if (res.data.exist) {
    console.log('秒传成功：', res)
    return res
  }

  // 5. 上传文件
  const {uploadId, parts} = res.data
  const part = parts[0]

  const uploadRes = await fetch(part.url, {
    method: 'PUT',
    body: file
  })

  if (!uploadRes.ok) throw new Error('上传失败')

  // 6. 通知后端合并
  return await request.post('/api/lemall-common/file/minio-public/complete', null, {
    params: {uploadId}
  })
}

// 阿里云oss上传
async function uploadToOSS(file) {
  // TODO 阿里云单文件上传
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
