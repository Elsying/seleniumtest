package com.test.util.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {
    private int port= 8080;
    public void run() throws Exception{
        //可以看做线程池 具有一个或多个 EventLoop，EventLoop用于处理 Channel 的 I/O 操作，一个 EventLoop 是势必为它的生命周期一个线程
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //配置服务器的启动代码
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)throws Exception{
                            //LineBasedFrameDecoder StringDecoder用于解决TCP粘包、解包的工具类
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder());
                            //ch.pipeline().addLast(new TimeServerHandler());
                        }
                    });
            ChannelFuture f=b.bind(port).sync();
            System.out.println("TimeServer Started on 8080...");
            f.channel().closeFuture().sync();

        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        new TimeServer().run();
    }

}
