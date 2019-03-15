<template>
    <div id="content_wrapper">
            <el-dialog title="代码自动生成任务" :visible.sync="generationTaskFormDialog">
                <el-form :model="generationTaskFormData" :rules="rules" ref="generationTaskFormData">
    <el-form-item label="作者:" prop="auth"><el-input v-model="generationTaskFormData.auth"></el-input></el-form-item>
      <el-form-item label="任务名称:" prop="taskName"><el-input v-model="generationTaskFormData.taskName"></el-input></el-form-item>
      <el-form-item label="任务状态:" prop="taskStatus"><el-input v-model="generationTaskFormData.taskStatus"></el-input></el-form-item>
      <el-form-item label="文件名称:" prop="fileName"><el-input v-model="generationTaskFormData.fileName"></el-input></el-form-item>
      <el-form-item label="文件下载url:" prop="fileDownloadUrl"><el-input type="textarea" :rows="2" v-model="generationTaskFormData.fileDownloadUrl"> </el-input></el-form-item>
    <el-form-item label="数据源:" prop="dataSource"><el-input type="textarea" :rows="2" v-model="generationTaskFormData.dataSource"> </el-input></el-form-item>
    <el-form-item label="项目元信息:" prop="projectMetadata"><el-input type="textarea" :rows="2" v-model="generationTaskFormData.projectMetadata"> </el-input></el-form-item>
    <el-form-item label="依赖信息:" prop="dependencyList"><el-input type="textarea" :rows="2" v-model="generationTaskFormData.dependencyList"> </el-input></el-form-item>
    <el-form-item label="表映射:" prop="domainMappers"><el-input type="textarea" :rows="2" v-model="generationTaskFormData.domainMappers"> </el-input></el-form-item>
  
                    <el-form-item>
                                <el-button @click="generationTaskFormDialog = false">取 消</el-button>
                                <el-button type="primary" @click="submitForm('generationTaskFormData')">确 定</el-button>
                            </el-form-item>
                </el-form>
            </el-dialog>
            <el-row :gutter="24">
                <el-col :span="24">
                        <el-row >
                            <el-form :model="generationTaskQueryFormData" inline>
                            <el-form-item label="作者:"><el-input v-model="generationTaskQueryFormData.auth"></el-input></el-form-item>
                            <el-form-item label="任务名称:"><el-input v-model="generationTaskQueryFormData.taskName"></el-input></el-form-item>
                            <el-form-item label="任务状态:"><el-input v-model="generationTaskQueryFormData.taskStatus"></el-input></el-form-item>
                            <el-form-item label="文件名称:"><el-input v-model="generationTaskQueryFormData.fileName"></el-input></el-form-item>
                            <el-form-item label="文件下载url:"><el-input v-model="generationTaskQueryFormData.fileDownloadUrl"></el-input></el-form-item>
                            <el-form-item label="数据源:"><el-input v-model="generationTaskQueryFormData.dataSource"></el-input></el-form-item>
                            <el-form-item label="项目元信息:"><el-input v-model="generationTaskQueryFormData.projectMetadata"></el-input></el-form-item>
                            <el-form-item label="依赖信息:"><el-input v-model="generationTaskQueryFormData.dependencyList"></el-input></el-form-item>
                            <el-form-item label="表映射:"><el-input v-model="generationTaskQueryFormData.domainMappers"></el-input></el-form-item>
                            <el-form-item><el-button @click="queryData" type="primary">查 询</el-button></el-form-item>
                            <el-form-item><el-button @click="addData" type="primary">新 增</el-button></el-form-item>
                        </el-form>

                        </el-row>
                        <el-row >
                            <el-table :data="generationTaskList"  border stripe style="width: 100%"  highlight-current-row  >
                                <el-table-column prop="id" label="#" width="60" ></el-table-column>
                            <el-table-column prop="auth" label="作者" ></el-table-column>
                            <el-table-column prop="taskName" label="任务名称" ></el-table-column>
                            <el-table-column prop="taskStatus" label="任务状态" ></el-table-column>
                            <el-table-column prop="fileName" label="文件名称" ></el-table-column>
                            <el-table-column prop="fileDownloadUrl" label="文件下载url" ></el-table-column>
                            <el-table-column prop="dataSource" label="数据源" ></el-table-column>
                            <el-table-column prop="projectMetadata" label="项目元信息" ></el-table-column>
                            <el-table-column prop="dependencyList" label="依赖信息" ></el-table-column>
                            <el-table-column prop="domainMappers" label="表映射" ></el-table-column>
                                <el-table-column fixed="right" label="操作" width="120">
                                    <template slot-scope="scope">
                                        <el-button @click="editGenerationTask(scope.row)" type="text" size="small">编 辑</el-button>
                                        <el-button @click="deleteGenerationTask(scope.row)" type="text" size="small">删 除</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <div align="right">
                                <el-pagination
                                        @size-change="sizeChange"
                                        @current-change="currentChange"
                                        :current-page="pager.pageNo"
                                        :page-sizes="[10, 20, 50]"
                                        :page-size="pager.pageSize"
                                        layout="sizes, prev, pager, next ,total,  jumper"
                                        :total="pager.pageTotal">
                                </el-pagination>
                            </div>
                        </el-row>
                </el-col>
            </el-row>
    </div>
