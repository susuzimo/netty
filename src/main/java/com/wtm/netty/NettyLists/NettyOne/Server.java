package com.wtm.netty.NettyLists.NettyOne;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class Server {

    private final int port;

    public Server(int port) {
        this.port = port;
    }



    public void start() throws InterruptedException {
        /**线程组*/
        EventLoopGroup group=new NioEventLoopGroup();
        try {
            /**服务端启动必备 与客户端不太一样*/
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.
                    group(group).
                    channel(NioServerSocketChannel.class).  //明使用NIO进行网络通讯
                    localAddress(new InetSocketAddress(port)).  //指明服务器监听端口
                    childHandler(new ServerHandler());          //接收到连接请求，新启一个socket通信，也就是channel，每个channel有自己的事件的handler
                    /*或者类似的
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });
                     */
            ChannelFuture channelFuture = serverBootstrap.bind().sync();  //绑定到端口，阻塞等待直到连接完成
            channelFuture.channel().closeFuture().sync();     //阻塞，直到channel关闭
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully().sync();   //释放资源
        }

    }


    public static void main(String[] args) throws InterruptedException {
        int port = 9999;
        Server echoServer = new Server(port);
        System.out.println("服务器即将启动");
        echoServer.start();
        System.out.println("服务器关闭");
    }

}
