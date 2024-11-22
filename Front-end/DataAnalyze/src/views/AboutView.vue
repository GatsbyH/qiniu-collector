<template>
  <div class="main" v-loading="loading"
       element-loading-text="加载中..."
       element-loading-background="rgba(255, 255, 255, 0.8)">
      <div class="contain flex-a-center">

        <div class="left">
           <div class="search-head flex-a-center">
            <div class="head-title item-label">
             <span class="title-prefix"></span>
             开发者筛选
           </div>
           <button class="sd-button-text flex-a-center" @click="clearFilters">
             <span style="margin-right:5px;">清除</span>
             <el-icon>
               <circle-close-filled />
             </el-icon>
           </button>
           </div>
          <div class="search-result">
            <label class="sd-label">
              <input
                v-model="searchName"
                placeholder="搜索开发者"
                class="sd-input"
                @keyup.enter="handleSearch"
              />
              <span class="sd-input-addon" @click="getfuzzySearch()">
              <el-icon>
                <Search />
              </el-icon>
              </span>
            </label>
          </div>
          <div class="search-type">
            <div class="item-label">开发者领域</div>
            <el-checkbox
              v-for="item in fields"
              :key="item"
              :v-model="selectedField == item"
              :label="item"
              @change="handleFieldChange(item)"
              class="checkbox"
            ></el-checkbox>
          </div>
<!--           <div class="search-type">-->
<!--               <div class="item-label">语言</div>-->
<!--                <ul>-->
<!--                  <li v-for="item in languages" :key="item" class="list-lang-item">{{ item }}</li>-->
<!--                </ul>-->
<!--           </div>-->
           <div class="search-nation">
             <div class="item-label">所属国家</div>
             <!-- <el-radio-group v-model="currentNation" @change="handleNationChange">
               <el-radio
                 v-for="item in selectedNation.value"
                 :key="item"
                 :label="item"
                 class="radio"
               >{{ item }}</el-radio>
             </el-radio-group> -->
             <el-select v-model="countryValue" placeholder="请选择">
              <el-option
              v-for="item in countriesOption"
              :key="item.value"
              :label="item.label"
              :value="item.value"
             >
    </el-option>
  </el-select>
           </div>

        </div>

        <div class="right">
           <div class="search-sub-header">
           </div>
           <ul>
             <template v-if="userData.data.length > 0">
            <li v-for="item in userData.data" :key="item.login" class="list-item flex-a-center" @click="changeAssessView(item.login)">
              <img  :src="item.avatarUrl" width="40" height="40" style="border-radius: 50%; margin-left:5px;">
              <a  :href="item.htmlUrl" class="list-item-a list-item-name">{{ item.login }}</a>
              <span v-if="item.talentRank" class="list-item-a item-rank" >{{ item.talentRank }}</span>
            </li>
          </template>
           </ul>
           <div v-if="userData.data.length == 0" class="zero-data ">
              <div>
                <h2 style="margin-bottom: 100px;">Your search did not match any users</h2>
               <img src="../assets/light.png">
              </div>
              <div>

              </div>
           </div>
           <div class="block">
              <el-pagination
                v-if="number > 0"
                layout="prev, pager, next"
                @prev-click ="handlePrevClick"
                @next-click = "handleNextClick"
                @current-change="handleCurrentClick"
                :total="number">
              </el-pagination>
  </div>
        </div>
      </div>
  </div>
</template>


