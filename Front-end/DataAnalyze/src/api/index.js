/**
 * 统一把api 集中管理
 */
import { request } from '../Utils/request'
export default {

   getRank(params) {
        return request({
            method: 'get',
            url: '/GraphQL/getTalentRankByUserName',
            data: {username:params},
        })
    },

    getDevelopers(page){
        return request({
            url: '/getDevelopersByFields',
            data:{
                field: 'deeplearning',
                page: page,
                pageSize: 8
            }
        })
    },
    getDeveloperLanguages(username){
        return request({
            url: '/GraphQL/getDeveloperLanguage',
            data:{
                username:username
            }
        })
    },
    getDeveloperField(username){
        return request({
           url: '/GraphQL/getDeveloperFiled',
           data:{
            username:username
           }
        })
    },
    getDeveloperRepos(userName){
        return request({
          method: 'get',
          url: `/getGithubUserRepos`,
          data:{
            username: userName
          }
        })
      }

}
