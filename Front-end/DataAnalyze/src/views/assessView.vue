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
            <span class="rank">{{rankResult?.data?.rankResult?.level }}</span>
            <div class="level">
              <el-icon><map-location /></el-icon>
              <span>{{ nationResult?.data}}</span>
            </div>
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
                <div v-text="assessResult?.data" style="margin-top:10px;font-size: 18px;"> </div>
          </div>
        </div>

      </div>
    </div>

  </div>

  <!-- 添加导出卡片 -->
  <div class="export-card">
    <el-card class="card-box" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>数据导出</span>
        </div>
      </template>
      <div class="card-content">
        <el-button
          type="primary"
          :icon="Download"
          @click="openSvg"
          class="export-button"
        >
          查看GitHub Stats卡片
        </el-button>
        <el-button
          :icon="DocumentCopy"
          @click="copyUrl"
          class="export-button"
        >
          复制链接
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { Chart } from '@antv/g2';
import { DataView } from '@antv/data-set';
import api from '../api/index';
import { ref, getCurrentInstance, reactive,watch,onMounted ,computed,onUnmounted} from 'vue';
import { useRoute } from 'vue-router';
import { Download, DocumentCopy } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
const loading = ref(true);
const chartReady = ref(false);
const route = useRoute();
const rankResult = ref(null)
const languageResult = ref(null)
const assessResult = ref(null)
const nationResult = ref(null)
const formattedHtml = ref('')
const talentRank = ref('')
const { appContext: { config: { globalProperties } } } = getCurrentInstance();
const getRank = globalProperties.$api.getRank;
const getDeveloperLanguages = globalProperties.$api.getDeveloperLanguages;
let userName = ref(route.query.username);
let imgUrl = ref('');
let cleanupChart = null;

