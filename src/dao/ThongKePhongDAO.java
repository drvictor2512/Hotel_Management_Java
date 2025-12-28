package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;

public class ThongKePhongDAO {

    public static ArrayList<Object[]> layDanhSachPhong() {
        ArrayList<Object[]> ds = new ArrayList<>();

        String sql = """
            SELECT 
                p.maPhong,
                p.tenPhong,
                lp.tenLoaiPhong,
                p.tang,
                p.donGia,
                COUNT(ct.maPhong) AS soLanDat
            FROM Phong p
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
            LEFT JOIN ChiTietHoaDon ct ON p.maPhong = ct.maPhong
            GROUP BY p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.tang, p.donGia
            ORDER BY p.maPhong
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(new Object[]{
                    rs.getString("maPhong"),
                    rs.getString("tenPhong"),
                    rs.getString("tenLoaiPhong"), 
                    rs.getInt("tang"),
                    rs.getDouble("donGia"),
                    rs.getInt("soLanDat")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi layDanhSachPhong: " + e.getMessage());
        }

        return ds;
    }
    
   
    public static ArrayList<Object[]> thongKePhongDatNhieuNhat(String loaiPhong) {
        ArrayList<Object[]> ds = new ArrayList<>();

        String sql = """
            SELECT TOP 1
                p.maPhong,
                p.tenPhong,
                lp.tenLoaiPhong,
                p.tang,
                p.donGia,
                COUNT(ct.maPhong) AS soLanDat
            FROM Phong p
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
            LEFT JOIN ChiTietHoaDon ct ON p.maPhong = ct.maPhong
            WHERE lp.tenLoaiPhong LIKE ?
            GROUP BY p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.tang, p.donGia
            HAVING COUNT(ct.maPhong) > 0
            ORDER BY soLanDat DESC
        """;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Sử dụng toán tử 3 ngôi trực tiếp để đặt tham số
            ps.setString(1, (loaiPhong != null && !loaiPhong.equals("Tất cả")) ? loaiPhong : "%"); 

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ds.add(new Object[]{
                    rs.getString("maPhong"),
                    rs.getString("tenPhong"),
                    rs.getString("tenLoaiPhong"),
                    rs.getInt("tang"),
                    rs.getDouble("donGia"),
                    rs.getInt("soLanDat")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi thongKePhongDatNhieuNhat: " + e.getMessage());
        }

        return ds;
    }

   
    public static ArrayList<Object[]> thongKePhongDatItNhat(String loaiPhong) {
        ArrayList<Object[]> ds = new ArrayList<>();
        
        String sql = """
            SELECT 
                p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.tang, p.donGia,
                COUNT(ct.maPhong) AS soLanDat
            FROM Phong p
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
            LEFT JOIN ChiTietHoaDon ct ON p.maPhong = ct.maPhong
            WHERE lp.tenLoaiPhong LIKE ?
            GROUP BY p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.tang, p.donGia
            ORDER BY soLanDat ASC
        """;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            // Sử dụng toán tử 3 ngôi trực tiếp để đặt tham số
            ps.setString(1, (loaiPhong != null && !loaiPhong.equals("Tất cả")) ? loaiPhong : "%");

            ResultSet rs = ps.executeQuery();
            
            int minSoLan = Integer.MAX_VALUE;
            ArrayList<Object[]> allPhong = new ArrayList<>();
            
            // Bước 1: Duyệt qua tất cả để tìm minSoLan và lưu tạm kết quả
            while (rs.next()) {
                int soLanDat = rs.getInt("soLanDat");
                Object[] row = new Object[]{
                    rs.getString("maPhong"),
                    rs.getString("tenPhong"),
                    rs.getString("tenLoaiPhong"),
                    rs.getInt("tang"),
                    rs.getDouble("donGia"),
                    soLanDat
                };
                allPhong.add(row);
                minSoLan = Math.min(minSoLan, soLanDat);
            }
            
            // Bước 2: Chỉ lấy các phòng có số lần đặt = minSoLan
            for (Object[] row : allPhong) {
                int soLan = (int) row[5];
                if (soLan == minSoLan) {
                    ds.add(row);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi thongKePhongDatItNhat: " + e.getMessage());
        }
        
        return ds;
    }

   
    // Thống kê theo Thời gian

    public static ArrayList<Object[]> thongKeTheoNgay(
            Date ngayBatDau, Date ngayKetThuc, String loaiPhong) {
        
        ArrayList<Object[]> ds = new ArrayList<>();
        String sql = """
            SELECT 
                p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.tang, p.donGia,
                COUNT(ct.maPhong) AS soLanDat
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHD = hd.maHD
            JOIN Phong p ON p.maPhong = ct.maPhong
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
            WHERE hd.ngayLap BETWEEN ? AND ?
            AND lp.tenLoaiPhong LIKE ?
            GROUP BY p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.tang, p.donGia
            ORDER BY soLanDat DESC
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setDate(1, ngayBatDau);
            ps.setDate(2, ngayKetThuc);
            // Sử dụng toán tử 3 ngôi trực tiếp để đặt tham số
            ps.setString(3, (loaiPhong != null && !loaiPhong.equals("Tất cả")) ? loaiPhong : "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new Object[]{
                    rs.getString("maPhong"),
                    rs.getString("tenPhong"),
                    rs.getString("tenLoaiPhong"),
                    rs.getInt("tang"),
                    rs.getDouble("donGia"),
                    rs.getInt("soLanDat")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi thongKeTheoNgay: " + e.getMessage());
        }
        
        return ds;
    }
    
