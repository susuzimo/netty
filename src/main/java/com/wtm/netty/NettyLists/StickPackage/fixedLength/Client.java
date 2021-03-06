package com.wtm.netty.NettyLists.StickPackage.fixedLength;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;


public class Client {
    private final  int port;
    private final  String address;
    public final static String REQUEST = "ZhangWuJi,ZhangSanFeng";
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
            //消息定长
            ch.pipeline().addLast(new FixedLengthFrameDecoder(Server.RESPONSE.length()));
            ch.pipeline().addLast(new ClientHandler());
        }
    }


    public static void main(String[] args) {
        new Client(9090,"127.0.0.1").start();
    }
}
