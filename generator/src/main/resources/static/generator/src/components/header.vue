<template>
  <nav class="navbar navbar-default navbar-fixed-top">
    <div class="navbar-left dark">
      <el-col :span="18" class="navbar_parent">
        <span class="navbar-brand text-uppercase" style="font-size:24px;padding-left:20px;">
          <router-link to="/index">代码自动生成</router-link>
        </span>
      </el-col>
      <el-col :span="6"  class="navbar_parent sildmenu_icon"><i id="sildmenu_icon" @click="toggleMenu" :class="el_icon"></i></el-col>
    </div>
    <div class="navbar-logo">
      <!--<img src="./../image/logo.png" alt="avatar" width="80%" />-->
    </div>
    <div class="navbar-right">

    </div>
  </nav>
</template>

<script>
import { mapState } from 'vuex'
export default {
  name: 'headerDiv',
  data () {
    return {
      el_icon : 'el-icon-d-arrow-left'
    }
  },
  mounted () {
    window.VM__ = this;
  },
  computed: mapState({
    'stateObject' : state => state.stateObject, //简写。完整的:this.$store.state.stateObject访问
    username() {
      return window.account.account;
    }
  }),

  methods: {
    toggleMenu ( e ){//开、关左侧菜单
      this.$data.el_icon = this.$data.el_icon != 'el-icon-d-arrow-left' ? 'el-icon-d-arrow-left' : 'el-icon-d-arrow-right';
      this.$store.state.stateObject.isShowSort = !this.$store.state.stateObject.isShowSort;
    }
  }
}
</script>

<style scoped lang="scss">
.navbar-logo {
    background-size: 100% 100%;
    float: left;
    width: 200px;
    height: 70px;
}
.username {
  float: left;
  width: 300px;
  height: 70px;
  text-align: center;
  span{
    line-height:70px;
    font-size:14px;
  }
  span.r{
    font-size:20px;
  }
}
</style>
