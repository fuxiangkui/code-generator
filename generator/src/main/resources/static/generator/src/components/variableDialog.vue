<template>
    <div id="step1">
        <el-dialog title="系统变量" :visible.sync="visible" :append-to-body="true" :lock-scroll='false' :close-on-click-modal="false">
            <el-alert
                    title="使用变量请使用${变量名},例如${yesterday}"
                    type="info"
                    show-icon :closable="false">
            </el-alert>
            <el-table
                    :data="variables"
                    stripe
                    style="width: 100%">
                <el-table-column
                        prop="name"
                        label="变量名"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="value"
                        label="变量值"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="resolved"
                        label="解析之后的值">
                </el-table-column>
                <el-table-column
                        prop="createTime"
                        label="创建时间">
                </el-table-column>
                <el-table-column
                        prop="updateTime"
                        label="更新时间">
                </el-table-column>
            </el-table>
        </el-dialog>
    </div>
</template>
<style>
</style>
<script type="text/ecmascript-6">
    import {codemirror} from 'vue-codemirror'
    var store={};
    export default {
        data() {
            return {
                variables:[],
                visible:false,
                name:null
            };
        },
        components: {codemirror},
        created: function () {
            store=this;
        },
        watch: {},
        methods: {
            showDialog(){
                store.visible=true;
                var findVariables = new common.findVariables();
                var param = {
                    name: this.name,
                };
                findVariables.param = JSON.stringify(param);
                var _self = store;
                findVariables.execute(function (data) {
                    if (data.status == 1 && data.error == "00000000") {
                        _self.variables=data.data;
                    } else {
                        _self.$message.error(data.msg);
                    }
                });
            }

        },
        mounted: function () {

        }

    }
</script>