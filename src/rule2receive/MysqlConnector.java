package rule2receive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlConnector {
	
	private static MysqlConnector mc;
    //����Connection����
    private  Connection con;
    //����������
    private String driver = "com.mysql.cj.jdbc.Driver";
    //URLָ��Ҫ���ʵ����ݿ���mydata
    private String url = "jdbc:mysql://localhost:3306/data_receive?serverTimezone=GMT%2B8&autoReconnect=true";
    //MySQL����ʱ���û���
    private String user = "root";
    //MySQL����ʱ������
    private String password = "";
    //private String password = "e6jSWVLNyAg0";
    
    private MysqlConnector (){
		try {
			//������������
            Class.forName(driver);
            //1.getConnection()����������MySQL���ݿ⣡��
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
		}catch(ClassNotFoundException e) {
            //���ݿ��������쳣����
            System.out.println("Sorry,can`t find the Driver!");   
            e.printStackTrace();   
            } catch (SQLException e1) {
			//���ݿ������쳣
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
				//������������
	            Class.forName(driver);
	            //1.getConnection()����������MySQL���ݿ⣡��
	            con = DriverManager.getConnection(url,user,password);
	            if(!con.isClosed())
	                System.out.println("Succeeded connecting to the Database!");
			}
		}catch(ClassNotFoundException e) {
            //���ݿ��������쳣����
            System.out.println("Sorry,can`t find the Driver!");   
            e.printStackTrace();   
            } catch (SQLException e1) {
			//���ݿ������쳣
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
