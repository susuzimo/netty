package com.wtm.netty.NettyLists.review;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;



public class Client {
    private final  int port;
    private final  String address;

    public Client(int port, String address) {
        this.port = port;
        this.address = address;
    }


    public  void start(){
        /** 线程组*/
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        try {
            /**启动*/
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(eventLoopGroup).
                    channel(NioSocketChannel.class).remoteAddress(address, port).handler(new ClientHandler());
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new Client(9090,"127.0.0.1").start();
    }
}
