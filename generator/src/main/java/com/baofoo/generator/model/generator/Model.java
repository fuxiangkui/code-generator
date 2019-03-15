package com.baofoo.generator.model.generator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Model {
    //api
    private String modelName;
    //API模块
    private String modelDesc;

    private ArrayList<DomainMapper> domainMappers=new ArrayList<>();
}
