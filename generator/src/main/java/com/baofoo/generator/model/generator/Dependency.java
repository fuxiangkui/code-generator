package com.baofoo.generator.model.generator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Dependency implements Serializable {

        private String id;

        private String name;

        private String group;

        private String description;

        private String weight;

        private String keywords;
}
