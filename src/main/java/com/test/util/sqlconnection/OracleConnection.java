package com.test.util.sqlconnection;

import org.junit.Test;

import java.sql.*;

import static java.lang.System.out;

public class OracleConnection {

    @Test
    public void Oracletest() {
        Connection conn = null;
        ResultSet rs = null;
        //Statement stmt=null;
        PreparedStatement pstm = null;
        //ResultSet rs = null;
        //boolean flag = false;
        if (PropertiesUtil.loadFile("oracle.properties")) {
            String driver = PropertiesUtil.getPropertyValue("driver");
            String url = PropertiesUtil.getPropertyValue("url");
            String username = PropertiesUtil.getPropertyValue("username");
            String password = PropertiesUtil.getPropertyValue("password");
            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(url, username, password);
                String sql = "select * from B2B2C_GOODS_ATTRIBUTE";
                pstm = conn.prepareStatement(sql);
                rs = pstm.executeQuery();
                while (rs.next()) {
                    out.println(rs.getString(1) + "\t" + rs.getString(2));// 入如果返回的是int类型可以用getInt()
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                        if (rs != null) {
                            rs.close();
                        }
                        if (pstm != null) {
                            pstm.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
