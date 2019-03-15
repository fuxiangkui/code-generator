package com.baofoo.generator.model.core;

import com.baofoo.framework.crud.annotation.AutoId;
import com.baofoo.framework.crud.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Table;

/**
* Created by ${author}  on 19-1-10 上午11:03.
* 代码自动生成任务与表字段对应
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Table(name = "generation_task")
@AutoId(value=true)
public class GenerationTaskEntity extends BaseEntity{

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
