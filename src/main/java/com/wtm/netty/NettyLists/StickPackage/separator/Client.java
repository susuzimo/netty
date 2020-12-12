package com.wtm.netty.NettyLists.StickPackage.separator;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;


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
                    channel(NioSocketChannel.class).remoteAddress(address, port).handler(new ChannelInitializerImp());
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
    private static class ChannelInitializerImp extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel ch) throws Exception {
            //	加分割符：系统回车符
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024*100000));
            ch.pipeline().addLast(new ClientHandler());
        }
    }


    public static void main(String[] args) {
        new Client(9090,"127.0.0.1").start();
    }
}
