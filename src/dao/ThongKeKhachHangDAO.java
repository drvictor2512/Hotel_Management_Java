package dao;

import java.sql.*;
import java.util.ArrayList;
import connectDB.ConnectDB;
import entity.KhachHang;

public class ThongKeKhachHangDAO {

    // Lấy danh sách khách hàng và ngày trả phòng
    public static ArrayList<Object[]> getKhachHangVaNgayTraPhong(
    		  java.util.Date ngayBD, java.util.Date ngayKT,
              String gioiTinh, String luaTuoi) {

          ArrayList<Object[]> dsKH = new ArrayList<>();

          StringBuilder sql = new StringBuilder("""
              SELECT KH.maKH, KH.hoTen, KH.CCCD, KH.tuoi, KH.SDT, KH.gioiTinh,
                     KH.maNhanVien, COUNT(CTHD.maPhong) AS soLanTra
              FROM HoaDon HD
              JOIN ChiTietHoaDon CTHD ON HD.maHD = CTHD.maHD
              JOIN KhachHang KH ON HD.maKH = KH.maKH
              WHERE HD.ngayLap BETWEEN ? AND ?
          """);

          if (gioiTinh != null && !gioiTinh.isEmpty()) {
              sql.append(" AND KH.gioiTinh = ? ");
          }

          if (luaTuoi != null && !luaTuoi.isEmpty()) {
        	  switch (luaTuoi) {
        	    case "18–39" -> sql.append(" AND KH.tuoi BETWEEN 18 AND 39 ");
        	    case "40–59" -> sql.append(" AND KH.tuoi BETWEEN 40 AND 59 ");
        	    case "60+"   -> sql.append(" AND KH.tuoi >= 60 ");
        	}

          }

          sql.append("""
              GROUP BY KH.maKH, KH.hoTen, KH.CCCD, KH.tuoi, KH.SDT, KH.gioiTinh, KH.maNhanVien
              ORDER BY soLanTra DESC
          """);

          try (Connection con = ConnectDB.getConnection();
               PreparedStatement stmt = con.prepareStatement(sql.toString())) {

              stmt.setDate(1, new java.sql.Date(ngayBD.getTime()));
              stmt.setDate(2, new java.sql.Date(ngayKT.getTime()));

              int paramIndex = 3;
              if (gioiTinh != null && !gioiTinh.isEmpty()) {
                  stmt.setString(paramIndex++, gioiTinh);
              }

              ResultSet rs = stmt.executeQuery();
              while (rs.next()) {
                  KhachHang kh = new KhachHang(
                      rs.getString("maKH"),
                      rs.getString("CCCD"),
                      rs.getString("hoTen"),
                      rs.getInt("tuoi"),
                      rs.getString("SDT"),
                      rs.getString("gioiTinh"),
                      rs.getString("maNhanVien")
                  );
                  int soLanTra = rs.getInt("soLanTra");
                  dsKH.add(new Object[]{kh, soLanTra});
              }
          } catch (Exception e) {
              e.printStackTrace();
              System.err.println("Lỗi getTatCaKhachHangVaSoLanTra: " + e.getMessage());
          }

          return dsKH;
    }

    // Thống kê khách hàng trả phòng nhiều nhất
    public static ArrayList<Object[]> getKhachHangTraPhongNhieuNhat(
            java.util.Date ngayBD, java.util.Date ngayKT,
            String gioiTinh, String luaTuoi) {

        ArrayList<Object[]> dsKH = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT TOP 1 KH.maKH, KH.hoTen, KH.CCCD, KH.tuoi, KH.SDT, KH.gioiTinh, 
                   KH.maNhanVien, COUNT(CTHD.maPhong) AS soLanTra
            FROM HoaDon HD
            JOIN ChiTietHoaDon CTHD ON HD.maHD = CTHD.maHD
            JOIN Phong P ON CTHD.maPhong = P.maPhong
            JOIN KhachHang KH ON HD.maKH = KH.maKH
            WHERE HD.ngayLap BETWEEN ? AND ?
        """);
        
        if (gioiTinh != null && !gioiTinh.isEmpty()) {
            sql.append(" AND KH.gioiTinh = ? ");
        }

        if (luaTuoi != null && !luaTuoi.isEmpty()) {
        	switch (luaTuoi) {
            case "18–39" -> sql.append(" AND KH.tuoi BETWEEN 18 AND 39 ");
            case "40–59" -> sql.append(" AND KH.tuoi BETWEEN 40 AND 59 ");
            case "60+"   -> sql.append(" AND KH.tuoi >= 60 ");
          }
        }

        sql.append("""
            GROUP BY KH.maKH, KH.hoTen, KH.CCCD, KH.tuoi, KH.SDT, KH.gioiTinh, KH.maNhanVien
            ORDER BY soLanTra DESC
        """);

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {

            stmt.setDate(1, new java.sql.Date(ngayBD.getTime()));
            stmt.setDate(2, new java.sql.Date(ngayKT.getTime()));
            
            int paramIndex = 3;
            if (gioiTinh != null && !gioiTinh.isEmpty()) {
                stmt.setString(paramIndex++, gioiTinh);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("CCCD"),
                    rs.getString("hoTen"),
                    rs.getInt("tuoi"),
                    rs.getString("SDT"),
                    rs.getString("gioiTinh"),
                    rs.getString("maNhanVien")
                );
                int soLanTra = rs.getInt("soLanTra");
                dsKH.add(new Object[]{kh, soLanTra});
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi getKhachHangTraPhongNhieuNhat: " + e.getMessage());
        }
        return dsKH;
    }

    // Thống kê khách hàng trả phòng ít nhất
    public static ArrayList<Object[]> getKhachHangTraPhongItNhat(
            java.util.Date ngayBD, java.util.Date ngayKT,
            String gioiTinh, String luaTuoi) {

        ArrayList<Object[]> dsKH = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT KH.maKH, KH.hoTen, KH.CCCD, KH.tuoi, KH.SDT, KH.gioiTinh, 
                   KH.maNhanVien, COUNT(CTHD.maPhong) AS soLanTra
            FROM HoaDon HD
            JOIN ChiTietHoaDon CTHD ON HD.maHD = CTHD.maHD
            JOIN Phong P ON CTHD.maPhong = P.maPhong
            JOIN KhachHang KH ON HD.maKH = KH.maKH
            WHERE HD.ngayLap BETWEEN ? AND ?
        """);

