package com.baofoo.generator.model.generator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ProjectMetadata implements Serializable {

        private String group="com.example";

        private String artifact="demo";

        private String nameFirstWordUpperCase="Demo";

        private String type="maven-project";

        private String language="java";

        private String packaging="jar";

        private String javaVersion="1.8";

        private String version="1.0.0";

        private String name="demo";

        private String description="示例项目";

        private String packageName="com.example.demo";

        private String packagePath;

        private String bootVersion="2.1.1.RELEASE";

        public void buildNameFirstWordUpperCase(){
                this.nameFirstWordUpperCase= this.name.substring(0, 1).toUpperCase()+this.name.substring(1,this.name.length());
        }
        public void buildPackagePath(){
                this.packagePath= this.packageName.replace(".","\\");
        }

}
