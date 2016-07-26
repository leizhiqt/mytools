package com.mooo.mytools.example;

public class loadClass {
    public static void main(String[] args) {
        try {
            Class.forName("org.apache.jasper.servlet").newInstance();
            System.out.println("is found.");
         } catch (Exception ex) {
            // handle the error
          System.out.println("Exception Load error of: " + ex.getMessage());
                     }
         }
}
