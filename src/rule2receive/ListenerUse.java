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

		MysqlConnector mc = MysqlConnector.getInstance();
		String sql = "INSERT INTO showdata_filetxt(fileName, store_time, t_identifier, t_time) " +
				 " VALUES(?,?,?,?)";
		try {
			PreparedStatement ps = mc.getPreparedStatement(sql);
			ps.setString(1, name);
			ps.setLong(2, store_time);
			ps.setString(3, T_identifier);
			ps.setString(4, T_time);
			ps.executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//get id
		sql = "SELECT id from showdata_filetxt where fileName = ?";
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
				String C_lon = content[4];
				String C_lat = content[5];
				//storing data into database
				MysqlConnector mc1 = MysqlConnector.getInstance();
				String sql1 = "INSERT INTO showdata_linetxt(store_time,t_identifier,t_time,c_imsi,c_tmsi,c_fcn,c_time,c_lon,c_lat,file_id) " +
							 " VALUES(?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps1 = mc1.getPreparedStatement(sql1);
				ps1.setLong(1, store_time);
				ps1.setString(2, T_identifier);
				ps1.setString(3, T_time);
				ps1.setString(4, C_imsi);
				ps1.setString(5, C_tmsi);
				ps1.setString(6, C_fcn);
				ps1.setString(7, C_time);
				ps1.setString(8, C_lon);
				ps1.setString(9, C_lat);
				ps1.setInt(10, fileid);
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
}

