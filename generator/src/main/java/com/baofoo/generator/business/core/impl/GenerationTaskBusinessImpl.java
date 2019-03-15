package com.baofoo.generator.business.core.impl;

import com.baofoo.framework.crud.bussiness.impls.BaseBussinessImpl;
import com.baofoo.generator.business.core.GenerationTaskBusiness;
import com.baofoo.generator.dao.core.GenerationTaskDao;
import com.baofoo.generator.model.core.GenerationTaskEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
* Created by ${author}  on 19-1-10 上午11:03.
* 代码自动生成任务业务实现
*/
@Component("generationTaskBusiness")
public class GenerationTaskBusinessImpl extends BaseBussinessImpl<GenerationTaskEntity> implements GenerationTaskBusiness {

        //引入Dao
        @Resource
        private GenerationTaskDao generationTaskDao;

}
