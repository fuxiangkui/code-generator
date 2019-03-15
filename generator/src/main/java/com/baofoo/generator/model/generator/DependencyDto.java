package com.baofoo.generator.model.generator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class DependencyDto implements Serializable {

        private String group;

        private List<Dependency> children=new ArrayList<>();


}
