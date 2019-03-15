package com.baofoo.generator.utils;

import com.baofoo.generator.model.generator.DomainMapper;
import com.baofoo.generator.model.generator.TableField;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Getter
@Setter
public class DatabaseUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);
    private static final String DRIVER = "com.mysql.jdbc.Driver";
//    private   String url = "jdbc:mysql://10.0.20.108:3306/baofoo_business_warning?useUnicode=true&characterEncoding=utf8";
//    private   String userName = "baofoo_api";
//    private   String password = "baofoo_api@123";
    private   String url = "";
    private   String userName = "";
    private   String password = "";
    private static final String SQL = "SELECT * FROM ";// 数据库操作

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public  Connection getConnection() {
        Connection conn = null;
        Properties props =new Properties();
        try {
            props.setProperty("user", userName);
            props.setProperty("password", password);
            props.setProperty("remarks", "true"); //设置可以获取remarks信息
            props.setProperty("useInformationSchema", "true");//设置可以获取tables remarks信息
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            LOGGER.error("get connection failure", e);
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     * @param conn
     */
    public  void closeConnection(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failure", e);
            }
        }
    }

    /**
     * 获取数据库下的所有表名
     */
    public  List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[] { "TABLE" });
            while(rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            LOGGER.error("getTableNames failure", e);
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
                LOGGER.error("close ResultSet failure", e);
            }
        }
        return tableNames;
    }

    /**
     * 获取数据库下的所有表名
     */
    public  void setDomainList( List<DomainMapper> domainMapperList) {
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            rs = db.getTables(null, null, null, new String[] { "TABLE"});
            while(rs.next()) {
                DomainMapper dm=new DomainMapper();
                String tableName=rs.getString(3);
                String tableDesc=rs.getString("REMARKS");
                dm.setTableName(tableName);
                dm.setModelName("core");
                dm.setModelDesc("默认");
                if(StringUtils.isEmpty(tableDesc)){
                    dm.setTableDesc(tableName);
                }else{
                    dm.setTableDesc(tableDesc);
                }
                domainMapperList.add(dm);
            }
        } catch (SQLException e) {
            LOGGER.error("getTableNames failure", e);
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
                LOGGER.error("close ResultSet failure", e);
            }
        }
    }


    public  void setTableField(DomainMapper dm) {
        Connection conn = getConnection();
        ResultSet colRet=null;
        try {
            DatabaseMetaData dbMetaData=conn.getMetaData();
            colRet = dbMetaData.getColumns(null,"%", dm.getTableName(),"%");
            List<TableField> tableFieldList=new ArrayList<TableField>();
            List<String> BaseFieldNameList=new ArrayList<String>();
            BaseFieldNameList.add("id");
            BaseFieldNameList.add("create_time");
            BaseFieldNameList.add("create_user");
            BaseFieldNameList.add("update_time");
            BaseFieldNameList.add("update_user");
            while(colRet.next()) {
                if(BaseFieldNameList.contains(colRet.getString("COLUMN_NAME"))){
                    continue;
                }
                TableField tf=new TableField();
                tf.setFieldName(colRet.getString("COLUMN_NAME"));
                tf.setType( colRet.getString("TYPE_NAME").toLowerCase());
                tf.setLength(String.valueOf(colRet.getInt("COLUMN_SIZE")));
                tf.setPoint(String.valueOf(colRet.getInt("DECIMAL_DIGITS")));
                tf.setIsNull(String.valueOf(colRet.getInt("NULLABLE")).equals("0")?"Y":"N");
                tf.setDesc(colRet.getString("REMARKS"));
                tf.buildDomainField();
                tf.buildDomainFieldFirstWordLowerCase();
                tf.buildJavaType();
                tableFieldList.add(tf);
            }
            dm.setTableFieldList(tableFieldList);
            System.out.println(dm);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (colRet != null) {
                try {
                    colRet.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("getColumnComments close ResultSet and connection failure", e);
                }
            }
        }

    }

    public static void main(String[] args) {
//        DomainMapper dm=new DomainMapper();
//        dm.setTableName("sys_task_function");
//        setTableField(dm);
        DatabaseUtil du=new DatabaseUtil();
        String url = "jdbc:mysql://10.0.20.108:3306/baofoo_business_warning?useUnicode=true&characterEncoding=utf8";
        String userName = "baofoo_api";
        String password = "baofoo_api@123";
        du.setUrl(url);
        du.setUserName(userName);
        du.setPassword(password);
        List<String> tableNames = du.getTableNames();
        System.out.println("tableNames:" + tableNames);
//        for (String tableName : tableNames) {
//            System.out.println("ColumnNames:" + getColumnNames(tableName));
//            System.out.println("ColumnTypes:" + getColumnTypes(tableName));
//            System.out.println("ColumnComments:" + getColumnComments(tableName));
//        }
    }
}