package com.wtm.netty.tcp;

import java.io.PrintWriter;
import java.net.Socket;

public class TcpClinet {
    public static void main(String[] args)  throws  Exception{
        String msg="hello world";
        //创建一个Socket,跟本机的8888端口进行连接
        Socket socket = new Socket("127.0.0.1", 8888);
        //使用socket创建一个PrintWriter进行写数据
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        //发送数据
        printWriter.println(msg);
        //刷新一下，使得服务立马可以收到请求信息
        printWriter.flush();
        printWriter.close();
        socket.close();

    }
}
