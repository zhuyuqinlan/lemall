import request from '@/utils/request'
export function fetchListAll() {
  return request({
    url:'/api/lemall-admin/content/subject/listAll',
    method:'get',
  })
}

export function fetchList(params) {
  return request({
    url:'/api/lemall-admin/content/subject/list',
    method:'get',
    params:params
  })
}
