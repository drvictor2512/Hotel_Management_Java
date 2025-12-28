package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import connectDB.ConnectDB;
import entity.ChiTietDichVu;
import entity.DichVu;

public class DichVuDAO {     
    public static ArrayList<DichVu> getAllDichVu() {
        ArrayList<DichVu> dsDV = new ArrayList<>();
        String sql = "SELECT maDV, tenDV, giaDV, maNhanVien, donViTinh, visible FROM DichVu WHERE visible = 1";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dsDV.add(extractDichVuFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách dịch vụ");
        }
        return dsDV;
    }

    public static boolean themChiTietDichVu(ChiTietDichVu ctdv) {
        String sql = "INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES (?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ctdv.getMaDP());
            stmt.setString(2, ctdv.getMaDV());
            stmt.setInt(3, ctdv.getSoLuong());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean addDichVu(DichVu dv) {
        String sql = "INSERT INTO DichVu (maDV, tenDV, giaDV, maNhanVien, donViTinh) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, dv.getMaDV());
            stmt.setString(2, dv.getTenDV());
            stmt.setDouble(3, dv.getGiaDV());
            stmt.setString(4, dv.getMaNhanVien());
            stmt.setString(5, dv.getDonViTinh());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm cập nhật dịch vụ
    public static boolean updateDichVu(DichVu dv) {
        String sql = "UPDATE DichVu SET tenDV = ?, giaDV = ?, maNhanVien = ?, donViTinh = ? WHERE maDV = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, dv.getTenDV());
            stmt.setDouble(2, dv.getGiaDV());
            stmt.setString(3, dv.getMaNhanVien());
            stmt.setString(4, dv.getDonViTinh());
            stmt.setString(5, dv.getMaDV());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm ẩn dịch vụ
    public static boolean hide(String maDV) {
        String sql = "UPDATE DichVu SET visible = 0 WHERE maDV = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maDV);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Khôi phục dịch vụ
    public static boolean restore(String maDV) {
        String sql = "UPDATE DichVu SET visible = 1 WHERE maDV = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maDV);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Dịch vụ đã ẩn
    public static ArrayList<DichVu> getDVAn() {
        ArrayList<DichVu> dsDV = new ArrayList<>();
        String sql = "SELECT maDV, tenDV, giaDV, maNhanVien, donViTinh, visible FROM DichVu WHERE visible = 0";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dsDV.add(extractDichVuFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách dịch vụ đã ẩn");
        }
        return dsDV;
    }
    
    // Tìm kiếm dịch vụ theo mã, tên
    public ArrayList<DichVu> searchDichVu(String maDV, String tenDV) {
        ArrayList<DichVu> dsDV = new ArrayList<>();
        String sql = "SELECT * FROM DichVu WHERE maDV LIKE ? AND tenDV LIKE ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, "%" + maDV + "%");
            pstmt.setString(2, "%" + tenDV + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                dsDV.add(extractDichVuFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm kiếm dịch vụ: " + e.getMessage());
        }

        return dsDV;
    }
    
    public static String getNextDichVuId() {
        String sql = "SELECT TOP 1 maDV FROM DichVu ORDER BY maDV DESC";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("maDV");
                int num = Integer.parseInt(lastId.substring(2)) + 1;
                return String.format("DV%03d", num);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi tạo mã DV tiếp theo: " + e.getMessage());
        }
        return "DV001";
    }
    public static ArrayList<Object[]> getDichVuTheoMaDP(String maDP){
	    ArrayList<Object[]> ds = new ArrayList<>();
	    String sql = """
	        SELECT dv.tenDV, dv.giaDV, ct.soLuong
	        FROM ChiTietDichVu ct
	        JOIN DichVu dv ON ct.maDV = dv.maDV
	        JOIN DonDatPhong dp ON dp.maDatPhong = ct.maDatPhong
	        WHERE ct.maDatPhong = ?
	    """;

	    try {
	        Connection con = ConnectDB.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, maDP);
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	            ds.add(new Object[] {
	                rs.getString("tenDV"),
	                rs.getDouble("giaDV"),
	                rs.getInt("soLuong")
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}	
    private static DichVu extractDichVuFromResultSet(ResultSet rs) throws SQLException {
        return new DichVu(
            rs.getString("maDV"),
            rs.getString("tenDV"),
            rs.getDouble("giaDV"),
            rs.getString("maNhanVien"),
            rs.getString("donViTinh")  // THÊM MỚI
        );
    }
}