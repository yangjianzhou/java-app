package com.iwill.io.prophet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;

public class Server {

    private final ServerBootstrap serverBootstrap = new ServerBootstrap();
    private final EventLoopGroup eventLoopGroupBoss = new NioEventLoopGroup(10);
    private final EventLoopGroup eventLoopGroupSelector = new NioEventLoopGroup(4);


    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        this.serverBootstrap.group(this.eventLoopGroupBoss, this.eventLoopGroupSelector)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535)
                .localAddress(new InetSocketAddress(8000))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new NettyEncoder(),
                                new NettyDecoder(),
                                new IdleStateHandler(0, 0, 100000),
                                new NettyServerHandler()

                        );
                    }
                });

        try {
            ChannelFuture sync = this.serverBootstrap.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) sync.channel().localAddress();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", ie);
        }

    }
}
