<template>
<div 
    class="oauth"
    v-loading="loading"
    element-loading-text="登录中"
  
>
    
</div>
</template>
<script setup>
import { onMounted,ref } from 'vue';
import { ElMessage } from 'element-plus'
const loading = ref(true)
async function handleCallback() {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');
    const state = urlParams.get('state');

    try {
        const response = await fetch(`http://106.54.234.202:8080/oauth/callback/github?code=${code}&state=${state}`);
        console.log("response",response)
        const data = await response.json();
        console.log("data",data)
        if (data.code === 200) {
            loading.value = false;
            alert("data",data)
            // 保存token
            localStorage.setItem('token', data.data);
            // alert('登录成功：');
            ElMessage.success({
            message: '登录成功',
            type: 'success'
          })
            // 跳转到首页
            window.location.href = '/';
           
        } else {
            alert('登录失败：' + data.info);
            window.location.href = '/';
            ElMessage.error('登录失败',data.info)
        }
    } catch (error) {
        console.log('登录出错',error)
        ElMessage.error('登录失败')
    }
}


onMounted(()=>{
    handleCallback()
})
</script>

<style scoped>
.oauth{
    width: 100vw;
    height: calc(100vh - 66px);
}

</style>