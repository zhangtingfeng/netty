package com.coder.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端数据处理类 自定义客户端业务逻辑类
 * @author Coder
 *
 */
public class TimeClientHandlerPOJO extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 直接将信息转换成Time类型输出即可
        Time time = (Time)msg;
        System.out.println(time);
        System.out.println("System.out.println(time)");
        ctx.close();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}