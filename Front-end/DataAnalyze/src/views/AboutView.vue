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
  { value: '阿富汗', label: '阿富汗' },
  { value: '阿尔巴尼亚', label: '阿尔巴尼亚' },
  { value: '阿尔及利亚', label: '阿尔及利亚' },
  { value: '安道尔', label: '安道尔' },
  { value: '安哥拉', label: '安哥拉' },
  { value: '阿根廷', label: '阿根廷' },
  { value: '亚美尼亚', label: '亚美尼亚' },
  { value: '澳大利亚', label: '澳大利亚' },
  { value: '奥地利', label: '奥地利' },
  { value: '阿塞拜疆', label: '阿塞拜疆' },
  { value: '巴哈马', label: '巴哈马' },
  { value: '巴林', label: '巴林' },
  { value: '孟加拉国', label: '孟加拉国' },
  { value: '巴巴多斯', label: '巴巴多斯' },
  { value: '白俄罗斯', label: '白俄罗斯' },
  { value: '比利时', label: '比利时' },
  { value: '伯利兹', label: '伯利兹' },
  { value: '贝宁', label: '贝宁' },
  { value: '不丹', label: '不丹' },
  { value: '玻利维亚', label: '玻利维亚' },
  { value: '波斯尼亚和黑塞哥维那', label: '波斯尼亚和黑塞哥维那' },
  { value: '博茨瓦纳', label: '博茨瓦纳' },
  { value: '巴西', label: '巴西' },
  { value: '文莱', label: '文莱' },
  { value: '保加利亚', label: '保加利亚' },
  { value: '布基纳法索', label: '布基纳法索' },
  { value: '布隆迪', label: '布隆迪' },
  { value: '柬埔寨', label: '柬埔寨' },
  { value: '喀麦隆', label: '喀麦隆' },
  { value: '加拿大', label: '加拿大' },
  { value: '佛得角', label: '佛得角' },
  { value: '中非共和国', label: '中非共和国' },
  { value: '乍得', label: '乍得' },
  { value: '智利', label: '智利' },
  { value: '中国', label: '中国' },
  { value: '哥伦比亚', label: '哥伦比亚' },
  { value: '科摩罗', label: '科摩罗' },
  { value: '刚果（布）', label: '刚果（布）' },
  { value: '刚果（金）', label: '刚果（金）' },
  { value: '哥斯达黎加', label: '哥斯达黎加' },
  { value: '克罗地亚', label: '克罗地亚' },
  { value: '古巴', label: '古巴' },
  { value: '塞浦路斯', label: '塞浦路斯' },
  { value: '捷克', label: '捷克' },
  { value: '丹麦', label: '丹麦' },
  { value: '吉布提', label: '吉布提' },
  { value: '多米尼克', label: '多米尼克' },
  { value: '多米尼加共和国', label: '多米尼加共和国' },
  { value: '厄瓜多尔', label: '厄瓜多尔' },
  { value: '埃及', label: '埃及' },
  { value: '萨尔瓦多', label: '萨尔瓦多' },
  { value: '赤道几内亚', label: '赤道几内亚' },
  { value: '厄立特里亚', label: '厄立特里亚' },
  { value: '爱沙尼亚', label: '爱沙尼亚' },
  { value: '埃斯瓦蒂尼', label: '埃斯瓦蒂尼' },
  { value: '埃塞俄比亚', label: '埃塞俄比亚' },
  { value: '斐济', label: '斐济' },
  { value: '芬兰', label: '芬兰' },
  { value: '法国', label: '法国' },
  { value: '加蓬', label: '加蓬' },
  { value: '冈比亚', label: '冈比亚' },
  { value: '格鲁吉亚', label: '格鲁吉亚' },
  { value: '德国', label: '德国' },
  { value: '加纳', label: '加纳' },
  { value: '希腊', label: '希腊' },
  { value: '格林纳达', label: '格林纳达' },
  { value: '危地马拉', label: '危地马拉' },
  { value: '几内亚', label: '几内亚' },
  { value: '几内亚比绍', label: '几内亚比绍' },
  { value: '圭亚那', label: '圭亚那' },
  { value: '海地', label: '海地' },
  { value: '洪都拉斯', label: '洪都拉斯' },
  { value: '匈牙利', label: '匈牙利' },
  { value: '冰岛', label: '冰岛' },
  { value: '印度', label: '印度' },
  { value: '印度尼西亚', label: '印度尼西亚' },
  { value: '伊朗', label: '伊朗' },
  { value: '伊拉克', label: '伊拉克' },
  { value: '爱尔兰', label: '爱尔兰' },
  { value: '以色列', label: '以色列' },
  { value: '意大利', label: '意大利' },
  { value: '牙买加', label: '牙买加' },
  { value: '日本', label: '日本' },
  { value: '约旦', label: '约旦' },
];
const fields = ref('')
let number = ref(0)
const searchName = ref('') // 开发者名字
const countryValue = ref("")
const router = useRouter()
const selectedField = ref('') // 用于存储选中的值
const languages = ['HTML','Python','JavaScript','Java','C++','PHP','C#','C']
let userData = reactive({
  data:[{}]
})
 searchName.value = useRoute().query.username

 onMounted(()=>{
    api.getDeveloperFields().then(res=>{
      console.log("返回的领域",res.data.data)
      fields.value = res.data.data
    })
  })


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
let selectedNation = reactive({
  value:{}
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
  const getDevelopers = globalProperties.$api.getDevelopers
  const  getfuzzySearch = async ()=>{
    const result = await api.fuzzySearch(searchName.value)
    const data = result.data.data.list
    console.log("319",result.data.data.list)
    number.value = data.length
    userData.data = data
    console.log("result",userData)
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
