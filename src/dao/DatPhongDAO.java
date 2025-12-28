package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietDonDatPhong;
import entity.DonDatPhong;

public class DatPhongDAO {
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
	        ORDER BY dp.maDatPhong DESC
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
	public static ArrayList<Object[]> layDonDatPhongTheoTrangThai(String trangThai) {
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
	        WHERE ct.trangThai = ?
	        GROUP BY 
	            dp.maDatPhong, dp.maKH, kh.hoTen, ct.maPhong, 
	            p.tenPhong, lp.tenLoaiPhong, dp.maNhanVien, 
	            ct.ngayDat, ct.ngayNhan, ct.ngayTraDuKien, 
	            ct.tienCoc, dp.maKM, km.mucGiamGia, ct.trangThai
	        ORDER BY ct.ngayNhan DESC
	    """;
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, trangThai);
	        ResultSet rs = ps.executeQuery();

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
	// Tìm kiếm theo SĐT khách hàng
	public static ArrayList<Object[]> timDonDatPhongTheoSDT(String sdt) {
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
	        WHERE kh.SDT LIKE ?
	        GROUP BY 
	            dp.maDatPhong, dp.maKH, kh.hoTen, ct.maPhong, 
	            p.tenPhong, lp.tenLoaiPhong, dp.maNhanVien, 
	            ct.ngayDat, ct.ngayNhan, ct.ngayTraDuKien, 
	            ct.tienCoc, dp.maKM, km.mucGiamGia, ct.trangThai
	        ORDER BY dp.maDatPhong DESC
	    """;
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, "%" + sdt + "%");
	        ResultSet rs = ps.executeQuery();

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
	
