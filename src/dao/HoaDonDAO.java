package dao;

import java.sql.*;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;

public class HoaDonDAO {
	public static ArrayList<Object[]> layTatCaHoaDon() {
	    ArrayList<Object[]> ds = new ArrayList<>();
	    String sql = """
	        SELECT hd.maHD, nv.hoTen AS hoTenNV, kh.hoTen AS hoTenKH, kh.SDT, 
	               p.tenPhong, lp.tenLoaiPhong, 
	               cthd.ngayNhanPhong, cthd.ngayTraPhong, 
	               cthd.soNgay, hd.phuongThucThanhToan, cthd.donGia
	        FROM HoaDon hd
	        JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD
	        JOIN Phong p ON cthd.maPhong = p.maPhong
	        JOIN KhachHang kh ON hd.maKH = kh.maKH
	        JOIN NhanVien nv ON nv.maNhanVien = hd.maNhanVien
	        JOIN LoaiPhong lp ON lp.maLoaiPhong = p.maLoaiPhong
	        ORDER BY hd.maHD DESC
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            ds.add(new Object[]{
	                rs.getString("maHD"),
	                rs.getString("hoTenNV"),
	                rs.getString("hoTenKH"),
	                rs.getString("SDT"),
	                rs.getString("tenPhong"),
	                rs.getString("tenLoaiPhong"),
	                rs.getDate("ngayNhanPhong"),
	                rs.getDate("ngayTraPhong"),
	                rs.getInt("soNgay"),
	                rs.getString("phuongThucThanhToan"),
	                rs.getDouble("donGia")
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}
	
	public static ArrayList<Object[]> layTatCaChiTietHoaDon(String maHD) {
	    ArrayList<Object[]> ds = new ArrayList<>();
	    String sql = """
	        SELECT *
	        FROM ChiTietHoaDon
	        WHERE maHD = ?
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);) {
	    	ps.setString(1, maHD);
	         ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            ds.add(new Object[]{
	                rs.getString("maHD"),
	                rs.getString("maPhong"),
	                rs.getInt("soNgay"),
	                rs.getDate("ngayNhanPhong"),
	                rs.getDate("ngayTraPhong"),
	                rs.getDouble("donGia")
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}


	
	public static boolean themHoaDon(HoaDon hd) {
	    String sqlDon = "INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES (?, ?, ?, ?, ?, ?)";
	    String sqlCT = "INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES (?, ?, ?, ?, ?, ?)";
	    try {Connection con = ConnectDB.getConnection();
	         PreparedStatement stmtDon = con.prepareStatement(sqlDon);
	         PreparedStatement stmtCT = con.prepareStatement(sqlCT);
	         //Thêm hóa đơn
	        stmtDon.setString(1, hd.getMaHD());
	        stmtDon.setString(2, hd.getKhachHang().getMaKH());
	        stmtDon.setString(3, hd.getNhanVien().getMaNhanVien());
	        stmtDon.setDate(4, hd.getNgayLap());
	        stmtDon.setString(5, hd.getPhuongThucThanhToan());
	        stmtDon.setDouble(6, hd.getTongTien());
	        stmtDon.executeUpdate();
	        
	        //Thêm chi tiết
	        for (ChiTietHoaDon ct : hd.getChiTietHD()) {
	        	stmtCT.setString(1, hd.getMaHD());
	        	stmtCT.setString(2, ct.getPhong().getMaPhong());
	        	stmtCT.setInt(3, ct.getSoNgay());
	        	stmtCT.setDate(4, ct.getNgayNhanPhong());
	        	stmtCT.setDate(5, ct.getNgayTraPhong());
	        	stmtCT.setDouble(6, ct.getDonGia());
	        	stmtCT.addBatch();
	        }
	        stmtCT.executeBatch();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	
	public static boolean themChiTietDon(String maHD, String maPhong, int soNgay, Date ngayNhanPhong, Date ngayTraPhong, double donGia) {
        String sql = "INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maHD);
            stmt.setString(2, maPhong);
            stmt.setInt(3, soNgay);
            stmt.setDate(4, ngayNhanPhong);
            stmt.setDate(5, ngayTraPhong);
            stmt.setDouble(6, donGia);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public static ArrayList<Object[]> getHDByMa(String ma){
	    ArrayList<Object[]> ds = new ArrayList<>();
	    String sql = """ 
	        SELECT hd.maHD, kh.hoTen, p.tenPhong, hd.maNhanVien, 
	            cthd.ngayNhanPhong, cthd.ngayTraPhong, hd.ngayLap, 
	            hd.phuongThucThanhToan, cthd.donGia, cthd.soNgay, hd.tongTien
	        FROM HoaDon hd
	        JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD
	        JOIN Phong p ON cthd.maPhong = p.maPhong
	        JOIN KhachHang kh ON hd.maKH = kh.maKH
	        WHERE hd.maHD = ?
	    """;
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, ma);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            ds.add(new Object[]{
	                rs.getString("maHD"),
	                rs.getString("hoTen"),
	                rs.getString("tenPhong"),
	                rs.getString("maNhanVien"),
	                rs.getDate("ngayNhanPhong"),
	                rs.getDate("ngayTraPhong"),
	                rs.getDate("ngayLap"),
	                rs.getString("phuongThucThanhToan"),
	                rs.getDouble("donGia"),
	                rs.getInt("soNgay"),
	                rs.getDouble("tongTien")
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}
	
	public static String getNextId() {
		String sql = "Select TOP 1 maHD from HoaDon ORDER BY maHD DESC";
		try {
			Connection con = ConnectDB.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				String lastID = rs.getString("maHD");
				int num = Integer.parseInt(lastID.substring(2)) + 1;
				return String.format("HD%03d", num);
				
			}else {
				return "HD001";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "HD001";
	}
	public static boolean xoaDonDatPhong(String maP) {
        String timMaDDP = "SELECT DISTINCT maDatPhong FROM ChiTietDonDatPhong WHERE maPhong = ?";
        String xoaChiTietDVSQL = "DELETE FROM ChiTietDichVu WHERE maDatPhong = ?";
        String xoaChiTietDDPSQL = "DELETE FROM ChiTietDonDatPhong WHERE maDatPhong = ? AND maPhong = ?";
        String xoaDDPSQL = "DELETE FROM DonDatPhong WHERE maDatPhong = ?";
        
        try (Connection con = ConnectDB.getConnection()) {
            try (
                PreparedStatement stmt1 = con.prepareStatement(timMaDDP);
                PreparedStatement stmtDV = con.prepareStatement(xoaChiTietDVSQL);
                PreparedStatement stmt2 = con.prepareStatement(xoaChiTietDDPSQL);
                PreparedStatement stmt3 = con.prepareStatement(xoaDDPSQL);
            ) {
                // Lấy danh sách maDatPhong theo maPhong
                stmt1.setString(1, maP);
                ResultSet rs = stmt1.executeQuery();
                
                while (rs.next()) {
                    String maDP = rs.getString("maDatPhong");

                    // 1. Xóa chi tiết dịch vụ trước
                    stmtDV.setString(1, maDP);
                    stmtDV.executeUpdate();

                    // 2. Xóa chi tiết đơn đặt phòng
                    stmt2.setString(1, maDP);
                    stmt2.setString(2, maP);
                    stmt2.executeUpdate();

                    // 3. Xóa đơn đặt phòng
                    stmt3.setString(1, maDP);
                    stmt3.executeUpdate();
                }

                // Cập nhật tình trạng phòng về "Trống"
                PhongDAO.capNhatTinhTrangPhong(maP, "Trống");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
public static boolean HoanThienDonDatPhong(String maPhong, String maDP, String trangThai) {
        
        String sqlChiTiet = """
        		UPDATE ct
				SET ct.trangThai = ?
				FROM ChiTietDonDatPhong ct 
				JOIN Phong p ON ct.maPhong = p.maPhong
				WHERE ct.maPhong = ? AND ct.maDatPhong = ?""";

        try (Connection con = ConnectDB.getConnection()) {
            con.setAutoCommit(false);

            try (
            	PreparedStatement stmt = con.prepareStatement(sqlChiTiet)) {
            	stmt.setString(1, trangThai);
                stmt.setString(2, maPhong);
                stmt.setString(3, maDP);
                stmt.executeUpdate();

                con.commit(); 
                return true;
            } catch (SQLException e) {
                con.rollback();
                e.printStackTrace();
                return false;
            }                      
           
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
public static ArrayList<Object[]> timDonDatPhongTheoMa(String maDon) {
    ArrayList<Object[]> ds = new ArrayList<>();
    String sql = """
        SELECT hd.maHD, dp.maKH, kh.hoTen, ct.maPhong, p.tenPhong, lp.tenLoaiPhong, 
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
        ORDER BY ct.ngayNhan DESC
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
            	    rs.getString("danhSachDV"),
            	    rs.getString("maNhanVien"),
            	    rs.getDate("ngayDat"),
            	    rs.getDate("ngayNhan"),
            	    rs.getDate("ngayTraDuKien"),
            	    rs.getDouble("tienCoc"),
            	    rs.getString("maKM"),
            	    rs.getString("mucGiamGia"),
            	    rs.getString("trangThai")
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return ds;
}

public static ArrayList<Object[]> layTatCaHoaDonTheoMa(String ma) {
    ArrayList<Object[]> ds = new ArrayList<>();
    String sql = """
        SELECT hd.maHD, nv.hoTen AS hoTenNV, kh.hoTen AS hoTenKH, kh.SDT, 
               p.tenPhong, lp.tenLoaiPhong, 
               cthd.ngayNhanPhong, cthd.ngayTraPhong, 
               cthd.soNgay, hd.phuongThucThanhToan, cthd.donGia
        FROM HoaDon hd
        JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD
        JOIN Phong p ON cthd.maPhong = p.maPhong
        JOIN KhachHang kh ON hd.maKH = kh.maKH
        JOIN NhanVien nv ON nv.maNhanVien = hd.maNhanVien
        JOIN LoaiPhong lp ON lp.maLoaiPhong = p.maLoaiPhong
        WHERE hd.maHD = ?
    """;

    try (Connection con = ConnectDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
    	ps.setString(1, ma);
        ResultSet rs = ps.executeQuery();
    	
        while (rs.next()) {
            ds.add(new Object[]{
                rs.getString("maHD"),
                rs.getString("hoTenNV"),
                rs.getString("hoTenKH"),
                rs.getString("SDT"),
                rs.getString("tenPhong"),
                rs.getString("tenLoaiPhong"),
                rs.getDate("ngayNhanPhong"),
                rs.getDate("ngayTraPhong"),
                rs.getInt("soNgay"),
                rs.getString("phuongThucThanhToan"),
                rs.getDouble("donGia")
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return ds;
}

public static ArrayList<Object[]> layTatCaHoaDonTheoSDT(String sdt) {
    ArrayList<Object[]> ds = new ArrayList<>();
    String sql = """
        SELECT hd.maHD, nv.hoTen AS hoTenNV, kh.hoTen AS hoTenKH, kh.SDT, 
               p.tenPhong, lp.tenLoaiPhong, 
               cthd.ngayNhanPhong, cthd.ngayTraPhong, 
               cthd.soNgay, hd.phuongThucThanhToan, cthd.donGia
        FROM HoaDon hd
        JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD
        JOIN Phong p ON cthd.maPhong = p.maPhong
        JOIN KhachHang kh ON hd.maKH = kh.maKH
        JOIN NhanVien nv ON nv.maNhanVien = hd.maNhanVien
        JOIN LoaiPhong lp ON lp.maLoaiPhong = p.maLoaiPhong
        WHERE kh.SDT = ?
        ORDER BY hd.maHD DESC
    """;

    try (Connection con = ConnectDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
    	ps.setString(1, sdt);
        ResultSet rs = ps.executeQuery();
    	
        while (rs.next()) {
            ds.add(new Object[]{
                rs.getString("maHD"),
                rs.getString("hoTenNV"),
                rs.getString("hoTenKH"),
                rs.getString("SDT"),
                rs.getString("tenPhong"),
                rs.getString("tenLoaiPhong"),
                rs.getDate("ngayNhanPhong"),
                rs.getDate("ngayTraPhong"),
                rs.getInt("soNgay"),
                rs.getString("phuongThucThanhToan"),
                rs.getDouble("donGia")
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return ds;
}

}

