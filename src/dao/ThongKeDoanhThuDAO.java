package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;

public class ThongKeDoanhThuDAO {
	public static ArrayList<Object[]> layHoaDonThongKe() {
	    ArrayList<Object[]> ds = new ArrayList<>();
	    String sql = """
	        SELECT hd.maHD, kh.hoTen, p.tenPhong, lp.tenLoaiPhong, hd.maNhanVien, 
	               hd.ngayLap, hd.phuongThucThanhToan, hd.tongTien
	        FROM HoaDon hd
	        JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD
	        JOIN Phong p ON cthd.maPhong = p.maPhong
	        JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
	        JOIN KhachHang kh ON hd.maKH = kh.maKH
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            ds.add(new Object[]{
	                rs.getString("maHD"),
	                rs.getDate("ngayLap"),
	                rs.getString("hoTen"),
	                rs.getString("tenPhong"),
	                rs.getString("tenLoaiPhong"),
	                rs.getString("maNhanVien"),
	                rs.getString("phuongThucThanhToan"),
	                rs.getDouble("tongTien")
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}
	public static ArrayList<Object[]> thongKeDoanhThuNgay(Date ngayBatDau, Date ngayKetThuc){
		ArrayList<Object[]> ds = new ArrayList<Object[]>();
		String sql = """
			SELECT hd.maHD, hd.ngayLap , kh.hoTen AS tenKH, p.tenPhong,lp.tenLoaiPhong, nv.hoTen AS tenNV,
				   hd.phuongThucThanhToan, hd.tongTien
		    FROM HoaDon hd
		    JOIN KhachHang kh ON hd.maKH = kh.maKH
		    JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD
		    JOIN Phong p ON cthd.maPhong = p.maPhong
		    JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
		    JOIN NhanVien nv ON nv.maNhanVien = hd.maNhanVien
		    WHERE hd.ngayLap BETWEEN ? AND ?
		    ORDER BY hd.ngayLap DESC
		""";
		try {
			Connection con = ConnectDB.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setDate(1, ngayBatDau);
			stmt.setDate(2, ngayKetThuc);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ds.add(new Object[] {
				 rs.getString("maHD"),
                 rs.getDate("ngayLap"),
                 rs.getString("tenKH"),
                 rs.getString("tenPhong"),
                 rs.getString("tenLoaiPhong"),
                 rs.getString("tenNV"),
                 rs.getString("phuongThucThanhToan"),
                 rs.getDouble("tongTien")
				});
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}
	public static ArrayList<Object[]> thongKeDoanhThuThang(int nam, int thang) {
        ArrayList<Object[]> ds = new ArrayList<>();
        String sql = """
            SELECT 
                hd.maHD, hd.ngayLap, kh.hoTen AS tenKhachHang, 
                p.tenPhong, lp.tenLoaiPhong, nv.hoTen AS tenNhanVien, 
                hd.phuongThucThanhToan, hd.tongTien
            FROM HoaDon hd
            JOIN KhachHang kh ON hd.maKH = kh.maKH
            JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD
            JOIN Phong p ON cthd.maPhong = p.maPhong
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
            JOIN NhanVien nv ON nv.maNhanVien = hd.maNhanVien
            WHERE MONTH(hd.ngayLap) = ? AND YEAR(hd.ngayLap) = ?
            ORDER BY hd.ngayLap DESC
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new Object[]{
                    rs.getString("maHD"),
                    rs.getDate("ngayLap"),
                    rs.getString("tenKhachHang"),
                    rs.getString("tenPhong"),
                    rs.getString("tenLoaiPhong"),
                    rs.getString("tenNhanVien"),
                    rs.getString("phuongThucThanhToan"),
                    rs.getDouble("tongTien")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
	 public static ArrayList<Object[]> thongKeDoanhThuNam(int nam) {
	        ArrayList<Object[]> ds = new ArrayList<>();
	        String sql = """
	            SELECT 
	                hd.maHD, hd.ngayLap, kh.hoTen AS tenKhachHang, 
	                p.tenPhong, lp.tenLoaiPhong, nv.hoTen AS tenNhanVien, 
	                hd.phuongThucThanhToan, hd.tongTien
	            FROM HoaDon hd
	            JOIN KhachHang kh ON hd.maKH = kh.maKH
	            JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD
	            JOIN Phong p ON cthd.maPhong = p.maPhong
	            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
	            JOIN NhanVien nv ON nv.maNhanVien = hd.maNhanVien
	            WHERE YEAR(hd.ngayLap) = ?
	            ORDER BY hd.ngayLap DESC
	        """;

	        try (Connection con = ConnectDB.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setInt(1, nam);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                ds.add(new Object[]{
	                    rs.getString("maHD"),
	                    rs.getDate("ngayLap"),
	                    rs.getString("tenKhachHang"),
	                    rs.getString("tenPhong"),
	                    rs.getString("tenLoaiPhong"),
	                    rs.getString("tenNhanVien"),
	                    rs.getString("phuongThucThanhToan"),
	                    rs.getDouble("tongTien")
	                });
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return ds;
	    }
	
}
