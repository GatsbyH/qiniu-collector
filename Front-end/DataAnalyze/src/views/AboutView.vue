<template>
<!--  <div class="main"  >-->
  <div class="main" v-loading="loading"
       element-loading-text="加载中..."
       element-loading-background="rgba(255, 255, 255, 0.8)">

    <!-- <div class="nav flex-a-center">
      <div class="icon">
        <el-icon>
         <fold />
        </el-icon>
      </div>
        <div class="searchBar">
        <div class="icon">
        <search style="  width: 20px;height: 20px; vertical-align: middle; " />
        </div>
        <input type="text" v-model="query" @keyup.enter="find">
      </div>
      </div> -->
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
           <!-- <div class="sd-spacing">
             <span>
               已选
               <span></span>
               条件
             </span>
             <span>|</span>
             <span>
              <span></span>
              结果
             </span>
           </div> -->
          <div class="search-result">
            <div class="item-label">开发者搜索</div>
            <label class="sd-label">
              <input
                v-model="searchQuery"
                placeholder="搜索开发者"
                class="sd-input"
                @keyup.enter="handleSearch"
              />
              <span class="sd-input-addon" @click="handleSearch">
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
              v-model="selectedFields"
              :label="item"
              @change="handleFieldChange"
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
<!--             <div class="item-label">开发者国家</div>-->
<!--             <el-checkbox-->
<!--               v-for="item in selectedNation.value"-->
<!--               :key="item"-->
<!--               v-model="selectedNations[item]"-->
<!--               :label="item"-->
<!--               class="checkbox"-->
<!--               @change="handleNationChange"-->
<!--             ></el-checkbox>-->
             <div class="item-label">开发者国家</div>
             <el-radio-group v-model="currentNation" @change="handleNationChange">
               <el-radio
                 v-for="item in selectedNation.value"
                 :key="item"
                 :label="item"
                 class="radio"
               >{{ item }}</el-radio>
             </el-radio-group>
           </div>

        </div>

        <div class="right">
           <div class="search-sub-header">
             <!-- <div class="count">
               <span>{{ number }}results</span>
             </div>
             <div class="sort">
             </div> -->
           </div>
           <ul>
            <li v-for="item in userData.data" :key="item.login" class="list-item flex-a-center" @click="changeAssessView(item.login)">
              <img v-if="item.avatar_url" :src="item.avatar_url" width="40" height="40" style="border-radius: 50%; margin-left:5px;">
              <img v-if="item.avatarUrl" :src="item.avatarUrl" width="40" height="40" style="border-radius: 50%; margin-left:5px;">
              <a v-if="item.htmlUrl" :href="item.htmlUrl" class="list-item-a">{{ item.login }}</a>
              <a v-if="item.html_url"  :href="item.html_url" class="list-item-a">{{ item.login }}</a>
              <span v-if="item.talentRank" class="list-item-a">{{ item.talentRank }}</span></li>
           </ul>
           <div class="block">
    <el-pagination
     layout="prev, pager, next"
     @current-change="handleCurrentClick"
    :total="number">
    </el-pagination>

<!--             @prevClick="handlePrevClick"-->
<!--             @next-click="handleNextClick"-->
  </div>
        </div>
      </div>
  </div>
</template>


<script setup>
import { shallowReactive, reactive, ref, getCurrentInstance, onMounted } from 'vue'
import { useRoute,useRouter} from 'vue-router'
import { fuzzySearch, getDeveloperFields, getDeveloperNationOptionsByField, getDevelopersPage } from '../Utils/request'
const loading = ref(false)



import { watch } from 'vue';
const router = useRouter()
const queryName = useRoute().query.username
let userData = reactive({
  data:[]
})

const selectedFields = ref([]) // 用于存储选中的值
const languages = ['HTML','Python','JavaScript','Java','C++','PHP','C#','C']
const nationArrs = [
  '新加坡',
  '中国',
  '英国',
  '美国',
  '澳大利亚',
  '德国'
]
const currentNation = ref('')

// 修改处理国家选择变化的函数
const handleNationChange =  (nation) => {
  queryParams.pageNum = 1
  // // 更新当前选中的国家
  // currentNation.value = nation
  // console.log('当前选中的国家：', currentNation.value)
  // // 重置其他国家的选中状态
  // Object.keys(selectedNations).forEach(key => {
  //   if (key !== nation) {
  //     selectedNations[key] = false
  //   }
  // })
  // // 更新查询参数
  // queryParams.nation = currentNation.value
  // handleSearch()
  try {
    // 更新当前选中的国家
    currentNation.value = nation
    // 更新查询参数
    queryParams.nation = nation
    // 执行搜索
    handleSearch()
  } catch (error) {
    console.error('处理国家变化出错:', error)
  }
}

