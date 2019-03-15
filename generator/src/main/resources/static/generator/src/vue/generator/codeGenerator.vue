<template>
    <div id="content_wrapper" >
        <el-row style="margin:30px">
            <el-steps :active="activeStep" finish-status="success">
                <el-step title="项目基本配置"></el-step>
                <el-step title="框架配置"></el-step>
                <el-step title="表配置"></el-step>
                <el-step title="完成"></el-step>
            </el-steps>
        </el-row>
        <el-row  >
                <el-row v-show="activeStep===1">
                    <el-col :span="12">
                        <el-form ref="form" :model="generateProjectTask.projectMetadata" label-width="120px" >
                        <el-form-item label="Group:" @keyup.native="buildPakageName">
                            <el-input v-model="generateProjectTask.projectMetadata.group"></el-input>
                        </el-form-item>
                        <el-form-item label="Artifact:">
                            <el-input v-model="generateProjectTask.projectMetadata.artifact" @keyup.native="buildPakageName"></el-input>
                        </el-form-item>
                        <el-form-item label="Type:">
                            <el-select v-model="generateProjectTask.projectMetadata.type" placeholder="maven-project" disabled>
                                <el-option label="maven-project" value="maven-project"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="Language:">
                            <el-select v-model="generateProjectTask.projectMetadata.language" placeholder="java" disabled>
                                <el-option label="java" value="java"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="Packaging:">
                            <el-select v-model="generateProjectTask.projectMetadata.packaging" placeholder="jar" disabled>
                                <el-option label="jar" value="jar"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="Java Version:">
                            <el-select v-model="generateProjectTask.projectMetadata.javaVersion" placeholder="1.8" disabled>
                                <el-option label="1.8" value="1.8"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="version:">
                            <el-input v-model="generateProjectTask.projectMetadata.version"></el-input>
                        </el-form-item>
                        <el-form-item label="Name:">
                            <el-input v-model="generateProjectTask.projectMetadata.name" ></el-input>
                        </el-form-item>
                        <el-form-item label="Description:">
                            <el-input v-model="generateProjectTask.projectMetadata.description" ></el-input>
                        </el-form-item>
                        <el-form-item label="Package:">
                            <el-input v-model="generateProjectTask.projectMetadata.packageName" disabled></el-input>
                        </el-form-item>
                    </el-form>
                    </el-col>
                </el-row>
                <el-row v-show="activeStep===2"  style="padding:30px;" :gutter="5">
                    <el-col :span="4">
                        <el-table :data="dependenpcies"   ref="singleTable" border stripe   highlight-current-row @current-change="handleCurrentChange" size="small ">
                            <el-table-column prop="group" label="Dependenpcies"  ></el-table-column>
                        </el-table>
                    </el-col>
                    <el-col :span="11">
                        <el-table :data="dependenpcyDetail" ref="dependenpcyDetailTable" border stripe @select="handleSelectionChange" @select-all="selectAll" size="small ">
                            <el-table-column type="selection" width="55"></el-table-column>
                            <el-table-column prop="name" label="Detail" width="140" show-overflow-tooltip></el-table-column>
                            <el-table-column prop="description" label="Description" show-overflow-tooltip></el-table-column>
                        </el-table>
                    </el-col>
                    <el-col :span="8">
                        <el-table :data="generateProjectTask.dependencyList" border stripe   :span-method="objectSpanMethod" size="small ">
                            <el-table-column prop="group" label="Dependency" ></el-table-column>
                            <el-table-column prop="name" label="Detail" ></el-table-column>
                        </el-table>
                    </el-col>
                </el-row>
                <el-row v-show="activeStep===3" style="padding:30px;" >
                    <el-row >
                        <el-form ref="dataSource" :model="generateProjectTask.dataSource"  :inline="true"  >
                            <el-form-item label="IP:">
                                <el-input v-model="generateProjectTask.dataSource.ip" ></el-input>
                            </el-form-item>
                            <el-form-item label="端口:">
                                <el-input v-model="generateProjectTask.dataSource.port" ></el-input>
                            </el-form-item>
                            <el-form-item label="数据库:">
                                <el-input v-model="generateProjectTask.dataSource.database" ></el-input>
                            </el-form-item>
                            <el-form-item label="用户名:">
                                <el-input v-model="generateProjectTask.dataSource.userName"></el-input>
                            </el-form-item>
                            <el-form-item label="密码:">
                                <el-input v-model="generateProjectTask.dataSource.password"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="connect()">连接</el-button>
                            </el-form-item>
                        </el-form>
                    </el-row>
                    <el-row>
                        <el-table :data="showDomainMappers"  ref="domainMappersTable" data-page-size="10" data-limit-navigation="5" border style="width: 100%" stripe highlight-current-row  @select="domainMappersSelectionChange" @select-all="domainMappersSelectionChange">
                            <el-table-column type="selection" width="55"></el-table-column>
                            <el-table-column prop="tableName" label="表名" ></el-table-column>
                            <el-table-column label="表说明" >
                                <template scope="scope">
                                    <el-input  v-model="scope.row.tableDesc" placeholder="请输入内容" ></el-input>
                                </template>
                            </el-table-column>
                            <el-table-column label="模块名" >
                                <template scope="scope">
                                    <el-input  v-model="scope.row.modelName" placeholder="请输入内容"></el-input>
                                </template>
                            </el-table-column>
                            <el-table-column label="模块说明" >
                                <template scope="scope">
                                    <el-input  v-model="scope.row.modelDesc" placeholder="请输入内容"></el-input>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-row>
                </el-row>
            <el-row v-show="activeStep===4" style="padding:30px;" >
                <el-row >
                     <span>下载地址：</span>
                     <a :href=fileUrl >{{fileUrl}}</a>
                </el-row>
            </el-row>
                <el-row style="padding:30px;" >
                    <el-button style="margin-top: 12px;" @click="pre" type="primary" id="preButton" :disabled="activeStep==1">上一步</el-button>
                    <el-button style="margin-top: 12px;" @click="next" type="primary" :disabled="activeStep==4">下一步</el-button>
                </el-row>
        </el-row>
    </div>
