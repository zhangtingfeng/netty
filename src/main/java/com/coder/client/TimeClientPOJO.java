package com.coder.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


///客户端实现
///　Netty 客户端的通信步骤大致为：
/*
创建一个 NIO 线程组，用于处理服务器与客户端的连接，客户端不需要用到 boss worker。
创建一个 Bootstrap 对象，配置 Netty 的一系列参数，由于客户端 SocketChannel 没有父亲，所以不需要使用 childoption。
创建一个用于实际处理数据的类ChannelInitializer，进行初始化的准备工作，比如设置接受传出数据的字符集、格式以及实际处理数据的接口。
配置服务器 IP 和端口号，建立与服务器的连接。*/
///需要注意的是，Eclipse 是可以同时运行多个 Java 程序的，可以通过点击 来切换不同程序的控制台输出窗口。
public class TimeClientPOJO {
    public static void main(String[] args) throws Exception{
        String host = "127.0.0.1";            // ip
        int port = 8080;                    // 端口
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            Bootstrap b = new Bootstrap();            // 与ServerBootstrap类似
            b.group(workerGroup);                    // 客户端不需要boss worker
            b.channel(NioSocketChannel.class);///它代表一个用于连接到实体如硬件设备、文件、网络套接字或程序组件，能够执行一个或多个不同的 I/O 操作的开放连接。可以比作传入和传出数据的传输工具。
            b.option(ChannelOption.SO_KEEPALIVE, true);    // 客户端的socketChannel没有父亲
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // POJO
                    ch.pipeline().addLast(new TimeDecoderPOJO() ,new TimeClientHandlerPOJO());
                }
            });
            
            // 启动客户端，客户端用connect连接
            ChannelFuture f = b.connect(host, port).sync();
            
            // 等待连接关闭
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}