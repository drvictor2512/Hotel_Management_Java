package entity;

public class LoaiNhanVien {
	private String maLoaiNV;
	private String tenLoaiNV;
	
	
	public LoaiNhanVien( String maLoaiNV, String tenLoaiNV) {
		this.maLoaiNV = maLoaiNV;
		this.tenLoaiNV = tenLoaiNV;
	}

	
	public String getMaLoaiNV() {
		return maLoaiNV;
	}


	public void setMaLoaiNV(String maLoaiNV) {
		this.maLoaiNV = maLoaiNV;
	}


	public String getTenLoaiNV() {
		return tenLoaiNV;
	}

	public void setTenLoaiNV(String tenLoaiNV) {
		this.tenLoaiNV = tenLoaiNV;
	}
	
	
}
