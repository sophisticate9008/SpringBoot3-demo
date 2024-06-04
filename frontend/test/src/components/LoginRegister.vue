<template>
  <div class="login-register">
    <div class="tabs">
      <button @click="activeTab = 'login'" :class="{ active: activeTab === 'login' }">Login</button>
      <button @click="activeTab = 'register'" :class="{ active: activeTab === 'register' }">Register</button>
    </div>
    <div v-if="activeTab === 'login'">
      <h2>Login</h2>
      <form @submit.prevent="handleLogin">
        <div>
          <el-input type="Account" v-model="loginAccount" placeholder="Account" required clearable/>
        </div>
        <div>
          <el-input type="password" v-model="loginPassword" placeholder="Password" required clearable/>
        </div>
        <div class="verification-code-container">
          <el-input type="code" v-model="loginCode" placeholder="Code" required clearable/>
          <img :src="verificationCodeUrl" @click="refreshVerificationCode" alt="verification code" class="verification-code"/>
        </div>
        <button type="submit">Login</button>
      </form>
    </div>
    <div v-if="activeTab === 'register'">
      <h2>Register</h2>
      <form @submit.prevent="handleRegister">
        <div>
          <el-input type="Account" v-model="registerAccount" placeholder="Account" required clearable/>
        </div>
        <div>
          <el-input v-model="registerPassword" type="password" placeholder="Password" required clearable/>
        </div>
        <div>
          <el-input type="password" v-model="registerConfirmPassword" placeholder="Confirm Password" required clearable/>
        </div>
        <button type="submit">Register</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'LoginRegister',
  data() {
    return {
      activeTab: 'login',
      loginAccount: '',
      loginPassword: '',
      registerAccount: '',
      registerPassword: '',
      registerConfirmPassword: '',
      loginCode: '',
      verificationCodeUrl: 'http://localhost:8888/login/getCode'
    };
  },
  methods: {
    handleLogin() {
        axios.post("/login/login", {
            account: this.loginAccount,
            password: this.loginPassword,
            code: this.loginCode
        }).then(response => {
            this.$message(response.data.msg);
        });

    },
    handleRegister() {
      // 模拟注册逻辑
      if (this.registerAccount && this.registerPassword && this.registerPassword === this.registerConfirmPassword) {
        alert(`Registering with Account: ${this.registerAccount}`);
        // 这里添加实际的注册逻辑
      } else {
        alert('Passwords do not match');
      }
    },
    refreshVerificationCode() {
      // 更新验证码图片的 URL 以强制刷新
      this.verificationCodeUrl = this.verificationCodeUrl + `?timestamp=${new Date().getTime()}`;
    }
  }
};
</script>

<style scoped>
.login-register {
  max-width: 400px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.tabs {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.tabs button {
  padding: 10px 20px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 16px;
}

.tabs button.active {
  border-bottom: 2px solid #42b983;
  font-weight: bold;
}

form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

form div {
  width: 300px;
  margin-bottom: 5px;
}

.verification-code-container {
  display: flex;
  align-items: center;
}

.verification-code {
  margin-left: 10px;
  height: 32px;
  border-radius: 4px;
  cursor: pointer;
}

button[type="submit"] {
  padding: 10px;
  border: none;
  background-color: #42b983;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  width: 300px;
}

button[type="submit"]:hover {
  background-color: #339966;
}
</style>
