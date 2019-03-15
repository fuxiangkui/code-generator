package com.baofoo.generator.controller.generator;

import com.alibaba.fastjson.JSONObject;
import com.baofoo.framework.crud.common.ResponseEntityDto;
import com.baofoo.generator.contract.generator.dto.ApiDataBaseDto;
import com.baofoo.generator.model.generator.*;
import com.baofoo.generator.service.core.GenerationTaskService;
import com.baofoo.generator.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


@Controller
@RequestMapping("/tool")
@Slf4j
public class GenerationController {


    @Resource
    private GenerationTaskService generationTaskService;

    @Value("${rootPath}")
    private String rootPath;

    @Value("${springInitializrUrl}")
    private String springInitializrUrl;

    @Value("${springInitializrDependenciesUrl}")
    private String springInitializrDependenciesUrl;

    @Value("${fileServer}")
    private String fileServer;

    @RequestMapping("/getTables")
    @ResponseBody
    public ResponseEntityDto getTables(@RequestBody JSONObject param) throws InvocationTargetException, IllegalAccessException {
        ApiDataBaseDto apiDataBaseDto= JSONObject.toJavaObject(param,ApiDataBaseDto.class);
        List<DomainMapper> domainMapperList=new ArrayList<DomainMapper>();
        DatabaseUtil databaseUtil=new DatabaseUtil();
        databaseUtil.setUrl(apiDataBaseDto.getUrl());
        databaseUtil.setUserName(apiDataBaseDto.getUserName());
        databaseUtil.setPassword(apiDataBaseDto.getPassword());
        databaseUtil.setDomainList(domainMapperList);
        ResponseEntityDto resp = new ResponseEntityDto();
        resp.setStatus("1");
        resp.setError("00000000");
        resp.setData(domainMapperList);
        return resp;
    }

    @RequestMapping("/getDependenpcies")
    @ResponseBody
    public ResponseEntityDto getDependenpcies(@RequestBody JSONObject param) throws InvocationTargetException, IllegalAccessException {
        List<Dependency> allList=HttpUtils.getDependenpcies(springInitializrDependenciesUrl);
        HashMap<String,List> groupData=new HashMap<>();
        for(int i=0;i<allList.size();i++){
            Dependency one=allList.get(i);
            if(groupData.keySet().contains(one.getGroup())){
                groupData.get(one.getGroup()).add(one);
            }else{
                List list=new ArrayList();
                list.add(one);
                groupData.put(one.getGroup(),list);
            }
        }
        List<DependencyDto> dependenpcies=new ArrayList<>();
        groupData.keySet().forEach(item->{
            DependencyDto dependencyDto=new DependencyDto();
            dependencyDto.setGroup(item);
            dependencyDto.setChildren(groupData.get(item));
            dependenpcies.add(dependencyDto);
        });
        ResponseEntityDto resp = new ResponseEntityDto();
        resp.setStatus("1");
        resp.setError("00000000");
        resp.setData(dependenpcies);
        return resp;
    }


