package com.iwill.io.nio2;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Nio2Server {

    private ExecutorService taskExecutors;

    private AsynchronousServerSocketChannel serverChannel;

    public static void main(String[] args) throws Exception {
        Nio2Server server = new Nio2Server();
        server.init();
        server.start();
    }

    class Worker implements Callable<String> {

        private CharBuffer charBuffer;

        private CharsetDecoder decoder = Charset.defaultCharset().newDecoder();

        private AsynchronousSocketChannel channel;

        public Worker(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public String call() throws Exception {

            final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

            while (channel.read(buffer).get() != -1) {
                buffer.flip();
                charBuffer = decoder.decode(buffer);
                String request = charBuffer.toString().trim();
                System.out.println("客户端请求：" + request);
                ByteBuffer outBuffer = ByteBuffer.wrap("请求收到".getBytes());
                channel.write(outBuffer).get();
                if (buffer.hasRemaining()) {
                    buffer.compact();
                } else {
                    buffer.clear();
                }
            }
            channel.close();
            return "ok";
        }
    }

    public void init() throws Exception {
        taskExecutors = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

        serverChannel = AsynchronousServerSocketChannel.open();
        if (serverChannel.isOpen()) {
            serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverChannel.bind(new InetSocketAddress("127.0.0.1", 8080));
        } else {
            throw new RuntimeException("通道未打打开！");
        }
    }

    public void start() throws Exception {
        System.out.println("等待客户端请求......");
        while (true) {
            Future<AsynchronousSocketChannel> future = serverChannel.accept();
            try {
                AsynchronousSocketChannel channel = future.get();
                taskExecutors.submit(new Worker(channel));
            } catch (Exception exp) {
                System.err.println(exp);
                System.err.println("服务器关闭!");
                taskExecutors.shutdown();
                while (!taskExecutors.isTerminated()) {

                }
                break;
            }
        }
    }
}
