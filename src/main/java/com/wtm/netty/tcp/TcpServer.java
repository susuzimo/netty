package com.wtm.netty.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    public static void main(String[] args) throws Exception {
        //创建一个ServerSocket监听一个端 8888
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("TCP服务器已经启动,端口是8888");
        //无限循环
        while (true){
            //等待客户端的请求，如果有请求分配一个Socket
            Socket socket = serverSocket.accept();
            //根据标准输入构造一个BufferedReader对象
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line=null;
            while ((line=reader.readLine())!=null && !line.equals("")){
                System.out.println(line);
            }
            //通过Socket对象得到输出流 构造BufferedWriter对象
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //模拟http的请求头信息
            writer.write("HTTP/1.1 200 OK \r\n Content-Type:text/html \r\n charset=UTF-8 \r\n\r\n");
            //写实体信息
            writer.write("<html><head><title>请求头</title></head><body><h1>返回信息</h1></body></html>");
            //刷新输出流 使得数据立马发送
            writer.flush();
            //关闭
            writer.close();
            reader.close();
            socket.close();
        }

    }
}