<script setup>
import { shallowReactive, reactive, ref, getCurrentInstance, onMounted,watch } from 'vue'
import { useRoute,useRouter} from 'vue-router'
import api from '../api/index'
const loading = ref(false)
 let countriesOption = [

];
const fields = ref('')
let number = ref(0)
const searchName = ref('') // 开发者名字
const countryValue = ref("")
const route = useRoute(); // 当前路由信息
const router = useRouter()
const selectedField = ref('') // 用于存储选中的值
const languages = ['HTML','Python','JavaScript','Java','C++','PHP','C#','C']
let userData = reactive({
  data:[]
})


 onMounted(()=>{
    searchName.value = useRoute().query.username
    getfuzzySearch(searchName.value)
    api.getDeveloperFields().then(res=>{
      console.log("返回的领域",res.data.data)
      fields.value = res.data.data
    })
  })
   // 更新 URL 中的 username 参数
   const updateUsername = () => {
      router.replace({
        path: route.path,
        query: {
          ...route.query,
          username: searchName.value || undefined, // 移除空值
        },
      });
    };

    // 监听路由中的 username 查询参数变化，更新输入框
    watch(
      () => route.query.username,
      (newUsername) => {
        searchName.value = newUsername || '';
      }
    );

// 修改清除过滤器的函数
const clearFilters = () => {
  // 清除所有选中状态
  // Object.keys(selectedNations).forEach(key => {
  //   selectedNations[key] = false
  // })
  countryValue.value = ''
  selectedField.value = ''
  searchName.value = ''
  queryParams.nation = ''
  queryParams.field = ''
  handleSearch()
}

watch(countryValue,(newCountry,oldCountry)=>{
  console.log("国家变化",newCountry)
  queryParams.pageNum= 1
  queryParams.nation = newCountry
  handleSearch()
})



let queryParams = {
  field: '',
  nation: '',
  pageNum: 1,
  pageSize: 10
}


const handleFieldChange = async (item) => {
  try {
    loading.value = true
    queryParams.nation = ''
    searchName.value = ''
    updateUsername()
    //不能重复选择
    selectedField.value = selectedField.value == item ? '' : item;
    // 更新领域
    queryParams.field = selectedField.value

    // 先获取新的国家选项
    if (queryParams.field) {
      const nationRes = await api.getDeveloperNationOptionsByField(queryParams).finally(
        () => {
          // 清除过滤器
          loading.value = false
        }
      )
      countriesOption = nationRes.data.data
    }

    // 然后搜索开发者
    await handleSearch()
  } catch (error) {
    console.error('处理领域变化出错:', error)
  } finally {
    loading.value = false
  }
};
const selectedNations = reactive({}) // 用于存储选中状态

const handleSearch = () => {
  // 这里调用您的搜索 API，传入搜索关键词和选中的开发者类型
  console.log('选中的开发者类型:', selectedField.value)
  queryParams.field = selectedField.value
  console.log("queryParams",queryParams)
  loading.value = true
  updateUsername()
  api.getDevelopersPage(queryParams).then(res => {
    userData.data = res.data.data.list
    number.value = res.data.data.total
    // getDeveloperNationOptionsByField(queryParams).then(
    //   res => {
    //     console.log("nation",res.data.data)
    //     selectedNation.value = res.data.data
    //     // 重置选中状态
    //     Object.keys(selectedNations).forEach(key => {
    //       selectedNations[key] = false
    //     })
    //   }
    // )
    console.log(" handleSearch",res)
  }).finally(
    () => {
      loading.value = false
    }
  )
}


  const { appContext : { config: { globalProperties} } } = getCurrentInstance()
  const  getfuzzySearch = async ()=>{
    updateUsername()
    const result = await api.fuzzySearch(searchName.value)
    const data = result.data.data.list
    console.log("319",result.data.data.list)
    number.value = data.length
    userData.data = data
    console.log("getfuzzySearch",userData)
  }

  const handlePrevClick = async (page)=>{
    console.log('page',await api.getDevelopers(page))
    console.log("prevClick",page)
  }
  const handleCurrentClick = async (page)=>{
    // const result = await getDevelopers(page)
    queryParams.pageNum = null
    queryParams.pageNum = page
    console.log("changePage queryParams.pageNum",queryParams.pageNum)
    loading.value = true
    api.getDevelopersPage(queryParams).then(res => {
      console.log("queryParams changePage",queryParams)
      userData.data = res.data.data.list
      number.value = res.data.data.total
      console.log("res",res)
    }).finally(
      () => {
        loading.value = false
      }
    )
  }
  const handleNextClick = async (page)=>{
    // reset()
    // const result = await getDevelopers(page+1)
    queryParams.pageNum += 1;
    handleSearch()
    // userData.data =[...result.data.data.list]
  }

  const changeAssessView = (user)=>{
     router.push({path:'/assess',query:{username:user}})
  }


