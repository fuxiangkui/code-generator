<template>
    <div></div>
</template>
<script>
export default {
  name: 'index',
  data () {
    return {

    }
  },
  mounted (){
      var storage=window.localStorage;
      storage.setItem("currentMenu","");
      $(".el-breadcrumb").hide();
  },
  methods : {

  }
}
</script>
<style scoped>
  .sgp p{
    font-size: 17px;
    color: red;
    margin: 0 0 0 30px;
  }

</style>
