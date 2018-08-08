package rule2receive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlConnector {
	
	private static MysqlConnector mc;
    //声明Connection对象
    private  Connection con;
    //驱动程序名
    private String driver = "com.mysql.cj.jdbc.Driver";
    //URL指向要访问的数据库名mydata
    private String url = "jdbc:mysql://localhost:3306/data_receive?serverTimezone=GMT%2B8&autoReconnect=true";
    //MySQL配置时的用户名
    private String user = "root";
    //MySQL配置时的密码
    private String password = "";
    //private String password = "e6jSWVLNyAg0";
    
    private MysqlConnector (){
		try {
			//加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
		}catch(ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");   
            e.printStackTrace();   
            } catch (SQLException e1) {
			//数据库连接异常
			e1.printStackTrace();
		}
    } 
    
    public static synchronized MysqlConnector getInstance() {  
        if (mc == null) {  
            mc = new MysqlConnector();  
        }  
        return mc;
    }
    
	public void connectMysql() {
		try {
			if(con.isClosed()) {
				//加载驱动程序
	            Class.forName(driver);
	            //1.getConnection()方法，连接MySQL数据库！！
	            con = DriverManager.getConnection(url,user,password);
	            if(!con.isClosed())
	                System.out.println("Succeeded connecting to the Database!");
			}
		}catch(ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");   
            e.printStackTrace();   
            } catch (SQLException e1) {
			//数据库连接异常
			e1.printStackTrace();
		}

	}
	
	public void  disconnectMysql() {
		try {
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
    	return con.prepareStatement(sql);
    }
}
