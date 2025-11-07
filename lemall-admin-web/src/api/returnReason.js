import request from '@/utils/request'
export function fetchList(params) {
  return request({
    url:'/api/lemall-admin/order/returnReason/list',
    method:'get',
    params:params
  })
}

export function deleteReason(params) {
  return request({
    url:'/api/lemall-admin/order/returnReason/delete',
    method:'post',
    params:params
  })
}

export function updateStatus(params) {
  return request({
    url:'/api/lemall-admin/order/returnReason/update/status',
    method:'post',
    params:params
  })
}

export function addReason(data) {
  return request({
    url:'/api/lemall-admin/order/returnReason/create',
    method:'post',
    data:data
  })
}

export function getReasonDetail(id) {
  return request({
    url:'/api/lemall-admin/order/returnReason/'+id,
    method:'get'
  })
}

export function updateReason(id,data) {
  return request({
    url:'/api/lemall-admin/order/returnReason/update/'+id,
    method:'post',
    data:data
  })
}
