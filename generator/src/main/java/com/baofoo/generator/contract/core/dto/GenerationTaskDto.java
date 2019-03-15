package com.baofoo.generator.contract.core.dto;

import com.baofoo.framework.crud.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
* Created by ${author} on 19-1-10 上午11:03.
* 代码自动生成任务前端展示用Dto
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class GenerationTaskDto extends BaseEntity {

        private static final long serialVersionUID = 1L;

        //作者
        private String auth;
        //任务名称
        private String taskName;
        //任务状态
        private String taskStatus;
        //文件名称
        private String fileName;
        //文件下载url
        private String fileDownloadUrl;
        //数据源
        private String dataSource;
        //项目元信息
        private String projectMetadata;
        //依赖信息
        private String dependencyList;
        //表映射
        private String domainMappers;

}
