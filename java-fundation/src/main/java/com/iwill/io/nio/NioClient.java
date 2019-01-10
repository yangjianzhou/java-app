package com.iwill.io.nio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioClient {

    private Selector selector;

    private BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        NioClient client = new NioClient();
        client.init();
        client.start();
    }

    private void init() throws Exception {
        /**
         * 创建选择器
         */
        this.selector = Selector.open();
        /**
         * 创建SocketChannel
         */
        SocketChannel channel = SocketChannel.open();
        /**
         * 设置为非阻塞
         */
        channel.configureBlocking(false);
        /**
         * 链接服务器
         */
        channel.connect(new InetSocketAddress("127.0.0.1", 8080));
        /**
         * 注册connect事件
         */
        channel.register(selector, SelectionKey.OP_CONNECT);
    }

    public void start() throws Exception {
        while (true) {
            /**
             * 此方法会阻塞，直到至少有一个已注册的事件发生
             */
            selector.select();
            /**
             * 获取发生事件的SelectionKey对象集合
             */
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                /**
                 * 从集合中移除即将处理的SelectionKey，避免重复处理
                 */
                ite.remove();
                if (key.isConnectable()) {
                    /**
                     * 链接事件
                     */
                    connect(key);
                } else if (key.isReadable()) {
                    /**
                     * 读事件
                     */
                    read(key);
                }
            }
        }
    }

    public void connect(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        /**
         * 如果正在链接
         */
        if (channel.isConnectionPending()) {
            /**
             * 完成链接
             */
            if (channel.finishConnect()) {
                /**
                 * 设置成非阻塞
                 */
                channel.configureBlocking(false);
                /**
                 * 注册读事件
                 */
                channel.register(this.selector, SelectionKey.OP_READ);
                /**
                 * 获取客户端输入
                 */
                String request = clientInput.readLine();
                /**
                 * 发送到服务端
                 */
                channel.write(ByteBuffer.wrap(request.getBytes()));
            } else {
                key.cancel();
            }
        }
    }

    private void read(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        /**
         * 创建读取缓冲区
         */
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        String response = new String(buffer.array()).trim();
        System.out.println("服务器端响应 ：" + response);
        String nextRequest = clientInput.readLine();
        ByteBuffer outBuffer = ByteBuffer.wrap(nextRequest.getBytes());
        /**
         * 将请求发送到服务端
         */
        channel.write(outBuffer);
    }
}
