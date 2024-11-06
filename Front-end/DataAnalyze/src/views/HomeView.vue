<template>
  <div class="home">
    <div class="logo">
     <div class="tip">
      <i style="color:#4285f4;">A</i>
      <i style="color:#ea4335;">s</i>
      <i style="color:#fbbc05;">s</i>
      <i style="color:#ea4335;">e</i>
      <i style="color:#ea4335;">ssment</i>
      </div>
    </div>
    <div class="main">
      <div class="searchBar">
        <div class="icon">
        <search style="  width: 20px;height: 20px; vertical-align: middle; " />
        </div>
        <input type="text" v-model="query" @keyup.enter="find" placeholder="搜索开发者~准确评估其能力">
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { reactive,ref } from 'vue';
  import { useRouter,useRoute } from 'vue-router'
  const router = useRouter()
  const route = useRoute()
  const inputValue = ref(''); 
  const query = ref('')
 
  
  const  find = async ()=>{
    router.push({path:'/about',query:{username:query.value}})
  }
</script>
<style scoped>

.home{

 height: 100%;
}
.logo{
  width: 100%;
}

.icon{
  display: inline;
  margin-left: 10px;
 
}
.searchBar{
    border-radius: 24px;
    box-shadow: 0px 0px 5px rgb(212, 212, 212);
    margin: 0 auto;
    width: 746px;
    height: 48px;
}
.tip{
  font-family: 'ali-font1';
  text-align: center;
  margin: 0 auto;
  font-size: 150px;
}
.searchBar input{
  height: 48px;
  outline: none;
  border: none;
  margin-left: 10px;
  width: 90%;
  color: rgb(112, 112, 112);
}
</style>