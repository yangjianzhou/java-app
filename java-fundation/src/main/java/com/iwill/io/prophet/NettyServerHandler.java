package com.iwill.io.prophet;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class NettyServerHandler extends SimpleChannelInboundHandler<RequestCommand> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestCommand msg) throws Exception {
        System.out.println(new Gson().toJson(msg));
        String str = new String(msg.getBody(), Charset.forName("UTF-8"));
        System.out.println(msg.getRemark());
        ExecuteRequest executeRequest = new Gson().fromJson(str, ExecuteRequest.class);
        System.out.println(new Gson().toJson(executeRequest));
    }
}