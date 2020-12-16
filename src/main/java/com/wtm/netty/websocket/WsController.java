package com.wtm.netty.websocket;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 *类说明： WebSocket服务器端推送消息示例Controller
 */
@RestController
@RequestMapping("/api/ws")
public class WsController {

	/**
	 * 群发消息内容
	 * @param message
	 * @return
	 */
	@RequestMapping(value="/sendAll", method=RequestMethod.GET)
	String sendAllMessage(@RequestParam(required=true) String message){
		try {
			WebSocketServer.BroadCastInfo(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ok";
	}

	/**
	 * 指定会话ID发消息
	 * @param message 消息内容
	 * @param id 连接会话ID
	 * @return
	 */
	@RequestMapping(value="/sendOne", method=RequestMethod.GET)
	String sendOneMessage(@RequestParam(required=true) String message,@RequestParam(required=true) String id){
		try {
			WebSocketServer.SendMessage(id,message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ok";
	}
}