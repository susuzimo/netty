package com.wtm.netty.NettyLists.NettyOne;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ClientHandler  extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        //往服务器写数据
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,Registered",
                CharsetUtil.UTF_8));
    }

    /*客户端被通知channel活跃以后，做事*/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ChannelHandlerContext:"+ctx.pipeline());

        //往服务器写数据
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,Netty",
                CharsetUtil.UTF_8));
    }

    /*处理异常*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /*客户端读取到数据后干什么*/
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        /*接收服务器传来的信息*/
        System.out.println("Client accetp:"+byteBuf.toString(CharsetUtil.UTF_8));
    }




}