        if (gioiTinh != null && !gioiTinh.isEmpty()) {
            sql.append(" AND KH.gioiTinh = ? ");
        }

        if (luaTuoi != null && !luaTuoi.isEmpty()) {
        	switch (luaTuoi) {
            case "18–39" -> sql.append(" AND KH.tuoi BETWEEN 18 AND 39 ");
            case "40–59" -> sql.append(" AND KH.tuoi BETWEEN 40 AND 59 ");
            case "60+"   -> sql.append(" AND KH.tuoi >= 60 ");
           }
        }

        sql.append("""
            GROUP BY KH.maKH, KH.hoTen, KH.CCCD, KH.tuoi, KH.SDT, KH.gioiTinh, KH.maNhanVien
            ORDER BY soLanTra ASC
        """);

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {

            stmt.setDate(1, new java.sql.Date(ngayBD.getTime()));
            stmt.setDate(2, new java.sql.Date(ngayKT.getTime()));
            
            int paramIndex = 3;
            if (gioiTinh != null && !gioiTinh.isEmpty()) {
                stmt.setString(paramIndex++, gioiTinh);
            }

            ResultSet rs = stmt.executeQuery();

            // Tìm số lần trả ít nhất
            int minSoLanTra = Integer.MAX_VALUE;
            ArrayList<Object[]> allKH = new ArrayList<>();

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("CCCD"),
                    rs.getString("hoTen"),
                    rs.getInt("tuoi"),
                    rs.getString("SDT"),
                    rs.getString("gioiTinh"),
                    rs.getString("maNhanVien")
                );
                int soLanTra = rs.getInt("soLanTra");
                allKH.add(new Object[]{kh, soLanTra});
                minSoLanTra = Math.min(minSoLanTra, soLanTra);
            }

            // Chỉ thêm KH có số lần trả bằng min
            for (Object[] row : allKH) {
                int soLanTra = (int) row[1];
                if (soLanTra == minSoLanTra) {
                    dsKH.add(row);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi getKhachHangTraPhongItNhat: " + e.getMessage());
        }

        return dsKH;
    }
    
    // Đếm tổng số khách hàng theo điều kiện
    public static int demSoKhachHang(
            java.util.Date ngayBD, java.util.Date ngayKT,
            String gioiTinh, String luaTuoi) {
        
        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(DISTINCT KH.maKH) AS tongSo
            FROM HoaDon HD
            JOIN ChiTietHoaDon CTHD ON HD.maHD = CTHD.maHD
            JOIN Phong P ON CTHD.maPhong = P.maPhong
            JOIN KhachHang KH ON HD.maKH = KH.maKH
            WHERE HD.ngayLap BETWEEN ? AND ?
        """);

        if (gioiTinh != null && !gioiTinh.isEmpty()) {
            sql.append(" AND KH.gioiTinh = ? ");
        }

        if (luaTuoi != null && !luaTuoi.isEmpty()) {
        	switch (luaTuoi) {
            case "18–39" -> sql.append(" AND KH.tuoi BETWEEN 18 AND 39 ");
            case "40–59" -> sql.append(" AND KH.tuoi BETWEEN 40 AND 59 ");
            case "60+"   -> sql.append(" AND KH.tuoi >= 60 ");
           }
        }

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {

            stmt.setDate(1, new java.sql.Date(ngayBD.getTime()));
            stmt.setDate(2, new java.sql.Date(ngayKT.getTime()));

            int paramIndex = 3;
            if (gioiTinh != null && !gioiTinh.isEmpty()) {
                stmt.setString(paramIndex++, gioiTinh);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("tongSo");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static ArrayList<Object[]> getKhachHangTheoThang(
            int nam, int thang, String gioiTinh, String luaTuoi) {

        ArrayList<Object[]> dsKH = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT KH.maKH, KH.hoTen, KH.CCCD, KH.tuoi, KH.SDT, KH.gioiTinh,
                   KH.maNhanVien, COUNT(CTHD.maPhong) AS soLanTra
            FROM HoaDon HD
            JOIN ChiTietHoaDon CTHD ON HD.maHD = CTHD.maHD
            JOIN KhachHang KH ON HD.maKH = KH.maKH
            WHERE YEAR(HD.ngayLap) = ? AND MONTH(HD.ngayLap) = ?
        """);

        if (gioiTinh != null && !gioiTinh.isEmpty()) {
            sql.append(" AND KH.gioiTinh = ? ");
        }
        if (luaTuoi != null && !luaTuoi.isEmpty()) {
        	switch (luaTuoi) {
            case "18–39" -> sql.append(" AND KH.tuoi BETWEEN 18 AND 39 ");
            case "40–59" -> sql.append(" AND KH.tuoi BETWEEN 40 AND 59 ");
            case "60+"   -> sql.append(" AND KH.tuoi >= 60 ");
            }
        }

        sql.append(" GROUP BY KH.maKH, KH.hoTen, KH.CCCD, KH.tuoi, KH.SDT, KH.gioiTinh, KH.maNhanVien");

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {

            stmt.setInt(1, nam);
            stmt.setInt(2, thang);

            int paramIndex = 3;
            if (gioiTinh != null && !gioiTinh.isEmpty()) {
                stmt.setString(paramIndex++, gioiTinh);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("CCCD"),
                    rs.getString("hoTen"),
                    rs.getInt("tuoi"),
                    rs.getString("SDT"),
                    rs.getString("gioiTinh"),
                    rs.getString("maNhanVien")
                );
                int soLanTra = rs.getInt("soLanTra");
                dsKH.add(new Object[]{kh, soLanTra});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsKH;
    }

    // THỐNG KÊ THEO NĂM
    public static ArrayList<Object[]> getKhachHangTheoNam(
            int nam, String gioiTinh, String luaTuoi) {

        ArrayList<Object[]> dsKH = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT KH.maKH, KH.hoTen, KH.CCCD, KH.tuoi, KH.SDT, KH.gioiTinh,
                   KH.maNhanVien, COUNT(CTHD.maPhong) AS soLanTra
            FROM HoaDon HD
            JOIN ChiTietHoaDon CTHD ON HD.maHD = CTHD.maHD
            JOIN KhachHang KH ON HD.maKH = KH.maKH
            WHERE YEAR(HD.ngayLap) = ?
        """);

        if (gioiTinh != null && !gioiTinh.isEmpty()) {
            sql.append(" AND KH.gioiTinh = ? ");
        }
        if (luaTuoi != null && !luaTuoi.isEmpty()) {
        	switch (luaTuoi) {
            case "18–39" -> sql.append(" AND KH.tuoi BETWEEN 18 AND 39 ");
            case "40–59" -> sql.append(" AND KH.tuoi BETWEEN 40 AND 59 ");
            case "60+"   -> sql.append(" AND KH.tuoi >= 60 ");
        }
        }

        sql.append(" GROUP BY KH.maKH, KH.hoTen, KH.CCCD, KH.tuoi, KH.SDT, KH.gioiTinh, KH.maNhanVien");

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {

            stmt.setInt(1, nam);
            int paramIndex = 2;
            if (gioiTinh != null && !gioiTinh.isEmpty()) {
                stmt.setString(paramIndex++, gioiTinh);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("CCCD"),
                    rs.getString("hoTen"),
                    rs.getInt("tuoi"),
                    rs.getString("SDT"),
                    rs.getString("gioiTinh"),
                    rs.getString("maNhanVien")
                );
                int soLanTra = rs.getInt("soLanTra");
                dsKH.add(new Object[]{kh, soLanTra});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsKH;
    }
    
    
    public static ArrayList<KhachHang> getTatCaKhachHang() {
        ArrayList<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("CCCD"),
                    rs.getString("hoTen"),
                    rs.getInt("tuoi"),
                    rs.getString("SDT"),
                    rs.getString("gioiTinh"),
                    rs.getString("maNhanVien")

                );
                ds.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi getTatCaKhachHang: " + e.getMessage());
        }
        return ds;
    }
    public static int demSoLanTraPhong(String maKH) {
        String sql = """
            SELECT COUNT(CTHD.maPhong) AS soLan
            FROM HoaDon HD
            JOIN ChiTietHoaDon CTHD ON HD.maHD = CTHD.maHD
            WHERE HD.maKH = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("soLan");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}