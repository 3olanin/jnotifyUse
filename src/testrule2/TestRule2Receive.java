package testrule2;



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


public class TestRule2Receive {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//MysqlConnector mc = MysqlConnector.getInstance();
		//mc.connectMysql();
        System.err.println(System.getProperty("java.library.path"));
        System.err.println("��ʼ����Ŀ¼������......");
        try {
            TestRule2Receive.watch();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
    /**
     * JNotify��ط���
     * @throws JNotifyException
     * @throws InterruptedException
     */
    private static void watch() throws JNotifyException, InterruptedException {
 
        //Ҫ����ĸ�Ŀ¼
        //String path = "C:/Users/Lushaobin/Desktop";
    	String path = "/home/ftphome/ftpuser";
 
        //����û��Ĳ���������ɾ���ģ�������
        int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED ;
 
        //�Ƿ�����Ŀ¼
        boolean subTree = false;
 
        //��ʼ���
        int watchID = JNotify.addWatch(path, mask, subTree, new TestListenerUse());
 
        
        while(true) {
        	try {
//        		MysqlConnector mc = MysqlConnector.getInstance();
//        		String sql = "SELECT CURDATE() as date";
//        		try {
//        			PreparedStatement ps = mc.getPreparedStatement(sql);
//        			ResultSet rs = ps.executeQuery();
//        			rs.next();
//        			System.out.println(rs.getString("date"));
//        		} catch (SQLException e1) {
//        			// TODO Auto-generated catch block
//        			e1.printStackTrace();
//        		}
        		Thread.sleep(1000 *  60 * 60);
        		
        	}catch (InterruptedException e) {
        		
        	}
        }


        	
        //boolean res = JNotify.removeWatch(watchID);
        //˯һ�ᣬ����Ч��
//        Thread.sleep(1000 * 60 * 3);
 
        //ֹͣ���
//        boolean res = JNotify.removeWatch(watchID);
// 
//        if (res) {
//            System.err.println("��ֹͣ����");
//        }
//        System.err.println(path);
    }
 
    
    
    
    
    
}