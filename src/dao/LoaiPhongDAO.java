package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.LoaiPhong;

public class LoaiPhongDAO {
	public static ArrayList<LoaiPhong> getAllLoaiPhong(){
		ArrayList<LoaiPhong> ds = new ArrayList<LoaiPhong>();
		String sql = "SELECT * FROM LoaiPhong WHERE visible = 1";
		try {
			Connection con = ConnectDB.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				ds.add(new LoaiPhong(rs.getString("maLoaiPhong") , rs.getString("tenLoaiPhong")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}
	public static boolean add(LoaiPhong lp) {
		String sql = "INSERT into LoaiPhong(maLoaiPhong, tenLoaiPhong) VALUES(?, ?)";
		try {
			Connection con = ConnectDB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, lp.getMaLoaiPhong());
			ps.setString(2, lp.getTenLoaiPhong());
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean update(String ma, String tenMoi) {
        String sql = "UPDATE LoaiPhong SET tenLoaiPhong = ? WHERE maLoaiPhong = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tenMoi);
            ps.setString(2, ma);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
	public static String getNextID() {
		String sql = "SELECT TOP 1 maLoaiPhong FROM LoaiPhong ORDER BY maLoaiPhong DESC";
		try {
			Connection con = ConnectDB.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				String lastID = rs.getString("maLoaiPhong");
				int num = Integer.parseInt(lastID.substring(2)) + 1;
				return String.format("LP%02d", num);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "LP01";
	}
	
	public static ArrayList<LoaiPhong> getLoaiPhongAn(){
		ArrayList<LoaiPhong> ds = new ArrayList<LoaiPhong>();
		String sql = "SELECT * FROM LoaiPhong WHERE visible = 0";
		try {
			Connection con = ConnectDB.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				ds.add(new LoaiPhong(rs.getString("maLoaiPhong") , rs.getString("tenLoaiPhong")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}
	
	public static boolean hide(String maP) {
        String sql = "UPDATE LoaiPhong SET visible = 0 WHERE maLoaiPhong = ?";

        try{
        	Connection con = ConnectDB.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maP);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean restore(String maP) {
        String sql = "UPDATE LoaiPhong SET visible = 1 WHERE maLoaiPhong = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maP);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
