<template>
<div class="user-assess">
    <div class="assess-center">
    <div class="left">
    <el-card>
      <div class="img">
        <img :src=imgUrl alt="" style="border-radius: 50%;" >
      </div>
      <div class="userInfo">
        <span class="name">{{ userName }}</span>
        <span class="rank">{{ RankData.data.rankResult?.level }}</span>
        <span class="nation">London</span>
        <span class="field">{{ fieldRef }}</span>
      </div>
    </el-card>
    <el-card>
        <div class="github-data">
          <div class="github-info ">
            <img src="../assets/favicon.svg">
            <span>github数据</span>
          </div>

            <div class="flex-s-between">
                <div>
                    Total Stars
                </div>
                <div>
                  {{ RankData.data.totalStars }}
                </div>
            </div>
            <div class="flex-s-between">
                <div>
                    Total Commits
                </div>
                <div>
                  {{ RankData.data.totalCommits }}
                </div>

            </div>
            <div class="flex-s-between">
                <div>
                    Total PRs
                </div>
                <div>
                  {{ RankData.data.totalPRs }}
                </div>
            </div>
            <div class="flex-s-between">
                <div>
                    Total issues
                </div>
                <div>
                  {{ RankData.data.totalIssues }}
                </div>

            </div>
            <div class="flex-s-between">
                <div>
                    contribute to
                </div>
                <div>
                  {{ RankData.data.contributeTo }}
                </div>

            </div>
        </div>
    </el-card>
    </div>
    <div class="right">

        <div id="container">
           <span style="font-size:18px;margin-left: 10px;">能力维度</span>
        </div>
        <div>
          <img width="500" height="340" :src="`https://github-readme-activity-graph.vercel.app/graph?username=${userName}&theme=dracula`">
        </div>
    </div>
</div>
</div>
</template>
<script setup>
import * as G2 from '@antv/g2';
import { DataView } from '@antv/data-set';
import { getDeveloperAurl } from '../Utils/request'
import { ref,getCurrentInstance ,onMounted} from 'vue'
import { useRoute }  from 'vue-router'
import { reactive } from 'vue';
const route = useRoute()
const { appContext : { config: { globalProperties} } } = getCurrentInstance();
  const getRank = globalProperties.$api.getRank
  const getDeveloperLanguages = globalProperties.$api.getDeveloperLanguages
  const getDeveloperField = globalProperties.$api.getDeveloperField
let userName = ref(route.query.username)
console.log("userName",userName.value)
let imgUrl = ref('')
const RankData = reactive({
  data:{}
})

const userLang = reactive({
  data:{}
})
let fieldRef = ref('')
let data = [
  { item: 'Java', a: 70 },
  { item: 'C++', a: 60 },
  { item: 'python', a: 50},
  { item: 'vue', a: 40},
  { item: 'React', a: 60 },
  { item: 'go', a: 70 },
  { item: 'C', a: 50},
  { item: 'shell', a: 30 },
  { item: 'html', a: 60 },
  { item: 'ts', a: 50 }

];
const getDeveloperInfo = async ()=>{
  let result = await getRank(userName.value)
  data = await getDeveloperLanguages(userName.value)
  const imgResult = await getDeveloperAurl(userName.value)
  const field = await getDeveloperField(userName.value)
  imgUrl.value = imgResult.data.avatar_url
  RankData.data = result.data.data
  userLang.data = data.data.data
  fieldRef.value = field.data.data
 console.log("头像",imgUrl)
 console.log("Rank",result)
 console.log("语言",data)
 console.log("领域",field.data.data)
  // return {
  //   result
  // }
}
 getDeveloperInfo()
const dv = new DataView().source(data);
dv.transform({
  type: 'fold',
  fields: ['a'], // 展开字段集
  key: 'user', // key字段
  value: 'score' // value字段
});
onMounted(()=>{
  const chart = new G2.Chart({
  container: 'container',
  forceFit: true,
  width: 500,
  height: 400,
  padding: [ 20, 20, 95, 20 ]
});
chart.source(dv, {
  score: {
    min: 0,
    max: 80
  }
});
chart.coord('polar', {
  radius: 0.8
});
chart.axis('item', {
  line: null,
  tickLine: null,
  grid: {
    lineStyle: {
      lineDash: null
    },
    hideFirstLine: false
  }
});
chart.axis('score', {
  line: null,
  tickLine: null,
  grid: {
    type: 'polygon',
    lineStyle: {
      lineDash: null
    }
  }
});
chart.legend('user', {
  marker: 'circle',
  offset: 30
});
chart.line().position('item*score').color('user')
 .size(2);
chart.point().position('item*score').color('user')
  .shape('circle')
  .size(4)
  .style({
    stroke: '#fff',
    lineWidth: 1,
    fillOpacity: 1
  });
chart.area().position('item*score').color('user');
chart.render();

})

</script>
<style scoped>
.assess-center{
    display: flex;
    margin: 0 auto;
    max-width: 970px;
    margin-top:20px;
}
.right{
    flex:1;
}
.img img{
    width: 200px;
    height: 200px;
}
.roda{
    width: 300px;
    height: 300px;
    border: 1px solid #251616;
}
.userInfo span{
    display: block;
    margin: 0 auto;
    text-align: center;
}
.userInfo .name{
  font-weight: 500;
   font-size: 18px;
}
.rank{
    color:#fff;
    background-color: #49c123;
}
.rank,.nation{
    width: 40px;
    height: 20px;
    /* text-align: center; */
    border-radius: 20%;
    line-height: 20px;
}
.field{
  width: 100%;
  height: 20px;
  font-size: 15px;
  border-radius: 20%;
  line-height: 20px;
  font-weight: 900
}
.nation {
  width: 100%;
}
#container{
  width: 500px;
  height: 340px;
}
.github-info img{
  vertical-align: middle;
}
.github-info span{
line-height: 45px;
font-size: 18px;
margin-left: 5px;
}
.flex-s-between{
  display: flex;
  justify-content: space-between;
}
</style>