</template>
<script type="text/ecmascript-6">
    export default {
        data() {
            return {
                flag:false,
                activeStep:1,
                generateProjectTask:{
                    projectMetadata:{
                        auth:"admin",
                        group:"com.example",
                        artifact:"demo",
                        type:"maven-project",
                        language:"java",
                        packaging:"jar",
                        javaVersion:"1.8",
                        version:"1.0.0",
                        name:"demo",
                        description:"示例项目",
                        packageName:"com.example.demo",
                    },
                    dependencyList:[],
                    dataSource:{
                        ip:"10.0.20.108",
                        port:"3306",
                        database:"baofoo_datamanager",
                        userName:"baofoo_api",
                        password:"baofoo_api@123",
                        url:""
                    },
                    domainMappers: []
                },
                showDomainMappers: [],
                dependenpcies:[],
                dependenpcyDetail:[],
                currentDependency: {},
                multipleSelection:[],
                spanArr:[],
                pos:0,
                fileUrl:"",

            };
        },

        created: function () {

        },
        watch: {

        },
        methods: {
            buildPakageName(){
                this.generateProjectTask.projectMetadata.packageName=this.generateProjectTask.projectMetadata.group+"."+this.generateProjectTask.projectMetadata.artifact;
            },
            pre(){
                this.activeStep--;
            },
            next() {
                this.activeStep++;
                if(this.activeStep==4){
                    this.generate();
                }
            },
            toggleSelection(rows) {
                if (rows) {
                    rows.forEach(row => {
                        this.$refs.dependenpcyDetailTable.toggleRowSelection(row,true);
                    });
                } else {
                    this.$refs.dependenpcyDetailTable.clearSelection();
                }
            },
            handleCurrentChange(val){
                this.dependenpcyDetail=val.children;
                this.currentDependency=val;

                var tempSelectedDependencies=[];
                for(var j=0;j<this.generateProjectTask.dependencyList.length;j++){
                    var temp2=this.generateProjectTask.dependencyList[j];
                    if(temp2.group==this.currentDependency.group){
                        for(var i=0;i<this.dependenpcyDetail.length;i++){
                            if(temp2.name==this.dependenpcyDetail[i].name){
                                tempSelectedDependencies.push(this.dependenpcyDetail[i])
                            }
                        }
                    }
                }
                var _self=this;
                setTimeout(() => {
                    _self.toggleSelection(tempSelectedDependencies);
                }, 0)
            },
            selectAll(selection){
                this.handleSelectionChange(selection,null);
            },
            domainMappersSelectionChange(selection){
                this.generateProjectTask.domainMappers = selection;
            },
            handleSelectionChange(selection,row){
                var tempSelectedDependencies=[];
                for(var j=0;j<this.generateProjectTask.dependencyList.length;j++){
                    var temp2=this.generateProjectTask.dependencyList[j];
                    if(temp2.group!=this.currentDependency.group){
                        tempSelectedDependencies.push(temp2)
                    }
                }
                for(var i=0;i<selection.length;i++){
                    tempSelectedDependencies.push(selection[i]);
                }
                this.generateProjectTask.dependencyList=tempSelectedDependencies;
                this.getSpanArr();
            },
            objectSpanMethod({ row, column, rowIndex, columnIndex }) {
                if (columnIndex === 0) {
                    const _row = this.spanArr[rowIndex];
                    const _col = _row > 0 ? 1 : 0;
                    return {
                        rowspan: _row,
                        colspan: _col
                    }
                }
            },
            getSpanArr() {
                var data=this.generateProjectTask.dependencyList;
                this.spanArr=[];
                for (var i = 0; i < data.length; i++) {
                    if (i === 0) {
                        this.spanArr.push(1);
                        this.pos = 0
                    } else {
                        // 判断当前元素与上一个元素是否相同
                        if (data[i].group === data[i - 1].group) {
                            this.spanArr[this.pos] += 1;
                            this.spanArr.push(0);
                        } else {
                            this.spanArr.push(1);
                            this.pos = i;
                        }
                    }
                }
                // console.log(this.spanArr);
            },
            connect() {
                var _self = this;
                _self.generateProjectTask.dataSource.url="jdbc:mysql://"+_self.generateProjectTask.dataSource.ip+":"+_self.generateProjectTask.dataSource.port+"/"+_self.generateProjectTask.dataSource.database+"?nullCatalogMeansCurrent=true"
                var param = _self.generateProjectTask.dataSource;
                var queryApiListByParam = new common.getTables();
                queryApiListByParam.param = JSON.stringify(param);
                queryApiListByParam.execute(function (data) {
                    if (data.status == 1 && data.error == "00000000") {
                        _self.showDomainMappers = data.data;
                        setTimeout(() => {
                            _self.$refs.domainMappersTable.toggleAllSelection();
                        }, 0)
                    } else {
                        _self.$message.error(data.msg);
                    }
                });
            },
            generate(){
                var _self = this;
                var generateParam = new common.generate();
                generateParam.param=JSON.stringify(_self.generateProjectTask);
                generateParam.execute(function (data) {
                    if (data.status == 1 && data.error == "00000000") {
                        _self.fileUrl=data.data;
                        _self.$message.success("生成成功,情点击下载");
                    } else {
                        _self.$message.error(data.msg);
                    }
                });
            },
        },
        mounted: function () {
            var _self = this;
            var getDependenpcies = new common.getDependenpcies();
            getDependenpcies.execute(function (data) {
                if (data.status == 1 && data.error == "00000000") {
                    _self.dependenpcies=data.data;

                    var web={};
                    web.id="web";
                    web.name="Web";
                    web.group="Web";
                    web.description="Full-stack web development with Tomcat and Spring MVC";

                    var mysql={};
                    mysql.id="mysql";
                    mysql.name="MySQL";
                    mysql.group="SQL";
                    mysql.description="MySQL JDBC driver";

                    var mybatis={};
                    mybatis.id="mybatis";
                    mybatis.name="MyBatis";
                    mybatis.group="SQL";
                    mybatis.description="Persistence support using MyBatis";

                    _self.generateProjectTask.dependencyList.push(web);
                    _self.generateProjectTask.dependencyList.push(mysql);
                    _self.generateProjectTask.dependencyList.push(mybatis);
                    _self.getSpanArr();
                    _self.$refs.singleTable.setCurrentRow(_self.dependenpcies[0]);
                    _self.connect();
                } else {
                    _self.$message.error(data.msg);
                }
            });




        }

    }
</script>
<style scoped>
    a{
        color:red;
    }
</style>
