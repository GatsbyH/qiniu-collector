<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="main">
  <header>
    <div class="nav">
      <div class="web-info">Developer Assessment<span class="assess">开发者评估应用</span></div>
      <div class="info">
      <div class="user flex-a-center">
       <el-icon size="30">
       <user-filled />
       </el-icon>
      </div>
  </div>
    </div>
  </header>
  <div class="view">
  <RouterView />
  </div>
</div>
</template>
<script setup>


</script>
<style scoped>

.web-info{
  font-family: 'ali-font1';
  font-size: 30px;
  margin-left: 20px;
}
.main{
  height:100%;
}
.view{
  height: calc(100% - 125px);
}
.assess{
  margin-right: 44px;
    margin-left: 8px;
    color: #3669f5;
    font-size: 14px;
    background: linear-gradient(89.9deg, rgba(54, 142, 245, 0.1) 0.09%, rgba(54, 105, 245, 0.1) 99.92%);
    border-radius: 10px 4px;
    padding: 4px 8px;
}
.nav{
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #d3d3d3;
}
.info{
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.user{
  width: 50px;
  height: 50px;
  text-align: center;
  margin-left: 10px;
}
</style>
