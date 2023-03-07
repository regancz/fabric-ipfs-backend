import request from '@/utils/request'

export function queryAccountList() {
  return request({
    url: '/queryAccountList',
    method: 'post'
  })
}

export function login(data) {
  return request({
    url: '/queryAccountList',
    method: 'post',
    data
  })
}
