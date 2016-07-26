package com.mooo.mytools.example;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.io.IOException;

public class UserController extends HttpServlet{
   String putin = new String("putin");
   public String Onclick(HttpServletRequest request, HttpServletResponse response){
       putin = request.getParameter("putin");
       System.out.println(putin);
       return putin;
        }
}