	// Tìm kiếm theo mã đơn đặt phòng
	public static ArrayList<Object[]> timDonDatPhongTheoMa(String maDon) {
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
	        WHERE dp.maDatPhong LIKE ?
	        GROUP BY 
	            dp.maDatPhong, dp.maKH, kh.hoTen, ct.maPhong, 
	            p.tenPhong, lp.tenLoaiPhong, dp.maNhanVien, 
	            ct.ngayDat, ct.ngayNhan, ct.ngayTraDuKien, 
	            ct.tienCoc, dp.maKM, km.mucGiamGia, ct.trangThai
	        ORDER BY dp.maDatPhong DESC
	    """;
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, "%" + maDon + "%");
	        ResultSet rs = ps.executeQuery();

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

	public static String getNextId() {
		String sql = "Select TOP 1 maDatPhong from DonDatPhong ORDER BY maDatPhong DESC";
		try {
			Connection con = ConnectDB.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				String lastID = rs.getString("maDatPhong");
				int num = Integer.parseInt(lastID.substring(2)) + 1;
				return String.format("DP%03d", num);
				
			}else {
				return "DP001";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "DP001";
	}
	
	public static boolean themDonDatPhong(DonDatPhong dp) {
        String sqlDon = "INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES (?, ?, ?, ?)";
        String sqlCT = "INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
        	Connection con = ConnectDB.getConnection();
        	PreparedStatement stmtDon = con.prepareStatement(sqlDon);
            PreparedStatement stmtCT = con.prepareStatement(sqlCT);
            
            // Thêm đơn đặt phòng
            stmtDon.setString(1, dp.getMaDatPhong());
            stmtDon.setString(2, dp.getNv().getMaNhanVien());
            stmtDon.setString(3, dp.getKhachHang().getMaKH());
            stmtDon.setString(4, dp.getKhuyenMai() != null ? dp.getKhuyenMai().getMaKM() : null);           
            stmtDon.executeUpdate();

            // Thêm tất cả chi tiết
            for (ChiTietDonDatPhong ct : dp.getChiTietDon()) {
                stmtCT.setString(1, dp.getMaDatPhong());
                stmtCT.setString(2, ct.getPhong().getMaPhong());
                stmtCT.setDate(3, ct.getNgayDat());
                stmtCT.setDate(4, ct.getNgayNhan());
                stmtCT.setDate(5, ct.getNgayTraDuKien());
                stmtCT.setDouble(6, ct.getTienCoc());
                stmtCT.setString(7, "Đã đặt"); // Trạng thái mặc định
                stmtCT.addBatch();
            }
            stmtCT.executeBatch();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
	
	// Cập nhật trạng thái đơn đặt phòng
	public static boolean capNhatTrangThaiDonDat(String maDatPhong, String trangThai) {
	    String sql = "UPDATE DonDatPhong SET trangThai = ? WHERE maDatPhong = ?";
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, trangThai);
	        ps.setString(2, maDatPhong);
	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static boolean capNhatNgayTra(String maPhong, Date ngayTra) {
	    String sql = """
	    		UPDATE ChiTietDonDatPhong 
	    		SET ngayTraDuKien = ? 
	    		WHERE maPhong = ?
	    		""";
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setDate(1, ngayTra);
	        ps.setString(2, maPhong);
	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static ArrayList<Object[]> layPhongDaDatTheoDPVaKH(String maKH, String maDatPhong, String trangThai){
	    ArrayList<Object[]> ds = new ArrayList<Object[]>();
	    String sql = """
	        SELECT p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.donGia, ct.ngayDat, ct.ngayNhan, ct.ngayTraDuKien
	        FROM ChiTietDonDatPhong ct
	        JOIN Phong p ON ct.maPhong = p.maPhong
	        JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
			JOIN DonDatPhong dp ON dp.maDatPhong = ct.maDatPhong
	        WHERE p.visible = 1 AND dp.maKH = ? AND p.trangThai = ? AND dp.maDatPhong = ?	        
	    """;

	    try {
	        Connection con = ConnectDB.getConnection();
	        
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, maKH);
	        ps.setString(2, trangThai);
	        ps.setString(3, maDatPhong);
	        
	        
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	            ds.add(new Object[] {
	                rs.getString("maPhong"),
	                rs.getString("tenPhong"),
	                rs.getString("tenLoaiPhong"),
	                rs.getDouble("donGia"),
	                rs.getDate("ngayDat"),
	                rs.getDate("ngayNhan"),
	                rs.getDate("ngayTraDuKien")
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}	
	
	public static ArrayList<Object[]> getDonDatPhongDaDat(String maKH){
	    ArrayList<Object[]> ds = new ArrayList<>();
	    String sql = """
	        SELECT DISTINCT dp.maKH, dp.maNhanVien, dp.maDatPhong, dp.maKM
	        FROM DonDatPhong dp
	        JOIN ChiTietDonDatPhong ctdp ON dp.maDatPhong = ctdp.maDatPhong
	        WHERE dp.maKH = ? AND ctdp.trangThai = N'Đã đặt'
	        GROUP BY dp.maDatPhong, dp.maNhanVien, dp.maKH, dp.maKM
	    """;

	    try {
	        Connection con = ConnectDB.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, maKH);
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	            ds.add(new Object[] {
	                rs.getString("maDatPhong"),
	                rs.getString("maNhanVien"),
	                rs.getString("maKH"),
	                rs.getString("maKM")
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}	
	public static ArrayList<Object[]> getDonDatPhongDangThue(String maKH){
	    ArrayList<Object[]> ds = new ArrayList<>();
	    String sql = """
	        SELECT dp.maKH, dp.maNhanVien, dp.maDatPhong, dp.maKM
	        FROM DonDatPhong dp
	        JOIN ChiTietDonDatPhong ctdp ON dp.maDatPhong = ctdp.maDatPhong
	        WHERE dp.maKH = ? AND ctdp.trangThai = N'Đang thuê'
	        GROUP BY dp.maDatPhong, dp.maNhanVien, dp.maKH, dp.maKM
	    """;

	    try {
	        Connection con = ConnectDB.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, maKH);
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	            ds.add(new Object[] {
	                rs.getString("maDatPhong"),
	                rs.getString("maNhanVien"),
	                rs.getString("maKH"),
	                rs.getString("maKM")
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}	
	
	public static boolean ktraTrangThaiDatPhong(String maDP) {
		ArrayList<Object[]> ds = new ArrayList<>();
	    String sql = """
	        SELECT *
			FROM ChiTietDonDatPhong ct
			JOIN Phong p On p.maPhong = ct.maPhong
			WHERE maDatPhong = ? AND (ct.trangThai = N'Đã đặt' OR ct.trangThai = N'Đang thuê')
	    """;
	    try {
	        Connection con = ConnectDB.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, maDP);
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	            ds.add(new Object[] {
	                rs.getString("maPhong")
	            });
	        }
	        
	        if (ds.size() == 0) {
	        	for (Object o : ds) {
	        		capNhatTrangThaiDonDat(maDP, "Hoàn thiện");
	        	}	        	
	        	return true;
	        }
	        return true;
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
    public static boolean ktraPhongDangThue(String maPhong) {
        String sql = """
            SELECT COUNT(*) as count
            FROM ChiTietDonDatPhong ct
            WHERE ct.maPhong = ? AND ct.trangThai = N'Đang thuê'
        """;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}