import request from '@/utils/request'
export function fetchList(params) {
  return request({
    url:'/api/lemall-admin/sale/coupon/list',
    method:'get',
    params:params
  })
}

export function createCoupon(data) {
  return request({
    url:'/api/lemall-admin/sale/coupon/create',
    method:'post',
    data:data
  })
}

export function getCoupon(id) {
  return request({
    url:'/api/lemall-admin/sale/coupon/'+id,
    method:'get',
  })
}

export function updateCoupon(id,data) {
  return request({
    url:'/api/lemall-admin/sale/coupon/update/'+id,
    method:'post',
    data:data
  })
}

export function deleteCoupon(id) {
  return request({
    url:'/api/lemall-admin/sale/coupon/delete/'+id,
    method:'post',
  })
}
