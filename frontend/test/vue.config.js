// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true
// })
module.exports = {
    devServer: {
    	//代理axios
        proxy: 'http://localhost:8888',
        //vue自己启动的端口
        port:9001
    }
}