<template>
        <el-breadcrumb separator=">" v-if="breadList.length>0">
            <el-breadcrumb-item v-for="(item,index) in breadList" :key="item.id"  :to="{ path: item.path }">{{item.name}}</el-breadcrumb-item>
        </el-breadcrumb>
</template>
<script>
    export default{
        created() {
            this.getBreadcrumb();
        },
        data() {
            return {
                breadList: [] // 路由集合
            }
        },
        methods: {
            getBreadcrumb() {
                var storage=window.localStorage;
                if(storage.getItem("currentMenu")!=""){
                    this.breadList=JSON.parse(storage.getItem("currentMenu"))||[];
                }

            }
        },
        watch: {
            //"$route": "getBreadcrumb"
            $route () {
                this.getBreadcrumb();
            }
        },
    }
</script>
