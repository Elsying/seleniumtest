package com.test.util.nio.SelectorDemo;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SelectorTimeClient {

    //连接超时时间
    static int connectTimeout = 3000;
    static ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    @Test
    public void start() throws IOException {
    //和TimeClient一样
    }
}
