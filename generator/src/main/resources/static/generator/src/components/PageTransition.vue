<template>
        <div style="height:100%;border:solid 15px #e9e9e9;">
            <bread-crumb></bread-crumb>
            <router-view class="child-view" style="padding:15px;"></router-view>
        </div>
</template>

<script>
  import breadCrumb from '../components/breadcrumb.vue'
  export default {
    data () {
      return {
        transitionName: 'slide-left'
      }
    },
     components:{
          breadCrumb
      }
  }
</script>

<style scoped>

</style>
