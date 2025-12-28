package entity;

public class NhanVien {
	private final String maNhanVien;
	private String username;
	private String password;
	private String hoTen;
	private String gioiTinh;
	private String SDT;
	private int tuoi;
	private LoaiNhanVien lnv;


	public NhanVien(String maNhanVien, String username, String password, String hoTen, String gioiTinh, String sDT,
			int tuoi, LoaiNhanVien lnv) {
		this.maNhanVien = maNhanVien;
		this.username = username;
		this.password = password;
		this.hoTen = hoTen;
		this.gioiTinh = gioiTinh;
		SDT = sDT;
		this.tuoi = tuoi;
		this.lnv = lnv;
	}

	public String getHoTen() {
		return hoTen;
	}
	
	public LoaiNhanVien getLnv() {
		return lnv;
	}

	public void setLnv(LoaiNhanVien lnv) {
		this.lnv = lnv;
	}

	public void setHoTen(String hoTen) {
		if(hoTen.isEmpty()) {
			throw new IllegalArgumentException("Họ tên không được để trống");
		}
		this.hoTen = hoTen;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if(username.isEmpty()) {
			throw new IllegalArgumentException("username không được để trống");
		}
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		if(password.isEmpty()) {
			throw new IllegalArgumentException("password không được để trống");
		}
		this.password = password;
	}
	public String getGioiTinh() {
		return gioiTinh;
	}
	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}
	public String getSDT() {
		return SDT;
	}
	public void setSDT(String sDT) {
		if(sDT == null || !sDT.matches("\\d{10}")) {
			throw new IllegalArgumentException("Số điện thoại phải gồm đúng 10 chữ số");
		}
		SDT = sDT;
	}
	public int getTuoi() {
		return tuoi;
	}
	public void setTuoi(int tuoi) {
		if(tuoi < 18) {
			throw new IllegalArgumentException("Tuổi phải từ 18 trở lên");
		}
		this.tuoi = tuoi;
	}
	public String getMaNhanVien() {
		return maNhanVien;
	}
}
