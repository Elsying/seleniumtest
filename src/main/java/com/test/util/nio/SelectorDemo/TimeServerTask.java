package com.test.util.nio.SelectorDemo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Calendar;

/**
 * 读取请求并返回响应
 */
public  class TimeServerTask implements Runnable{
    private SelectionKey selectionKey;

    public TimeServerTask(SelectionKey selectionKey){
        selectionKey=this.selectionKey;
    }

    @Override
    public void run() {
        SocketChannel channel= (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        try{
            int count=0;
            while ((count=channel.read(byteBuffer))>0){
                byteBuffer.flip();
                byte[] request=new byte[byteBuffer.remaining()];
                byteBuffer.get(request);
                String requestStr=new String(request);
                byteBuffer.clear();
                if (!"GET CURRENT TIME".equals(requestStr)) {
                    channel.write(byteBuffer.put("BAD_REQUEST".getBytes()));
                } else {
                    byteBuffer.put(Calendar.getInstance().getTime().toLocaleString().getBytes());
                    byteBuffer.flip();
                    channel.write(byteBuffer);
                }

            }
        }catch (IOException e){
            e.printStackTrace();;
        }

    }
}