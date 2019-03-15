import Vue from 'vue'
import Router from 'vue-router'
import PageTransition from '@/components/PageTransition' //默认

var dataSrc = '';

Vue.use(Router);


/*按需加载*/
export const vueConfig = {
    index : resolve => require.ensure([], () => resolve(require('@/vue/index')), 'common/index'),//首页
    tpl : {
        header_tpl : resolve => require.ensure([], () => resolve(require('@/components/header')), 'common/header'),//头部
        menu_tpl   : resolve => require.ensure([], () => resolve(require('@/components/menu')), 'common/menu'),//左测菜单
    },
    vue : {
        generator: {
            codeGenerator       : resolve => require.ensure([], () => resolve(require('@/vue/generator/codeGenerator')), 'vue/generator/codeGenerator'),
        },
        core: {
            GenerationTask       : resolve => require.ensure([], () => resolve(require('@/vue/core/GenerationTask')), 'vue/core/GenerationTask'),

        },
}
}
export default new Router({
    mode: 'history', //去掉#
    routes: [
        {
            path: '/',
            component : PageTransition,
            children: [
                {path: '',							      component : vueConfig.index},//登陆
                {path: dataSrc+'/index',           		  component : vueConfig.index},//首页
                {path: dataSrc+'/generator/codeGenerator',         component : vueConfig.vue.generator.codeGenerator},
                {path: dataSrc+'/core/GenerationTask',         component : vueConfig.vue.core.GenerationTask}
            ]
        }
    ]
})
