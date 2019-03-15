package com.baofoo.generator.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baofoo.generator.model.generator.Dependency;
import com.baofoo.generator.model.generator.GenerateProjectTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
public class HttpUtils {


    public static void main(String args[]){

//        String url="http://10.0.19.248:8082/starter.zip";
//        HashMap map=new HashMap();
//        map.put("type","maven-project");
//        map.put("language","java");
//        map.put("bootVersion","2.1.1.RELEASE");
//        map.put("baseDir","demo");
//        map.put("groupId","com.example");
//        map.put("artifactId","demo");
//        map.put("name","demo");
//        map.put("description","Demo project for Spring Boot");
//        map.put("packageName","com.example.demo");
//        map.put("packaging","jar");
//        map.put("javaVersion","1.8");
//        map.put("autocomplete","");
//        map.put(" generate-project","");
//
//        String rootPath="D:\\generationZip";

//        getSpringInitializrFile(url,map,rootPath);

        String springInitializrDependenciesUrl="http://10.0.19.248:8082/ui/dependencies?version=2.1.1.RELEASE";
        System.out.print(getDependenpcies(springInitializrDependenciesUrl));

    }

    public static File getSpringInitializrFile(String url, Map<String,String> paramMap, String springInitializrFileName,GenerateProjectTask generateProjectTask) {
        CloseableHttpClient httpCilent = HttpClients.createDefault();
        try {
            URIBuilder builder = new URIBuilder(url);
            Set<String> set = paramMap.keySet();
            for(String key: set){
                    builder.setParameter(key, paramMap.get(key));
            }
            generateProjectTask.getDependencyList().forEach(item->{
                builder.addParameter("style",item.getId());
            });

            HttpGet request = new HttpGet(builder.build());
            log.info("ready to send request,url:" + url + ",params:" + JSON.toJSONString(paramMap));
            CloseableHttpResponse result = httpCilent.execute(request);

            HttpEntity entity = result.getEntity();//从response里获取数据实体
            InputStream in = entity.getContent();//获取数据流

            //文件存放地址
            File file =  new File(springInitializrFileName);//生成file（如果文件路径不存在，先创建）
            FileOutputStream fos = new FileOutputStream(file);
            int len;
            byte [] bytes = new byte[2048];
            BufferedOutputStream bos  = new BufferedOutputStream(fos,2048);
            while((len = in.read(bytes, 0, 2048)) != -1) {
                bos.write(bytes, 0, len);
            }
            bos.flush();
            bos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                httpCilent.close();//释放资源
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static List<Dependency> getDependenpcies(String springInitializrDependenciesUrl) {
        CloseableHttpClient httpCilent = HttpClients.createDefault();
        try {
            URIBuilder builder = new URIBuilder(springInitializrDependenciesUrl);
            HttpGet request = new HttpGet(builder.build());
            log.info("ready to send request,url:" + springInitializrDependenciesUrl + ",params:" );
            CloseableHttpResponse result = httpCilent.execute(request);
            List<Dependency> list=new ArrayList<>();
            HttpEntity entity = result.getEntity();//从response里获取数据实体

            if (entity != null) {
                String raw = EntityUtils.toString(entity, "UTF-8");
                log.info("http post response:" + raw);
                JSONObject.parseObject(raw).getJSONArray("dependencies").forEach(item->{
                    Dependency Dependency=JSONObject.parseObject(item.toString(),Dependency.class);
                    list.add(Dependency);
                });
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                httpCilent.close();//释放资源
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }




}
