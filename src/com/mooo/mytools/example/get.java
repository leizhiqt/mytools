package com.mooo.mytools.example;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.io.IOException;
public class get{
public void doGet (HttpServletRequest req, HttpServletResponse res)
             throws ServletException, IOException {
     
        String clientLanguage = req.getHeader("Accept-Language");
        
             
        if ( clientLanguage.equals("zh-cn") ) {            
            req.setCharacterEncoding("GBK");
            res.setContentType("text/html; charset=GBK");
        }
        
        else if ( clientLanguage.equals("zh-tw") ) {
            req.setCharacterEncoding("BIG5");
            res.setContentType("text/html; charset=BIG5");
        }
        
        else if ( clientLanguage.equals("jp") ) {
            req.setCharacterEncoding("SJIS");
            res.setContentType("text/html; charset=SJIS");
        }
       
        else {
            req.setCharacterEncoding("ISO-8859-1");
            res.setContentType("text/html; charset=ISO-8859-1");
        }
        
       
    }
}
