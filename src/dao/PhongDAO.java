package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import entity.LoaiPhong;
import entity.Phong;
import connectDB.ConnectDB;

public class PhongDAO {
	public static ArrayList<Phong> getDSPhong() {
		String sql = """
			SELECT p.maPhong, p.tenPhong, p.tang, p.trangThai, p.donGia, p.maNhanVien, p.maLoaiPhong, lp.tenLoaiPhong
			FROM Phong p
			JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
			WHERE p.visible = 1
	    """;
        ArrayList<Phong> dsPhong = new ArrayList<Phong>();
        
        try {
			Connection con = ConnectDB.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
					String maP = rs.getString("maPhong");
					String maLP = rs.getString("maLoaiPhong");
					String loaiP = rs.getString("tenLoaiPhong");
					String tenP = rs.getString("tenPhong");
					int tangP = rs.getInt("tang");
					String ttP = rs.getString("trangThai");
					double giaP = rs.getDouble("donGia");
					String maNV = rs.getString("maNhanVien");
					
					LoaiPhong lp = new LoaiPhong(maLP, loaiP);
	                Phong p = new Phong(maP, tenP, tangP, ttP, giaP, lp, maNV);
	                dsPhong.add(p);
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách phòng");
		}
		return dsPhong;
    }
	
	public static ArrayList<Phong> getDSPhongDat() {
		String sql = """
			SELECT p.maPhong, p.tenPhong, p.tang, p.trangThai, p.donGia, p.maNhanVien, p.maLoaiPhong, lp.tenLoaiPhong
			FROM Phong p
			JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
			WHERE p.visible = 1 AND p.trangThai = N'Đã đặt' OR p.trangThai = N'Đang thuê'
	    """;
        ArrayList<Phong> dsPhong = new ArrayList<Phong>();
        
        try {
			Connection con = ConnectDB.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
					String maP = rs.getString("maPhong");
					String maLP = rs.getString("maLoaiPhong");
					String loaiP = rs.getString("tenLoaiPhong");
					String tenP = rs.getString("tenPhong");
					int tangP = rs.getInt("tang");
					String ttP = rs.getString("trangThai");
					double giaP = rs.getDouble("donGia");
					String maNV = rs.getString("maNhanVien");
					
					LoaiPhong lp = new LoaiPhong(maLP, loaiP);
	                Phong p = new Phong(maP, tenP, tangP, ttP, giaP, lp, maNV);
	                dsPhong.add(p);
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách phòng");
		}
		return dsPhong;
    }

