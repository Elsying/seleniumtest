package com.test.util.nio.SocketChannel;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.*;
import static java.lang.System.out;

/**
 * 使用nio中的channel+线程池，来实现TimeServer
 */
public class TimeServer {
    //阻塞队列
    private BlockingQueue<SocketChannel> idleQueue = new LinkedBlockingDeque<SocketChannel>();
    //作为结果Future接口封装了取消，获取线程结果，以及状态判断是否取消，是否完成这几个方法
    private BlockingQueue<Future<SocketChannel>> workingQueue = new LinkedBlockingDeque<Future<SocketChannel>>();
    //创建线程池
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    {
        //不断的循环idleQueue和workingQueue
        /*
        首先循环idleQueue，迭代出其中的SocketChannel，然后封装成一个TimeServerHandleTask对象，
        提交到线程池中处理这个SocketChannel的请求，同时我们会将SocketChannel中移除，放到workingQueue中。
        需要注意的是，这个SocketChannel可能只是与服务端建立了连接，但是没有发送请求，又或者是发送了一次或者多次请求。
        发送一次"GET CURRENT TIME”，就相当于一次请求。在TimeServerHandleTask中，会判断是否发送了请求，如果没有请求则不需要处理。
        如果SocketChannel发送了多次请求，TimeServerHandleTask一次也只会处理一个请求。其他的请求等到下一次循环的时候再处理。因
        为使用线程池的情况，线程的数量有限，所以要合理的分配，不能让一个线程一直处理一个client的请求

        使用ServerSocket、Socket类时，服务端Socket往往要为每一个客户端Socket分配一个线程，而每一个线程都有可能处于长时间的阻塞状态中。
        过多的线程也会影响服务器的性能（可以使用线程池优化，具体看这里：如何编写多线程Socket程序）。
        而使用SocketChannel、ServerSocketChannel类可以非阻塞通信，这样使得服务器端只需要一个线程就能处理所有客户端socket的请求。

        问题：
            因为我们并不知道一个SocketChannel是否发送了请求，所以必须迭代所有的SocketChannel，然后尝试读取请求数据，如果有请求，
            就处理，否则就跳过。假设一个有10000个连接，前9999个连接都没有请求，刚好最后一个连接才有请求。那么前9999次任务处理都是没有必要的。
            如果有一种方式，可以让我们直接获取到真正发送了请求的SocketChannel，那么效率将会高的多。
            这就是我们下一节将要讲解的Selector(选择器)，其可以帮助我们管理所有与server端已经建立了连接的client(SocketChannel)，
            并将准备好数据的client过滤出来。我们可以有一个专门的线程来运行Selector，将准备好数据的client交给工作线程来处理。


         */
        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        //task1：迭代当前idleQueue中的SocketChannel，提交到线程池中执行任务，并将其移到workingQueue中
                        for (int i = 0; i < idleQueue.size(); i++) {
                            //删除
                            SocketChannel socketChannel = idleQueue.poll();
                            if (null != socketChannel) {
                                //submit返回 Future 对象用于判断 Runnable是否结束执行
                                Future<SocketChannel> result = executor.submit(new TimeServerHandleTask(socketChannel), socketChannel);
                                workingQueue.put(result);
                            }
                        }
                        //task2：迭代当前workingQueue中的SocketChannel，如果任务执行完成，将其移到idleQueue中
                        for (int i = 0; i < workingQueue.size(); i++) {
                            Future<SocketChannel> future = workingQueue.poll();
                            if (!future.isDone()) {
                                workingQueue.put(future);
                                continue;
                            }
                            SocketChannel channel = null;
                            try {
                                channel = future.get();
                                idleQueue.put(channel);
                            } catch (ExecutionException e) {
                                //如果future.get()抛出异常，关闭SocketChannel，不再放回idleQueue
                                channel.close();
                                e.printStackTrace();
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    /**
     * 接受请求GET CURRENT TIME，返回当前时间
     */
    public class TimeServerHandleTask implements Runnable {
        SocketChannel socketChannel;

        TimeServerHandleTask(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            try {
                ByteBuffer requestBuffer = ByteBuffer.allocate("GET CURRENT TIME".length());
                //尝试读取数据，因为是非阻塞，所以如果没有数据会立即返回。
                int byteRead = socketChannel.read(requestBuffer);
                if (byteRead <= 0) {
                    return;
                }
                // //如果读取到了数据，则需要考虑粘包、解包问题，这个while代码是为了读取一个完整的请求信息"GET CURRENT TIME"，
                while (requestBuffer.hasRemaining()) {
                    socketChannel.read(requestBuffer);
                }
                String requestStr = new String(requestBuffer.array());
                if (!"GET CURRENT TIME".equals(requestStr)) {
                    String bad_request = "BAD_REQUEST";
                    ByteBuffer responseBuffer = ByteBuffer.allocate(bad_request.length());
                    responseBuffer.put(bad_request.getBytes());
                    responseBuffer.flip();
                    socketChannel.write(requestBuffer);
                } else {
                    //讲当前时间写入socketchannl
                    String timrStr = DateFormat.getDateInstance().format(new Date());//获取当前时间
                    ByteBuffer reponseBuffer = ByteBuffer.allocate(timrStr.length());
                    reponseBuffer.put(timrStr.getBytes());
                    reponseBuffer.flip();
                    socketChannel.write(reponseBuffer);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void testing()throws IOException  {
        String timrStr = DateFormat.getDateTimeInstance().format(new Date());
        out.println(timrStr);

        TimeServer timeServer=new TimeServer();
        ServerSocketChannel ssc=ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(new InetSocketAddress(8080));
        while (true){
            //如果以非阻塞模式被调用，当没有传入连接在等待时， ServerSocketChannel.accept( )会立即返回 null
            SocketChannel socketChannel=ssc.accept();
            if(null!=socketChannel){
                socketChannel.configureBlocking(false);
                timeServer.idleQueue.add(socketChannel);
            }
        }

    }
}
