package com.baofoo.generator.contract.core.spi;

import com.alibaba.fastjson.JSONObject;
import com.baofoo.framework.crud.common.ResponseEntityDto;

import java.lang.reflect.InvocationTargetException;
/**
* Created by ${author} on 19-1-10 上午11:03.
* 代码自动生成任务对外接口
*/
public interface GenerationTaskSpi {
        //插入代码自动生成任务
        public ResponseEntityDto<?> insertGenerationTask(JSONObject param) throws InvocationTargetException, IllegalAccessException;
        //批量插入报表
        public ResponseEntityDto<?> insertGenerationTaskList(JSONObject param) throws InvocationTargetException, IllegalAccessException;
        //更新代码自动生成任务
        public ResponseEntityDto<?> updateGenerationTask(JSONObject param) throws InvocationTargetException, IllegalAccessException;
        //查询所有代码自动生成任务记录
        public ResponseEntityDto<?> queryAllGenerationTaskList(JSONObject param) throws InvocationTargetException, IllegalAccessException ;
        //通过条件查询代码自动生成任务记录(不分页)
        public ResponseEntityDto<?> queryGenerationTaskListByParam(JSONObject param) throws InvocationTargetException, IllegalAccessException;
        //通过条件查询代码自动生成任务记录(分页)
        public ResponseEntityDto<?> queryPageGenerationTaskListByParam(JSONObject param) throws InvocationTargetException, IllegalAccessException;
        //通过ID查询代码自动生成任务记录
        public ResponseEntityDto<?> queryGenerationTaskById(JSONObject param) throws InvocationTargetException, IllegalAccessException ;
        //通过Id删除代码自动生成任务记录
        public ResponseEntityDto<?> deleteGenerationTaskById(JSONObject param) throws InvocationTargetException, IllegalAccessException;
        //批量删除代码自动生成任务记录
        public ResponseEntityDto<?> deleteBatchGenerationTask(JSONObject param) throws InvocationTargetException, IllegalAccessException;

}
