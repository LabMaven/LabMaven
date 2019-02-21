// see http://vuejs-templates.github.io/webpack for documentation.
var path = require('path')

module.exports = {
  build: {
    env: require('./prod.env'),
    index: path.resolve(__dirname, '../dist/index.html'),
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    productionSourceMap: true,
    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],
    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  },
  dev: {
    env: require('./dev.env'),
    port: 8080,
    autoOpenBrowser: true,
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    // 代理 参考https://vuejs-templates.github.io/webpack/proxy.html
    proxyTable: {
        '/userMgr': {
            target: 'http://localhost:8085/ailab/',
            changeOrigin: true,
            filter: function (pathname, req) {
              return pathname.match('^/login') || pathname.match('^/user')
            },
            pathRewrite: {
               '^/userMgr': ''
            }
        },
        '/cfgMgr': {
            target: 'http://localhost:8086/ailab/',
            changeOrigin: true,
            filter: function (pathname, req) {
              return pathname.match('^/cfgMgr')
            },
            pathRewrite: {
               '^/cfgMgr': ''
            }
        },
        '/deviceMgr': {
            target: 'http://localhost:8087/ailab/',
            changeOrigin: true,
            pathRewrite: {
               '^/deviceMgr': ''
            }
        },
        '/alarmMgr': {
            target: 'http://localhost:8088/ailab/',
            changeOrigin: true,
            pathRewrite: {
                '^/alarmMgr': ''
            }
        }
    },
    //${base}/user/login


    // CSS Sourcemaps off by default because relative paths are "buggy"
    // with this option, according to the CSS-Loader README
    // (https://github.com/webpack/css-loader#sourcemaps)
    // In our experience, they generally work as expected,
    // just be aware of this issue when enabling this option.
    cssSourceMap: false
  }
}