</template>
<script type="text/ecmascript-6">
    export default {
        data() {
             return {
                 pager:{
                     pageNo:1,
                     pageSize:10,
                     pageTotal:100
                 },

                 generationTaskFormData:{
                    auth:"",
                    taskName:"",
                    taskStatus:"",
                    fileName:"",
                    fileDownloadUrl:"",
                    dataSource:"",
                    projectMetadata:"",
                    dependencyList:"",
                    domainMappers:"",
                         },
                generationTaskQueryFormData:{
                    auth:"",
                    taskName:"",
                    taskStatus:"",
                    fileName:"",
                    fileDownloadUrl:"",
                    dataSource:"",
                    projectMetadata:"",
                    dependencyList:"",
                    domainMappers:"",
                        },
                rules: {
                                            auth: [
                            { required: true, message: '请输入作者', trigger: 'blur' },
                        ],
                                            taskName: [
                            { required: true, message: '请输入任务名称', trigger: 'blur' },
                        ],
                                            taskStatus: [
                            { required: true, message: '请输入任务状态', trigger: 'blur' },
                        ],
                                            fileName: [
                            { required: true, message: '请输入文件名称', trigger: 'blur' },
                        ],
                                            fileDownloadUrl: [
                            { required: true, message: '请输入文件下载url', trigger: 'blur' },
                        ],
                                            dataSource: [
                            { required: true, message: '请输入数据源', trigger: 'blur' },
                        ],
                                            projectMetadata: [
                            { required: true, message: '请输入项目元信息', trigger: 'blur' },
                        ],
                                            dependencyList: [
                            { required: true, message: '请输入依赖信息', trigger: 'blur' },
                        ],
                                            domainMappers: [
                            { required: true, message: '请输入表映射', trigger: 'blur' },
                        ],
                                    },
                generationTaskFormDialog:false,
                generationTaskList: []
             };
        },
        methods: {
            sizeChange(val){
                var _self = this;
                _self.pager.pageSize = val;
                _self.queryData();
            },
            currentChange(val){
                var _self = this;
                _self.pager.pageNo = val;
                _self.queryData();
            },
            addData(){
                var _self = this;
                _self.generationTaskFormData={
                    auth:"",
                    taskName:"",
                    taskStatus:"",
                    fileName:"",
                    fileDownloadUrl:"",
                    dataSource:"",
                    projectMetadata:"",
                    dependencyList:"",
                    domainMappers:"",
                        };
                _self.generationTaskFormDialog = true;
            },
            editGenerationTask(row){
                var _self = this;
                //_self.generationTaskFormData= row;
                _self.generationTaskFormData=JSON.parse( JSON.stringify(row) );
                _self.generationTaskFormDialog = true;
                this.$refs['generationTaskFormData'].resetFields();
            },
            saveData(){
                var _self = this;
                var param = {};
                param= _self.generationTaskFormData;
                var insertGenerationTask = new common.insertGenerationTask();
                insertGenerationTask.param = JSON.stringify(param);
                insertGenerationTask.execute(function (data) {
                    if (data.status == 1 && data.error == "00000000") {
                        _self.queryData();
                    } else {

                    }
                });
            },
            submitForm(formName) {
                var _self = this;
                var param = {};
                param=self.generationTaskFormData;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                    _self.saveData();
                    } else {
                    return false;
                    }
                });
            },
            deleteGenerationTask(row){
                var _self = this;
                var param = {};
                param.id=row.id;
                var deleteGenerationTaskById = new common.deleteGenerationTaskById();
                deleteGenerationTaskById.param = JSON.stringify(param);
                deleteGenerationTaskById.execute(function (data) {
                    if (data.status == 1 && data.error == "00000000") {
                        _self.queryData();
                    } else {

                    }
                });
            },
            queryData(){
                var _self = this;
                _self.generationTaskFormDialog = false;
                var param = {};
                param=_self.generationTaskQueryFormData;
                param.pageNo=_self.pager.pageNo;
                param.pageSize=_self.pager.pageSize;
                var queryGenerationTaskListByParam = new common.queryPageGenerationTaskListByParam();
                queryGenerationTaskListByParam.param = JSON.stringify(param);
                queryGenerationTaskListByParam.execute(function (data) {
                    if (data.status == 1 && data.error == "00000000") {
                        _self.generationTaskList = data.data.list;
                        _self.pager.pageTotal = data.data.total;
                    } else {

                    }
                });
            }
        },
        mounted: function () {
            var _self = this;
            _self.queryData();
        }
    }
</script>

<style type="scss">
    .el-row {
        margin-bottom: 20px;
        &:last-child {
            margin-bottom: 0;
        }
    }
</style>