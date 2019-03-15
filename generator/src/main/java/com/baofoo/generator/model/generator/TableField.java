package com.baofoo.generator.model.generator;

import com.baofoo.generator.utils.FieldUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class TableField implements Serializable {

    //group_name
    private String fieldName="";
    //GroupName
    private String domainField="";
    //groupName
    private String domainFieldFirstWordLowerCase="";

    private String desc="";

    private String type="";

    private String javaType="";

    private String length="20";

    private String point="0";

    private String isNull="N";

    private String isPrimaryKey="N";

    public void buildDomainField(){
        this.domainField = FieldUtils.getBeanName(fieldName);
    }
    public void buildDomainFieldFirstWordLowerCase(){
        this.domainFieldFirstWordLowerCase = FieldUtils.buildFirstWordLowerCase(domainField);
    }

    public void buildJavaType(){
        this.javaType = FieldUtils.sqlType2JavaType(type);
    }

}
