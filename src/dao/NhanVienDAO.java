package dao;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import connectDB.ConnectDB;
import entity.LoaiNhanVien;
import entity.NhanVien;

public class NhanVienDAO {

    public static ArrayList<NhanVien> getAll() {
        String sql = """
                SELECT nv.maNhanVien, nv.username, nv.password, nv.hoTen, nv.gioiTinh, nv.SDT, nv.tuoi,
                       lnv.maLoaiNV, lnv.tenLoaiNV
                FROM NhanVien nv
                JOIN LoaiNhanVien lnv ON nv.maLoaiNV = lnv.maLoaiNV
                WHERE nv.visible = 1
                """;

        ArrayList<NhanVien> dsNV = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maNV = rs.getString("maNhanVien");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String hoTen = rs.getString("hoTen");
                String gioiTinh = rs.getString("gioiTinh");
                String sdt = rs.getString("SDT");
                int tuoi = rs.getInt("tuoi");
                String maLNV = rs.getString("maLoaiNV");
                String tenLNV = rs.getString("tenLoaiNV");

                LoaiNhanVien lnv = new LoaiNhanVien(maLNV, tenLNV);
                NhanVien nv = new NhanVien(maNV, username, password, hoTen, gioiTinh, sdt, tuoi, lnv);
                dsNV.add(nv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách nhân viên");
        }
        return dsNV;
    }

    public static NhanVien dangnhap(String username, String password) {
        String sql = """
                SELECT nv.*, lnv.maLoaiNV, lnv.tenLoaiNV
                FROM NhanVien nv
                JOIN LoaiNhanVien lnv ON nv.maLoaiNV = lnv.maLoaiNV
                WHERE username = ? AND password = ?
                """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String maNV = rs.getString("maNhanVien");
                String hoTen = rs.getString("hoTen");
                String gioiTinh = rs.getString("gioiTinh");
                String sdt = rs.getString("SDT");
                int tuoi = rs.getInt("tuoi");
                String maLNV = rs.getString("maLoaiNV");
                String tenLNV = rs.getString("tenLoaiNV");

                LoaiNhanVien lnv = new LoaiNhanVien(maLNV, tenLNV);
                return new NhanVien(maNV, username, password, hoTen, gioiTinh, sdt, tuoi, lnv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean add(NhanVien nv) {
        String sql = """
                INSERT INTO NhanVien (maNhanVien, username, password, hoTen, gioiTinh, SDT, tuoi, maLoaiNV)
                VALUES (?,?,?,?,?,?,?,?)
                """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nv.getMaNhanVien());
            stmt.setString(2, nv.getUsername());
            stmt.setString(3, nv.getPassword());
            stmt.setString(4, nv.getHoTen());
            stmt.setString(5, nv.getGioiTinh());
            stmt.setString(6, nv.getSDT());
            stmt.setInt(7, nv.getTuoi());
            stmt.setString(8, nv.getLnv().getMaLoaiNV());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(NhanVien nv) {
        String sql = """
                UPDATE NhanVien
                SET username = ?, password = ?, hoTen = ?, gioiTinh = ?, SDT = ?, tuoi = ?, maLoaiNV = ?
                WHERE maNhanVien = ?
                """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nv.getUsername());
            stmt.setString(2, nv.getPassword());
            stmt.setString(3, nv.getHoTen());
            stmt.setString(4, nv.getGioiTinh());
            stmt.setString(5, nv.getSDT());
            stmt.setInt(6, nv.getTuoi());
            stmt.setString(7, nv.getLnv().getMaLoaiNV());
            stmt.setString(8, nv.getMaNhanVien());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static NhanVien getMaNV(String ma) {
        String sql = """
                SELECT nv.*, lnv.maLoaiNV, lnv.tenLoaiNV
                FROM NhanVien nv
                JOIN LoaiNhanVien lnv ON nv.maLoaiNV = lnv.maLoaiNV
                WHERE nv.maNhanVien = ?
                """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, ma);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String maNV = rs.getString("maNhanVien");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String hoTen = rs.getString("hoTen");
                String gioiTinh = rs.getString("gioiTinh");
                String sdt = rs.getString("SDT");
                int tuoi = rs.getInt("tuoi");
                String maLNV = rs.getString("maLoaiNV");
                String tenLNV = rs.getString("tenLoaiNV");

                LoaiNhanVien lnv = new LoaiNhanVien(maLNV, tenLNV);
                return new NhanVien(maNV, username, password, hoTen, gioiTinh, sdt, tuoi, lnv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean hide(String maNV) {
        String sql = "UPDATE NhanVien SET visible = 0 WHERE maNhanVien = ?";

        try{
        	Connection con = ConnectDB.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maNV);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean restore(String maNV) {
        String sql = "UPDATE NhanVien SET visible = 1 WHERE maNhanVien = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maNV);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static ArrayList<NhanVien> getNVAn() {
        String sql = """
                SELECT nv.maNhanVien, nv.username, nv.password, nv.hoTen, nv.gioiTinh, nv.SDT, nv.tuoi,
                       lnv.maLoaiNV, lnv.tenLoaiNV
                FROM NhanVien nv
                JOIN LoaiNhanVien lnv ON nv.maLoaiNV = lnv.maLoaiNV
                WHERE nv.visible = 0
                """;

        ArrayList<NhanVien> dsNV = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maNV = rs.getString("maNhanVien");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String hoTen = rs.getString("hoTen");
                String gioiTinh = rs.getString("gioiTinh");
                String sdt = rs.getString("SDT");
                int tuoi = rs.getInt("tuoi");
                String maLNV = rs.getString("maLoaiNV");
                String tenLNV = rs.getString("tenLoaiNV");

                LoaiNhanVien lnv = new LoaiNhanVien(maLNV, tenLNV);
                NhanVien nv = new NhanVien(maNV, username, password, hoTen, gioiTinh, sdt, tuoi, lnv);
                dsNV.add(nv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách nhân viên");
        }
        return dsNV;
    }
    public static String getNextNhanVienId() {
        String sql = "SELECT TOP 1 maNhanVien FROM NhanVien ORDER BY maNhanVien DESC";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("maNhanVien");
                int num = Integer.parseInt(lastId.substring(2)) + 1;
                return String.format("NV%02d", num);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi tạo mã NV tiếp theo: " + e.getMessage());
        }

        return "NV01";
    }
}
