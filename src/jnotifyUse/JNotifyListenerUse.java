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

import net.contentobjects.jnotify.JNotifyListener;


public class JNotifyListenerUse implements JNotifyListener{
	 
    @Override
    public void fileCreated(int wd, String rootPath, String name){
        System.err.println("create: --->" + wd + "--->" + rootPath + "--->" + name);
        long store_time = System.currentTimeMillis();
		//get title
		String title[] = name.split("_");
		if(title.length!=8) {
			return;
		}
		String T_mac = title[0];
		String T_ip = title[1].split("\\[")[1].split("\\]")[0];
		String T_date = title[2] + "-" + title[3] + "-" + title[4] + " " +
						title[5] + ":" + title[6] + ":" + title[7].split("\\.")[0];		
        MysqlConnector1 mc = MysqlConnector1.getInstance();
        String sql = "INSERT INTO showdata_filepns(fileName,store_time,t_mac,t_ip,t_date) " +
				 " VALUES(?,?,?,?,?)";
        PreparedStatement ps;
		try {
			ps = mc.getPreparedStatement(sql);
			ps.setString(1, name);
			ps.setLong(2, store_time);
			ps.setString(3, T_mac);
			ps.setString(4, T_ip);
			ps.setString(5, T_date);
			ps.executeUpdate();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
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
        //get id
  		sql = "SELECT id from showdata_filepns where fileName = ?";
  		int fileid = 1;
  		try {
  			ps = mc.getPreparedStatement(sql);
  			ps.setString(1, name);
  			ResultSet rs = ps.executeQuery();
  			rs.next();
  			fileid = rs.getInt("id");
  		} catch (SQLException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
		//storing
		try {
			while((lineTxt = bufferedReader.readLine()) != null){
				//get content
				String content[] = lineTxt.trim().split(",");
				String C_mac;
				String C_imei;
				String C_imsi;
				String C_date;
				String C_rsrp;
				String C_lon;
				String C_lat;
				String C_isp;
				if(content.length==7) {
					C_mac = "0";
					C_imei = content[0];
					C_imsi = content[1];
					C_date = content[2];
					C_rsrp = content[3];
					C_lon = content[4];
					C_lat = content[5];
					C_isp = content[6];
				}
				else if(content.length==8) {
					C_mac = content[0];
					C_imei = content[1];
					C_imsi = content[2];
					C_date = content[3];
					C_rsrp = content[4];
					C_lon = content[5];
					C_lat = content[6];
					C_isp = content[7];
				}
				else {
					bufferedReader.close();
					read.close();
					return;
				}
				
				//storing data into database
				MysqlConnector1 mc1 = MysqlConnector1.getInstance();
				sql = "INSERT INTO showdata_linepns(store_time,t_mac,t_ip,t_date,c_mac,c_imei,c_imsi,c_date,c_rsrp,c_lon,c_lat,c_isp,file_id) " +
							 " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				ps = mc1.getPreparedStatement(sql);
				ps.setLong(1, store_time);
				ps.setString(2, T_mac);
				ps.setString(3, T_ip);
				ps.setString(4, T_date);
				ps.setString(5, C_mac);
				ps.setString(6, C_imei);
				ps.setString(7, C_imsi);
				ps.setString(8, C_date);
				ps.setString(9, C_rsrp);
				ps.setString(10, C_lon);
				ps.setString(11, C_lat);
				ps.setString(12, C_isp);
				ps.setInt(13, fileid);
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
        //System.err.println("modified: --->" + wd + "--->" + rootPath + "--->" + name);
    }
 
    @Override
    public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
        System.err.println("rename: --->" + wd + "--->" + rootPath + "--->" + oldName + "--->" + newName);
    }
}

