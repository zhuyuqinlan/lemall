import request from '@/utils/request'
export function fetchList(params) {
  return request({
    url:'/api/lemall-admin/sale/flashProductRelation/list',
    method:'get',
    params:params
  })
}
export function createFlashProductRelation(data) {
  return request({
    url:'/api/lemall-admin/sale/flashProductRelation/create',
    method:'post',
    data:data
  })
}
export function deleteFlashProductRelation(id) {
  return request({
    url:'/api/lemall-admin/sale/flashProductRelation/delete/'+id,
    method:'post'
  })
}
export function updateFlashProductRelation(id,data) {
  return request({
    url:'/api/lemall-admin/sale/flashProductRelation/update/'+id,
    method:'post',
    data:data
  })
}