    //  Thống kê theo Thời gian

    public static ArrayList<Object[]> thongKeTheoThang(
            int nam, int thang, String loaiPhong) {
        
        ArrayList<Object[]> ds = new ArrayList<>();
        String sql = """
            SELECT 
                p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.tang, p.donGia,
                COUNT(ct.maPhong) AS soLanDat
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHD = hd.maHD
            JOIN Phong p ON p.maPhong = ct.maPhong
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
            WHERE YEAR(hd.ngayLap) = ? AND MONTH(hd.ngayLap) = ?
            AND lp.tenLoaiPhong LIKE ?
            GROUP BY p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.tang, p.donGia
            ORDER BY soLanDat DESC
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, nam);
            ps.setInt(2, thang);
            // Sử dụng toán tử 3 ngôi trực tiếp để đặt tham số
            ps.setString(3, (loaiPhong != null && !loaiPhong.equals("Tất cả")) ? loaiPhong : "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new Object[]{
                    rs.getString("maPhong"),
                    rs.getString("tenPhong"),
                    rs.getString("tenLoaiPhong"),
                    rs.getInt("tang"),
                    rs.getDouble("donGia"),
                    rs.getInt("soLanDat")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi thongKeTheoThang: " + e.getMessage());
        }
        
        return ds;
    }

   
    // Thống kê theo Thời gian 

    public static ArrayList<Object[]> thongKeTheoNam(
            int nam, String loaiPhong) {
        
        ArrayList<Object[]> ds = new ArrayList<>();
        String sql = """
            SELECT 
                p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.tang, p.donGia,
                COUNT(ct.maPhong) AS soLanDat
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHD = hd.maHD
            JOIN Phong p ON p.maPhong = ct.maPhong
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
            WHERE YEAR(hd.ngayLap) = ?
            AND lp.tenLoaiPhong LIKE ?
            GROUP BY p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.tang, p.donGia
            ORDER BY soLanDat DESC
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, nam);
            // Sử dụng toán tử 3 ngôi trực tiếp để đặt tham số
            ps.setString(2, (loaiPhong != null && !loaiPhong.equals("Tất cả")) ? loaiPhong : "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new Object[]{
                    rs.getString("maPhong"),
                    rs.getString("tenPhong"),
                    rs.getString("tenLoaiPhong"),
                    rs.getInt("tang"),
                    rs.getDouble("donGia"),
                    rs.getInt("soLanDat")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi thongKeTheoNam: " + e.getMessage());
        }
        
        return ds;
    }
}