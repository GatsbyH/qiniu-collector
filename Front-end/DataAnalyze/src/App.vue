<template>
  <div class="main">
    <header>
      <div class="nav">
        <div class="web-info">Developer Assessment<span class="assess">开发者评估应用</span></div>
        <div class="info">
          <div class="user">
            <template v-if="avatarUrl == undefined">
              <a @click.prevent="handleGithubLogin">
                <el-icon size="30">
                  <user-filled />
                </el-icon>
              </a>
            </template>
            <template v-else>
              <div class="user-info">
                <span class="username">{{ userName }}</span>
                <img :src="avatarUrl" alt="User" class="avatar" />
              </div>
            </template>
          </div>
        </div>
      </div>
    </header>
    <div class="view">
      <RouterView />
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref ,onMounted} from 'vue'
import { RouterLink, RouterView,useRouter } from 'vue-router'
import { request } from '../src/Utils/request'
const router = useRouter();
const avatarUrl = ref(localStorage.getItem('avatar'))
const userName = ref(localStorage.getItem('userName'))

const handleGithubLogin = async () => {
    try {
        const redirectUrl = await request({
            url: '/oauth/github/render',
            method: 'get'
        });
        console.log("redirectUrl是",redirectUrl)
        window.location.href = redirectUrl.data.data;
    } catch (error) {
        console.error('GitHub login error:', error);
    }
}

</script>

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
  align-items: center;
  padding-right: 20px;
}
.user{
  display: flex;
  align-items: center;
}
.user-info{
  display: flex;
  align-items: center;
  gap: 10px;
}
.username{
  color: #333;
  font-size: 14px;
  white-space: nowrap;
}
.avatar{
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
</style>
