package com.iwill.io.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer {

    private Selector selector;

    public static void main(String[] args) throws Exception {
        NioServer server = new NioServer();
        server.init();
        server.start();
    }

    public void init() throws Exception {
        /**
         * 创建选择器
         */
        this.selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        /**
         * 设置为非阻塞
         */
        channel.configureBlocking(false);
        ServerSocket serverSocket = channel.socket();
        /**
         * 绑定端口
         */
        InetSocketAddress address = new InetSocketAddress(8080);
        serverSocket.bind(address);
        /**
         * 注册accept事件
         */
        channel.register(this.selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws Exception {
        while (true) {
            this.selector.select();
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                ite.remove();
                /**
                 * 客户端请求链接事件
                 */
                if (key.isAcceptable()) {
                    accept(key);
                    /**
                     * 读事件
                     */
                } else if (key.isReadable()) {
                    read(key);
                }
            }
        }
    }

    private void accept(SelectionKey key) throws Exception {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel channel = server.accept();
        channel.configureBlocking(false);
        /**
         * 注册读事件
         */
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        String request = new String(buffer.array()).trim();
        System.out.println("客户端请求：" + request);
        ByteBuffer outBuffer = ByteBuffer.wrap("请求收到".getBytes());
        channel.write(outBuffer);
    }
}
