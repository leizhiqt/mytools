package com.mooo.mytools.example;

import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.db.pool.DbConnectionManager;
import com.mooo.mytools.sockt.Server;

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!
public class ProcJdbc {
	private static Log log = LogFactory.getLog(ProcJdbc.class);



public void start(){
		try {
            if (log.isDebugEnabled()) log.debug("are u here");
			Connection con = null;
			CallableStatement cstmt = null;
			ResultSet rs = null;
			try {

				con = DbConnectionManager.getConnection();
            if (log.isDebugEnabled()) log.debug("con="+con);
				con.setCatalog("xpcBranch");
				cstmt = con.prepareCall("{call xpcBranch.JobReport()}");
				
				cstmt.execute();
				while (cstmt.getMoreResults()) {
					rs = cstmt.getResultSet();
				while (rs.next()) {
					if (log.isDebugEnabled()) log.debug("GroupID=" + rs.getString("jn.GroupID")
							+"jn.BranchID=" + rs.getString("jn.BranchID")
							+"jj.DCType=" + rs.getString("jj.DCType")
							+"SUM(Amount)=" + rs.getString("SUM(Amount)")
							+"cu.ISOCode=" + rs.getString("cu.ISOCode")
							+"jn.Date=" + rs.getString("jn.Date")

							);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
            if (log.isDebugEnabled()) log.debug("Exception="+e.getMessage());
			} finally {
				try {
					cstmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			System.err.println("Error reading Yazd System properties in SystemProperty.loadProps() " + e);
			e.printStackTrace();
		} finally {
			try {

			} catch (Exception e) {

			}
		}
}
    public static void main(String[] args) {

           String driver = "org.gjt.mm.mysql.Driver"; 
           String url="jdbc:mysql://localhost/xpcwBranch?useUnicode=true&amp;characterEncoding=utf8";
           Statement stmt = null;
           ResultSet rs = null;

        try {
            Class.forName(driver).newInstance();
            Connection connxpcBranch = DriverManager.getConnection(url,"xpcwuser","click89");

            CallableStatement proc = connxpcBranch.prepareCall("{CALL list_ClientJob('root')}");
            //proc.registerOutParameter(String parameterName, int sqlType, String typeName);
            //String sql = "SELECT ";
            //sql+=" BookDetail.VendorID=vendor.ID";
            //String sql = "CALL test.userlist(@name)";

            rs = proc.executeQuery();
           System.out.println("CALL xpcwBranch.proc");
           while(rs.next()) {
            System.out.println("ID:"+rs.getString("ID")+" RefJobID:"+rs.getString("RefJobID"));
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
