package com.baofoo.generator.model.generator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class DataSource {

    private String ip;

    private String port;

    private String database;

    private String userName;

    private String password;

    private String url;

}
