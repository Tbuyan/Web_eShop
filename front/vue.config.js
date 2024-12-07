const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer:{
    proxy: {
      '/eShop': {
        target: 'http://121.37.45.62:8081',
        changeOrigin: true,
        pathRewrite: { '^/eShop': '/eShop' },
      },
    },
    port: 5373
  }
})

