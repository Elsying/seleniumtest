package com.test.util.nio;


import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ConcurrentHashMap;
import static java.lang.System.out;

public class FileNio {
    ConcurrentHashMap concurrentHashMap=new ConcurrentHashMap();
    private JsonObject jsonObject=new JsonObject();


    @Test
    public void start(){
        jsonObject.addProperty("a","aa");
        out.println(jsonObject);
        redFile("E:\\aa.txt");
        writeFile("E:\\aa.txt","nihaohhhhh梁世懿");
    }

    /**
     * 使用Nio读取文件
     * 中文乱码已解决
     * @param filename 文件路径
     */
    private void redFile(String filename) {
        RandomAccessFile aFile = null;
        try {
            Charset charset = Charset.forName("GBK");//Java.nio.charset.Charset处理了字符转换问题。它通过构造CharsetEncoder和CharsetDecoder将字符序列转换成字节和逆转换。
            CharsetDecoder decoder = charset.newDecoder();
            //这个类是基于指针的方式对文件进行读写的 模式mode一般用”rw”读写、”r”读
             aFile = new RandomAccessFile(filename, "rw");
            FileChannel inChannel = aFile.getChannel();
            //分配512字节capacity的ByteBuffer
            ByteBuffer buf = ByteBuffer.allocate(512);
            CharBuffer cb = CharBuffer.allocate(512);
            //从Channel读取到Buffer
            int bytesRead = inChannel.read(buf);
            while (-1 != bytesRead) {
                //flip方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成之前position的值
                buf.flip();
                decoder.decode(buf, cb,false);
                cb.flip();
                while (cb.hasRemaining()) {
                    //从Buffer中读取数据
                    out.print( cb.get());
                }
                buf.clear();
                cb.clear();
                bytesRead = inChannel.read(buf);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (aFile != null) {
                    aFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 使用Nio写入文件
     *
     * @param filename 文件路径
     * @param count 写入内容
     */
    private void writeFile(String filename,String count) {
        RandomAccessFile aFile = null;
        try {
            ByteBuffer src = Charset.forName("GBK").encode(count);
            // 字节缓冲的容量和limit会随着数据长度变化，不是固定不变的
            aFile = new RandomAccessFile(filename, "rw");
            long fileLength = aFile.length();// 获取文件的长度即字节数
            // 将写文件指针移到文件尾 ,追加写
            aFile.seek(fileLength);
            aFile.write("\r\n".getBytes());
            //
            System.out.println("初始化容量和limit：" + src.capacity() + ","
                    + src.limit());
            FileChannel inChannel = aFile.getChannel();
            //从Channel写到Buffer
            int bytesRead = 0;
            while (0 != inChannel.write(src)) {
                /*
                 * 注意，这里不需要clear，将缓冲中的数据写入到通道中后 第二次接着上一次的顺序往下读
                 */
                System.out.println("写入长度:" + bytesRead);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (aFile != null) {
                    aFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
