/*
* axios 二次封装
*/

import axios from 'axios'
import {ElMessage} from 'element-plus'
import router from '../router'
const TOKEN_INVALID  = 'Token认证失败,请重新登录'
const NETWORK_ERROR = '网络请求异常,请稍后重试'
//创建 axios 实例对象 ，添加全局配置
const service = axios.create({
       
 baseURL: "http://106.54.234.202:8080",
 timeout: 8000
})

export const fuzzySearch = function (userName){
   return axios({
      method: 'get',
      url: `https://api.github.com/search/users?q=${userName}`
   }) 
}
export const getDeveloperAurl = function(userName){
   return axios({
      method: 'get',
      url: `https://api.github.com/users/${userName}`
   })
}
export const getDeveloperFollower = function(userName){
   return axios({
      method: 'get',
      url:  `https://api.github.com/users/${userName}/followers`
   })
}
// //请求拦截
// service.interceptors.request.use((req)=>{
//     //TO-DO
//     const headers = req.headers;
//     if(!headers.Authorization) headers.Authorization = 'Bear Jack'
//     return req;
// })

//响应拦截

/**
 * 请求核心函数
 * @param {*} options 请求配置
 */

export function request(options){
    console.log('request.options',options)
    options.method = options.method || 'get'
 if(options.method.toLowerCase() === 'get')
 {
    options.params = options.data;
 }
console.log(service(options))
 return service(options);
}

// ['get','post','put','delete','patch'].forEach((item)=>{
//     request[item] = (url,data,options)=>{
//      return request({
//         url,
//         data :data,
//         method: item,
//         ...options
//         })
//     }
// })