package com.coder.server;

import com.coder.client.Time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 服务器数据编码类  然后我们需要自定义服务器数据编码类
 * @author Coder
 *
 */
public class TimeEncoderPOJO extends MessageToByteEncoder<Time> {

    // 发送数据时调用
    @Override
    protected void encode(ChannelHandlerContext ctx, Time msg, ByteBuf out) throws Exception {
        // 只传输当前时间，精确到秒
        out.writeInt((int)msg.value());
    }

}