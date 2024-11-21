<template>
  <div class="user-assess" v-loading="loading"
       element-loading-text="加载中..."
       element-loading-background="rgba(255, 255, 255, 0.8)">
    <div class="assess-center">
      <div class="left">
        <el-card>
          <div class="img">
            <img :src="imgUrl" alt="" style="border-radius: 50%;">
          </div>
          <div class="userInfo">
            <span class="name">{{ userName }}</span>
            <!-- <span class="score">talentRank:{{  }}</span> -->
            <span class="rank">{{rankResult?.data?.rankResult?.level }}</span>
            <div class="level">
                  
            </div>
            <span class="nation"><el-icon><map-location /></el-icon> {{ nationResult?.data}}</span>
            <span class="field">{{ fieldResult?.data}}</span>
          </div>
        </el-card>
        <el-card>
          <el-tabs type="border-card">
    <div class="github-data">
            <div class="github-info">
              <img src="../assets/favicon.svg">
              <span style="font-weight: bold;">GitHub 社区贡献</span>
            </div>
            <div class="flex-s-between">
              <div class="flex-a-center github-icon">
                <el-icon>
                  <star />
                </el-icon>
                <span>Total Stars</span>
              </div>
              <div>{{ rankResult?.data?.totalStars }}</div>
            </div>
            <div class="flex-s-between">
              <div class="flex-a-center github-icon">
                <el-icon>
                   <timer />
                </el-icon>       
                <span>Total Commits</span>
              </div>
              <div>
                {{ rankResult?.data?.totalCommits }}</div>
            </div>
            <div class="flex-s-between">
              <div class="flex-a-center github-icon">
                <el-icon>
                   <shop />
                </el-icon>
                <span>Total Followers</span>
              </div>
              <div>
                {{ rankResult?.data?.totalFollowers }}</div>
            </div>
            <div class="flex-s-between">
              <div class="flex-a-center github-icon">
                <el-icon>
                    <operation />
                </el-icon>
                <span>Total PRs</span>
              </div>
              <div>{{ rankResult?.data?.totalPRs }}</div>
            </div>
              <div class="flex-s-between">
              <div class="flex-a-center github-icon">
                  <el-icon>
                    <wallet />
                  </el-icon>
                <span>Total Reviews</span>
              </div>
              <div>{{ rankResult?.data?.totalReviews }}</div>
            </div>
            <div class="flex-s-between">
              <div class="flex-a-center github-icon">
                <el-icon>
                  <warning/>
                </el-icon>
                <span>Total issues</span>
               </div>
              <div>
               {{ rankResult?.data?.totalIssues }}</div>
            </div>
            <div class="flex-s-between">
              <div class="github-icon flex-a-center">
                <el-icon><collection /></el-icon>
                <span> Contribute to</span>
               </div>
              <div>
               {{ rankResult?.data?.contributeTo }}
              </div>
            </div>
          </div>
</el-tabs>
          
        </el-card>
      </div>
      <div class="right">
        <div>
          <span style="font-size:18px;margin-left: 10px;font-family: 'ali-font1';">Talent Rank 评估</span>
          <div class="user-rank">
            <!-- <div class="talentRank">
               {{ rankResult?.data?.rankResult?.percentile }}
            </div> -->
            <div id="shape-container">
              <!-- <span class="item-font">TalenkRank 得分</span> -->
            </div>
             <div id="rank-item">
               <span class="item-font">评分维度</span>
            </div>
            <div id="radar-container">
              <span class="item-font">技能能力分布图</span>
            </div>
          </div>
          <div id="ai-assess" >
                <p style="font-size: 20px;margin-top: -20px;">大模型评估开发者信息:</p>
                <div v-html="formattedHtml" style="margin-top:10px;font-size: 18px;"> </div>
               
         </div> 
        </div>
        
      </div>
    </div>
   
  </div>
</template>

<script setup>
import { Chart } from '@antv/g2';
import { DataView } from '@antv/data-set';
import api from '../api/index';
import { ref, getCurrentInstance, reactive,watch,onMounted ,computed} from 'vue';
import { useRoute } from 'vue-router';
const loading = ref(true);
const chartReady = ref(false);
const route = useRoute();
const rankResult = ref(null)
const languageResult = ref(null)
const assessResult = ref(null)
const fieldResult = ref(null)
const nationResult = ref(null)
const formattedHtml = ref('')
const talentRank = ref('')
const { appContext: { config: { globalProperties } } } = getCurrentInstance();
const getRank = globalProperties.$api.getRank;
const getDeveloperLanguages = globalProperties.$api.getDeveloperLanguages;
const getDeveloperField = globalProperties.$api.getDeveloperField;
let userName = ref(route.query.username);
let imgUrl = ref('');


