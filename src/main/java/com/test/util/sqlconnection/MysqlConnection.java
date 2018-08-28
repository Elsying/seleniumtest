package com.test.util.sqlconnection;

import org.testng.annotations.Test;
import static java.lang.System.out;

import java.sql.*;

public class MysqlConnection {

    @Test
    public void test() throws Exception {
        Connection conn=null;
        ResultSet rs=null;
        Statement stmt=null;
        String sql;
        String sqls;
        if (PropertiesUtil.loadFile("mysql.properties")) {
            String driver = PropertiesUtil.getPropertyValue("driver");
            String url = PropertiesUtil.getPropertyValue("url");
            String username = PropertiesUtil.getPropertyValue("username");
            String password = PropertiesUtil.getPropertyValue("password");
            Class.forName(driver);
            try {
                conn=DriverManager.getConnection(url,username,password);
                conn.setAutoCommit(false);//开启事务
                 stmt=conn.createStatement();
                sql="create table student(NO char(20),name varchar(20),primary key(NO))";
                sqls="select * from student";
                int result=stmt.executeUpdate(sql);
                rs=stmt.executeQuery(sqls);
                while (rs.next()){
                    out.println(rs.getString(1) + "\t" + rs.getString(2));// 入如果返回的是int类型可以用getInt()
                }
                conn.commit();//提交事务
            } catch (Exception  e) {
                //回滚
                if (conn != null) {
                    conn.rollback();
                }
                e.printStackTrace();
            }

            finally {
                if (conn != null) {
                    conn.close();
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }

                }
            }


        }
    }

}