// 修改清除过滤器的函数
const clearFilters = () => {
  // 清除所有选中状态
  Object.keys(selectedNations).forEach(key => {
    selectedNations[key] = false
  })
  currentNation.value = '' // 清除当前选中的国家
  selectedFields.value = []
  searchQuery.value = ''
  queryParams.nation = ''
  queryParams.field = ''
  handleSearch()
}


let selectedNation = reactive({
  value:{}
})
const searchQuery = ref('') // 添加搜索关键词
// 监听选择变化，确保只能选中一个
watch(selectedFields, (newVal) => {
  if (newVal.length > 1) {
    // 只保留最后选中的值
    selectedFields.value = [newVal[newVal.length - 1]]
    handleSearch()
  }
})


let queryParams = {
  field: '',
  nation: '',
  pageNum: 1,
  pageSize: 10
}


const handleFieldChange = async () => {
  // // 清除所有选中状态
  // Object.keys(selectedNations).forEach(key => {
  //   selectedNations[key] = false
  // })
  // selectedNation.value = {}
  // queryParams.nation = ''
  // currentNation.value = '' // 清除当前选中的国家
  // handleSearch();
  try {
    loading.value = true
    // 清除国家选择
    currentNation.value = ''
    selectedNation.value = {} // 如果没有选择领域，清空国家选项

    queryParams.nation = ''
    // 更新领域
    queryParams.field = selectedFields.value[0]


    // 先获取新的国家选项
    if (queryParams.field) {
      const nationRes = await getDeveloperNationOptionsByField(queryParams).finally(
        () => {
          // 清除过滤器
          loading.value = false
        }
      )
      selectedNation.value = nationRes.data.data
    } else {
      selectedNation.value = {} // 如果没有选择领域，清空国家选项
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
  console.log('搜索关键词:', searchQuery.value)
  console.log('选中的开发者类型:', selectedFields.value[0])
  queryParams.field = selectedFields.value[0]
  // queryParams.nation = selectedNation.value
  console.log("queryParams",queryParams)
  loading.value = true
  getDevelopersPage(queryParams).then(res => {
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
    console.log("res",res)
  }).finally(
    () => {
      loading.value = false
    }
  )
}



const fields = ref('')
let number = ref(0)
const { appContext : { config: { globalProperties} } } = getCurrentInstance()
  console.log("context",globalProperties.$api)
  const getDevelopers = globalProperties.$api.getDevelopers
  const  find = async ()=>{
     console.log(queryName)
    const result = await fuzzySearch(queryName)
    const data = result.data.items
    number.value = data.length
    userData.data = data.slice(0,8)
    console.log("result",userData)
  }
  find()
  const handlePrevClick = async (page)=>{
    console.log('page',await getDevelopers(page))
    console.log("prevClick",page)
  }
  const handleCurrentClick = async (page)=>{
    // const result = await getDevelopers(page)
    queryParams.pageNum = null
    queryParams.pageNum = page
    console.log("changePage queryParams.pageNum",queryParams.pageNum)
    loading.value = true
    getDevelopersPage(queryParams).then(res => {
      console.log("queryParams changePage",queryParams)
      userData.data = res.data.data.list
      number.value = res.data.data.total
      console.log("res",res)
    }).finally(
      () => {
        loading.value = false
      }
    )
    // userData.data =[...result.data.data.list]
    // number.value = result.data.data.total
    console.log('page',result)
    console.log("changePage",page)
    console.log(userData)
  }
  const handleNextClick = async (page)=>{
    // reset()
    // const result = await getDevelopers(page+1)
    queryParams.pageNum += 1;
    handleSearch()
    // userData.data =[...result.data.data.list]
    console.log('page',result)
    console.log("changePage",page)
    console.log(userData)
  }
  watch(()=>userData.data,()=>{
    console.log("监听到了变化")
  },{
    deep:true
  })
  const changeAssessView = (user)=>{
     router.push({path:'/assess',query:{username:user}})
  }
  onMounted(()=>{
    getDeveloperFields().then(res=>{
      console.log("res",res.data.data)
      fields.value = res.data.data
      console.log("fields",fields.value[1])
    })
  })

</script>
<style scoped>
.radio-group {
  display: flex;
  flex-direction: column;
  gap: 12px;  /* 控制选项之间的间距 */
}

.radio-item {
  height: 32px;  /* 控制每个选项的高度 */
  margin: 0;     /* 清除默认边距 */
}

/* 可选：鼠标悬停效果 */
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
  /* border-left: 1px solid #d4d3d3; */
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
  min-height:80px;
  list-style: none;
  border: 1px solid #d1d9e0;
  width: 80%;
  margin-bottom: 10px;
}
.title-prefix{
  width: 4px;
  height: 16px;
  font-weight: 900;
  display:inline-block;
  border-radius: 100px;
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
  margin-left:10px;
  font-size: 18px;

 }
 .list-item-a:hover{
  text-decoration: underline;
  color:#0068FF;
 }
 .checkbox{
  display: block;
 }
</style>
