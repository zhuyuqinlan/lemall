import request from '@/utils/request'

// 本地上传
async function uploadToLocal(file) {
  await request.get('/api/lemall-common/file/local/access-code')
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/lemall-common/file/local/upload', formData)
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
