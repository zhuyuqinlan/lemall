import request from '@/utils/request'

export function listAllCate() {
  return request({
    url: '/api/lemall-admin/system/resourceCategory/listAll',
    method: 'get'
  })
}

export function createResourceCategory(data) {
  return request({
    url: '/api/lemall-admin/system/resourceCategory/create',
    method: 'post',
    data: data
  })
}

export function updateResourceCategory(id, data) {
  return request({
    url: '/api/lemall-admin/system/resourceCategory/update/' + id,
    method: 'post',
    data: data
  })
}

export function deleteResourceCategory(id) {
  return request({
    url: '/api/lemall-admin/system/resourceCategory/delete/' + id,
    method: 'post'
  })
}
