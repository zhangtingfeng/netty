package com.coder.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloServer {
    private int port;
    
    public HelloServer(int port) {
        this.port = port;
    }
    /*Netty 服务器的通信步骤为：

 创建两个NIO线程组，一个专门用于接收来自客户端的连接，另一个则用于处理已经被接收的连接。
 创建一个ServerBootstrap对象，配置Netty的一系列参数，例如接受传出数据的缓存大小等。
 创建一个用于实际处理数据的类ChannelInitializer，进行初始化的准备工作，比如设置接受传出数据的字符集、格式以及实际处理数据的接口。
 绑定端口，执行同步阻塞方法等待服务器端启动即可。*/
    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();        // 用来接收进来的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();    // 用来处理已经被接收的连接
        System.out.println("准备运行端口：" + port);
        
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)            // 这里告诉Channel如何接收新的连接
            .childHandler( new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 自定义处理类
                    ch.pipeline().addLast(new HelloServerHandler());
                }
            })
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true);
            
            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(port).sync();
            
            // 等待服务器socket关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    /*
     那么，这便是一个可执行的服务端程序了。运行后控制台输出如下：
     我们可以去自定义客户端程序，这里为了方便使用 telnet 充当客户端。

　　需要注意的是，Windows 默认是没有开启 telnet 客户端的，需要我们手动开启。

　　菜单->控制面板->程序->打开或关闭Windows功能，设置如图：
     这样我们就能用 telnet 客户端了。

　　在 cmd 窗口输入命令如下：
这时候我们在该窗口输入什么，eclipse 控制台也会对应输出相应内容。如下：

     */
    public static void main(String[] args) throws Exception {
        int port = 10110;
        new HelloServer(port).run();
    }

}