package com.baofoo.generator.utils;

import org.apache.commons.lang.WordUtils;

public class FieldUtils {

    public static String getBeanName(String str) {
        if(str.length()>0){
            StringBuilder sb = new StringBuilder();
            String words[] = str.split("_");
            for (int i = 0; i < words.length; i++) {
                //首字母大写
                sb.append(words[i].substring(0, 1).toUpperCase()).append(null != words[i].substring(1) ? words[i].substring(1) : "");
            }
            return sb.toString();
        }
        return str;
    }

    public static String buildFirstWordLowerCase(String str) {
        return WordUtils.uncapitalize(str);
    }

    /*
     * mysql的字段类型转化为java的类型*/
    public static String sqlType2JavaType(String sqlType) {

        if(sqlType.equalsIgnoreCase("bit")){
            return "Boolean";
        }else if(sqlType.equalsIgnoreCase("tinyint")){
            return "Byte";
        }else if(sqlType.equalsIgnoreCase("smallint")){
            return "Short";
        }else if(sqlType.equalsIgnoreCase("Integer")||sqlType.equalsIgnoreCase("int")){
            return "Integer";
        }else if(sqlType.equalsIgnoreCase("bigint")){
            return "Long";
        }else if(sqlType.equalsIgnoreCase("float")){
            return "Float";
        }else if(sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")){
            return "BigDecimal";
        }else if(sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text")){
            return "String";
        }else if(sqlType.equalsIgnoreCase("datetime") ||sqlType.equalsIgnoreCase("date")){
            return "java.util.Date";
        }else if(sqlType.equalsIgnoreCase("timestamp") ||sqlType.equalsIgnoreCase("timestamp")){
            return "java.sql.Timestamp";
        }else if(sqlType.equalsIgnoreCase("image")){
            return "Blod";
        }

        return null;
    }

}
