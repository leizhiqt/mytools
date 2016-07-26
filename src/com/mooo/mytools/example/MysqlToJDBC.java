package com.mooo.mytools.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!
public class MysqlToJDBC {
    public static void main(String[] args) {

           Statement stmt = null;
           ResultSet rs = null;

        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            Connection connxpcShared = DriverManager.getConnection("jdbc:mysql://localhost/xpcwShared","xpcwuser","click89");
            Connection connBranch = DriverManager.getConnection("jdbc:mysql://localhost/xpcwBranch","xpcwuser","click89");
            stmt = connBranch.createStatement();

            int branchID=15;
            String Category="PE";

            //List Booking Request (RequestNo==1)
            String sql = "SELECT BookDetail.ID,BookDetail.RefJobID,BookDetail.RequestNo,BookDetail.JobTypeID,RefJob.JobDate,vendor.ShortName,BookDetail.DischargePortID,BookDetail.IsConfirmed FROM BookDetail,RefJob,xpcwShared.Partner AS vendor WHERE";
            sql+=" BookDetail.VendorID=vendor.ID";
            sql+=" AND BookDetail.RefJobID=RefJob.ID";
            sql+=" AND BookDetail.BranchID="+ branchID;
            sql+=" AND RefJob.BranchID="+ branchID;
            sql+=" AND RefJob.Category='"+ Category +"'";
            //sql+=" AND RefJob.SelectedBookNo = BookDetail.RequestNo";
            sql+=" AND BookDetail.RequestNo > 0 ";
            //String sql = "SELECT RequestNo,RequestNo FROM BookDetail WHERE ID="+ id +" AND BranchID="+branchID;
            //String sql = "INSERT INTO user(name,password) VALUES('root','admin')";
            //stmt.execute(sql);
            rs = stmt.executeQuery(sql);
           System.out.println("List Booking Request");
           while(rs.next()) {
            System.out.println("ID:"+rs.getString("BookDetail.ID")+"\tRefJobID:"+rs.getString("BookDetail.RefJobID")+"\tRequestNo:"+rs.getString("BookDetail.RequestNo")+"\tJobTypeID:"+rs.getString("BookDetail.JobTypeID")+"\tJobDate:"+rs.getString("RefJob.JobDate")+"\tPartner:"+rs.getString("vendor.ShortName")+"\tDischargePortID:"+rs.getString("BookDetail.DischargePortID")+"\tIsConfirmed:"+rs.getString("BookDetail.IsConfirmed"));
               }
              System.out.println("select BookDetail table");
              //select BookDetail
              int key=6672;
              sql="SELECT * FROM BookDetail WHERE";
              sql+=" ID="+key;
              sql+=" AND BranchID="+branchID;
              rs = stmt.executeQuery(sql);
              String refJobID="";
              int bookDetailID=0;
          while(rs.next()) {
            System.out.println("RequestNo:"+rs.getString("RequestNo") + "\tRefJobID:" + rs.getString("RefJobID"));
            refJobID=rs.getString("RefJobID");
            bookDetailID=rs.getInt("ID");
               }
              System.out.println("select RefJob table");
              //select RefJob
              sql="SELECT * FROM RefJob WHERE";
              sql+=" ID='"+refJobID+"'";
              sql+=" AND BranchID="+branchID;
              rs = stmt.executeQuery(sql);
              int tmpInt=0;
            while(rs.next()) {
            System.out.println("SelectedBookNo:"+rs.getInt("SelectedBookNo"));
            tmpInt=rs.getInt("SelectedBookNo");
               }
             System.out.println("exec start..........");
             //exec delete
             if(tmpInt==1){
                //check table ClientJob and SeaHouseBill if have data link
                 sql="SELECT COUNT(ClientJob.ID) AS flat FROM ClientJob,SeaHouseBill WHERE";
                 sql+=" ClientJob.ID=SeaHouseBill.ClientJobID";
                 sql+=" AND ClientJob.RefJobID='"+refJobID+"'";
                 sql+=" AND SeaHouseBill.BranchID="+branchID;
                 sql+=" AND ClientJob.BranchID="+branchID;
                 //rs = stmt.executeQuery(sql);
                 stmt.execute(sql);
                 rs = stmt.getResultSet();
                 rs.first();
                 //rs.next();
                 System.out.println("Check conut:"+rs.getInt("flat"));

                 if(rs.getInt("flat")<=0){
                  try{//update RefJob table
                   sql="UPDATE RefJob SET SelectedBookNo=0 WHERE ID='"+refJobID+"'";
                   stmt.execute(sql);
                   System.out.println("update RefJob table");
                     }catch (SQLException ex) {
                       try{
                          sql="UPDATE RefJob SET SelectedBookNo=1 WHERE ID='"+refJobID+"'";
                          stmt.execute(sql);
                         }catch (SQLException sex) {
                         System.out.println("SQLException: " + sex.getMessage());
                         System.out.println("SQLState: " + sex.getSQLState());
                         System.out.println("VendorError: " + sex.getErrorCode());
                          }
                       }
                  try{//must update ClientJob table as well
                    //back delete table
                    try{
                       sql="DROP TABLE IF EXISTS `ClientJobTmp`";
                       stmt.execute(sql);

                       sql="CREATE TABLE ClientJobTmp AS SELECT * FROM ClientJob WHERE";
                       sql+=" RefJobID='"+refJobID+"'";
                       stmt.execute(sql);
                       }catch (SQLException sex) {
                         System.out.println("SQLException: " + sex.getMessage());
                         System.out.println("SQLState: " + sex.getSQLState());
                         System.out.println("VendorError: " + sex.getErrorCode());
                       }
                    //update ClientJob table as well
                   sql="UPDATE ClientJob SET RefJobID='NA',IsAssigned='N' WHERE";
                   sql+=" RefJobID='"+refJobID+"'";
                   stmt.execute(sql);
                   System.out.println("update ClientJob table as well");
                    } catch (SQLException ex) {
                      try{
                          sql="UPDATE RefJob SET SelectedBookNo=1 WHERE ID='"+refJobID+"'";
                          stmt.execute(sql);
                         }catch (SQLException sex) {
                         System.out.println("SQLException: " + sex.getMessage());
                         System.out.println("SQLState: " + sex.getSQLState());
                         System.out.println("VendorError: " + sex.getErrorCode());
                         }
                    }
                try{//Delete SeaBookInfo table
                   try{
                       sql="DROP TABLE IF EXISTS `ClientJobTmp`";
                       stmt.execute(sql);

                       sql="CREATE TABLE SeaBookInfoTmp AS SELECT * FROM SeaBookInfo WHERE";
                       sql+=" BookDetailID="+bookDetailID;
                       sql+=" AND BranchID="+branchID;
                       stmt.execute(sql);
                       }catch (SQLException sex) {
                         System.out.println("SQLException: " + sex.getMessage());
                         System.out.println("SQLState: " + sex.getSQLState());
                         System.out.println("VendorError: " + sex.getErrorCode());
                       }
                   sql="DELETE FROM SeaBookInfo WHERE";
                   sql+=" BookDetailID="+bookDetailID;
                   sql+=" AND BranchID="+branchID;
                   stmt.execute(sql);
                   System.out.println("Delete SeaBookInfo table");
                   }catch (SQLException ex) {
                      try{
                          sql="UPDATE RefJob SET SelectedBookNo=1 WHERE ID='"+refJobID+"'";
                          stmt.execute(sql);

                          sql="UPDATE ClientJob SET RefJobID='"+ refJobID +"',";
                          sql+=" IsAssigned='Y' WHERE";
                          sql+=" ID IN (SELECT ID FORM ClientJobTmp)";
                          stmt.execute(sql);
                         }catch (SQLException sex) {
                         System.out.println("SQLException: " + sex.getMessage());
                         System.out.println("SQLState: " + sex.getSQLState());
                         System.out.println("VendorError: " + sex.getErrorCode());
                         }
                    }
                  //Delete BookDetail table
                   /*sql="DELETE FROM BookDetail WHERE";
                   sql+=" RefJobID='"+refJobID+"'";
                   sql+=" AND BranchID="+branchID;
                   stmt.execute(sql);
                   System.out.println("Delete BookDetail table");*/
                  //Delete RefJobFreight and Freight prepair or check 
                    sql="SELECT COUNT(ID) AS flat FROM BookDetail WHERE";
                    sql+=" RefJobID='"+refJobID+"'";
                    sql+=" AND BranchID="+branchID;
                    stmt.execute(sql);
                    rs = stmt.getResultSet();
                    rs.first();
                    if(rs.getInt("flat")==0){//Delete RefJobFreight and Freight of start
                      sql="SELECT * FROM RefJobFreight WHERE";
                      sql+=" RefJobID='"+refJobID+"'";
                      sql+=" AND BranchID="+branchID;
                      rs = stmt.executeQuery(sql);
                  try{
                       sql="DROP TABLE IF EXISTS `RefJobFreightTmp`";
                       stmt.execute(sql);
                       sql="DROP TABLE IF EXISTS `FreightTmp`";
                       stmt.execute(sql);

                       sql="CREATE TABLE RefJobFreightTmp AS SELECT * FROM RefJobFreight WHERE";
                       sql+=" RefJobID='"+refJobID+"'";
                       sql+=" AND BranchID="+branchID;
                       stmt.execute(sql);
                       sql="CREATE TABLE FreightTmp AS SELECT * FROM Freight WHERE ID IN";
                       sql+=" (SELECT FreightID FROM RefJobFreight WHERE";
                       sql+=" RefJobID='"+refJobID+"'";
                       sql+=" AND BranchID="+branchID+")";
                       stmt.execute(sql);
                      while(rs.next()){
                         //Delete RefJobFreight
                         sql="DELETE FROM RefJobFreight WHERE";
                         sql+=" FreightID="+rs.getInt("FreightID");
                         sql+=" AND BranchID="+branchID;
                         stmt.execute(sql);
                         System.out.println("Delete RefJobFreight");
                          //Delete Freight
                         sql="DELETE FROM Freight WHERE";
                         sql+=" ID="+rs.getInt("FreightID");
                         sql+=" AND BranchID="+branchID;
                         stmt.execute(sql);
                         System.out.println("Delete Freight");
                         }
                       } catch (SQLException ex) {

                        }
                   //we must delete RefJobContainer as well before deleting RefJob
                     sql="DELETE FROM RefJobContainer WHERE";
                     sql+=" RefJobID='"+refJobID+"'";
                     sql+=" AND BranchID="+branchID;
                     stmt.execute(sql);
                     System.out.println("delete RefJobContainer");
                  //must update ClientJob table as well
                     /*sql="UPDATE ClientJob SET RefJobID='NA',IsAssigned='N' WHERE";
                     sql+=" RefJobID='"+refJobID+"'";
                     sql+=" AND BranchID="+branchID;
                     stmt.execute(sql);
                     *********/
                  //Delete RefJob table
                     sql="DELETE FROM RefJob WHERE";
                     sql+=" RefJobID='"+refJobID+"'";
                     sql+=" AND BranchID="+branchID;
                     stmt.execute(sql);
                     System.out.println("Delete RefJob table");
                   }//END rs.getInt("flat")==0
                 }//END rs.getInt("flat")<=0
                 
             }//tmpInt==1
            //if (stmt.execute(sql)) {
               //rs = stmt.getResultSet();
                 //}

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
