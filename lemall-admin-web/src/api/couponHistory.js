import request from '@/utils/request'
export function fetchList(params) {
  return request({
    url:'/api/lemall-admin/sale/couponHistory/list',
    method:'get',
    params:params
  })
}
