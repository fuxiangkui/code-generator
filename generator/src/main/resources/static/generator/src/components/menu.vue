<template>
<aside id="sidebar_left" :class="stateObject.sidebarTheme.theme">
	<el-menu :defaultActive="$route.path" class="el-menu-vertical-demo" :collapse="!stateObject.isShowSort" router :theme="stateObject.sidebarTheme.theme" unique-opened @select="buildCurrentMemu">
		<template v-if="menuItems && menuItems.length > 0" v-for="item in menuItems">
			<el-submenu v-if="item.children" :index="item.path">
				<template slot="title">
					<!--第三方字体库用法-->
					<i  :class="item.icon"></i>
					<!--elementUI字体库用法-->
					<!--<i :class="!item.icon ? 'el-icon-setting' : item.icon"></i>-->
					<span slot="title" class="sidebar-title">{{ item.name }}</span>
				</template>
  			<template v-if="item.children" v-for="subItem in item.children">
  				<el-menu-item :index="subItem.path" >
  					<i  :class="subItem.icon"></i>
  					<span slot="title">{{ subItem.name }}</span>
  				</el-menu-item>
  			</template>
			</el-submenu>
			<el-menu-item v-if="!item.children" :index="item.path">
				<i  :class="item.icon"></i>
				<span slot="title">{{ item.name }}</span>
			</el-menu-item>
		</template>
	</el-menu>
</aside>
</template>
<script>
import { mapState } from 'vuex'
import menuJson from './menuJson.js'
import '@/font/iconfont.css'

export default {
	name: 'menuDiv',
	data () {
	    return {
  			menuItems : menuJson.data.menuItems
	    }
	},
	created (){//对象被创建时

	},
	mounted () {
	},
	computed: mapState({
    	'stateObject' : state => state.stateObject //简写。完整的:this.$store.state.stateObject访问
	}),
	methods: {

        buildCurrentMemu(index, indexPath){
            var currentMenu=[];
            for(var i=0;i<indexPath.length;i++){
                var temp={};
                temp.id=indexPath[i];
                temp.path=indexPath[i];
                for(var j=0;j<this.menuItems.length;j++){
                    if(this.menuItems[j].path==indexPath[i]){
                        temp.name=this.menuItems[j].name;
                        break;
					}else{
                        if(typeof(this.menuItems[j].children)!="undefined"&& this.menuItems[j].children.length>0)
						{
                            for(var m=0;m<this.menuItems[j].children.length;m++){
                                if(this.menuItems[j].children[m].path==indexPath[i]){
                                    temp.name=this.menuItems[j].children[m].name;
                                    break;
								}
							}
						}
					}
				}
                currentMenu.push(temp);
			}
            // console.log(this.menuItems);
            // console.log(currentMenu);
            var storage=window.localStorage;
            storage.setItem("currentMenu",JSON.stringify(currentMenu));
		}
	}
}
</script>
<style scoped>

	.el-menu {
		border-right: unset;
	}
	.el-menu-item, .el-submenu__title span{
		color: white;
	}
	.el-submenu .el-menu-item{
		min-width:50px
	}
	.el-menu-vertical-demo:not(.el-menu--collapse) {
	    width: 250px;
	    min-height: 400px;
	    transition: all .2s ease;
	}
	.el-menu--collapse .el-submenu .el-menu {
		z-index: 1000;
	}

	.el-submenu.is-active .el-menu-item.is-active{
		color: rgb(255, 208, 75);
	 }
	.el-menu-item:focus, .el-menu-item:hover{
		background-color: rgba(0,0,0,.8);
	}
	.el-menu-item i {
		color: white;
		margin: 10px 0;
		transition: font-size .25s ease-out 0s
	}
	i.icon {
	    font-size: 20px;
	    color: white;
	    margin: 10px 0;
	    transition: font-size .25s ease-out 0s
	}
</style>
