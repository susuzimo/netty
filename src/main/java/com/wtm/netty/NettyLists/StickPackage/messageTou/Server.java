package com.wtm.netty.NettyLists.StickPackage.messageTou;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


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
            //根据消息长度，从中剥离出完整的实际数据
            /***
             * 发送数据包长度 = 长度域的值 + lengthFieldOffset + lengthFieldLength + lengthAdjustment。
             * maxFrameLength：最大帧长度。也就是可以接收的数据的最大长度。如果超过，此次数据会被丢弃。
             *lengthFieldOffset：长度域偏移。就是说数据开始的几个字节可能不是表示数据长度，需要后移几个字节才是长度域。
             *lengthFieldLength：长度域字节数。用几个字节来表示数据长度。
             *lengthAdjustment：数据长度修正。因为长度域指定的长度可以使header+body的整个长度，也可以只是body的长度。如果表示header+body的整个长度，那么我们需要修正数据长度。
             *initialBytesToStrip：跳过的字节数。如果你需要接收header+body的所有数据，此值就是0，如果你只想接收body数据，那么需要跳过header所占用的字节数。
             */
            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,
                    0,2,0,2));
            ch.pipeline().addLast(new ServerHandler());
        }
    }

    public static void main(String[] args) {
        new Server(9090).start();
    }
}
