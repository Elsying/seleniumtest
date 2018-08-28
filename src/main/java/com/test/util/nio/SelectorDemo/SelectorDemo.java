package com.test.util.nio.SelectorDemo;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * 使用操作系统io模型  Windows IOCP模型  Linux EPOLL模块
 * Selector之所以能监听socket，使用epoll，epoll能够高效支持百万级别的句柄监听，windows使用iocp
 * 内部用了一个红黑树记录添加的socket，用了一个双向链表接收内核触发的事件。是系统级别的支持的
 *
 * //select核心方法
 * public abstract class Selector
 * {
 * // This is a partial API listing
 * public abstract Set keys( );
 * public abstract Set selectedKeys( );
 * public abstract int select( ) throws IOException;
 * public abstract int select (long timeout) throws IOException;
 * public abstract int selectNow( ) throws IOException;
 * public abstract void wakeup( );
 * }
 *
 * 流程：
 * 创建selector
 * 获取selectionkey
 * 遍历selectionkey看是1、connect：客户端连接服务端事件，对应值为SelectionKey.OP_CONNECT(8)
 *                   2、accept：服务端接收客户端连接事件，对应值为SelectionKey.OP_ACCEPT(16)
 *                   3、read：读事件，对应值为SelectionKey.OP_READ(1)
 *                   4、write：写事件，对应值为SelectionKey.OP_WRITE(4)
 * 哪种类型，然后进行相应操作；
 *
 *
 *
 * 每一个 Selector 对象维护三个键的集合：
 *   已注册的键的集合(Registered key set)已注册的键的集合(Registered key set)
 *   已选择的键的集合(Selected key set)
 *   已取消的键的集合(Cancelled key set)
 *
 *
 */
public class SelectorDemo {


    public void createSelector()throws Exception{
        //创建方法一Selector
        Selector selector=Selector.open();

        //方法二
        SelectorProvider provider=SelectorProvider.provider();
        Selector abstractSelector=provider.openSelector();
    }

    /**
     * 注册通道到选择器上
     */
    public void registerc()throws Exception{
        ServerSocketChannel ssc=ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress("localhost",80));
        ssc.configureBlocking(false);
        Selector selector=Selector.open();
        //需要监听的事件
        // 1、connect：客户端连接服务端事件，对应值为SelectionKey.OP_CONNECT(8)
        //2、accept：服务端接收客户端连接事件，对应值为SelectionKey.OP_ACCEPT(16)
        //3、read：读事件，对应值为SelectionKey.OP_READ(1)
        //4、write：写事件，对应值为SelectionKey.OP_WRITE(4)
        SelectionKey selectionKey=ssc.register(selector,SelectionKey.OP_ACCEPT);
        while (true){
            SocketChannel sc=ssc.accept();
            if(null==sc){
                continue;
            }
            sc.configureBlocking(false);
            //注册SocketChannl
            SelectionKey scselectionKey=sc.register(selector,SelectionKey.OP_ACCEPT|SelectionKey.OP_WRITE);
            //
        }
    }

    /**
     * 检查操作是否就绪
     * public abstract int interestOps( );//感兴趣兴趣的操作
     * public abstract int readyOps( );//感兴趣的操作中，已经准备就绪的操作
     * isReadable( )， isWritable( )， isConnectable( )， 和 isAcceptable( )同上
     */
    public void ready(SelectionKey key){
        if ((key.readyOps( ) & SelectionKey.OP_READ) != 0)
        {
//            myBuffer.clear( );
//            key.channel( ).read (myBuffer);
//            doSomethingWithBuffer (myBuffer.flip( ));
        }
    }
}
