import request from '@/utils/request'
export function fetchList() {
  return request({
    url:'/api/lemall-admin/content/prefrenceArea/listAll',
    method:'get',
  })
}
