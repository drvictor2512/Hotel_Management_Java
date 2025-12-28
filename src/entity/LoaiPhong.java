package entity;

public class LoaiPhong {
	private String maLoaiPhong;
	private String tenLoaiPhong;

	public LoaiPhong(String maLoaiPhong, String tenLoaiPhong) {
		this.maLoaiPhong = maLoaiPhong;
		this.tenLoaiPhong = tenLoaiPhong;
	}
	
	public String getMaLoaiPhong() {
		return maLoaiPhong;
	}

	public void setMaLoaiPhong(String maLoaiPhong) {
		this.maLoaiPhong = maLoaiPhong;
	}

	public String getTenLoaiPhong() {
		return tenLoaiPhong;
	}

	public void setTenLoaiPhong(String tenLoaiPhong) {
		this.tenLoaiPhong = tenLoaiPhong;
	}
	
	
}