const getDeveloperInfo = async ()=>{
  try {
  const [rank, language, assess, nation] = await Promise.all([
    getRank(userName.value),
    getDeveloperLanguages(userName.value),
    api.getDeveloperTechnicalAbility(userName.value),
    api.getDeveloperNation(userName.value)
  ]);
  console.log("123")
  rankResult.value = rank.data;
  languageResult.value = language.data.data.sort().slice(0,10);
  assessResult.value = assess.data;
  nationResult.value = nation.data;
  imgUrl.value = rank.data.data.avatarUrl;
  console.log("rankResult",rankResult.value)
  console.log("languageResult",languageResult.value)
  console.log("assessResult",assessResult.value)
  console.log("nationResult",nationResult.value)
  formattedHtml.value = assessResult.value.data
  talentRank.value =  rankResult.value?.data?.totalScore
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

const startRender = () => {
  if (!languageResult.value?.length) return;

  // 1. 创建图表实例
  const chart = new Chart({
    container: 'radar-container',
    autoFit: true,
    height: 400,
    padding: [20, 30, 50, 30]
  });

  // 2. 数据处理：只取前5个最常用的语言
  const data = languageResult.value
    .sort((a, b) => b.a - a.a)  // 按使用频率降序排序
    .slice(0, 5)    // 只取前5个
    .map(item => ({
      language: item.item,
      value: item.a
    }));

  // 3. 配置坐标系
  chart.coordinate('polar', {
    radius: 0.8
  });

  chart.data(data);

  // 4. 配置度量
  chart.scale('value', {
    min: 0,
    nice: true,
  });

  chart.scale('language', {
    range: [0, 1]
  });

  // 5. 配置坐标轴
  chart.axis('language', {
    line: null,
    grid: {
      line: {
        style: {
          lineDash: [4, 4]
        }
      }
    },
    label: {
      style: {
        fontSize: 14,  // 可以使用更大的字体，因为只有5个标签
        fill: '#666',
        fontWeight: 'bold'
      },
      offset: 15
    }
  });

  chart.axis('value', {
    line: null,
    grid: {
      line: {
        style: {
          lineDash: [4, 4]
        }
      }
    }
  });

  // 6. 绘制图形
  // 面积
  chart
    .area()
    .encode('x', 'language')
    .encode('y', 'value')
    .style({
      fill: '#1890ff',
      fillOpacity: 0.3
    });

  // 线条
  chart
    .line()
    .encode('x', 'language')
    .encode('y', 'value')
    .style({
      stroke: '#1890ff',
      lineWidth: 2
    });

  // 数据点
  chart
    .point()
    .encode('x', 'language')
    .encode('y', 'value')
    .encode('shape', 'circle')
    .style({
      fill: '#1890ff',
      r: 6,  // 增大点的大小
      stroke: '#fff',
      lineWidth: 1,
      fillOpacity: 1
    });

  // 7. 配置 tooltip
  chart.interaction('tooltip', {
    shared: true,
    crosshairs: {
      type: 'xy',
      line: {
        style: {
          dash: [4, 4]
        }
      }
    },
    domStyles: {
      'g2-tooltip': {
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        padding: '8px',
        fontSize: '12px',
        boxShadow: '0 2px 6px rgba(0,0,0,0.1)'
      }
    }
  });

  // 8. 渲染图表
  chart.render();

  // 9. 添加自适应处理
  const handleResize = () => {
    chart.forceFit();
  };
  window.addEventListener('resize', handleResize);

  return () => {
    window.removeEventListener('resize', handleResize);
    chart.destroy();
  };
};

const renderRankItem = ()=>{
  const chart = new Chart({
    container: 'rank-item' ,
    height: 442,
  });

chart
  .interval()
  .data([
    { genre: 'Total Stars', score: rankResult.value?.data?.starScore},
    { genre: 'Total Commits', score: rankResult.value?.data?.commitScore },
    { genre: 'Total Followers', score: rankResult.value?.data?.followerScore  },
    { genre: 'Total PRs', score: rankResult.value?.data?.prScore},
    { genre: 'Total Reviews', score: rankResult.value?.data?.reviewScore},
    { genre: 'Contribute to', score: rankResult.value?.data?.contributeTo },
  ])
  .encode('x', 'genre')
  .encode('y', 'score')
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

// 监听数据变化
watch(() => chartReady.value, (newVal) => {
  if (newVal && languageResult.value) {
    console.log("开始渲染雷达图");
    console.log("语言数据:", languageResult.value);
    const cleanup = startRender();
    if (cleanup) {
      cleanupChart = cleanup;
    }
    renderShape();
    renderRankItem();
    loading.value = false;
  }
});

// 组件卸载时清理
onUnmounted(() => {
  if (typeof cleanupChart === 'function') {
    cleanupChart();
  }
});

// 计算导出URL
const exportUrl = computed(() => {
  return `http://106.54.234.202:8080/GraphQL/stats/${userName.value}`;
});

// 打开SVG
const openSvg = () => {
  window.open(exportUrl.value, '_blank');
}

// 复制链接功能
const copyUrl = async () => {
  try {
    await navigator.clipboard.writeText(exportUrl.value);
    ElMessage({
      message: '链接已复制到剪贴板',
      type: 'success',
      duration: 2000
    });
  } catch (err) {
    ElMessage({
      message: '复制失败，请手动复制',
      type: 'error',
      duration: 2000
    });
  }
};
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

.userInfo {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.level {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  color: #666;
  font-size: 14px;
}

.level .el-icon {
  font-size: 16px;
}

/* 移除之前的一些可能冲突的样式 */
.name, .score, .rank, .level {
  line-height: normal;
}

.userInfo span {
  margin: 5px 0;
}

.userInfo .name {
  font-weight: 500;
  font-size: 18px;
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
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;  /* Reduce this value to bring elements closer together */
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

.export-card {
  position: fixed;
  right: 20px;
  bottom: 20px;
  z-index: 1000;
}

.card-box {
  width: 250px;
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: 500;
}

.card-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.export-button {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 图标样式调整 */
:deep(.el-button .el-icon) {
  margin-right: 6px;
}

/* 卡片样式调整 */
:deep(.el-card__header) {
  padding: 12px 20px;
  border-bottom: 1px solid #e4e7ed;
}

:deep(.el-card__body) {
  padding: 16px;
}
</style>

