<script>
import { mapState } from 'vuex'
import { vueConfig } from '@/router/router'
export default {
  data () {
    return {
    }
  },
  components : {
    'login' : vueConfig.login,
    'header_tpl' : vueConfig.tpl.header_tpl,
    'menu_tpl'   : vueConfig.tpl.menu_tpl
  },
  computed: mapState({
    'stateObject' : state => state.stateObject //简写。完整的:this.$store.state.stateObject访问
  }),
  mounted (){
    window.VM__ = this;
  }
}
</script>
<template>
  <div id="app" :class="stateObject.isShowSort ? '' : 'sb-l-m'">
        <header_tpl></header_tpl>
        <menu_tpl   ref="menu_tpl"></menu_tpl>
        <section class="content"  id="appBody" v-loading.fullscreen = "stateObject.fullscreenLoading" element-loading-text="拼命加载中">
          <router-view></router-view>
        </section>
  </div>
</template>
<style scoped>

</style>
