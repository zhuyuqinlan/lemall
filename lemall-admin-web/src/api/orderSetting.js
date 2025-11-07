import request from '@/utils/request'
export function getOrderSetting(id) {
  return request({
    url:'/api/lemall-admin/order/orderSetting/'+id,
    method:'get',
  })
}

export function updateOrderSetting(id,data) {
  return request({
    url:'/api/lemall-admin/order/orderSetting/update/'+id,
    method:'post',
    data:data
  })
}
