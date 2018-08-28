package com.test.util.blockchain;


import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 生成签名的方法
 * 使用SHA256 加密算法
 */
public class StringUtil {
    public static String applySha256(String input) {
        {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                digest.update(input.getBytes("UTF-8"));
                byte[] hash = digest.digest();//上面和下面等价digest.digest(input.getBytes());
                StringBuilder hexString = new StringBuilder();
                //字节数组转换为字符串 将byte转为16进制
                for (byte aHash : hash) {
                    hexString.append((Integer.toHexString(0xff & aHash)));
                    //hexString.append( aHash);
                }
                return hexString.toString();
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Test
    public void mm(){
        System.out.println(applySha256("a"));

        Block first = new Block("Hi i am the first block", "0");
        System.out.println("Hash for block 1 : " + first.hash);
        Block second = new Block("Hi i am the second block", first.hash);
        System.out.println("Hash for block 2 : " + second.hash);
        Block third = new Block("Hi i am the third block", second.hash);
        System.out.println("Hash for block 3 : " + third.hash);
    }

}
