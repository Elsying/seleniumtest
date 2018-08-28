package com.test.util.nio.SelectorDemo;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 基于Selector+Channel+线程池的Timeserver
 * 1 把Channel的就绪选择放在了主线程(Acceptor线程)中来处理(等待数据准备阶段)
 * 2 而真正的读取请求并返回响应放在了线程池中提交一个任务来执行(处理数据阶段)
 * 真正意义上实现了一个线程服务于多个client
 */
public class SelectorTimeServer {
    private static ExecutorService executorService;
    static {
        //创建线程池，指定大小
        executorService=new ThreadPoolExecutor(5,10,10,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(1000));
    }
    public static void main(String[]args) throws Exception{
        ServerSocketChannel ssc=ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(8080));
        ssc.configureBlocking(false);
        Selector selector=Selector.open();
        ssc.register(selector,ssc.validOps());//注册选择器

        while (true){
            int readCount=selector.select(1000);
            if(readCount==0){
                continue;
            }
            Set<SelectionKey>selectionKeys=selector.selectedKeys();//获得key
            Iterator<SelectionKey>keyIterator=selectionKeys.iterator();//迭代key
            while (keyIterator.hasNext()){
                SelectionKey selectionKey=keyIterator.next();
                //告知此键是否有效
                if (selectionKey.isValid()){
                    //表示ServerSocketChannel  判断有accept是否就绪的key
                    if (selectionKey.isAcceptable()){
                        //获取已经就绪的ServerSocketChannel
                        ServerSocketChannel server= (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel=server.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                    }
                    // 一个有数据可读的通道可以说是“读就绪”
                    if(selectionKey.isReadable()){
                        executorService.submit(new TimeServerTask(selectionKey));
                    }
                    keyIterator.remove();
                }
            }
        }
    }



    }
