package com.mooo.mytools.example;

//类serverThread
import java.io.*;
import java.net.*;

class serverThread extends Thread {

Socket clientRequest;
//用户连接的通信套接字
BufferedReader input; //输入流
PrintWriter output; //输出流

public serverThread(Socket s)
{ //serverThread的构造器
this.clientRequest=s;
//接收receiveServer传来的套接字
InputStreamReader reader;
OutputStreamWriter writer;
try{ //初始化输入、输出流
reader=new InputStreamReader(clientRequest.getInputStream());
writer=new OutputStreamWriter(clientRequest.getOutputStream());
input=new BufferedReader(reader);
output=new PrintWriter(writer,true);
}catch(IOException e){
System.out.println(e.getMessage());}
output.println("Welcome to the server!");
//客户机连接欢迎词
output.println("Now is:"+new java.util.Date()+" "+
"Port:"+clientRequest.getLocalPort());
output.println("What can I do for you?");
}
public void run(){ //线程的执行方法
String command=null; //用户指令
String str=null;
boolean done=false;

while(!done){
try{
str=input.readLine(); //接收客户机指令
}catch(IOException e){
System.out.println(e.getMessage());}
command=str.trim().toUpperCase();
if(str==null || command.equals("QUIT"))
//命令quit结束本次连接
done=true;
else if(command.equals("HELP")){
//命令help查询本服务器可接受的命令
output.println("query");
output.println("quit");
output.println("help");
}
else if(command.startsWith("QUERY"))
{ //命令query
output.println("OK to query something!");
}
//else if …….. //在此可加入服务器的其他指令
else if(!command.startsWith("HELP") &&
!command.startsWith("QUIT") &&
!command.startsWith("QUERY")){
output.println("Command not Found!Please refer to the HELP!");
}
}//end of while
try{
clientRequest.close(); //关闭套接字
}catch(IOException e){
System.out.println(e.getMessage());
}
command=null;
}
}
