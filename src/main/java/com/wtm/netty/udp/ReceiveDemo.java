package com.wtm.netty.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveDemo {
    public static void main(String[] args) throws Exception {
        //创建一个datagramSocket实例，并且把实例绑定到本机地址，端口10005
        DatagramSocket datagramSocket = new DatagramSocket(10005);
        byte[] bytes = new byte[1024];
        //以一个空数组来创建datagramPacket,这个对象作用是接收DatagramSocket中的数据
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        while (true){  //无线循环，因为不知道数据何时来
            //接收数据包
            datagramSocket.receive(datagramPacket);
            //获取接收到的数据
            byte[] data = datagramPacket.getData();
            //把数字转成字符
            String str = new String(data, 0, datagramPacket.getLength());
            if("88".equals(str)){
                break;
            }
            System.out.println("接收到的数据:"+str);
        }
        //关闭
        datagramSocket.close();
    }
}
