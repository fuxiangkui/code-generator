
package com.baofoo.generator.controller.core;

import com.alibaba.fastjson.JSONObject;
import com.baofoo.framework.crud.common.ResponseEntityDto;
import com.baofoo.generator.service.core.GenerationTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

/**
* Created by ${author} on 19-1-10 上午11:03.
*/
@Controller
@RequestMapping("/core/generationTask")
@Slf4j
public class GenerationTaskController {

		@Resource
		private GenerationTaskService generationTaskService;
		//插入代码自动生成任务
		@RequestMapping("/insertGenerationTask")
		@ResponseBody
		public ResponseEntityDto insertGenerationTask(@RequestBody JSONObject param, HttpServletRequest request) throws InvocationTargetException, IllegalAccessException {
			ResponseEntityDto<?> entity = generationTaskService.insertGenerationTask(param);
			return entity;
		}
		//批量插入代码自动生成任务
		@RequestMapping("/insertGenerationTaskList")
		@ResponseBody
		public ResponseEntityDto insertGenerationTaskList(@RequestBody JSONObject param, HttpServletRequest request) throws InvocationTargetException, IllegalAccessException {
			ResponseEntityDto<?> entity = generationTaskService.insertGenerationTaskList(param);
			return entity;
		}
		//更新代码自动生成任务
		@RequestMapping("/updateGenerationTask")
		@ResponseBody
		public ResponseEntityDto updateGenerationTask(@RequestBody JSONObject param) throws InvocationTargetException, IllegalAccessException {
			ResponseEntityDto<?> entity = generationTaskService.updateGenerationTask(param);
			return entity;
		}
		//查询所有代码自动生成任务记录
		@RequestMapping("/queryAllGenerationTaskList")
		@ResponseBody
		public ResponseEntityDto queryAllGenerationTaskList(@RequestBody JSONObject param) throws InvocationTargetException, IllegalAccessException {
			ResponseEntityDto<?> entity = generationTaskService.queryAllGenerationTaskList(param);
			return entity;
		}
		//通过条件查询代码自动生成任务记录(不分页)
		@RequestMapping("/queryGenerationTaskListByParam")
		@ResponseBody
		public ResponseEntityDto queryGenerationTaskListByParam(@RequestBody JSONObject param) throws InvocationTargetException, IllegalAccessException {
			ResponseEntityDto<?> entity = generationTaskService.queryGenerationTaskListByParam(param);
			return entity;
		}
		//通过条件查询代码自动生成任务记录(分页)
		@RequestMapping("/queryPageGenerationTaskListByParam")
		@ResponseBody
		public ResponseEntityDto queryPageGenerationTaskListByParam(@RequestBody JSONObject param) throws InvocationTargetException, IllegalAccessException {
			ResponseEntityDto<?> entity = generationTaskService.queryPageGenerationTaskListByParam(param);
			return entity;
		}
		//通过ID查询代码自动生成任务记录
		@RequestMapping("/queryGenerationTaskById")
		@ResponseBody
		public ResponseEntityDto queryGenerationTaskById(@RequestBody JSONObject param) throws InvocationTargetException, IllegalAccessException {
			ResponseEntityDto<?> entity = generationTaskService.queryGenerationTaskById(param);
			return entity;
		}
		//通过Id删除代码自动生成任务记录
		@RequestMapping("/deleteGenerationTaskById")
		@ResponseBody
		public ResponseEntityDto deleteGenerationTaskById(@RequestBody JSONObject param) throws InvocationTargetException, IllegalAccessException {
			ResponseEntityDto<?> entity = generationTaskService.deleteGenerationTaskById(param);
			return entity;
		}
		//批量删除代码自动生成任务记录
		@RequestMapping("/deleteBatchGenerationTask")
		@ResponseBody
		public ResponseEntityDto deleteBatchGenerationTask(@RequestBody JSONObject param) throws InvocationTargetException, IllegalAccessException {
			ResponseEntityDto<?> entity = generationTaskService.deleteBatchGenerationTask(param);
			return entity;
		}
}
