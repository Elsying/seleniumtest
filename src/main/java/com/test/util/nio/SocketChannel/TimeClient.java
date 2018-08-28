package com.test.util.nio.SocketChannel;

import org.junit.Test;
import sun.misc.Unsafe;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TimeClient {

    //连接超时时间
    static int connectTimeout=3000;
    static ByteBuffer byteBuffer=ByteBuffer.allocate(1024);

    @Test
    public void start() throws IOException {
        SocketChannel socketChannel=SocketChannel.open(new InetSocketAddress(8080));
        socketChannel.configureBlocking(false);
        long start=System.currentTimeMillis();
        while (!socketChannel.finishConnect()){
            if(System.currentTimeMillis()-start>=connectTimeout){
                throw new RuntimeException("尝试建立连接超过3秒");
            }
        }

        while (true){
            byteBuffer.put("GET CURRENT TIME".getBytes());
            byteBuffer.flip();
            //向服务器发送数据
            socketChannel.write(byteBuffer);
            //清楚缓冲区
            byteBuffer.clear();
            if(socketChannel.read(byteBuffer)>0){
                byteBuffer.flip();
                byte[] response=new byte[byteBuffer.remaining()];
                byteBuffer.get(response);
                System.out.println("reveive response:"+new String(response));
                byteBuffer.clear();
            }
        }

    }
}
