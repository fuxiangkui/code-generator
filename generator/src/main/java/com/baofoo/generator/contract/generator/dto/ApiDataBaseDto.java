package com.baofoo.generator.contract.generator.dto;

import com.baofoo.framework.crud.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述
 *
 * @author tony
 * @version 1.0.0 createTime: 2018/10/16 17:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ApiDataBaseDto extends BaseEntity {
    private String url;
    private String userName;
    private String password;
}
