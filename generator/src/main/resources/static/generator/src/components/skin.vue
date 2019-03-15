<template>
<div>
	<div id="skin-toolbox" class="bg-white" :class="isToolboxOpen" style="display:none;">
		<div class="panel">
			<div class="panel-heading" @click="themeToggle">
				<span class="panel-icon" :class="theme.opts_class"><i class="el-icon-setting"></i></span>
				<span class="panel-title"  >皮肤设置选项</span>
			</div>
			<div class="panel-body">
				<el-tabs v-model="activeName" @tab-click="skinToggle">
				    <el-tab-pane label="导航栏" name="first">
				    	<div class="tab-content p20">
							<div id="toolbox-header" class="tab-pane" >
								<form id="toolbox-header-skin">
									<div class="skin-toolbox-swatches">
										<div v-for="(the,index) in theme.header">
											<label class="el-radio radio" @click="actionAheme('headerSkin',the)" :class="the.colors">
												<span  class="el-radio__input" :class="the.checked ? 'is-checked':''">
												<span  class="el-radio__inner"></span>
												<input class="el-radio__original" :id="'headerSkin'+(index+1)" type="radio" :value="the.value" /></span>
												<span  class="el-radio__label" :for="'headerSkin'+(index+1)">{{the.title}}</span>
											</label>

										</div>
									</div>
								</form>
							</div>
						</div>
				    </el-tab-pane>
				    <el-tab-pane label="侧边栏" name="second">
				    	<div class="tab-content p20">
							<div id="toolbox-header" class="tab-pane">
								<form id="toolbox-header-skin">
									<div class="skin-toolbox-swatches">
										<div v-for="(the,index) in theme.sidebar">
											<label class="el-radio radio" @click="actionAheme('sidebarSkin',the)" :class="the.colors">
												<span  class="el-radio__input" :class="the.checked ? 'is-checked':''">
												<span  class="el-radio__inner"></span>
												<input class="el-radio__original" :id="'sidebarSkin'+(index+1)" type="radio" :value="the.value" /></span>
												<span  class="el-radio__label" :for="'sidebarSkin'+(index+1)">{{the.title}}</span>
											</label>

										</div>
									</div>
								</form>
							</div>
						</div>
				    </el-tab-pane>
			  	</el-tabs>
				<div class="center"><button type="button" class="el-button el-button--primary" :class="theme.opts_class" @click="clearLocalStorage"><span>初始化配置</span></button></div>
			</div>
		</div>
	</div>
</div>
</template>

<script>
export default {
	name: 'footerDiv',
	data () {
		return {
			isToolboxOpen : '',
			theme : {
				header: [
					{title: "蓝绿色", colors: "checkbox-primary", value: "bg-primary", checked: true},
					{title: "绿色"  , colors: "checkbox-success", value: "bg-success",checked: false},
					{title: "淡蓝色", colors: "checkbox-info", value: "bg-info",checked: false},
					{title: "黄色"  , colors: "checkbox-warning", value: "bg-warning",checked: false},
					{title: "红色"  , colors: "checkbox-danger", value: "bg-danger",checked: false},
					{title: "紫色"  , colors: "checkbox-alert", value: "bg-alert",checked: false},
					{title: "深蓝色", colors: "checkbox-system", value: "bg-system",checked: false},
					{title: "灰色"  , colors: "checkbox-disabled", value: "bg-dark",checked: false},
					{title: "深灰色", colors: "checkbox-disabled2", value: "bg-dark2",checked: false}
				],
				sidebar: [
					{title: "灰色"  , colors: "checkbox-disabled",  value: "light",checked: false},
					{title: "深灰色", colors: "checkbox-disabled2", value: "dark", checked: true}
				],
				opts_class :"checkbox-primary",

			},
		  	headerSkin : true,
		    skin : {//右测
				active : true //设置默认选中
			},
			activeName: 'first',
			selectionObj : ''
		}
	},
	mounted () {},
    created : function(){
    	//从本地获取配置数据
		var selectionGet = localStorage.getItem("theme-selection");
		//获取失败，存储默认配置
		if ( !selectionGet ) {
			localStorage.setItem("theme-selection", JSON.stringify(this.$data.theme));
			this.$data.theme = JSON.parse(localStorage.getItem("theme-selection"));
		}else{
			this.$data.theme = JSON.parse(selectionGet);
		}
		this.loadCheme();//加载头部默认样式
	},
	methods: {
		loadCheme(){//加载头部默认样式
			var selectionObj = this.$data.theme;
			if( selectionObj ){
				var header = selectionObj.header;
				for(var i =0;i<header.length;i++){
					var d = header[i];
					if( d.checked){
						this.$store.state.stateObject.headerTheme.theme = d.value;
						this.$data.theme.opts_class = d.colors;
						break;
					}
				}
				var sidebar = selectionObj.sidebar;
				for(var i =0;i<header.length;i++){
					var d = sidebar[i];
					if( d.checked){
						this.$store.state.stateObject.sidebarTheme.theme = d.value;
						break;
					}
				}
			}
		},
        skinToggle( tab , ev){

		},
		clearLocalStorage(){//初始化配置
			localStorage.clear();
			location.reload();
		},
		themeToggle( e ){
			if( !this.$data.isToolboxOpen ){
				this.$data.isToolboxOpen = "toolbox-open";
			}else{
				this.$data.isToolboxOpen = "";
			}
		},
		actionAheme( headerSkin , item ) {
			this.$data.headerSkin = headerSkin ? headerSkin : this.$data.headerSkin;
			if( this.$data.headerSkin === 'headerSkin'){
				this.$store.state.stateObject.headerTheme.theme = item.value;
				this.$data.theme.opts_class = item.colors;
				var header = this.$data.theme.header;
				for(var i=0;i<header.length;i++){
					var d = header[i];
					d.checked = d.value == item.value ? true : false;
				}
				localStorage.setItem("theme-selection", JSON.stringify(this.$data.theme));
			}else{
				this.$store.state.stateObject.sidebarTheme.theme = item.value;
				var sidebar = this.$data.theme.sidebar;
				for(var i=0;i<sidebar.length;i++){
					var d = sidebar[i];
					d.checked = d.value == item.value ? true : false;
				}
				localStorage.setItem("theme-selection", JSON.stringify(this.$data.theme));
			}
			return false;
		}
	}
}
</script>
<style scoped>

</style>