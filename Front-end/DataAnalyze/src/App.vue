<script setup lang="ts">
import { RouterLink, RouterView,useRouter } from 'vue-router'
const router = useRouter();


const openLogin = () => {
        ElMessageBox.alert('这是一段内容', '标题名称', {
          confirmButtonText: '确定',
          callback: (action) => {
            ElMessage({
              type: 'info',
              message: `action: ${action}`,
            });
          },
        });
      };
</script>

<template>
  <div class="main">
  <header>
    <div class="nav">
      <div class="web-info">Developer Assessment<span class="assess">开发者评估应用</span></div>
      <div class="info">
      <div class="user flex-a-center" @click="login()">
           <el-icon size="30">
              <user-filled />
            </el-icon>
       <div class="dropdown">
      </div>
      </div>
      
  </div>
    </div>
  </header>
  <div class="view">
  <RouterView />
  </div>
</div>
</template>
<style scoped>
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
  position: relative;
}
.user:hover{
  cursor: pointer;
}
.dropdown{
  position: absolute;
  top: 50px;
  left: -50px;

}
.dropdown ul{
  list-style: none;
  
}
</style>
