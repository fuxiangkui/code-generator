package com.baofoo.generator.utils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import java.io.*;


public class MergeUtil {

    // 创建目录
    public static void createDir(String destDirName) {
        File dir = new File(destDirName);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdirs();
        }
    }

    public static void merge(Template template, VelocityContext ctx, String path) throws IOException {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            template.merge(ctx, writer);
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}
