package com.wtm.netty.NettyLists.StickPackage.messageTou;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;


/**
 * SimpleChannelInboundHandler  有一个重要特性，就是消息被读取后，会自动释放资
 *SimpleChannelInboundHandler类是继承了ChannelInboundHandlerAdapter类，
 * 新增了抽象channelRead0方法 和 重写了channelRead()方法
 * 把处理逻辑不变的内容写好在 channelRead(ctx,msg) 中，
 * 并且在里面调用 channelRead0 ，这样变化的内容通过抽象方法实现传递到子类中去了(在Netty5中channelRead0已被重命名为messageReceived)。
 * */
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    /*客户端被通知channel活跃以后，做事*/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf msg = null;
        for(int i=0;i<10;i++){
            msg = Unpooled.buffer(Client.REQUEST.length());
            msg.writeBytes(Client.REQUEST.getBytes());
            ctx.writeAndFlush(msg);
        }
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
