package com.iwill.io.prophet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyEncoder extends MessageToByteEncoder<RequestCommand> {

    @Override
    public void encode(ChannelHandlerContext ctx, RequestCommand remotingCommand, ByteBuf out) {
        try {
            out.writeBytes(remotingCommand.encode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
