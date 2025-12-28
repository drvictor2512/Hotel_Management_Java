package dao;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import connectDB.ConnectDB;
import entity.ChiTietDonDatPhong;
import entity.DonDatPhong;
import entity.LoaiPhong;
import entity.Phong;

public class DoiPhongDAO {
	public static ArrayList<Object[]> layTatCaDonDatPhong() {
	    ArrayList<Object[]> ds = new ArrayList<>();
	    String sql = """
	        SELECT dp.maDatPhong, dp.maKH, kh.hoTen, ct.maPhong, p.tenPhong, lp.tenLoaiPhong, 
	               STRING_AGG(dv.tenDV, ', ') AS danhSachDV,
	               dp.maNhanVien, ct.ngayDat, ct.ngayNhan, ct.ngayTraDuKien, ct.tienCoc, 
	               dp.maKM, ISNULL(km.mucGiamGia, 0) AS mucGiamGia, ct.trangThai
	        FROM DonDatPhong dp
	        JOIN ChiTietDonDatPhong ct ON dp.maDatPhong = ct.maDatPhong
	        JOIN Phong p ON ct.maPhong = p.maPhong
	        JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
	        JOIN KhachHang kh ON dp.maKH = kh.maKH
	        LEFT JOIN KhuyenMai km ON dp.maKM = km.maKM
	        LEFT JOIN ChiTietDichVu ctdv ON dp.maDatPhong = ctdv.maDatPhong
	        LEFT JOIN DichVu dv ON ctdv.maDV = dv.maDV
	        GROUP BY 
	            dp.maDatPhong, dp.maKH, kh.hoTen, ct.maPhong, 
	            p.tenPhong, lp.tenLoaiPhong, dp.maNhanVien, 
	            ct.ngayDat, ct.ngayNhan, ct.ngayTraDuKien, 
	            ct.tienCoc, dp.maKM, km.mucGiamGia, ct.trangThai
	        ORDER BY ct.ngayNhan DESC
	    """;
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            ds.add(new Object[]{
	                rs.getString("maDatPhong"),      
	                rs.getString("maKH"),           
	                rs.getString("hoTen"),           
	                rs.getString("maPhong"),         
	                rs.getString("tenPhong"),        
	                rs.getString("tenLoaiPhong"),    
	                rs.getString("maNhanVien"),      
	                rs.getDate("ngayDat"),          
	                rs.getDate("ngayNhan"),        
	                rs.getDate("ngayTraDuKien"),     
	                rs.getDouble("tienCoc"),         
	                rs.getString("mucGiamGia"),      
	                rs.getString("danhSachDV"),      
	                rs.getString("trangThai")        
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}
	
	
	public static boolean capNhatPhongTrongCTDP(String maDP, String maPhongCu, String maPhongMoi, double tienCocMoi) {
	    // Cập nhật cả mã phòng và tiền cọc
	    String sql = "UPDATE ChiTietDonDatPhong SET maPhong = ?, tienCoc = ? WHERE maDatPhong = ? AND maPhong = ?";
	    
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        
	        ps.setString(1, maPhongMoi);   // Phòng mới
	        ps.setDouble(2, tienCocMoi);   // Tiền cọc mới 
	        ps.setString(3, maDP);         // Mã đơn đặt phòng
	        ps.setString(4, maPhongCu);    // Phòng cũ
	        
	        int rowsAffected = ps.executeUpdate();
	        
	        if (rowsAffected == 0) {
	            System.out.println("[DEBUG] capNhatPhongTrongCTDP: Không có dòng nào được cập nhật. "
	                + "Kiểm tra lại maDP=" + maDP + ", maPhongCu=" + maPhongCu);
	        }
	        
	        return rowsAffected > 0;
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static void capNhatTinhTrangPhong(String maPhong, String tinhTrangMoi) {
	    String sql = "UPDATE Phong SET trangThai = ? WHERE maPhong = ?";
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setString(1, tinhTrangMoi);
	        stmt.setString(2, maPhong);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static ArrayList<Phong> getDSPhong() {
		String sql = """
			SELECT p.maPhong, p.tenPhong, p.tang, p.trangThai, p.donGia, p.maNhanVien, p.maLoaiPhong, lp.tenLoaiPhong
			FROM Phong p
			JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
			WHERE p.visible = 1
	    """;
        ArrayList<Phong> dsPhong = new ArrayList<Phong>();
        
        try {
			Connection con = ConnectDB.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
					String maP = rs.getString("maPhong");
					String maLP = rs.getString("maLoaiPhong");
					String loaiP = rs.getString("tenLoaiPhong");
					String tenP = rs.getString("tenPhong");
					int tangP = rs.getInt("tang");
					String ttP = rs.getString("trangThai");
					double giaP = rs.getDouble("donGia");
					String maNV = rs.getString("maNhanVien");
					
					LoaiPhong lp = new LoaiPhong(maLP, loaiP);
	                Phong p = new Phong(maP, tenP, tangP, ttP, giaP, lp, maNV);
	                dsPhong.add(p);
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách phòng");
		}
		return dsPhong;
    }
	
	public static Phong getMaP(String ma) {
		String sql = """
				SELECT p.maPhong, p.tenPhong, p.tang, p.trangThai, p.donGia, p.maNhanVien, p.maLoaiPhong, lp.tenLoaiPhong
				FROM Phong p
				JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
				WHERE p.maPhong = ? AND p.visible = 1
		    """;
		try {
			Connection con = ConnectDB.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, ma);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String maP = rs.getString("maPhong");
				String maLP = rs.getString("maLoaiPhong");
				String loaiP = rs.getString("tenLoaiPhong");
				String tenP = rs.getString("tenPhong");
				int tangP = rs.getInt("tang");
				String ttP = rs.getString("trangThai");
				double giaP = rs.getDouble("donGia");
				String maNV = rs.getString("maNhanVien");
				LoaiPhong lp = new LoaiPhong(maLP, loaiP);
                Phong p = new Phong(maP, tenP, tangP, ttP, giaP, lp, maNV);
                return p;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Object[]> layPhongTrongTheoNgay(Date ngayNhan, Date ngayTra){
	    ArrayList<Object[]> ds = new ArrayList<Object[]>();
	    String sql = """
	        SELECT p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.donGia
	        FROM Phong p
	        JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
	        WHERE p.maPhong NOT IN (
	            SELECT maPhong
	            FROM ChiTietDonDatPhong
	            WHERE NOT (ngayTraDuKien <= ? OR ngayNhan >= ?)
	        ) AND p.visible = 1
	    """;

	    try {
	        Connection con = ConnectDB.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setDate(1, ngayNhan);
	        ps.setDate(2, ngayTra);
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	            ds.add(new Object[] {
	                rs.getString("maPhong"),
	                rs.getString("tenPhong"),
	                rs.getString("tenLoaiPhong"),
	                rs.getDouble("donGia")
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}
	
	
	public static int demSoDonDatConLai(String maPhong) {
	    String sql = "SELECT COUNT(*) FROM ChiTietDonDatPhong WHERE maPhong = ? AND trangThai = N'Đã đặt'";
	    int count = 0;
	    
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	         
	        ps.setString(1, maPhong);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                count = rs.getInt(1);
	            }
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return count;
	}
	public static int demSoDonDangThue(String maPhong) {
	    String sql = "SELECT COUNT(*) FROM ChiTietDonDatPhong WHERE maPhong = ? AND trangThai = N'Đang thuê'";
	    int count = 0;
	    
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	         
	        ps.setString(1, maPhong);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                count = rs.getInt(1);
	            }
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return count;
	}
	// Lấy danh sách phòng có ít nhất 1 đơn "Đã đặt"
	public static ArrayList<String> layPhongCoDonDaDat() {
	    ArrayList<String> ds = new ArrayList<>();
	    String sql = """
	        SELECT DISTINCT ct.maPhong
	        FROM ChiTietDonDatPhong ct
	        WHERE ct.trangThai = N'Đã đặt'
	        ORDER BY ct.maPhong
	    """;
	    
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        
	        while (rs.next()) {
	            ds.add(rs.getString("maPhong"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return ds;
	}

}
	

	

	