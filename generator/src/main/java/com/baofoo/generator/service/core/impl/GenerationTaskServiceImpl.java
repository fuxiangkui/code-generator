package com.baofoo.generator.service.core.impl;

import com.alibaba.fastjson.JSONObject;
import com.baofoo.framework.crud.common.ResponseEntityDto;
import com.baofoo.framework.crud.service.BaseServiceImpl;
import com.baofoo.framework.crud.utils.CustomPageHepler;
import com.baofoo.generator.business.core.GenerationTaskBusiness;
import com.baofoo.generator.contract.core.dto.GenerationTaskDto;
import com.baofoo.generator.model.core.GenerationTaskEntity;
import com.baofoo.generator.service.core.GenerationTaskService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
* Created by ${author}  on 19-1-10 上午11:03.
* 默认服务接口实现
*/
@Service("generationTaskService")
@SuppressWarnings("all")
public class GenerationTaskServiceImpl extends BaseServiceImpl implements GenerationTaskService {

private static final Logger LOGGER = LoggerFactory.getLogger(GenerationTaskServiceImpl.class);

        @Autowired
        private GenerationTaskBusiness generationTaskBusiness;

        //插入默认
        @Override
        public ResponseEntityDto<?> insertGenerationTask(JSONObject param)  {
            GenerationTaskDto generationTaskDto= JSONObject.toJavaObject(param,GenerationTaskDto.class);
            GenerationTaskEntity generationTaskEntity=new GenerationTaskEntity();
            BeanUtils.copyProperties(generationTaskDto,generationTaskEntity);
            generationTaskBusiness.insert(generationTaskEntity);
            BeanUtils.copyProperties(generationTaskEntity,generationTaskDto);
            return this.buildSuccesResponseEntityDto(generationTaskDto);
        }

        //插入默认（批量）
        @Override
        public ResponseEntityDto<?> insertGenerationTaskList(JSONObject param)  {
            List<GenerationTaskDto> generationTaskDtoList= JSONObject.parseArray(param.toJSONString(),GenerationTaskDto.class);
            List<GenerationTaskEntity> dataList=new ArrayList<>();
            generationTaskDtoList.forEach(item->{
                GenerationTaskEntity generationTaskEntity=new GenerationTaskEntity();
                BeanUtils.copyProperties(item,generationTaskEntity);
                dataList.add(generationTaskEntity);
            });
            generationTaskBusiness.insertBatch(dataList);
            return this.buildSuccesResponseEntityDto();
        }

        //更新默认
        @Override
        public ResponseEntityDto<?> updateGenerationTask(JSONObject param)  {
            GenerationTaskDto generationTaskDto= JSONObject.toJavaObject(param,GenerationTaskDto.class);
            GenerationTaskEntity generationTaskEntity=new GenerationTaskEntity();
            BeanUtils.copyProperties(generationTaskDto,generationTaskEntity);
            generationTaskBusiness.update(generationTaskEntity);
            BeanUtils.copyProperties(generationTaskEntity,generationTaskDto);
            return this.buildSuccesResponseEntityDto(generationTaskDto);
        }

        //查询所有默认记录
        @Override
        public ResponseEntityDto<?> queryAllGenerationTaskList(JSONObject param)  {
            List<GenerationTaskEntity> resultList=generationTaskBusiness.getAll();
            List<GenerationTaskDto> dataList=new ArrayList<>();
            resultList.forEach(item->{
                GenerationTaskDto generationTaskDto=new GenerationTaskDto();
                BeanUtils.copyProperties(item,generationTaskDto);
                dataList.add(generationTaskDto);
            });
            return this.buildSuccesResponseEntityDto(dataList);
        }

        //通过条件查询默认记录（不分页）
        @Override
        public ResponseEntityDto<?> queryGenerationTaskListByParam(JSONObject param)  {
            GenerationTaskDto generationTaskDto= JSONObject.toJavaObject(param,GenerationTaskDto.class);
            GenerationTaskEntity queryParam=new GenerationTaskEntity();
            BeanUtils.copyProperties(generationTaskDto,queryParam);
            List<GenerationTaskEntity> resultList=generationTaskBusiness.findByObj(queryParam);
            List<GenerationTaskDto> dataList=new ArrayList<>();
            resultList.forEach(item->{
                    BeanUtils.copyProperties(item,generationTaskDto);
                    dataList.add(generationTaskDto);
            });
            return this.buildSuccesResponseEntityDto(dataList);
        }

        //通过条件查询默认记录（分页）
        @Override
        public ResponseEntityDto<?> queryPageGenerationTaskListByParam(JSONObject param)  {
            GenerationTaskDto generationTaskDto= JSONObject.toJavaObject(param,GenerationTaskDto.class);
            Page<GenerationTaskDto> page = PageHelper.startPage(param.getIntValue("pageNo"), param.getIntValue("pageSize"));
            GenerationTaskEntity queryParam=new GenerationTaskEntity();
            BeanUtils.copyProperties(generationTaskDto,queryParam);
            List<GenerationTaskEntity> list=generationTaskBusiness.findByObj(queryParam);
            PageInfo<GenerationTaskDto> pageInfo = CustomPageHepler.wrapPageInfo(GenerationTaskDto.class, list);
            return this.buildSuccesResponseEntityDto(pageInfo);
        }

        //通过ID查询默认记录
        @Override
        public ResponseEntityDto<?> queryGenerationTaskById(JSONObject param)  {
            GenerationTaskDto generationTaskDto= JSONObject.toJavaObject(param,GenerationTaskDto.class);
            GenerationTaskEntity result=generationTaskBusiness.getById(generationTaskDto.getId());
            BeanUtils.copyProperties(result,generationTaskDto);
            return this.buildSuccesResponseEntityDto(generationTaskDto);
        }

        //通过Id删除默认记录
        @Override
        public ResponseEntityDto<?> deleteGenerationTaskById(JSONObject param)  {
            GenerationTaskDto generationTaskDto= JSONObject.toJavaObject(param,GenerationTaskDto.class);
            generationTaskBusiness.deleteById(generationTaskDto.getId());
            return this.buildSuccesResponseEntityDto();
        }

        //批量删除默认记录
        @Override
        public ResponseEntityDto<?> deleteBatchGenerationTask(JSONObject param)  {
            List<GenerationTaskDto> generationTaskDtoList= JSONObject.parseArray(param.toJSONString(),GenerationTaskDto.class);
            List<GenerationTaskEntity> dataList=new ArrayList<>();
            generationTaskDtoList.forEach(item->{
                GenerationTaskEntity generationTaskEntity=new GenerationTaskEntity();
                BeanUtils.copyProperties(item,generationTaskEntity);
                dataList.add(generationTaskEntity);

            });
            generationTaskBusiness.delete(dataList);
            return this.buildSuccesResponseEntityDto();
        }


}
