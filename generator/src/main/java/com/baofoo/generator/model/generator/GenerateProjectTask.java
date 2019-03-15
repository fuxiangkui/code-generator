package com.baofoo.generator.model.generator;

import com.baofoo.framework.crud.entity.BaseEntity;
import com.baofoo.generator.utils.FileUtils;
import com.baofoo.generator.utils.MergeUtil;
import com.baofoo.generator.utils.ZipUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.util.StringUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * 描述
 *
 * @author fuxiangkui
 * @version 1.0.0 createTime: 2018/10/18 10:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class GenerateProjectTask extends BaseEntity {

    private String auth;

    private String taskName;

    private String taskStatus;

    private String fileDownloadUrl;

    private String remark;

    private DataSource dataSource;

    private ProjectMetadata projectMetadata=new ProjectMetadata();

    private List<Dependency> dependencyList=new ArrayList<>();

    private ArrayList<DomainMapper> domainMappers=new ArrayList<>();


    /**
     * 修改Springboot主文件XXXApplication.java，添加主文件的ComponentScan和mapper扫描
     * @param file
     */
    public void buildApplicationFile(String file) {
        BufferedReader br = null;
        String line = null;
        StringBuffer bufAll = new StringBuffer();  //保存修改过后的所有内容，不断增加
        try {
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                StringBuffer buf = new StringBuffer();
                buf.append(line).append("\r\n");
                //添加import引入
                if (line.startsWith("package")) {
                    buf.append("import org.mybatis.spring.annotation.MapperScan;").append("\r\n");
                    buf.append("import org.springframework.context.annotation.ComponentScan;").append("\r\n");
                }
                //添加ComponentScan和MapperScan
                if (line.startsWith("@SpringBootApplication")) {
                    buf.append("@ComponentScan(basePackages = {\""+this.getProjectMetadata().getPackageName()+"\",\"com.baofoo.framework\"})").append("\r\n");
                    buf.append("@MapperScan(\""+this.getProjectMetadata().getPackageName()+".dao\")").append("\r\n");
                }
                bufAll.append(buf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
        FileUtils.writeFile(file, bufAll.toString());
    }

    /**
     * 删除Springboot中的application.properties
     * @param applicationRro
     */
    public void deleteApplicationProFile(String applicationRro) {
        File applicationRroFile=new File(applicationRro);
        if(applicationRroFile.exists()){
            applicationRroFile.delete();
        }
    }

    /**
     * 生成项目需要的application.yml
     * @param velocityEngine
     * @param resourceFolderPath
     * @throws Exception
     */
    public void buildApplicationYml(VelocityEngine velocityEngine, String resourceFolderPath) throws Exception {
        VelocityContext applicationCtx = new VelocityContext();
        applicationCtx.put("artifact", this.getProjectMetadata().getName());
        applicationCtx.put("url", this.getDataSource().getUrl());
        applicationCtx.put("username", this.getDataSource().getUserName());
        applicationCtx.put("password", this.getDataSource().getPassword());
        Template applicationTemplate = velocityEngine.getTemplate("/templates/Application.vm");
        MergeUtil.merge(applicationTemplate, applicationCtx, resourceFolderPath + "/application.yml");
    }

    public void appendPomFile(String pomFile) {
        BufferedReader br = null;
        String line = null;
        StringBuffer bufAll = new StringBuffer();  //保存修改过后的所有内容，不断增加
        try {
            br = new BufferedReader(new FileReader(pomFile));
            while ((line = br.readLine()) != null) {
                StringBuffer buf = new StringBuffer();
                if (line.trim().equals("</dependencies>")) {
                    buf.append( "\t\t<dependency>\r\n" +
                                "\t\t\t<groupId>com.baofoo.framework</groupId>\r\n" +
                                "\t\t\t<artifactId>baofoo-fx-generation</artifactId>\r\n" +
                                "\t\t\t<version>2.3.0-SNAPSHOT</version>\r\n" +
                                "\t\t</dependency>\r\n");
                }
                //修改内容核心代码
                buf.append(line).append("\r\n");
                bufAll.append(buf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
        FileUtils.writeFile(pomFile, bufAll.toString());
    }

    public void buildGenerationCode(VelocityEngine velocityEngine, String javaFolderPath, String resourceFolderPath, String vueRootPath) throws Exception {
        Properties props =new Properties();
        props.setProperty("user", this.getDataSource().getUserName());
        props.setProperty("password", this.getDataSource().getPassword());
        props.setProperty("remarks", "true"); //设置可以获取remarks信息
        props.setProperty("useInformationSchema", "true");//设置可以获取tables remarks信息
        Connection conn = DriverManager.getConnection(this.getDataSource().getUrl(), props);
        List<DomainMapper> domainMapperList=this.getDomainMappers();

        domainMapperList.forEach(domainMapper -> {
            domainMapper.setPackageName(this.getProjectMetadata().getPackageName());
            domainMapper.buildDomainName();
            domainMapper.buildDomainFirstWordLowerCase();
            try {
                domainMapper.buildTableField(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String path = javaFolderPath;
            path += domainMapper.getPackageName().replace(".","/");
            VelocityContext ctx = new VelocityContext();
            ctx.put("packageName", domainMapper.getPackageName());
            ctx.put("modelName", domainMapper.getModelName());
            ctx.put("dataTime", domainMapper.getDateTime());
            ctx.put("tableDesc", domainMapper.getTableDesc());
            ctx.put("tableName", domainMapper.getTableName());
            ctx.put("domainName", domainMapper.getDomainName());
            ctx.put("domainDesc", domainMapper.getModelDesc());
            ctx.put("author",this.getAuth());
            ctx.put("domainFirstWordLowerCase", domainMapper.getDomainFirstWordLowerCase());
            ctx.put("tableFieldList", domainMapper.getTableFieldList());

            Template controllerTemplate = null;
            try {
                controllerTemplate = velocityEngine.getTemplate("/templates/ControllerTemplate.vm");
                String controllerPath = path + "/controller/" + domainMapper.getModelName();
                MergeUtil.createDir(controllerPath);
                MergeUtil.merge(controllerTemplate, ctx, controllerPath + "/" + domainMapper.getDomainName() + "Controller.java");

                Template serviceTemplate = velocityEngine.getTemplate("/templates/ServiceTemplate.vm");
                Template serviceImplTemplate = velocityEngine.getTemplate("/templates/ServiceImplTemplate.vm");
                String servicePath = path + "/service/" + domainMapper.getModelName();
                MergeUtil.createDir(servicePath + "/impl");
                MergeUtil.merge(serviceTemplate, ctx, servicePath + "/" + domainMapper.getDomainName() + "Service.java");
                MergeUtil.merge(serviceImplTemplate, ctx, servicePath + "/impl/" + domainMapper.getDomainName() + "ServiceImpl.java");

                Template dtoTemplate = velocityEngine.getTemplate("/templates/DtoTemplate.vm");
                String contractDtoPath = path + "/contract/" + domainMapper.getModelName()+ "/dto";
                MergeUtil.createDir(contractDtoPath);
                MergeUtil.merge(dtoTemplate, ctx, contractDtoPath+"/" + domainMapper.getDomainName() + "Dto.java");

                Template spiTemplate = velocityEngine.getTemplate("/templates/SpiTemplate.vm");
                String contractSpiPath = path + "/contract/" + domainMapper.getModelName() + "/spi";
                MergeUtil.createDir(contractSpiPath);
                MergeUtil.merge(spiTemplate, ctx, contractSpiPath + "/" + domainMapper.getDomainName() + "Spi.java");

                Template businessTemplate = velocityEngine.getTemplate("/templates/BusinessTemplate.vm");
                Template businessImplTemplate = velocityEngine.getTemplate("/templates/BusinessImplTemplate.vm");
                String businessPath = path + "/business/" + domainMapper.getModelName() ;
                MergeUtil.createDir(businessPath + "/impl");
                MergeUtil.merge(businessTemplate, ctx, businessPath +"/"+ domainMapper.getDomainName() + "Business.java");
                MergeUtil.merge(businessImplTemplate, ctx, businessPath + "/impl/" + domainMapper.getDomainName() + "BusinessImpl.java");

                Template daoTemplate = velocityEngine.getTemplate("/templates/DaoTemplate.vm");
                String daoPath = path + "/dao/" + domainMapper.getModelName();
                MergeUtil.createDir(daoPath);
                MergeUtil.merge(daoTemplate, ctx, daoPath +"/"+domainMapper.getDomainName() + "Dao.java");

                Template entityTemplate = velocityEngine.getTemplate("/templates/EntityTemplate.vm");
                String modelPath = path + "/model/" + domainMapper.getModelName();
                MergeUtil.createDir(modelPath);
                MergeUtil.merge(entityTemplate, ctx, modelPath +"/" + domainMapper.getDomainName() + "Entity.java");

                Template mapperTemplate = velocityEngine.getTemplate("/templates/MapperTemplate.vm");
                String mapperPath = resourceFolderPath + "/mappers/" + domainMapper.getModelName();
                MergeUtil.createDir(mapperPath);
                MergeUtil.merge(mapperTemplate, ctx, mapperPath + "/" + domainMapper.getDomainName() + "Mapper.xml");

                Template vueTemplate = velocityEngine.getTemplate("/templates/VueTemplate.vm");
                String vuePath = vueRootPath + "/src/vue/" + domainMapper.getModelName();
                MergeUtil.createDir(vuePath);
                MergeUtil.merge(vueTemplate, ctx, vuePath + "/" + domainMapper.getDomainName() + ".vue");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }


    /**
     * 创建项目的静态代码文件夹，解压前端文件结构代码至静态项目根目录 D:\generationZip\indicator\src\main\resources\static\indicator
     * @param staticFile
     * @param staticPath
     */
    public void unzipStaticPackage(File staticFile,String staticPath) {
        MergeUtil.createDir(staticPath);
        ZipUtils.unZip(staticFile,staticPath);
    }

    /**
     * 修改静态资源文件
     * @param velocityEngine
     * @param resourceFolderPath
     * @throws Exception
     */
    public void buildStaticContent(VelocityEngine velocityEngine, String resourceFolderPath) throws Exception {
        VelocityContext ctx = new VelocityContext();
        ctx.put("artifact", this.getProjectMetadata().getName());
        HashMap<String,Model> modelMap=new HashMap<>();
        this.getDomainMappers().forEach(item->{
            if(modelMap.containsKey(item.getModelName())){
                modelMap.get(item.getModelName()).getDomainMappers().add(item);
            }else{
                Model model=new Model();
                model.setModelName(item.getModelName());
                model.setModelDesc(item.getModelDesc());
                model.getDomainMappers().add(item);
                modelMap.put(item.getModelName(),model);
            }
        });
        List<Model> modelList=new ArrayList<>();
        modelMap.entrySet().forEach(item->{
            modelList.add(item.getValue());
        });
        ctx.put("projectDesc",this.getProjectMetadata().getDescription());
        ctx.put("modelList",modelList);

        Template webpackTemplate = velocityEngine.getTemplate("/templates/webpack.config.vm");
        MergeUtil.merge(webpackTemplate, ctx, resourceFolderPath +"/static/"+this.getProjectMetadata().getName()+ "/webpack.config.js");

        Template apiConfigTemplate = velocityEngine.getTemplate("/templates/apiConfig.vm");
        MergeUtil.merge(apiConfigTemplate, ctx, resourceFolderPath +"/static/"+this.getProjectMetadata().getName()+ "/src/commonjs/apiConfig.js");

        Template menuJsonTemplate = velocityEngine.getTemplate("/templates/menuJson.vm");
        MergeUtil.merge(menuJsonTemplate, ctx, resourceFolderPath +"/static/"+this.getProjectMetadata().getName()+ "/src/components/menuJson.js");

        Template routerTemplate = velocityEngine.getTemplate("/templates/router.vm");
        MergeUtil.merge(routerTemplate, ctx, resourceFolderPath +"/static/"+this.getProjectMetadata().getName()+ "/src/router/router.js");

    }

}