</script>
<style scoped>
.radio-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.radio-item {
  height: 32px;
  margin: 0;
}


.radio-item:hover {
  color: #0068FF;
}
.main{
  height: 100%;
}
.contain{
  height: 100%;
}
.nav{
  width: 100%;
  height: 64px;
}
.left{
  width: 200px;
  height: calc(100% - 120px);
  margin: 20px;
}
.right{
  flex:1;
  height: 100%;
}
.searchBar{
    border-radius: 24px;
    box-shadow: 0px 0px 5px rgb(212, 212, 212);
    width: 746px;
    height: 48px;
    margin-left: 20px;
}
.icon{
  display: inline;
  margin-left: 10px;

}
.searchBar input{
  height: 48px;
  outline: none;
  border: none;
  margin-left: 10px;
  width: 90%;
  color: rgb(112, 112, 112);
}
.search-sub-header{
  height: 40px;
}
.list-item{
  position: relative;
  min-height:80px;
  list-style: none;
  border: 1px solid #d1d9e0;
  width: 80%;
  margin-bottom: 10px;
}
.item-rank{
 padding-right: 10px;
 position: absolute;
 right: 0;
}
.title-prefix{
  width: 4px;
  height: 16px;
  font-weight: 900;
  display:inline-block;
  border-radius: 100px;
  margin-right: 10px;
  background-color:#0068FF ;
}
.item-label{
  display: flex;
  align-items: center;
  font-size: 18px;
  color:#141933;
  font-weight: 500;
  height: 40px;
}
.list-item-name{
  padding: 0 20px;
}
.search-head{
  display:flex;
  justify-content: space-between;
  height:24px;
}
.sd-button-text{
  padding:0;
  border-color: transparent;
  background-color: transparent;
  color: #8589a6
}
.sd-button-text:hover{
  color:#0068FF;
  cursor: pointer;
}
.sd-spacing{
  margin-top:10px;
  color: #8589a6;
  font-size:14px;
}
.sd-input{
    width: 100%;
    height: 40px;
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
    padding-left: 8px;
    border: none;
    outline:none;
    border: 1px solid #dadce5;
    background-color: transparent;
    vertical-align: top;
    -webkit-border-radius: 8px;
    -moz-border-radius: 8px;
    border-radius: 8px;
}
.search-result{
  margin: 20px auto;
}
.sd-label{
  position: relative;
}
.sd-input-addon{
    width: 16px;
    height: 100%;
    line-height: 40px;
    position:absolute;
    top: 0;
    right: 8px;
    cursor: pointer;
}
.sd-input-addon:hover{
  color:#0068FF;
}
.list-lang-item:hover{
  color:#0068FF;
  cursor: pointer;
}
.list-lang-item{
  height: 20px;
  cursor: pointer;
}

.item-label li:nth-of-type(odd){
   background:#00ccff;
  }
.item-label li:nth-of-type(even){
  background:#ffcc00;
 }
 .list-item:hover{
  cursor: pointer;
 }
 .list-item-a{
  text-decoration: none;
  color:black;
  text-align: right;
  font-size: 18px;

 }
 .list-item-a:hover{
  text-decoration: underline;
  color:#0068FF;
 }
 .checkbox{
  display: block;
 }
 .block{
  padding:40px;
  height: 50px;
 }
 .zero-data div{
  margin: 0 auto;
  text-align: center;
 }
 .zero-data{
  display: flex;
  align-items: center;
  height: 710px;
 }
</style>
