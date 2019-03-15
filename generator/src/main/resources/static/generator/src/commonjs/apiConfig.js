import axios from 'axios'

import message from '@/commonjs/message.js'
import loading from '@/commonjs/loading.js'

function extend(p, c) {
	var c = c || {};
	for (var i in p) {
		if(typeof p[i] === 'object') {
			c[i] = (p[i].constructor === Array) ? [] : {};
			extend(p[i], c[i]);
		} else {
			c[i] = p[i];
		}
	}
	return c;
}

/***
 * 判断请求是否为查询请求，查询路径中请求以query字符串开头为查询请求
 * @param url
 * @returns {boolean}
 */
function questIsQueryByUrl(url) {
    if (typeof url == "undefined" || typeof url != "string" || url.length <= 0) {
        return false;
    }
    let urlArrary = url.split('/');
    if (urlArrary.length <= 0) {
        false;
    }
    return urlArrary[urlArrary.length - 1].toLowerCase().startsWith("query");
}

/**
 * 测试参数封装，查询参数不为空时，设置分页起始页为1，并将参数前后空字符清楚
 * @param params
 * @returns {*}
 */
function trimRequestParams(params) {
    if (typeof params == "undefined" || typeof params != "string" || params.length <= 0) {
        return params
    }
    let json_params = JSON.parse(params);
    let retParams = {};
    let containParams = false;
    for (let item in json_params) {
        if (!item.toLowerCase().startsWith("page") && String(json_params[item]).trim().length > 0) {
            containParams = true;
            break;
        }
    }

    if (!containParams) {
        return params;
    }

    for (let item in json_params) {
        if (item.toLowerCase().startsWith("pagen")) {
            retParams[item] = 1;
        } else {
            retParams[item] = typeof json_params[item] == "string" ? json_params[item].trim() : json_params[item];
        }
    }

    return retParams;
}
/*
* @type : get/post ， 默认post,
* @success : 请求成功callback
* @error : 请求失败callback

ajax请求使用 eq:
var load = new api.urlApi.resource.getResourceDetail();
load.param = {
  userName: 'normal',
  password:'b123456'
}
load.exec(function( success ){
  console.info("success:"+success.msg);
},function( error ){
  console.info("error");
})
*/

function Ajax() {}
Ajax.prototype.execute = function (success, error) {
	if(!this.isLoading){
        loading.loading(true);
	}
	var wait = this.wait || false; //是否是多个请求
	var type = 'post'
	var dataType = this.dataType || 'application/x-www-form-urlencoded';
	var url = this.url || '';
	var params = this.param || {};

    /** 查询请求中，参数不为空时修改分页参数 **/
/*    if(questIsQueryByUrl(url)){
        params = trimRequestParams(params);
    }*/
	//params.token = sessionStorage.getItem('token');
	//axios.defaults.headers.common['token'] = sessionStorage.getItem('token');
	if( type === 'post'){
		axios.defaults.headers.post['Content-Type'] = 'application/json';
	}
	axios[ type ]( url , params )
		.then(function ( ret ) {
			if( ret.statusText === 'OK'||ret.status === 200){
				if( ret.data.status === '0' || ret.data.status === 'success' || ret.data.status === '1'){
					success && success( ret.data );
				}else{
                    if(ret.data.msg=="未登录用户"||ret.data.msg=="登录过期"||ret.data.msg=="登录已过期,请刷新页面"){
                        window.location.href=window.location.href;
                    }
					//alert(ret.data.msg);
                    message.message(ret.data.msg);
					error && error( ret.data.msg || ret.data.error);
				}
			}
			loading.loading(false);
		}).catch(function ( ret ) {
            message.message(ret.message);
			//alert(ret.message);
			error && error( ret.message );
            loading.loading(false);
		});
};
var InhModel = {};
InhModel.Class = function( obj ){
	obj.prototype = extend( obj.prototype , Ajax.prototype );
	return obj;
}


window.apiUrl = '';
// var baseUrl = '';
//所有请求路径参数配置入口
var m = {};

m.getTables = InhModel.Class(function(){
    this.url = apiUrl + "/generator/api/tool/getTables";
    this.contentType="application/json";
}, Ajax);

m.generate = InhModel.Class(function(){
    this.url = apiUrl + "/generator/api/tool/generate";
    this.contentType="application/json";
}, Ajax);

m.getDependenpcies = InhModel.Class(function(){
    this.url = apiUrl + "/generator/api/tool/getDependenpcies";
    this.contentType="application/json";
}, Ajax);

//插入代码自动生成任务
m.insertGenerationTask = InhModel.Class(function(){
    this.url = apiUrl + "/generator/api/core/generationTask/insertGenerationTask";
    this.contentType="application/json";
}, Ajax);
//分页查询代码自动生成任务
m.queryPageGenerationTaskListByParam = InhModel.Class(function(){
    this.url = apiUrl + "/generator/api/core/generationTask/queryPageGenerationTaskListByParam";
    this.contentType="application/json";
}, Ajax);
//删除代码自动生成任务
m.deleteGenerationTaskById = InhModel.Class(function(){
    this.url = apiUrl + "/generator/api/core/generationTask/deleteGenerationTaskById";
    this.contentType="application/json";
}, Ajax);

export default { m };
