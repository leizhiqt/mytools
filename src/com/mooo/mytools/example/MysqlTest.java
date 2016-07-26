package com.mooo.mytools.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!
public class MysqlTest {
    public static void main(String[] args) {

           Statement stmt = null;
           ResultSet rs = null;

        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            Connection connTest = DriverManager.getConnection("jdbc:mysql://localhost/test","root","");
	    stmt = connTest.createStatement();

	    String sql = "SELECT * FROM User";
	    rs = stmt.executeQuery(sql);
	   while(rs.next()) {
            System.out.println("Name:"+rs.getString("Name")+"\tPassword:"+rs.getString("Password"));
               }

            System.out.println("load ok!");
        

        } catch (SQLException ex) {
            // handle the error
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());

        } catch (Exception ex) {
            // handle the error
          System.out.println("Exception Load error of: " + ex.getMessage());
         } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed
           if (rs != null) {
                try {
                     rs.close();
                } catch (SQLException sqlEx) { // ignore }
                  rs = null;
                      }
            if (stmt != null) {
                try {
                    stmt.close();
                 } catch (SQLException sqlEx) { // ignore }
                 stmt = null;
                     }
                }
          }
    }
}
}