	public static boolean add(Phong p){
	    String sql = "INSERT INTO Phong (maPhong, maLoaiPhong, tenPhong, tang, trangThai, donGia, maNhanVien) VALUES (?, ?, ?, ?, ?, ?, ?)";
	    try {
	        Connection con = ConnectDB.getConnection();
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, p.getMaPhong());
	        stmt.setString(2, p.getLoaiPhong().getMaLoaiPhong());
	        stmt.setString(3, p.getTenPhong());
	        stmt.setInt(4, p.getTang());
	        stmt.setString(5, p.getTrangThai());
	        stmt.setDouble(6, p.getDonGia());
	        stmt.setString(7, p.getMaNhanVien());
	        stmt.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	
	public static boolean update(Phong p) {
		String sql = "UPDATE Phong SET maLoaiPhong = ?, tenPhong = ?, tang = ?, trangThai = ?, donGia = ? WHERE maPhong = ?";
		try {
			Connection con = ConnectDB.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			
			stmt.setString(1, p.getLoaiPhong().getMaLoaiPhong());
			stmt.setString(2, p.getTenPhong());
			stmt.setInt(3, p.getTang());
			stmt.setString(4, p.getTrangThai());
			stmt.setDouble(5, p.getDonGia());
			stmt.setString(6, p.getMaPhong());
			stmt.executeUpdate();
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Phong getMaP(String ma) {
		String sql = """
				SELECT p.maPhong, p.tenPhong, p.tang, p.trangThai, p.donGia, p.maNhanVien, p.maLoaiPhong, lp.tenLoaiPhong
				FROM Phong p
				JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
				WHERE p.maPhong = ? AND p.visible = 1
		    """;
		try {
			Connection con = ConnectDB.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, ma);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String maP = rs.getString("maPhong");
				String maLP = rs.getString("maLoaiPhong");
				String loaiP = rs.getString("tenLoaiPhong");
				String tenP = rs.getString("tenPhong");
				int tangP = rs.getInt("tang");
				String ttP = rs.getString("trangThai");
				double giaP = rs.getDouble("donGia");
				String maNV = rs.getString("maNhanVien");
				LoaiPhong lp = new LoaiPhong(maLP, loaiP);
                Phong p = new Phong(maP, tenP, tangP, ttP, giaP, lp, maNV);
                return p;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getNextPhongId() {
        String sql = "SELECT TOP 1 maPhong FROM Phong ORDER BY maPhong DESC";
        try (	Connection con = ConnectDB.getConnection();
        		Statement st = con.createStatement();
        		ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("maPhong"); 
                int num = Integer.parseInt(lastId.substring(2)) + 1;
                return String.format("P%03d", num);
            } else {
                return "P001"; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        return "P001";
    }

	// Cập nhật tình trạng phòng
	public static void capNhatTinhTrangPhong(String maPhong, String tinhTrangMoi) {
	    String sql = "UPDATE Phong SET trangThai = ? WHERE maPhong = ?";
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setString(1, tinhTrangMoi);
	        stmt.setString(2, maPhong);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	public static ArrayList<Object[]> layPhongTrongTheoNgay(Date ngayNhan, Date ngayTra){
	    ArrayList<Object[]> ds = new ArrayList<Object[]>();
	    String sql = """
	        SELECT p.maPhong, p.tenPhong, lp.tenLoaiPhong, p.donGia
	        FROM Phong p
	        JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
	        WHERE p.maPhong NOT IN (
	            SELECT maPhong
	            FROM ChiTietDonDatPhong
	            WHERE NOT (ngayTraDuKien <= ? OR ngayNhan >= ?)
	            AND trangThai NOT IN (N'Đã hủy')
	        ) AND p.visible = 1
	    """;

	    try {
	        Connection con = ConnectDB.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setDate(1, ngayNhan);
	        ps.setDate(2, ngayTra);
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	            ds.add(new Object[] {
	                rs.getString("maPhong"),
	                rs.getString("tenPhong"),
	                rs.getString("tenLoaiPhong"),
	                rs.getDouble("donGia")
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}	
	
	@Deprecated
	public static ArrayList<Object[]> layPhongDangDat(){
	    ArrayList<Object[]> ds = new ArrayList<Object[]>();
	    String sql = """
	        SELECT p.maPhong, p.tenPhong, p.tenLoaiPhong, p.donGia, kh.maKH, nv.maNhanVien, 
	               ctdp.ngayNhan, ctdp.ngayTraDuKien, ctdp.tienCoc, km.mucGiamGia,
	               ISNULL(SUM(dv.giaDV * ctdv.soLuong), 0) AS tongTienDV
	        FROM Phong p
	        JOIN ChiTietDonDatPhong ctdp ON ctdp.maPhong = p.maPhong
	        JOIN DonDatPhong dp ON ctdp.maDatPhong = dp.maDatPhong
	        JOIN KhachHang kh ON dp.maKH = kh.maKH
	        JOIN NhanVien nv ON nv.maNhanVien = dp.maNhanVien
	        LEFT JOIN ChiTietDichVu ctdv ON ctdv.maDatPhong = dp.maDatPhong
	        LEFT JOIN DichVu dv ON dv.maDV = ctdv.maDV
	        LEFT JOIN KhuyenMai km ON km.maKM = dp.maKM
	        WHERE p.trangThai = 'Đang thuê'
	        GROUP BY p.maPhong, p.tenPhong, p.tenLoaiPhong, p.donGia, kh.maKH, 
	                 nv.maNhanVien, ctdp.ngayNhan, ctdp.ngayTraDuKien, ctdp.tienCoc, km.mucGiamGia
	    """;

	    try {
	        Connection con = ConnectDB.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	            ds.add(new Object[] {
	                rs.getString("maPhong"),           // 0
	                rs.getString("tenPhong"),          // 1
	                rs.getString("tenLoaiPhong"),      // 2
	                rs.getDouble("donGia"),            // 3
	                rs.getString("maKH"),              // 4
	                rs.getString("maNhanVien"),        // 5
	                rs.getDate("ngayNhan"),            // 6
	                rs.getDate("ngayTraDuKien"),       // 7
	                rs.getDouble("tienCoc"),           // 8
	                rs.getDouble("mucGiamGia"),
	                rs.getDouble("tongTienDV")         // 10 - Tổng tiền dịch vụ đã tính sẵn
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ds;
	}
	
	public static boolean hide(String maP) {
        String sql = "UPDATE Phong SET visible = 0 WHERE maPhong = ?";

        try{
        	Connection con = ConnectDB.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maP);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean restore(String maP) {
        String sql = "UPDATE Phong SET visible = 1 WHERE maPhong = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maP);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static ArrayList<Phong> getPhongAn() {
        String sql = """
                SELECT p.maPhong, p.tenPhong, p.tang, p.trangThai, p.donGia, p.maNhanVien, p.maLoaiPhong, lp.tenLoaiPhong
        		FROM Phong p
        		JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
                WHERE p.visible = 0
                """;

        ArrayList<Phong> dsNV = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
            	String maP = rs.getString("maPhong");
				String maLP = rs.getString("maLoaiPhong");
				String loaiP = rs.getString("tenLoaiPhong");
				String tenP = rs.getString("tenPhong");
				int tangP = rs.getInt("tang");
				String ttP = rs.getString("trangThai");
				double giaP = rs.getDouble("donGia");
				String maNV = rs.getString("maNhanVien");
				
				LoaiPhong lp = new LoaiPhong(maLP, loaiP);
                Phong p = new Phong(maP, tenP, tangP, ttP, giaP, lp, maNV);
                dsNV.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách phòng");
        }
        return dsNV;
    }
	
    public static boolean ktraConDatPhong(String maP) {
        String sql = """
        		SELECT maPhong
        		FROM ChiTietDonDatPhong
        		WHERE maPhong = ? AND (trangThai = N'Đã đặt' OR trangThai = N'Đang thuê')
        		""";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maP);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            	return true;
            else return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
