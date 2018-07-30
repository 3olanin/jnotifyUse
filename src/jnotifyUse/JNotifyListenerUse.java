package jnotifyUse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.contentobjects.jnotify.JNotifyListener;


public class JNotifyListenerUse implements JNotifyListener{
	 
    @Override
    public void fileCreated(int wd, String rootPath, String name){
        System.err.println("create: --->" + wd + "--->" + rootPath + "--->" + name);
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        File file=new File(rootPath+File.separator+name);
		InputStreamReader read = null;
		try {
			read = new InputStreamReader(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;

		//get title
		String title[] = name.split("_");
		String T_mac = title[0];
//		String T_ip = title[1].split("\\[")[1].split("\\]")[0];
//		
//		String T_date = title[2] + "-" + title[3] + "-" + title[4] + " " +
//						title[5] + ":" + title[6] + ":" + title[7].split("\\.")[0];

		System.out.println("test1");
		String T_ip = title[1].split("\\[")[1].split("\\]")[0];
		System.out.println("test2");
		String T_date = title[2] + "-" + title[3] + "-" + title[4] + " " +
						title[5] + ":" + title[6] + ":" + title[7].split("\\.")[0];		
		System.out.println("test3");
		System.out.println("T_ip="+T_ip);
		System.out.println("T_date="+T_date);
		//storing
		try {
			while((lineTxt = bufferedReader.readLine()) != null){
				//get content
				System.out.println("loop");
				System.out.println("content="+lineTxt);
				String content[] = lineTxt.trim().split(",");
				String C_mac = content[0];
				String C_imei = content[1];
				String C_imsi = content[2];
				String C_date = content[3];
				String C_rsrp = content[4];
				String C_lon = content[5];
				String C_lat = content[6];
				int C_isp = Integer.valueOf(content[7]);
				
				
				
				//storing data into database
				MysqlConnector mc = MysqlConnector.getInstance();
				String sql = "INSERT INTO type_pns(T_mac,T_ip,T_date,C_mac,C_imei,C_imsi,C_date,C_rsrp,C_lon,C_lat,C_isp) " +
							 " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps = mc.getPreparedStatement(sql);
				ps.setString(1, T_mac);
				ps.setString(2, T_ip);
				ps.setString(3, T_date);
				ps.setString(4, C_mac);
				ps.setString(5, C_imei);
				ps.setString(6, C_imsi);
				ps.setString(7, C_date);
				ps.setString(8, C_rsrp);
				ps.setString(9, C_lon);
				ps.setString(10, C_lat);
				ps.setInt(11, C_isp);
				ps.executeUpdate();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
 
    @Override
    public void fileDeleted(int wd, String rootPath, String name) {
        System.err.println("delete: --->" + wd + "--->" + rootPath + "--->" + name);
    }
 
    @Override
    public void fileModified(int wd, String rootPath, String name) {
        System.err.println("modified: --->" + wd + "--->" + rootPath + "--->" + name);
    }
 
    @Override
    public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
        System.err.println("rename: --->" + wd + "--->" + rootPath + "--->" + oldName + "--->" + newName);
    }
}