const getDeveloperInfo = async ()=>{
  try {
  const [rank, language, assess, field, nation] = await Promise.all([
    getRank(userName.value),
    getDeveloperLanguages(userName.value),
    api.getDeveloperTechnicalAbility(userName.value),
    getDeveloperField(userName.value),
    api.getDeveloperNation(userName.value)
  ]);
  console.log("123")
  rankResult.value = rank.data;
  languageResult.value = language.data.data.sort().slice(0,10);
  assessResult.value = assess.data;
  fieldResult.value = field.data;
  nationResult.value = nation.data;
  imgUrl.value = rank.data.data.avatarUrl;
  console.log("rankResult",rankResult.value)
  console.log("languageResult",languageResult.value)
  console.log("assessResult",assessResult.value)
  console.log("fieldResult", fieldResult.value)
  console.log("nationResult",nationResult.value)
  formattedHtml.value = formatToHtml(assessResult.value.data)
  talentRank.value =  parseFloat(rankResult.value?.data?.rankResult?.percentile).toFixed(2)
  console.log("formattedHtml",formattedHtml);
   // 准备渲染雷达图
  chartReady.value = true;
  console.log("chartReady",chartReady.value)
} catch (error) {
  console.error("请求失败:", error);
}
}
getDeveloperInfo()
// const getDeveloperInfo = async () => {
//   loading.value = true;
//     imgUrl.value = rankResult.data.data.avatarUrl;
//     RankData.data = rankResult.data.data;
//     userLang.data = languageResult.data.data;
//     content.value = assessResult.data.data;
//     fieldRef.value = fieldResult.data.data;
//     loading.value = false;
//     nation.value = nationResult.data.data;
//     // 准备渲染雷达图
//     chartReady.value = true;
//   } catch (error) {
//     console.error('加载开发者数据出错:', error);
//     loading.value = false;
//   }
// };

// const startRender = ()=> {
//     console.log("languageResult11111111111111111111111",languageResult.value)
//     const dv = new DataView().source(languageResult.value);
//     dv.transform({
//       type: 'fold',
//       fields: ['a'], // 展开字段集
//       key: 'user', // key字段
//       value: 'score' // value字段
//     });

//     const chart = new G2.Chart({
//       container: 'radar-container',
//       forceFit: true,
//       width: 500,
//       height: 400,
//       padding: [20, 20, 95, 20]
//     });

//     chart.source(dv, {
//       score: {
//         min: 0,
//         max: 30
//       }
//     });

//     chart.coord('polar', {
//       radius: 0.8
//     });

//     chart.axis('item', {
//       line: null,
//       tickLine: null,
//       grid: {
//         lineStyle: {
//           lineDash: null
//         },
//         hideFirstLine: false
//       }
//     });

//     chart.axis('score', {
//       line: null,
//       tickLine: null,
//       grid: {
//         type: 'polygon',
//         lineStyle: {
//           lineDash: null
//         }
//       }
//     });

//     chart.legend('user', {
//       marker: 'circle',
//       offset: 30
//     });

//     chart.line().position('item*score').color('user').size(2);
//     chart.point().position('item*score').color('user')
//       .shape('circle')
//       .size(4)
//       .style({
//         stroke: '#fff',
//         lineWidth: 1,
//         fillOpacity: 1
//       });
//     chart.area().position('item*score').color('user');

//     chart.render();
// }
const formatToHtml = (infor)=>{
  console.log("infor",infor,typeof infor)
     const lines = infor.split('\n')
     let htmlLines = []
      // 遍历分割后的每行内容
      lines.forEach(line => {
        // 检查是否是标题或特殊格式
         if (line.startsWith("总体来说")) {
            htmlLines.push('<p style="font-size: 18px;">' + line + '</p>');
        } else if (line.trim().startsWith("1.") || line.trim().startsWith("2.") || line.trim().startsWith("3.") || line.trim().startsWith("4.")) {
            htmlLines.push('<p>' + line.trim() + '</p>');
        }
        // else 
        // {
        //   htmlLines.push(line.trim())
        // }
    });
     // 将数组中的HTML内容合并成一个字符串
     return htmlLines.join('\n');
}
const startRender = ()=>{
const data = [
  { item: 'Design', type: 'a', score: 70 },
  { item: 'Design', type: 'b', score: 30 },
  { item: 'Development', type: 'a', score: 60 },
  { item: 'Development', type: 'b', score: 70 },
  { item: 'Marketing', type: 'a', score: 50 },
  { item: 'Marketing', type: 'b', score: 60 },
  { item: 'Users', type: 'a', score: 40 },
  { item: 'Users', type: 'b', score: 50 },
  { item: 'Java', type: 'a', score: 60 },
  { item: 'Test', type: 'b', score: 70 },
  { item: 'Language', type: 'a', score: 70 },
  { item: 'Language', type: 'b', score: 50 },
  { item: 'Technology', type: 'a', score: 50 },
  { item: 'Technology', type: 'b', score: 40 },
  { item: 'Support', type: 'a', score: 30 },
  { item: 'Support', type: 'b', score: 40 },
  { item: 'Sales', type: 'a', score: 60 },
  { item: 'Sales', type: 'b', score: 40 },
  { item: 'UX', type: 'a', score: 50 },
  { item: 'UX', type: 'b', score: 60 },
];

const chart = new Chart({
  container: 'radar-container',
  autoFit: true,
});

chart.coordinate({ type: 'polar' });

chart
  .data(data)
  .scale('x', { padding: 0.5, align: 0 })
  .scale('y', { tickCount: 5 })
  .axis('x', { grid: true })
  .axis('y', { zIndex: 1, title: false });

chart
  .area()
  .encode('x', 'item')
  .encode('y', 'score')
  .encode('color', 'type')
  .encode('shape', 'smooth')
  .style('fillOpacity', 0.5)
  .scale('y', { domainMax: 80 });

chart
  .line()
  .encode('x', 'item')
  .encode('y', 'score')
  .encode('color', 'type')
  .encode('shape', 'smooth')
  .style('lineWidth', 2);

chart.interaction('tooltip', { crosshairsLineDash: [4, 4] });

chart.render();
}

