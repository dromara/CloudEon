# 前端项目

# 使用到的技术
1. [Ant Design Pro](https://pro.ant.design)
2. [Umi3](https://v3.umijs.org/)
3. [typescript](https://www.typescriptlang.org/zh/docs/)
4. [React](https://zh-hans.reactjs.org/)
5. [node](https://github.com/nodejs/node)，版本要在12.0.0 以上 

# 第一次启动项目
1. cd web-ui
2. 如果有node_modules和src/.umi文件夹，删除node_modules和src/.umi文件夹
3. 安装tyarn：
```bash
npm install yarn tyarn -g
```
4. 用tyarn安装依赖：
```bash
tyarn
```
5. 本地环境启动修改接口地址
   在config/proxy.ts文件修改：
   ```bash
    '/apiPre/': {
      target: 'http://192.168.31.30:7700', // 要代理的地址改成后端接口实际的地址
      changeOrigin: true,
      pathRewrite: { '^/apiPre' : '' },
    }
   ```
6. 启动项目
```bash
npm start
```

7. 生产环境打包
```bash
npm run build
```

# 代码说明
1. 如果要修改websocket接口的地址，在src/services/apiConfig/index.ts文件修改