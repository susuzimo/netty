package com.wtm.netty.udp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class sendDemo {
    public static void main(String[] args) throws Exception {
        //创建一个datagramSocket实例
        DatagramSocket datagramSocket = new DatagramSocket();
        //使用键盘输入构建一个BufferReader
        /*
        *BufferedReader类从字符输入流中读取文本并缓冲字符，以便有效地读取字符，数组和行
        *InputStreamReader类是从字节流到字符流的桥接器
         */
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line=null;
        while ((line=bufferedReader.readLine())!=null){
            //转成byte
            byte[] bytes = line.getBytes();
            //创建一个用于发送的DatagramPacket对象
            DatagramPacket datagramPacket = new DatagramPacket(bytes,bytes.length, InetAddress.getByName("127.0.0.1"),10005);
            //发送数据
            datagramSocket.send(datagramPacket);
            if("88".equals(line)){
                break;
            }
        }
        datagramSocket.close();
    }
}
