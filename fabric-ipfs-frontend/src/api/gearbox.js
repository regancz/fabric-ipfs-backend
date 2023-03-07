import request from '@/utils/request'

export function createGearbox(data) {
    return request({
        url: '/createGearbox',
        method: 'post',
        data
    })
}

export function queryGearboxById(data) {
    return request({
        url: '/queryGearboxById',
        method: 'get',
        data
    })
}

export function queryAllGearboxes() {
    return request({
        url: '/queryAllGearboxes',
        method: 'get'
    })
}