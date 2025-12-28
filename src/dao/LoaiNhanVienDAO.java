package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import connectDB.ConnectDB;
import entity.LoaiNhanVien;

public class LoaiNhanVienDAO {
	public static ArrayList<LoaiNhanVien> getAllLoaiNV(){
		ArrayList<LoaiNhanVien> ds = new ArrayList<LoaiNhanVien>();
		String sql = "SELECT * FROM LoaiNhanVien WHERE visible = 1"; 
		try {
			Connection con = ConnectDB.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				ds.add(new LoaiNhanVien(rs.getString("maLoaiNV"), rs.getString("tenLoaiNV")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ds;
	}
	
	public static boolean add(LoaiNhanVien nv) {
		String sql = "INSERT INTO LoaiNhanVien(maLoaiNV, tenLoaiNV) VALUES(?, ?)";
		try {
			Connection con = ConnectDB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, nv.getMaLoaiNV());
			ps.setString(2, nv.getTenLoaiNV());
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean update(String ma, String tenMoi) {
        String sql = "UPDATE LoaiNhanVien SET tenLoaiNV = ? WHERE maLoaiNV = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tenMoi);
            ps.setString(2, ma);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	public static boolean hide(String ma) {
		String sql = "UPDATE LoaiNhanVien SET visible = 0 WHERE maLoaiNV = ?";
		try {
			Connection con = ConnectDB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, ma);
			ps.executeUpdate();
			return true; 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean restore(String ma) {
		String sql = "UPDATE LoaiNhanVien SET visible = 1 WHERE maLoaiNV = ?";
		try {
			Connection con = ConnectDB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, ma);
			ps.executeUpdate();
			return true; 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static ArrayList<LoaiNhanVien> getHiddenLoaiNV() {
	    ArrayList<LoaiNhanVien> ds = new ArrayList<>();
	    String sql = "SELECT * FROM LoaiNhanVien WHERE visible = 0";
	    try {
	    	Connection con = ConnectDB.getConnection();
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
	            ds.add(new LoaiNhanVien(rs.getString("maLoaiNV"), rs.getString("tenLoaiNV")));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}

	public static String getNextID() {
		String sql = "SELECT TOP 1 maLoaiNV FROM LoaiNhanVien ORDER BY maLoaiNV DESC";
		try {
			Connection con = ConnectDB.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				String lastID = rs.getString("maLoaiNV");
				int num = Integer.parseInt(lastID.substring(3)) + 1;
				return String.format("LNV%02d", num);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "LNV01";
	}
}