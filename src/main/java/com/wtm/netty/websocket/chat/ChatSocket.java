package com.wtm.netty.websocket.chat;

import com.google.gson.Gson;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint("/chatSocket")
public class ChatSocket {

    //定义一个全局变量集合sockets,用户存放每个登录用户的通信管道
    private  static Set<ChatSocket> sockets=new HashSet<ChatSocket>();
    //定义一个全局变量Session,用于存放登录用户的用户名
    private Session session;
    //定义一个全局变量map，key为用户名，该用户对应的session为value
    private  static Map<String, Session> map=new HashMap<String, Session>();
    //定义一个数组，用于存放所有的登录用户,显示在聊天页面的用户列表栏中
    private  static List<String> names=new ArrayList<String>();
    private String username;
    private Gson  gson=new Gson();

    /*
     * 监听用户登录
     */
    @OnOpen
    public void open(Session session){
        System.out.println("建立了一个socket通道" + session.getId());
        this.session = session;
        //将当前连接上的用户session信息全部存到scokets中
        sockets.add(this);
        //拿到URL路径后面所有的参数信息
        String queryString = session.getQueryString();
        System.out.println();
        //截取=后面的参数信息(用户名)，将参数信息赋值给全局的用户名
        this.username = queryString.substring(queryString.indexOf("=")+1);
        //每登录一个用户，就将该用户名存入到names数组中,用于刷新好友列表
        names.add(this.username);
        //将当前登录用户以及对应的session存入到map中
        this.map.put(this.username, this.session);
        System.out.println("用户"+this.username+"进入聊天室");
        Message message = new Message();
        message.setAlert("用户"+this.username+"进入聊天室");
        //将当前所有登录用户存入到message中，用于广播发送到聊天页面
        message.setNames(names);
        //将聊天信息广播给所有通信管道(sockets)
        broadcast(sockets, gson.toJson(message) );
    }

    /*
     * 退出登录
     */
    @OnClose
    public void close(Session session){
        //移除退出登录用户的通信管道
        sockets.remove(this);
        //将用户名从names中剔除，用于刷新好友列表
        names.remove(this.username);
        Message message = new Message();
        System.out.println("用户"+this.username+"退出聊天室");
        message.setAlert(this.username+"退出当前聊天室！！！");
        //刷新好友列表
        message.setNames(names);
        broadcast(sockets, gson.toJson(message));
    }

    /*
     * 接收客户端发送过来的消息，然后判断是广播还是单聊
     */
    @OnMessage
    public void receive(Session  session,String msg) throws IOException {
        //将客户端消息转成json对象
        ContentVo vo = gson.fromJson(msg, ContentVo.class);
        //如果是群聊，就像消息广播给所有人
        if(vo.getType()==1){
            Message message = new Message();
            message.setDate(new Date().toLocaleString());
            message.setFrom(this.username);
            message.setSendMsg(vo.getMsg());
            broadcast(sockets, gson.toJson(message));
        }else{
            Message message = new Message();
            message.setDate(new Date().toLocaleString());
            message.setFrom(this.username);
            message.setAlert(vo.getMsg());
            message.setSendMsg("<font color=red>正在私聊你：</font>"+vo.getMsg());
            String to  = vo.getTo();
            //根据单聊对象的名称拿到要单聊对象的Session
            Session to_session = this.map.get(to);
            //如果是单聊，就将消息发送给对方
            to_session.getBasicRemote().sendText(gson.toJson(message));
        }
    }

    /*
     * 广播消息
     */
    public void broadcast(Set<ChatSocket>sockets ,String msg){
        //遍历当前所有的连接管道，将通知信息发送给每一个管道
        for(ChatSocket socket : sockets){
            try {
                //通过session发送信息
                socket.session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
