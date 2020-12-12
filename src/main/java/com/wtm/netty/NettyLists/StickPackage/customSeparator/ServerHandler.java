package com.wtm.netty.NettyLists.StickPackage.customSeparator;



import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ChannelInboundHandlerAdapter 不会像 SimpleChannelInboundHandler 一样在 channelRead() 里面释放资源，
 * 而是在收到消息处理完成的事件时，当 writeAndFlush() 方法被调用时才会释放资源。
 * */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private AtomicInteger counter = new AtomicInteger(0);
    /*** 服务端读取到网络数据后的处理*/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf)msg;/*netty实现的缓冲区*/
        String request = in.toString(CharsetUtil.UTF_8);
        System.out.println("Server Accept:"+request
                +" the counter is "+counter.incrementAndGet());
        String resp = "Hello,"+request+",welcome"
                +Server.DELIMITER_SYMBOL;
        //将String转为ByteBuf对象   Unpooled.copiedBuffer
        ctx.write(Unpooled.copiedBuffer(resp,CharsetUtil.UTF_8));
    }

    /*** 服务端读取完成网络数据后的处理  释放资源*/
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)/*flush掉所有的数据*/
                .addListener(ChannelFutureListener.CLOSE);/*当flush完成后，关闭连接*/
    }

    /**发生异常后的处理*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
