package rule2receive;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.contentobjects.jnotify.JNotifyListener;


public class ListenerUse implements JNotifyListener{
	 
    @Override
    public void fileCreated(int wd, String rootPath, String name){
        System.err.println("create: --->" + wd + "--->" + rootPath + "--->" + name);
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
        long store_time = System.currentTimeMillis();
        store_time = store_time/1000;
        String name = newName;
        File file=new File(rootPath+File.separator+name);
		InputStreamReader read = null;
		try {
			read = new InputStreamReader(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//store filetxt
		String lineTxt = null;
		//get title
		String title[] = name.split("_");
		if(title.length!=2) {
			try {
				read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String T_identifier = title[0];		
		String T_time = title[1].split("\\.")[0];	
		long T_time_stamp = 0;
		try {
			T_time_stamp = dateToStampForTitle(T_time);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		int machine_type = 2;
		String default_value = "0";

		MysqlConnector mc = MysqlConnector.getInstance();
		String sql = "INSERT INTO showData_filetxt(fileName, store_time, t_identifier, t_time, t_time_stamp, machine_type, t_mac, t_ip) " +
				 " VALUES(?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = mc.getPreparedStatement(sql);
			ps.setString(1, name);
			ps.setLong(2, store_time);
			ps.setString(3, T_identifier);
			ps.setString(4, T_time);
			ps.setLong(5, T_time_stamp);
			ps.setInt(6, machine_type);
			ps.setString(7, default_value);
			ps.setString(8, default_value);
			ps.executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//get id
		sql = "SELECT id from showData_filetxt where fileName = ?";
		int fileid = 1;
		try {
			PreparedStatement ps = mc.getPreparedStatement(sql);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			rs.next();
			fileid = rs.getInt("id");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(read);
		//storing
		try {
			while((lineTxt = bufferedReader.readLine()) != null){
				//get content
				String content[] = lineTxt.trim().split("\t");
				if(content.length!=6) {
					bufferedReader.close();
					read.close();
					return;
				}
				String C_imsi = content[0];
				String C_tmsi = content[1];
				String C_fcn = content[2];
				String C_time = content[3];
				long C_time_stamp = 0;
				String C_lon = content[4];
				String C_lat = content[5];
				try {
					C_time_stamp = dateToStamp(C_time);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//storing data into database
				MysqlConnector mc1 = MysqlConnector.getInstance();
				String sql1 = "INSERT INTO showData_linetxt(store_time,t_identifier,t_time,t_time_stamp,c_imsi,c_tmsi,c_fcn,c_time,c_time_stamp,c_lon,c_lat,file_id,machine_type,t_mac,t_ip,c_mac,c_imei,c_rsrp,c_isp) " +
							 " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps1 = mc1.getPreparedStatement(sql1);
				ps1.setLong(1, store_time);
				ps1.setString(2, T_identifier);
				ps1.setString(3, T_time);
				ps1.setLong(4, T_time_stamp);
				ps1.setString(5, C_imsi);
				ps1.setString(6, C_tmsi);
				ps1.setString(7, C_fcn);
				ps1.setString(8, C_time);
				ps1.setLong(9, C_time_stamp);
				ps1.setString(10, C_lon);
				ps1.setString(11, C_lat);
				ps1.setInt(12, fileid);
				ps1.setInt(13, machine_type);
				ps1.setString(14, default_value);
				ps1.setString(15, default_value);
				ps1.setString(16, default_value);
				ps1.setString(17, default_value);
				ps1.setString(18, default_value);
				ps1.setString(19, default_value);
				ps1.executeUpdate();
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
			read.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static long dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime()/1000;
        //res = String.valueOf(ts);
        //return res;
        return ts;
    }
    public static long dateToStampForTitle(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime()/1000;
        //res = String.valueOf(ts);
        //return res;
        return ts;
    }
}

