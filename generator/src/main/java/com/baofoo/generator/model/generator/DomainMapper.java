package com.baofoo.generator.model.generator;

import com.baofoo.generator.utils.FieldUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fuxiangkui on 2017/3/2.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class DomainMapper implements Serializable {
    //api_group
    private String tableName="";
    //API分组
    private String tableDesc="";
    //ApiGroup
    private String domainName="";
    //apiGroup
    private String domainFirstWordLowerCase="";
    //com.baofoo.apimarket
    private String packageName;
    //api
    private String modelName;
    //API模块
    private String modelDesc;
    //generation
    private String authName="generation";
    //创建时间
    private String dateTime=new SimpleDateFormat().format(new Date());

    private List<TableField> tableFieldList=new ArrayList<>();

    public void buildDomainName(){
        this.domainName = FieldUtils.getBeanName(tableName);
    }
    public void buildDomainFirstWordLowerCase(){
        this.domainFirstWordLowerCase= FieldUtils.buildFirstWordLowerCase(this.domainName);
    }

    public static List<String> baseFieldNameList =new ArrayList<String>();
    static{
        baseFieldNameList.add("id");
        baseFieldNameList.add("create_time");
        baseFieldNameList.add("create_user");
        baseFieldNameList.add("update_time");
        baseFieldNameList.add("update_user");
    }



    public static void main(String Args[]){
        DomainMapper domainMapper=new DomainMapper();
        domainMapper.setTableName("api_group");
        domainMapper.setTableDesc("api分组表");
        domainMapper.setPackageName("com.baofoo.apimarket");
        domainMapper.setModelName("api");
        domainMapper.setModelDesc("API模块");

        TableField tableField=new TableField();
        tableField.setFieldName("id");
        tableField.setDesc("ID");
        tableField.setType("bigint");
        tableField.setLength("20");
        tableField.setIsPrimaryKey("Y");

        domainMapper.getTableFieldList().add(tableField);

        TableField tableField2=new TableField();
        tableField2.setFieldName("group_name");
        tableField2.setDesc("分组名称");
        tableField2.setType("varchar");
        tableField2.setLength("255");
        tableField2.setIsPrimaryKey("N");
        domainMapper.getTableFieldList().add(tableField2);

        System.out.println(domainMapper);
    }


    public void buildTableField(Connection conn) throws SQLException {
        DatabaseMetaData dbMetaData=conn.getMetaData();
        ResultSet colRet = dbMetaData.getColumns(null,"%", this.getTableName(),"%");
        while(colRet.next()) {
            if(baseFieldNameList.contains(colRet.getString("COLUMN_NAME"))){
                continue;
            }
            TableField tf=new TableField();
            tf.setFieldName(colRet.getString("COLUMN_NAME"));
            tf.setType( colRet.getString("TYPE_NAME").toLowerCase());
            tf.setLength(String.valueOf(colRet.getInt("COLUMN_SIZE")));
            tf.setPoint(String.valueOf(colRet.getInt("DECIMAL_DIGITS")));
            tf.setIsNull(String.valueOf(colRet.getInt("NULLABLE")).equals("0")?"Y":"N");
            tf.buildDomainField();
            tf.buildDomainFieldFirstWordLowerCase();
            tf.buildJavaType();
            tf.setDesc(!("".equals(colRet.getString("REMARKS"))) ? colRet.getString("REMARKS"):tf.getDomainFieldFirstWordLowerCase());
            this.getTableFieldList().add(tf);
        }
    }
}
