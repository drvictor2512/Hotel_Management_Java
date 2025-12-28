package dao;

import java.sql.*;
import java.util.ArrayList;

import connectDB.ConnectDB;

public class NhanPhongDAO {
	
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
	        WHERE p.trangThai = 'Đã đặt' OR p.trangThai = 'Đang thuê'
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
	
	public static ArrayList<Object[]> getDPByMa(String ma){
		ArrayList<Object[]> ds = new ArrayList<Object[]>();
		String sql = """ 
				SELECT dp.maDatPhong, dp.maKH, kh.hoTen, ct.maPhong, p.tenPhong, p.tenLoaiPhong,
					   dp.maNhanVien, p.trangThai, ct.ngayDat, ct.ngayNhan, ct.ngayTraDuKien, ct.tienCoc
				FROM DonDatPhong dp
				JOIN ChiTietDonDatPhong ct ON dp.maDatPhong = ct.maDatPhong
				JOIN Phong p ON ct.maPhong = p.maPhong
				JOIN KhachHang kh ON dp.maKH = kh.maKH
				where dp.maDatPhong = ?
			""";
		 try {
			 Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
			 ps.setString(1, ma);
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
			                rs.getString("trangThai"),
			                rs.getDate("ngayDat"),
			                rs.getDate("ngayNhan"),
			                rs.getDate("ngayTraDuKien"),
			                rs.getDouble("tienCoc"),
		            });
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return ds;
	}

	public static boolean NhanDonDatPhong(String maPhong, String maDP, String trangThai) {
        
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
	    
}
