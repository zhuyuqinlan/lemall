import request from '@/utils/request'
export function fetchList(params) {
  return request({
    url:'/api/lemall-admin/system/memberLevel/list',
    method:'get',
    params:params
  })
}
