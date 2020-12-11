package com.wtm.netty.NettyLists.review;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class Server {
    private final  int port;


    public Server(int port) {
        this.port = port;

    }


    public  void start(){
        /** 线程组*/
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        try {
            /**启动*/
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(eventLoopGroup).
                    channel(NioServerSocketChannel.class).localAddress(port).childHandler(new ServerHandler());
            ChannelFuture channelFuture = bootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new Server(9090).start();
    }
}
