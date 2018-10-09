package jnotifyUse;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;


public class JNotifyUse {

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		MysqlConnector1 mc = MysqlConnector1.getInstance();
		mc.connectMysql();
        System.err.println(System.getProperty("java.library.path"));
        System.err.println("开始监听目录下内容......");
        try {
            JNotifyUse.watch();
        } catch (Exception e) {
            e.printStackTrace();
        }

	}
	
    /**
     * JNotify监控方法
     * @throws JNotifyException
     * @throws InterruptedException
     */
    private static void watch() throws JNotifyException, InterruptedException {
 
        //要监控哪个目录
        //String path = "C:/Users/Lushaobin/Desktop";
    	String path = "/home/ftphome/ftpuser";
    	
        //监控用户的操作，增，删，改，重命名
        int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED ;
 
        //是否监控子目录
        boolean subTree = false;
 
        //开始监控
        int watchID = JNotify.addWatch(path, mask, subTree, new JNotifyListenerUse());
 
        
        while(true) {
        	try {
        		MysqlConnector1 mc = MysqlConnector1.getInstance();
        		String sql = "SELECT CURDATE() as date";
        		try {
        			PreparedStatement ps = mc.getPreparedStatement(sql);
        			ResultSet rs = ps.executeQuery();
        			rs.next();
        			System.out.println(rs.getString("date"));
        		} catch (SQLException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
        		Thread.sleep(1000 *  60 * 60);
        		
        	}catch (InterruptedException e) {
        		
        	}
        }



        //睡一会，看看效果
//        Thread.sleep(1000 * 60 * 3);
 
        //停止监控
//        boolean res = JNotify.removeWatch(watchID);
// 
//        if (res) {
//            System.err.println("已停止监听");
//        }
//        System.err.println(path);
    }
 
    
    
    
    
    
}
