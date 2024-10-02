package Base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBContext {
    String instance = "";
    String serverName = "LAPTOP-07FP2NPP";
    String portNumber = "1433";
    String dbName = "master";
    String userID = "sa";
    String password = "123456";
    public Connection getConnection() throws ClassNotFoundException, SQLException{
        
        String url = "jdbc:sqlserver://" + serverName + ":" + portNumber + "\\" + instance + ";databaseName=" + dbName;
        if (instance == null || instance.trim().isEmpty()){
         url = "jdbc:sqlserver://" + serverName + ":" + portNumber +";databaseName=" + dbName;
         }
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      // Class.forName(): Load class Driver tại thời điểm chạy.
      //"com.microsoft.sqlserver.jdbc.SQLServerDriver" là địa chỉ của Driver.
      return DriverManager.getConnection(url, userID, password);      
    }
    
    public static void main(String[] args){    
    DBContext db = new DBContext();
        try {
            Connection connect = db.getConnection();
            String sql = "select * from dbo.Fee";
            Statement statement = connect.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                System.out.println(rs.getInt(1));
                System.out.println(rs.getString(2));
            }
            System.out.print("ket noi thanh cong");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}