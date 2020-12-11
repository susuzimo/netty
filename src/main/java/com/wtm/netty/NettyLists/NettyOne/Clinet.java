package com.wtm.netty.NettyLists.NettyOne;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class Clinet {
      private final int  port;
      private final String host;

    public Clinet(int port, String host) {
        this.port = port;
        this.host = host;
    }


    public  void start() throws InterruptedException {
        /**
         * 线程组
         * EventLoopGroup 包含一组 EventLoop，Channel 通过注册到 EventLoop 中执行操作
         * NioEventLoopGroup 实际上就是个线程池，一个 EventLoopGroup 包含一个或者多个 EventLoop；
         *一个 EventLoop 在它的生命周期内只和一个 Thread 绑定；
         *所有有 EnventLoop 处理的 I/O 事件都将在它专有的 Thread 上被处理；
         *一个 Channel 在它的生命周期内只注册于一个 EventLoop；
         *每一个 EventLoop 负责处理一个或多个 Channel；
         * */
        EventLoopGroup eventLoop=new NioEventLoopGroup();
        try {
            /*客户端启动必备*/
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(eventLoop).
                    channel(NioSocketChannel.class).   //指明使用NIO进行网络通讯
                    remoteAddress(new InetSocketAddress(host,port))  //配置远程服务器的地址
                    .handler(new ClientHandler());  //定义一个handler用来发送消息
            ChannelFuture channelFuture= bootstrap.connect().sync(); //connect连接到远程节点，sync阻塞等待直到连接完成
            channelFuture.channel().closeFuture().sync();   //阻塞，直到channel关闭
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventLoop.shutdownGracefully().sync();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        new Clinet(9999,"127.0.0.1").start();
    }
}
