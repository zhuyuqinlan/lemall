import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/api/lemall-admin/system/resource/list',
    method: 'get',
    params: params
  })
}

export function createResource(data) {
  return request({
    url: '/api/lemall-admin/system/resource/create',
    method: 'post',
    data: data
  })
}

export function updateResource(id, data) {
  return request({
    url: '/api/lemall-admin/system/resource/update/' + id,
    method: 'post',
    data: data
  })
}

export function deleteResource(id) {
  return request({
    url: '/api/lemall-admin/system/resource/delete/' + id,
    method: 'post'
  })
}

export function fetchAllResourceList() {
  return request({
    url: '/api/lemall-admin/system/resource/listAll',
    method: 'get'
  })
}
