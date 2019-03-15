import Vue from 'vue'
import Router from './router/router'
import App from './App'
import Vuex from 'vuex'
import Store from './vuex/store'
import ApiConfig from '@/commonjs/apiConfig.js'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(ElementUI);

require('@/commoncss/theme.css');
require('@/commoncss/common.css');

// theme css
import 'codemirror/theme/solarized.css'

//commonjs
window.common = ApiConfig.m;

Vue.use(Vuex);
Vue.config.devtools = true;
window.vm = new Vue({
	el: '#app',
	router : Router,
	store : Store,
	template: '<App/>',
	components: { App }
});

