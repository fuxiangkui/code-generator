```
## 简介
涉及的技术栈：
- MVVM框架：   [Vue](https://github.com/vuejs/vue)
- 路由管理：   [Vue-Router](https://github.com/vuejs/vue-router)
- 状态管理：   [Vuex](https://github.com/vuejs/vuex)
- 服务端通信： [Axios](https://github.com/mzabriskie/axios)
- 组件库：     [element-ui](http://element.eleme.io/#/zh-CN)
工具支持：
- 代码检测：   [ESLint](http://eslint.org/)
- 构建工具：   [Webpack](https://webpack.js.org/)
- 包管理工具： [Npm](https://www.npmjs.com/)
- Chrome 插件：[Vue-devtools](https://github.com/vuejs/vue-devtools)

## 快速开始
> 需安装 [Node.js](https://nodejs.org/en/), 版本 > 4.0.0
> npm install


启动 (jenkins统一编译h5):
	目的：1.减少研发支持版本发布时间（减少编译及上传dist时间）.
		  2.保证服务器代码的完整性和正确性.
		  3.源代  码和可执行文件分开，减少代码仓库冗余体积，减少代码仓库下载时间.
		  4.自动推到CDN加速(默认 dist/static/)
	变量用法：	 
	        env：版本运行环境标识，可选值有sat、uat、fat、prod，默认prod
	        res：//(0,1)默认1打包  是否打包resouse.js文件
	开发环境运行：
	    npm run start
	线上环境运行：
		npm run prod --env sat(OPS推荐)
		npm run prod sat
		npm run prod -- --env sat

```
```
项目结构如下：
|-----dist              (打包后生成的文件夹)  
|      |----  static 目录
|      |----  index.html
|-----src
|	   |----commoncss   (公共css文件夹)
|      |----commonjs    (公共js文件夹)
|      |----components  (公共组件文件夹)
|      |----fonts       (字体库文件夹)
|      |----image       (图片文件夹)
|      |----router      (路由文件夹)
|      |----vue         (vue页面文件夹)
|      |----vuex        (全局状态文件夹)
|      |----App.vue
|      |----main.js     (项目总入口)
|      |----resouse.js  (请求路径) 
|-----.babelrc
|-----.eslintrc.js
|-----.postcssrc.js
|-----index.html
|-----package.json
|-----README.md
|-----webpack.config.js
|-----webpack.prod.config.js

```
使用：
1): vue2使用：https://cn.vuejs.org/
2): element使用： http://element.eleme.io/#/zh-CN

3): 新建一个页面(配置路由)如：
	1.在src/vue/下新建页面 XXX.vue
 	2.在src/router/router.js中配：
    	1)在 vueConfig 变量中加：
    		export const vueConfig = {
				vue : {
					test : {
						tree              : resolve => require.ensure([], () => resolve(require('@/vue/test/tree')), 'vue/test/tree'),
					}
				}
			}
		2)在Router下加： 
		    routes: [
			    {path: '/',component : vueConfig.login},//登陆
			    {
					path: '/',
					component : PageTransition,
					children: [
					    {path: 'tree',            component : vueConfig.vue.test.tree},  //Tree 树形控件
					]
			    }
			]

4): 添加全局JS和Css 如：
   在main.js中配：
   //js
   import Util from '@/commonjs/util.js'
   window.util = Util;
   //使用：uitl.isLogin();

   //css
   require('@/commoncss/mui.css');


5): 状态管理用法(vuex)：src/vuex/store.js配：
	let store = new Vuex.Store({
		state : {
			stateObject : {
				isShowTemp : false
		    }
		}

	页面使用 .vue 
	computed: mapState({
	    'stateObject' : state => state.stateObject //简写。完整的:this.$store.state.stateObject访问
	}),
	mounted (){
       this.$store.state.stateObject.isShowTemp = false; //代码中使用(js)
    },

    //template 中使用(html)
	<nav class="navbar navbar-default navbar-fixed-top" :class="stateObject.headerTheme.theme">

6): apiConfig公共请求库使用：
	var param = {
		username : 'wer',
	}
	//Form Data格式
	util.ajax(apiConfig.test.save,param, 'Form Data',(success)=>{
		Dialog.alert( success );
	})
	//JSON格式
	util.ajax(apiConfig.test.save,param,(success)=>{
		Dialog.alert( success );
	})
7): 处理JSON跨域问题(webpack.config.js):
	访问如：http://139.196.253.143:8801/agent/gateway/getData;
	var config = {
	    ...
	    devServer: {
	        ...
	        proxy: {//本地代理
	            '/agent/gateway*': {
	                target: 'http://139.196.253.143:8801',
	                secure: false
	            }
	        }
	    }
	    ...
	}
8): 解决URL地址中带 "#" 问题(http://localhost:9999/tree/#  -- > http://localhost:9999/tree)。
    1 .router.js
	    new Router({
			mode: 'history', //去掉#
			routes: [...]
	    });
	2 .后端nginx配:
		server {
		    location / {
	            root   E:\gzy\h5\h5-vue-element\dist;
				try_files $uri $uri/ /index.html;
	            index  index.html index.htm;
	        }
        }
9): GZIP压缩
    1.后端nginx配:
		http {
		    #gzip  on;
			gzip  on;
		    gzip_disable "msie6";
		    gzip_vary on;
		    gzip_comp_level 2;
		    gzip_min_length 1k;
		    gzip_buffers 4 16k;
		    gzip_http_version 1.1;
		    gzip_types text/plain application/x-javascript text/css application/xml application/javascript;
			
			log_format main '$request_time $upstream_response_time $remote_addr - $upstream_addr [$time_local] '
		    '"$host" "$request" $status $bytes_sent '
		    '"$http_referer" "$http_user_agent" "$gzip_ratio" "$http_x_forwarded_for" - "$server_addr" ';

		    server {

		    }
		}
```