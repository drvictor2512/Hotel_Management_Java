package dao;

import java.sql.*;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHangDAO {

	 public static ArrayList<KhachHang> getAllKhachHang() {
	        ArrayList<KhachHang> dsKH = new ArrayList<>();
	        String sql = "SELECT * FROM KhachHang WHERE visible = 1";
	        try (Connection con = ConnectDB.getConnection();
	             Statement stmt = con.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {

	            while (rs.next()) {
	                String maKH = rs.getString("maKH");
	                String hoTen = rs.getString("hoTen");
	                String cccd = rs.getString("CCCD");
	                int tuoi = rs.getInt("tuoi");
	                String sdt = rs.getString("SDT");
	                String gioiTinh = rs.getString("gioiTinh");
	                String maNV = rs.getString("maNhanVien");
	                KhachHang kh = new KhachHang(maKH, cccd, hoTen, tuoi, sdt, gioiTinh, maNV);
	                dsKH.add(kh);
	            }

	        } catch (SQLException e) {
	            System.out.println("❌ Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
	        }
	        return dsKH;
	    }


	// Thêm khách hàng
	    public boolean addKhachHang(KhachHang kh) {
	        String sql = "INSERT INTO KhachHang(MaKH, CCCD, HoTen, Tuoi, SDT, GioiTinh, maNhanVien, visible) "
	                   + "VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
	        try (Connection con = ConnectDB.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, kh.getMaKH());
	            ps.setString(2, kh.getCCCD());
	            ps.setString(3, kh.getHoTen());
	            ps.setInt(4, kh.getTuoi());
	            ps.setString(5, kh.getSDT());
	            ps.setString(6, kh.getGioiTinh());
	            ps.setString(7, kh.getMaNhanVien());
	            return ps.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

    //Tao ma tu dong khong can nhap
    public String getNextKhachHangId() {
        String sql = "SELECT TOP 1 maKH FROM KhachHang ORDER BY maKH DESC";
        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("maKH"); 
                int num = Integer.parseInt(lastId.substring(2)) + 1;
                return String.format("KH%03d", num);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi tạo mã KH tiếp theo: " + e.getMessage());
        }
        return "KH001";
    }
    public boolean updateKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET hoTen=?, CCCD=?, tuoi=?, SDT=?, gioiTinh=? WHERE maKH=?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, kh.getHoTen());
            pstmt.setString(2, kh.getCCCD());
            pstmt.setInt(3, kh.getTuoi());
            pstmt.setString(4, kh.getSDT());
            pstmt.setString(5, kh.getGioiTinh());
            pstmt.setString(6, kh.getMaKH());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật khách hàng: " + e.getMessage());
        }
        return false;
    }
    public static boolean tonTaiKhachHang(String maKH) {
        String sql = "SELECT COUNT(*) FROM KhachHang WHERE maKH = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())  rs.getInt(1);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String layTenKhachHang(String maKH) {
	    String sql = "SELECT hoTen FROM KhachHang WHERE maKH = ?";
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setString(1, maKH);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("hoTen");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
    public static KhachHang getKhachHangbyID(String maKH) {
	    String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
	    KhachHang kh = null;
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setString(1, maKH);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	        	String ma = rs.getString("maKH");
                String hoTen = rs.getString("hoTen");
                String cccd = rs.getString("CCCD");
                int tuoi = rs.getInt("tuoi");
                String sdt = rs.getString("SDT");
                String gioiTinh = rs.getString("gioiTinh");
                String maNV = rs.getString("maNhanVien");
                kh = new KhachHang(ma, cccd, hoTen, tuoi, sdt, gioiTinh, maNV);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return kh;
	}
    public boolean hideKhachHang(String maKH) {
        String sql = "UPDATE KhachHang SET visible = 0 WHERE MaKH = ?";
	    try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // Lấy khách hàng đã bị ẩn (visible = 0)
    public static ArrayList<KhachHang> getKhachHangAn() {
        ArrayList<KhachHang> list = new ArrayList<>();
        String sql = "SELECT MaKH, CCCD, HoTen, Tuoi, SDT, GioiTinh FROM KhachHang WHERE visible = 0";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new KhachHang(
                    rs.getString("MaKH"),
                    rs.getString("CCCD"),
                    rs.getString("HoTen"),
                    rs.getInt("Tuoi"),
                    rs.getString("SDT"),
                    rs.getString("GioiTinh"),
                    null // Bỏ MaNV nếu không có trong DB
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Khôi phục khách hàng
    public static boolean restoreKhachHang(String maKH) {
        String sql = "UPDATE KhachHang SET visible = 1 WHERE MaKH = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static ArrayList<KhachHang> getAllKhachHangCoDatPhong() {
        ArrayList<KhachHang> dsKH = new ArrayList<>();
        String sql = """
            SELECT kh.maKH, kh.hoTen, kh.CCCD, kh.tuoi, kh.SDT, kh.gioiTinh, kh.maNhanVien
            FROM KhachHang kh
            WHERE EXISTS (
                SELECT 1
                FROM DonDatPhong dp
                JOIN ChiTietDonDatPhong ct ON dp.maDatPhong = ct.maDatPhong
                WHERE dp.maKH = kh.maKH 
                  AND ct.trangThai = N'Đã đặt'
            )
            ORDER BY kh.maKH
        """;
        
        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maKH = rs.getString("maKH");
                String hoTen = rs.getString("hoTen");
                String cccd = rs.getString("CCCD");
                int tuoi = rs.getInt("tuoi");
                String sdt = rs.getString("SDT");
                String gioiTinh = rs.getString("gioiTinh");
                String maNV = rs.getString("maNhanVien");
                KhachHang kh = new KhachHang(maKH, cccd, hoTen, tuoi, sdt, gioiTinh, maNV);
                dsKH.add(kh);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
        }
        return dsKH;
    }
    
    public static ArrayList<KhachHang> getAllKhachHangDangThue() {
        ArrayList<KhachHang> dsKH = new ArrayList<>();
        String sql = """
            SELECT kh.maKH, kh.hoTen, kh.CCCD, kh.tuoi, kh.SDT, kh.gioiTinh, kh.maNhanVien
            FROM KhachHang kh
            WHERE EXISTS (
                SELECT 1
                FROM DonDatPhong dp
                JOIN ChiTietDonDatPhong ct ON dp.maDatPhong = ct.maDatPhong
                WHERE dp.maKH = kh.maKH 
                  AND ct.trangThai = N'Đang thuê'
            )
            ORDER BY kh.maKH
        """;
        
        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maKH = rs.getString("maKH");
                String hoTen = rs.getString("hoTen");
                String cccd = rs.getString("CCCD");
                int tuoi = rs.getInt("tuoi");
                String sdt = rs.getString("SDT");
                String gioiTinh = rs.getString("gioiTinh");
                String maNV = rs.getString("maNhanVien");
                KhachHang kh = new KhachHang(maKH, cccd, hoTen, tuoi, sdt, gioiTinh, maNV);
                dsKH.add(kh);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
        }
        return dsKH;
    }
   
}