const renderRankItem = ()=>{
  const chart = new Chart({ 
    container: 'rank-item' ,
    height: 442, // 设置高度
  });

chart
  .interval()
  .data([
    { genre: 'Total Stars', sold: rankResult.value?.data?.starScore},
    { genre: 'Total Commits', sold: rankResult.value?.data?.commitScore },
    { genre: 'Total Followers', sold:  rankResult.value?.data?.followerScore  },
    { genre: 'Total PRs', sold:  rankResult.value?.data?.prScore},
    { genre: 'Total Reviews', sold:  rankResult.value?.data?.reviewScore},
    { genre: 'Contribute to', sold:  rankResult.value?.data?.contributeTo },
  ])
  .encode('x', 'genre')
  .encode('y', 'sold')
  .encode('color', 'genre')
  .axis('x', { label: null })
  .style('minHeight', 50);

chart.render();
}
const renderShape = ()=>{
  const chart = new Chart({
  container: 'shape-container',
  autoFit: true,
});

chart
  .gauge()
  .data({
    value: {
      target:  talentRank.value,
      total: 100,
      name: 'score',
    },
  })
  .style(
    'textContent',
    (target, total) => `得分：${target}`,
  )
  .legend(false);

chart.render();
}
watch(()=>chartReady.value,(newVal)=>{
  if(newVal){
      console.log("开始渲染")
      console.log("===",rankResult.value)
      console.log("===")
      startRender()
      renderShape()
      renderRankItem()
      loading.value = false
  }
     
})
</script>

<style scoped>
.assess-center {
  position: relative;
  display: flex;
  margin: 0 auto;
  max-width: 1780px;
  margin-top: 10px;
  background-size: 20px 20px;
  background-image: linear-gradient(90deg, rgba(60, 10, 30, .1) 3%, transparent 0), linear-gradient(1turn, rgba(60, 10, 30, .1) 3%, transparent 0); 
  font-family: 'ali-font1';
}

.right {
  flex: 1;
}

.img img {
  width: 200px;
  height: 200px;
}

.userInfo span {
  display: block;
  margin: 0 auto;
  text-align: center;
}

.userInfo .name {
  font-weight: 500;
  font-size: 18px;
  height: 50px;
}
.name,.score,.rank,.nation{
  line-height: 50px;
}
.userInfo .score,.rank,.name{
  font-weight: 500;
  font-size: 18px;
  height: 50px;

}
.rank {
  color: #fff;
  background-color: #49c123;
}

.rank, .nation {
  width: 40px;
  border-radius: 20%;
  line-height: 50px;
}

.field {
  width: 100%;
  height: 20px;
  font-size: 15px;
  border-radius: 20%;
  line-height: 20px;
  font-weight: 900;
}

.nation,.field {
  width: 100%;
  height: 50px;
}

#container {
  width: 500px;
  height: 340px;
}

.github-info img {
  vertical-align: middle;
}

.github-info span {
  line-height: 45px;
  font-size: 18px;
  margin-left: 5px;
}

.flex-s-between {
  display: flex;
  justify-content: space-between;
}
.github-icon .el-icon{
  height: 24px;
  width: 24px;
  font-size: 20px;
  color: #0969da;
}
#ai-assess{
  /* width: 250px; */
  display: inline-block;
  height: 286px;
  background-size: 20px 20px;
  margin-top: 20px;
  padding-left: 20px;
  /* background-image: linear-gradient(90deg, rgba(60, 10, 30, .1) 3%, transparent 0), linear-gradient(1turn, rgba(60, 10, 30, .1) 3%, transparent 0); */
  font-family: 'ali-font1';
  border-radius: 30px;
}
.right{
  flex: 1;
}
.message{
  position: absolute;
  width: 300px;
  right:0;
  top: 66px;

}
.text-container{
  /* width: 300px; */
  border-radius: 30px;
}
.user-rank{
  height: 430px;
  display: flex;
  justify-content: space-around;
}
#rank-item{
  position: relative;
  top: -30px;
  width: 600px;
  height: 300px;
}
#rank-item canvas{
  height: 442px;
}
#shape-container{
  width: 200px;
}
#radar-container{
  width: 300px;
  height: 410px;
}
#shape-container{
  position: relative;
}
</style>      
 