    @RequestMapping("/generate")
    @ResponseBody
    public ResponseEntityDto generate(@RequestBody GenerateProjectTask generateProjectTask, HttpServletRequest request){


        generateProjectTask.getProjectMetadata().buildNameFirstWordUpperCase();
        generateProjectTask.getProjectMetadata().buildPackagePath();
        try {

            //1.获取界面参数
            HashMap paramMap=new HashMap();
            paramMap.put("type",generateProjectTask.getProjectMetadata().getType());
            paramMap.put("language",generateProjectTask.getProjectMetadata().getLanguage());
            paramMap.put("bootVersion",generateProjectTask.getProjectMetadata().getBootVersion());
            paramMap.put("baseDir",generateProjectTask.getProjectMetadata().getName());
            paramMap.put("groupId", generateProjectTask.getProjectMetadata().getGroup());
            paramMap.put("artifactId",generateProjectTask.getProjectMetadata().getArtifact());
            paramMap.put("name",generateProjectTask.getProjectMetadata().getName());
            paramMap.put("description",generateProjectTask.getProjectMetadata().getDescription());
            paramMap.put("packageName",generateProjectTask.getProjectMetadata().getPackageName());
            paramMap.put("packaging",generateProjectTask.getProjectMetadata().getPackaging());
            paramMap.put("javaVersion",generateProjectTask.getProjectMetadata().getJavaVersion());
            paramMap.put("autocomplete","");
            paramMap.put("generate-project","");
            //2.请求接口返回zip的file
            String springInitializrFileName=rootPath +"/"+ paramMap.get("name")+".zip";
            File springInitializrFile=HttpUtils.getSpringInitializrFile(springInitializrUrl,paramMap,springInitializrFileName,generateProjectTask);

            //3.获取zip文件，解压到rootPath下
            ZipUtils.unZip(springInitializrFile,rootPath);

            //4.修改Springboot主文件XXXApplication.java，添加主文件的ComponentScan和mapper扫描
            String applicationName=rootPath+"/"+generateProjectTask.getProjectMetadata().getName()+"/src/main/java/"+generateProjectTask.getProjectMetadata().getPackagePath()+"/"+generateProjectTask.getProjectMetadata().getNameFirstWordUpperCase()+"Application.java";
            generateProjectTask.buildApplicationFile(applicationName);

            //5.修改pom文件，追加pom依赖，添加baofoo-fx-generation
            String pomFile=rootPath+"/"+generateProjectTask.getProjectMetadata().getName()+"/pom.xml";
            generateProjectTask.appendPomFile(pomFile);

            //6.删除原有的application.properties
            String applicationRro=rootPath+"/"+generateProjectTask.getProjectMetadata().getName()+"/src/main/resources/application.properties";
            generateProjectTask.deleteApplicationProFile(applicationRro);

            VelocityEngine velocityEngine = getVelocityEngine();
            String javaFolderPath=rootPath+"/"+generateProjectTask.getProjectMetadata().getName()+"/src/main/java/";
            String resourceFolderPath=rootPath+"/"+generateProjectTask.getProjectMetadata().getName()+"/src/main/resources";
            String vueRootPath=rootPath+"/"+generateProjectTask.getProjectMetadata().getName()+"/src/main/resources/static/"+generateProjectTask.getProjectMetadata().getName();

            //7.添加application.yml
            generateProjectTask.buildApplicationYml(velocityEngine, resourceFolderPath);
            //8.生成表对应的其他代码
            generateProjectTask.buildGenerationCode(velocityEngine, javaFolderPath, resourceFolderPath, vueRootPath);
            //9.创建项目的静态代码文件夹，解压前端文件结构代码至静态项目根目录 D:\generationZip\indicator\src\main\resources\static\indicator
            File staticFile=new File(rootPath+"/static.zip");
            String staticPath=rootPath+"/"+generateProjectTask.getProjectMetadata().getName()+"/src/main/resources/static/"+generateProjectTask.getProjectMetadata().getName();
            generateProjectTask.unzipStaticPackage(staticFile,staticPath);

            //10.修改静态资源文件
            generateProjectTask.buildStaticContent(velocityEngine,resourceFolderPath);
            //11.删除原有的springboot生成的文件
            if(springInitializrFile.exists()){
                springInitializrFile.delete();
            }
            String zipFileName = rootPath+"/"+generateProjectTask.getProjectMetadata().getName()+".zip";
            String zipFilePath = rootPath+"/"+generateProjectTask.getProjectMetadata().getName();
            //12.将修改后的前后端代码打包成zip
            ZipCompress.toZip(zipFilePath, zipFileName);//压缩生成ZIP
            //13.将修改的打包前的文件夹删除
            String fileUrl=uploadToFileServer(zipFileName);
            FileUtils.deleteAll(zipFilePath);//生成zip文件后删除原模板生成文件夹
            FileUtils.deleteAll(zipFileName);//生成zip文件后删除原模板生成文件夹

            JSONObject taskParam=new JSONObject();
            taskParam.put("taskName",generateProjectTask.getProjectMetadata().getDescription()+"生成任务");
            taskParam.put("taskStatus","已完成");
            taskParam.put("fileDownloadUrl",fileUrl);
            taskParam.put("fileName",generateProjectTask.getProjectMetadata().getName()+".zip");
            taskParam.put("dataSource",generateProjectTask.getDataSource().toString());
            taskParam.put("projectMetadata",generateProjectTask.getProjectMetadata().toString());
            taskParam.put("dependencyList",generateProjectTask.getDependencyList().toString());
            taskParam.put("domainMappers",generateProjectTask.getDomainMappers().toString());

            generationTaskService.insertGenerationTask(taskParam);

            ResponseEntityDto resp = new ResponseEntityDto();
            resp.setStatus("1");
            resp.setError("00000000");
            resp.setCode("200");
            resp.setData(fileUrl);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            ResponseEntityDto resp = new ResponseEntityDto();
            resp.setStatus("500");
            resp.setError(e.getMessage());
            resp.setData(e.getMessage());
            return resp;
        }
    }

    /**
     * 上传文件至文件服务器
     * @param filePath
     * @return
     */
    public String uploadToFileServer(String filePath)  {
        String uploadFileServer="http://"+fileServer;
        File file = new File(filePath);
        String uploadUrl=uploadFileServer+"/file-service/file/uploadFile";
        Map<String, String> header = new HashMap();
        Map<String, String> param = new HashMap();
        FileBody fileBody = new FileBody(file);
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("file", fileBody);//file1为请求后台的File upload;属性
        try {
            String result= HttpsUtils.post(uploadUrl, header, param, reqEntity);
            JSONObject fileUploadResult = JSONObject.parseObject(result);
            if (fileUploadResult.getString("code").equals("1")) {
                return uploadFileServer+"/file-service/file/downloadFile?fileId="+fileUploadResult.getJSONObject("data").getString("fileId");
            }else{
                throw new Exception("文件上传失败,"+fileUploadResult.get("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            file.delete();
        }
        return null;
    }

    private VelocityEngine getVelocityEngine() throws Exception {
        VelocityEngine velocityEngine = new VelocityEngine();
        Properties properties = new Properties();
        properties.put("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(velocityEngine.INPUT_ENCODING, "utf-8");
        properties.setProperty(velocityEngine.OUTPUT_ENCODING, "utf-8");
        velocityEngine.init(properties);   //初始化
        return velocityEngine;
    }

}

