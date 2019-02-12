package com.iwill.io.prophet;

import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class Client {

    private static Bootstrap bootstrap = new Bootstrap();

    public static void main(String[] args) {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(10);

        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                //事件处理不采用新的executorGroup
                                new NettyEncoder(),
                                new NettyDecoder(),
                                new IdleStateHandler(0, 0, 20000)

                        );
                    }
                });
     Client client = new Client();
     client.invoke();
    }



    public void invoke() {
        RequestCommand requestCommand = buildRequestRemotingCommand();
        Channel channel = openChannel();
        channel.writeAndFlush(requestCommand);
    }

    public RequestCommand buildRequestRemotingCommand() {
        ExecuteRequest executeRequest = new ExecuteRequest(0, 1);

        byte[] requestBody = new Gson().toJson(executeRequest).getBytes(Charset.forName("UTF-8"));
        RequestCommand requestCommand = new RequestCommand(100, "TEST");
        requestCommand.setBody(requestBody);
        return requestCommand;
    }

    public Channel openChannel() {
        ChannelFuture channelFuture = this.bootstrap.connect(new InetSocketAddress("127.0.0.1", 8000));
        try {
            if (channelFuture.awaitUninterruptibly(10000)) {
                return channelFuture.channel();
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            return null;
        }
        return null;
    }
}
