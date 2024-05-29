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
          <label for="login-email">Email:</label>
          <input type="email" v-model="loginEmail" required />
        </div>
        <div>
          <label for="login-password">Password:</label>
          <input type="password" v-model="loginPassword" required />
        </div>
        <button type="submit">Login</button>
      </form>
    </div>
    <div v-if="activeTab === 'register'">
      <h2>Register</h2>
      <form @submit.prevent="handleRegister">
        <div>
          <label for="register-email">Email:</label>
          <input type="email" v-model="registerEmail" required />
        </div>
        <div>
          <label for="register-password">Password:</label>
          <input type="password" v-model="registerPassword" required />
        </div>
        <div>
          <label for="register-confirm-password">Confirm Password:</label>
          <input type="password" v-model="registerConfirmPassword" required />
        </div>
        <button type="submit">Register</button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginRegister',
  data() {
    return {
      activeTab: 'login',
      loginEmail: '',
      loginPassword: '',
      registerEmail: '',
      registerPassword: '',
      registerConfirmPassword: ''
    };
  },
  methods: {
    handleLogin() {
      // 模拟登录逻辑
      if (this.loginEmail && this.loginPassword) {
        alert(`Logging in with email: ${this.loginEmail}`);
        // 这里添加实际的登录逻辑
      }
    },
    handleRegister() {
      // 模拟注册逻辑
      if (this.registerEmail && this.registerPassword && this.registerPassword === this.registerConfirmPassword) {
        alert(`Registering with email: ${this.registerEmail}`);
        // 这里添加实际的注册逻辑
      } else {
        alert('Passwords do not match');
      }
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
}

form div {
  margin-bottom: 10px;
}

form label {
  margin-bottom: 5px;
}

form input {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button[type="submit"] {
  padding: 10px;
  border: none;
  background-color: #42b983;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

button[type="submit"]:hover {
  background-color: #339966;
}
</style>
