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
      },
     fuzzySearch(userName){
        return request({
            method: 'get',
            url: '/getGithubDevelopers',
            data:{
                username: userName
            }
        })
     },
     getDeveloperFields(){
        return request({
          url: '/getDeveloperFields'
        })
      },
     getDevelopersPage(params){
        return request({
          url: '/getDevelopersByFields',
          data:{
            nation:params.nation,
            field:params.field,
            page: params.page,
            pageSize: params.pageSize,
            pageNum: params.pageNum
          }
        })
      },
      
      getDeveloperTechnicalAbility(username){
        return request({
          url: '/getDeveloperTechnicalAbility',
          data:{
            username:username
          }
        })
      },
      getDeveloperNation(username){
        return request({
          url: '/getDeveloperNation',
          data:{
            username:username
          }
        })
      },
      
     getDeveloperNationOptionsByField(params){
        return request({
          url: '/getDeveloperNationOptionsByField',
          data:{
            field:params.field,
          }
        })
      }
}
