package com.test.util.nio;

import org.junit.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * 概念上，缓冲区是包在一个对象内的基本数据元素数组。
 * Buffer 类相比一个简单数组的优点 是它将关于数据的数据内容和信息包含在一个单一的对象中。
 * Buffer 类以及它专有的子类定义了 一个用于处理数据缓冲区的 API。
 * Buffer类定义了所有的缓冲区都具有四个属性来提供关于其所包含的数据元素的信息。
 * 容量（ Capacity）
 * 缓冲区能够容纳的数据元素的最大数量，可以理解为数组的长度。 这一容量在缓冲区创建时被设定，并且永远不能被改变。
 * 上界（ Limit）
 * 缓冲区的第一个不能被读或写的元素。或者说，缓冲区中现存元素的计数。
 * 位置（ Position）
 * 下一个要被读或写的元素的索引。Buffer类提供了get( )和 put( )函数 来读取或存入数据，position位置会自动进行相应的更新。
 * 标记（ Mark）
 * 一个备忘位置。调用 mark( )来设定 mark = postion。调用 reset( )设定 position = mark。标记在设定前是未定义的(undefined)。
 *
 */
public class BufferDemo {
    //JDK1.4时，引入的api
    /*
    public final int capacity( )//返回此缓冲区的容量
    public final int position( )//返回此缓冲区的位置
    public final Buffer position (int newPositio)//设置此缓冲区的位置
    public final int limit( )//返回此缓冲区的限制
    public final Buffer limit (int newLimit)//设置此缓冲区的限制
    public final Buffer mark( )//在此缓冲区的位置设置标记
    public final Buffer reset( )//将此缓冲区的位置重置为以前标记的位置
    public final Buffer clear( )//清除此缓冲区
    public final Buffer flip( )//反转此缓冲区
    public final Buffer rewind( )//重绕此缓冲区
    public final int remaining( )//返回当前位置与限制之间的元素数
    public final boolean hasRemaining( )//告知在当前位置和限制之间是否有元素
    public  boolean isReadOnly( );//告知此缓冲区是否为只读缓冲区

    //JDK1.6时引入的api
    public  boolean hasArray();//告知此缓冲区是否具有可访问的底层实现数组
    public  Object array();//返回此缓冲区的底层实现数组
    public  int arrayOffset();//返回此缓冲区的底层实现数组中第一个缓冲区元素的偏移量
    public  boolean isDirect();//告知此缓冲区是否为直接缓冲区
    */

    @Test
    public void BufferCreateDemo(){
        //方式1：allocate方式直接分配，内部将隐含的创建一个数组
        ByteBuffer allocate=ByteBuffer.allocate(10);

        //方式2：通过wrap根据一个已有的数组创建
        byte[] bytes=new byte[10];
        ByteBuffer wrap=ByteBuffer.wrap(bytes);

        //方式3：通过wrap根据一个已有的数组指定区间创建
        ByteBuffer wrapoffset = ByteBuffer.wrap(bytes,2,5);

        //打印出刚刚创建的缓冲区的相关信息
        print(allocate,wrap,wrapoffset);

    }

    private static void print(Buffer... buffers){  //...表示可以传入多个Buffer类型参数...和参数值中间要有个空格
        for (Buffer buffer:buffers) {
            System.out.println("capacity="+buffer.capacity()//返回此缓冲区的容量
                    +",limit="+buffer.limit()
                    +",position="+buffer.position()
                    +",hasRemaining:"+buffer.hasArray()
                    +",remaining="+buffer.remaining()
                    +",hasArray="+buffer.hasArray()
                    +",isReadOnly="+buffer.isReadOnly()
                    +",arrayOffset="+buffer.arrayOffset());
        }
    }


    private void flip(){
        ByteBuffer allocate=ByteBuffer.allocate(10);
        //当positoin到limit没有数值时，将position值重新设为 0,limit变为position以前的位置，这时候position到limit区间就是有数值的区间
        allocate.flip();
    }
}
