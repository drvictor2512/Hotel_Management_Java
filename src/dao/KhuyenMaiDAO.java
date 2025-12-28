package dao;

import entity.KhuyenMai;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import connectDB.ConnectDB;

public class KhuyenMaiDAO {
   
    // THÊM KHUYẾN MÃI
    public static boolean addKhuyenMai(KhuyenMai km) {
        // SỬA LẠI CÂU SQL: Thêm cột 'visible'
        String sql = "INSERT INTO KhuyenMai (maKM, tenKM, mucGiamGia, dieuKienApDung, ngayBatDau, ngayKetThuc, visible) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getTenKM());
            ps.setDouble(3, km.getMucGiamGia());
            ps.setString(4, km.getDieuKienApDung());
            ps.setDate(5, Date.valueOf(km.getNgayBatDau()));
            ps.setDate(6, Date.valueOf(km.getNgayKetThuc()));
            // SỬA LẠI: Thêm tham số 'visible'. Mặc định là true 
            ps.setBoolean(7, km.isVisible()); 
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi thêm khuyến mãi: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // CẬP NHẬT KHUYẾN MÃI
    public static boolean updateKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET tenKM=?, mucGiamGia=?, dieuKienApDung=?, ngayBatDau=?, ngayKetThuc=? "
                   + "WHERE maKM=?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getTenKM());
            ps.setDouble(2, km.getMucGiamGia());
            ps.setString(3, km.getDieuKienApDung());
            ps.setDate(4, Date.valueOf(km.getNgayBatDau()));
            ps.setDate(5, Date.valueOf(km.getNgayKetThuc()));
            ps.setString(6, km.getMaKM());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi cập nhật khuyến mãi: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // TÌM THEO MÃ KHUYẾN MÃI
    public static KhuyenMai getKhuyenMaiById(String maKM) {
        String sql = "SELECT maKM, tenKM, mucGiamGia, dieuKienApDung, ngayBatDau, ngayKetThuc, visible "
                   + "FROM KhuyenMai WHERE maKM=?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKM);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractKhuyenMaiFromResultSet(rs); 
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi tìm khuyến mãi theo mã: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // LẤY KHUYẾN MÃI THEO MÃ (alias cho getKhuyenMaiById)
    public static KhuyenMai layKhuyenMaiTheoMa(String maKM) {
        return getKhuyenMaiById(maKM);
    }

    // LẤY DANH SÁCH TOÀN BỘ
    public static ArrayList<KhuyenMai> getAllKhuyenMai() {
        String sql = """
                SELECT maKM, tenKM, mucGiamGia, dieuKienApDung, ngayBatDau, ngayKetThuc, visible
                FROM KhuyenMai
                WHERE visible = 1
                """;

        ArrayList<KhuyenMai> dsKM = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

        	while (rs.next()) {
        	    String maKM = rs.getString("maKM");
        	    String tenKM = rs.getString("tenKM");
        	    double mucGiamGia = rs.getDouble("mucGiamGia");
        	    String dieuKienApDung = rs.getString("dieuKienApDung");
        	    LocalDate ngayBatDau = rs.getDate("ngayBatDau").toLocalDate();
        	    LocalDate ngayKetThuc = rs.getDate("ngayKetThuc").toLocalDate();
        	    boolean visible = rs.getBoolean("visible");
        	    
      
        	    KhuyenMai km = new KhuyenMai(maKM, tenKM, mucGiamGia, dieuKienApDung, ngayBatDau, ngayKetThuc, visible); 
        	    dsKM.add(km);
        	}

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách khuyến mãi");
        }

        return dsKM;
    }


    // TÌM THEO TÊN KHUYẾN MÃI
    public static List<KhuyenMai> findKhuyenMaiByName(String tenKM) {
        List<KhuyenMai> list = new ArrayList<>();
        
        String sql = "SELECT maKM, tenKM, mucGiamGia, dieuKienApDung, ngayBatDau, ngayKetThuc, visible "
                   + "FROM KhuyenMai WHERE tenKM LIKE ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tenKM + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractKhuyenMaiFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi tìm khuyến mãi theo tên: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // HÀM HỖ TRỢ CHUYỂN DỮ LIỆU
    private static KhuyenMai extractKhuyenMaiFromResultSet(ResultSet rs) throws SQLException {
                return new KhuyenMai(
            rs.getString("maKM"),
            rs.getString("tenKM"),
            rs.getDouble("mucGiamGia"),
            rs.getString("dieuKienApDung"),
            rs.getDate("ngayBatDau").toLocalDate(),
            rs.getDate("ngayKetThuc").toLocalDate(),
            rs.getBoolean("visible") // <-- Thêm dòng này
        );
    }

    // LẤY MÃ KHUYẾN MÃI TIẾP THEO
    public static String getNextKhuyenMaiId() {
        String sql = "SELECT TOP 1 maKM FROM KhuyenMai ORDER BY maKM DESC";
        try (Connection con = ConnectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("maKM"); // ví dụ "KM005"
                int num = Integer.parseInt(lastId.substring(2)) + 1;
                return String.format("KM%03d", num);
            } else {
                return "KM001"; // nếu chưa có bản ghi nào
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "KM001";
        }
    }
    
    // LẤY MỨC GIẢM GIÁ THEO MÃ
    public static double getMucGiamGiaById(String maKM) {
        String sql = "SELECT mucGiamGia FROM KhuyenMai WHERE maKM = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKM);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("mucGiamGia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    

    // KIỂM TRA TỒN TẠI KHUYẾN MÃI
    public static boolean tonTaiKhuyenMai(String maKM) {
        String sql = "SELECT COUNT(*) FROM KhuyenMai WHERE maKM = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKM);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ẨN KHuyến Mãi
    public static boolean hideKhuyenMai(String maKM) {
        String sql = "UPDATE KhuyenMai SET visible = 0 WHERE maKM = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // KHÔI PHỤC Khuyến Mãi
    public static boolean restoreKhuyenMai(String maKM) {
        String sql = "UPDATE KhuyenMai SET visible = 1 WHERE maKM = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // LẤY DANH SÁCH Khuyến Mãi đang bị ẩn
    public static List<KhuyenMai> getKhuyenMaiAn() {
        List<KhuyenMai> list = new ArrayList<>();
        // SỬA LẠI CÂU SQL: Chọn rõ các cột
        String sql = "SELECT maKM, tenKM, mucGiamGia, dieuKienApDung, ngayBatDau, ngayKetThuc, visible "
                   + "FROM KhuyenMai WHERE visible = 0";
        try (Connection con = ConnectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractKhuyenMaiFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
 // LẤY KHUYẾN MÃI PHÙ HỢP VỚI KHOẢNG NGÀY
    public static ArrayList<KhuyenMai> getKhuyenMaiTheoKhoangNgay(LocalDate ngayNhan, LocalDate ngayTra) {
        ArrayList<KhuyenMai> dsKM = new ArrayList<>();
        String sql = """
                SELECT maKM, tenKM, mucGiamGia, dieuKienApDung, ngayBatDau, ngayKetThuc, visible
                FROM KhuyenMai
                WHERE visible = 1 
                AND ngayBatDau <= ? 
                AND ngayKetThuc >= ?
                ORDER BY mucGiamGia DESC
                """;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setDate(1, Date.valueOf(ngayNhan));
            ps.setDate(2, Date.valueOf(ngayTra));
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String maKM = rs.getString("maKM");
                String tenKM = rs.getString("tenKM");
                double mucGiamGia = rs.getDouble("mucGiamGia");
                String dieuKienApDung = rs.getString("dieuKienApDung");
                LocalDate ngayBatDau = rs.getDate("ngayBatDau").toLocalDate();
                LocalDate ngayKetThuc = rs.getDate("ngayKetThuc").toLocalDate();
                boolean visible = rs.getBoolean("visible");
                
                KhuyenMai km = new KhuyenMai(maKM, tenKM, mucGiamGia, dieuKienApDung, ngayBatDau, ngayKetThuc, visible);
                dsKM.add(km);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách khuyến mãi theo khoảng ngày");
        }
        
        return dsKM;
    }
}