import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus' //全局引入
import 'element-plus/dist/index.css'
import Axios from 'axios'
const app = createApp(App)
app.use(ElementPlus)
app.use(Axios)
app.mount('#app')
