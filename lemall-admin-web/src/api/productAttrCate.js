import request from '@/utils/request'
export function fetchList(params) {
  return request({
    url:'/api/lemall-admin/productAttribute/category/list',
    method:'get',
    params:params
  })
}

export function createProductAttrCate(data) {
  return request({
    url:'/api/lemall-admin/productAttribute/category/create',
    method:'post',
    data:data
  })
}

export function deleteProductAttrCate(id) {
  return request({
    url:'/api/lemall-admin/productAttribute/category/delete/'+id,
    method:'get'
  })
}

export function updateProductAttrCate(id,data) {
  return request({
    url:'/api/lemall-admin/productAttribute/category/update/'+id,
    method:'post',
    data:data
  })
}
export function fetchListWithAttr() {
  return request({
    url:'/api/lemall-admin/productAttribute/category/list/withAttr',
    method:'get'
  })
}
