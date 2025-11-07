import request from '@/utils/request'
export function fetchList() {
  return request({
    url:'/api/lemall-admin/order/companyAddress/list',
    method:'get'
  })
}
