package com.mooo.mytools.example;

//类receiveServer
import java.io.*;
import java.util.*;
import java.net.*;

public class receiveServer{

final int RECEIVE_PORT=9090;
//该服务器的端口号

//receiveServer的构造器
public receiveServer() {
ServerSocket rServer=null;
//ServerSocket的实例
Socket request=null; //用户请求的套接字
Thread receiveThread=null;
try{
rServer=new ServerSocket(RECEIVE_PORT);
//初始化ServerSocket
System.out.println("Welcome to the server!");
System.out.println(new Date());
System.out.println("The server is ready!");
System.out.println("Port: "+RECEIVE_PORT);
while(true){ //等待用户请求
request=rServer.accept();
//接收客户机连接请求
receiveThread=new serverThread(request);
//生成serverThread的实例
receiveThread.start();
//启动serverThread线程
}
}catch(IOException e){
System.out.println(e.getMessage());}
}

public static void main(String args[]){
new receiveServer();
} //end of main

} //end of class
