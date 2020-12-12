package com.wtm.netty.NettyLists.StickPackage.fixedLength;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;


public class Server {
    private final  int port;
    public static final String RESPONSE = "Welcome to Netty!";
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
                    channel(NioServerSocketChannel.class).
                    //如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭
                    childOption(ChannelOption.TCP_NODELAY,true).
                    localAddress(port).childHandler(new ChannelInitializerImp());
            ChannelFuture channelFuture = bootstrap.bind().sync();
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
            //消息定长
            ch.pipeline().addLast(new FixedLengthFrameDecoder(Client.REQUEST.length()));
            ch.pipeline().addLast(new ServerHandler());
        }
    }

    public static void main(String[] args) {
        new Server(9090).start();
    }
}
