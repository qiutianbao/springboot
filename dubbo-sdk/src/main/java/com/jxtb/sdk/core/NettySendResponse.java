package com.jxtb.sdk.core;

import com.jxtb.sdk.SendResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by jxtb on 2019/6/14.
 */
public class NettySendResponse implements SendResponse{
    private ChannelHandlerContext ctx;

    public NettySendResponse (Response response){
        this.ctx = (ChannelHandlerContext) response.getRequestInstance();
    }

    @Override
    public void sendMessage(String jsonResponse) {
        ByteBuf resp = Unpooled.copiedBuffer(jsonResponse.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void sendError(String errorResponse) {
        ByteBuf resp = Unpooled.copiedBuffer(errorResponse.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void sendECfcaMessage(String jsonResponse) {

    }
}
