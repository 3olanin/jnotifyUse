package jnotifyUse;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;


public class JNotifyUse {

	public static void main(String[] args) {
		//test
//		File file=new File(System.getProperty("user.dir")+File.separator+"testinput.txt");
//		InputStreamReader read = null;
//		try {
//			read = new InputStreamReader(new FileInputStream(file));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String lineTxt = "";
//		BufferedReader bufferedReader = new BufferedReader(read);
//			try {
//				while((lineTxt = bufferedReader.readLine()) != null){
//					//get content
//					System.out.println(lineTxt);
//					
//				}
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		try {
//			bufferedReader.close();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		// TODO Auto-generated method stub
		MysqlConnector mc = MysqlConnector.getInstance();
		mc.connectMysql();
        System.err.println(System.getProperty("java.library.path"));
        System.err.println("��ʼ����Ŀ¼������......");
        try {
            JNotifyUse.watch();
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
        boolean subTree = true;
 
        //��ʼ���
        int watchID = JNotify.addWatch(path, mask, subTree, new JNotifyListenerUse());
 
        
        //while(true) {
        	try {
        		Thread.sleep(1000 *  60 * 3);
        	}catch (InterruptedException e) {
        		
        	}
        	
        	
        //}


        	
        boolean res = JNotify.removeWatch(watchID);
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
