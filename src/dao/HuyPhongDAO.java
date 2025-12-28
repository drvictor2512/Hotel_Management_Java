package dao;
import java.sql.*;
import java.util.ArrayList;
import connectDB.ConnectDB;

public class HuyPhongDAO {

    // Lấy tất cả đơn đặt phòng
    public static ArrayList<Object[]> layTatCaDonDatPhong() {
        ArrayList<Object[]> ds = new ArrayList<>();
        String sql = """
            SELECT dp.maDatPhong, dp.maKH, kh.hoTen, ct.maPhong, p.tenPhong, p.tenLoaiPhong,
                   STRING_AGG(dv.tenDV, ', ') AS danhSachDV, dp.maNhanVien, ct.ngayDat,
                   ct.ngayNhan, ct.ngayTraDuKien, ct.tienCoc, dp.maKM,
                   ISNULL(km.mucGiamGia, 0) AS mucGiamGia
            FROM DonDatPhong dp
            JOIN ChiTietDonDatPhong ct ON dp.maDatPhong = ct.maDatPhong
            JOIN Phong p ON ct.maPhong = p.maPhong
            JOIN KhachHang kh ON dp.maKH = kh.maKH
            LEFT JOIN KhuyenMai km ON dp.maKM = km.maKM
            LEFT JOIN ChiTietDichVu ctdv ON dp.maDatPhong = ctdv.maDatPhong
            LEFT JOIN DichVu dv ON ctdv.maDV = dv.maDV
            WHERE ct.trangThai = N'Đã hủy'
            GROUP BY dp.maDatPhong, dp.maKH, kh.hoTen, ct.maPhong, p.tenPhong, p.tenLoaiPhong,
                     dp.maNhanVien, ct.ngayDat, ct.ngayNhan, ct.ngayTraDuKien, ct.tienCoc,
                     dp.maKM, km.mucGiamGia
        	
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
                        rs.getString("danhSachDV")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
    public static ArrayList<Object[]> layPhongDaDatTheoKH(String maKH) {
        ArrayList<Object[]> ds = new ArrayList<>();
        String sql = """
			    SELECT dp.maDatPhong, ct.maPhong, p.tenPhong, lp.tenLoaiPhong,
			    ct.ngayDat, ct.ngayNhan, ct.ngayTraDuKien, ct.tienCoc
			FROM DonDatPhong dp
			JOIN ChiTietDonDatPhong ct ON dp.maDatPhong = ct.maDatPhong
			JOIN Phong p ON ct.maPhong = p.maPhong
			JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
			WHERE dp.maKH = ? AND ct.trangThai = N'Đã đặt'
			ORDER BY ct.ngayNhan DESC
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ds.add(new Object[]{
                    rs.getString("maDatPhong"),      
                    rs.getString("maPhong"),         
                    rs.getString("tenPhong"),       
                    rs.getString("tenLoaiPhong"),   
                    rs.getDate("ngayDat"),            
                    rs.getDate("ngayNhan"),        
                    rs.getDate("ngayTraDuKien"),    
                    rs.getDouble("tienCoc")          
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }
    
    public static boolean xoaPhongTrongDon(String maDatPhong, String maPhong) {
        String sql = """
            UPDATE ChiTietDonDatPhong
            SET trangThai = N'Đã hủy'
            WHERE maDatPhong = ? AND maPhong = ?
        """;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maDatPhong);
            ps.setString(2, maPhong);
            int rows = ps.executeUpdate();
            
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean conDonDangDatPhong(String maPhong) {
    	 String sql = "SELECT COUNT(*) FROM ChiTietDonDatPhong ctdp " +
                 "JOIN DonDatPhong ddp ON ctdp.maDatPhong = ddp.maDatPhong " +
                 "WHERE ctdp.maPhong = ? AND ctdp.trangThai = N'Đã Đặt'";
    	 		try (Connection con = ConnectDB.getConnection();
    	 			PreparedStatement ps = con.prepareStatement(sql)) {
    	 			ps.setString(1, maPhong);
    	 			ResultSet rs = ps.executeQuery();
    	 	if (rs.next()) {
            return rs.getInt(1) > 0;
             }
    	   } catch (SQLException e) {
    	 			e.printStackTrace();
    	 }
    	 return false;
   }
    public static boolean conDonDangThue(String maPhong) {
        String sql = """
            SELECT COUNT(*) 
            FROM ChiTietDonDatPhong 
            WHERE maPhong = ? AND trangThai = N'Đang thuê'
        """;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
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
