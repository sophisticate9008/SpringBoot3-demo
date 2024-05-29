<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png" />

    <LoginRegister />
  </div>
</template>

<script>
import LoginRegister from "./components/LoginRegister.vue"; // 导入LoginRegister组件

export default {
  name: "App",
  components: {
    LoginRegister, // 注册LoginRegister组件
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
