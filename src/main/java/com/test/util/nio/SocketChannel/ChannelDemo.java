package com.test.util.nio.SocketChannel;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import static java.lang.System.out;

/**
 * channel和stream类似
 * 区别：channel既能读又能写双向的
 * Channl.read(ByteBuffer)从channl中读取到buffer
 * Channl.write(ByteBuffer)从buffer里数据写到Channl
 */
public class ChannelDemo {

    /**
     * 创建通道
     * @throws Exception
     */
    private void ChannelCreat()throws Exception{
        //创建SocketChannel
        SocketChannel sc = SocketChannel.open( );
        sc.connect (new InetSocketAddress("somehost", 8888));

        //c创建ServerSocketChannel
        ServerSocketChannel ssc = ServerSocketChannel.open( );
        ssc.socket( ).bind (new InetSocketAddress (8888));

        //创建DatagramChannel
        DatagramChannel dc = DatagramChannel.open( );

        //创建RandomAccessFile
        RandomAccessFile raf = new RandomAccessFile ("somefile", "r");
        FileChannel fc = raf.getChannel( );

        // false 值设为非阻塞模式,默认情况下阻塞
        sc.configureBlocking(false);
        //判断阻塞还是非阻塞
        sc.isBlocking();
    }

    //禁止修改socket状态
    private void blockingLock(SocketChannel serverChannel) throws Exception{
        Socket socket = null;
        Object lockObj = serverChannel.blockingLock( );
        // 执行关键代码部分的时候，使用这个锁进行同步
        synchronized (lockObj)
        {
            // 一旦进入这个部分，锁就被获取到了，其他线程不能改变这个channel的阻塞模式
            boolean prevState = serverChannel.isBlocking( );
            serverChannel.configureBlocking (false);
            //这里需要
            //socket = serverChannel.accept( );
            serverChannel.configureBlocking (prevState);
        }
        // 释放锁，此时其他线程可以修改channel的阻塞模式
        if (socket != null) {
            //doSomethingWithTheSocket (socket);
        }
    }

    @Test
    /**
     * 非阻塞连接ServerSocketChannel
     */
    public void ChannelAccept()throws Exception{
        final String GREETING = "Hello I must be going.\r\n";
        int port =1234;
        ByteBuffer  buffer=ByteBuffer.wrap(GREETING.getBytes());
        ServerSocketChannel ssc=ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        while(true){
            out.println("Waiting for connections");
            //如果以非阻塞模式被调用，当没有传入连接在等待时， ServerSocketChannel.accept( )会立即返回 null
            SocketChannel sc=ssc.accept();
            if(null==sc){
                Thread.sleep(2000);
            }else {
                sc.configureBlocking(false);
                ByteBuffer alloacte=ByteBuffer.allocate(16*1024);
                while (sc.read(alloacte)>0){
                    alloacte.flip();
                    while (buffer.hasRemaining()){
                        byte b=buffer.get();
                        out.println(b);
                    }
                    alloacte.clear();
                }
                System.out.println("Incoming connection from: "
                        + sc.socket().getRemoteSocketAddress());
                buffer.rewind();
                sc.write(buffer);
                sc.close();
            }
        }

    }


}
