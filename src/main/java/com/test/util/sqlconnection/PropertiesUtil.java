package com.test.util.sqlconnection;

import java.io.IOException;
import java.util.Properties;


/**
 * properties文件读取工具
 */
public class PropertiesUtil {
    private static Properties properties=new Properties();

    public PropertiesUtil(){
    }
    static boolean loadFile(String filename){
        try {
            //getClassLoader是从整个classes文件夹找
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
     static String getPropertyValue(String key){
        return properties.getProperty(key);
    }
}
