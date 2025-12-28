package entity;
import java.util.Objects;

public class KhachHang {
	private String maKH;
	private String CCCD;
	private String hoTen;
	private int tuoi;
	private String SDT;
	private String gioiTinh;
	private String maNhanVien;
	public KhachHang(String maKH, String cCCD, String hoTen, int tuoi, String sDT, String gioiTinh, String maNhanVien) {
		this.maKH = maKH;
		CCCD = cCCD;
		this.hoTen = hoTen;
		this.tuoi = tuoi;
		SDT = sDT;
		this.gioiTinh = gioiTinh;
		this.maNhanVien = maNhanVien;
	}
	
	public KhachHang() {
		super();
		
	}
	
	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}

	public String getCCCD() {
		return CCCD;
	}

	public String getMaKH() {
		return maKH;
	}
	public void setMaKH(String maKH)  {
		if (maKH == null|| !maKH.matches("^KH\\d{3}$") ) {
			throw new IllegalArgumentException("Mã khách hàng phải có định dạng KHxxx (ví dụ: KH001)");
		}
		this.maKH = maKH;
	}
	 public void setCCCD(String CCCD)  {
	        if (CCCD == null || !CCCD.matches("\\d{12}")) {
	            throw new IllegalArgumentException("CCCD phải gồm đúng 12 chữ số!");
	        }
	        this.CCCD = CCCD;
	    }

	    public String getHoTen() {
	        return hoTen;
	    }

	    public void setHoTen(String hoTen)  {
	        if (hoTen == null || hoTen.trim().isEmpty()) {
	            throw new IllegalArgumentException("Họ tên không được để trống!");
	        }
	        this.hoTen = hoTen.trim();
	    }

	    public int getTuoi() {
	        return tuoi;
	    }

	    public void setTuoi(int tuoi)  {
	        if (tuoi <= 0) {
	            throw new IllegalArgumentException("Tuổi phải lớn hơn 0!");
	        }
	        this.tuoi = tuoi;
	    }

	    public String getSDT() {
	        return SDT;
	    }

	    public void setSDT(String SDT) {
	        if (SDT == null || !SDT.matches("\\d{10}")) {
	            throw new IllegalArgumentException("Số điện thoại phải gồm đúng 10 chữ số!");
	        }
	        this.SDT = SDT;
	    }

	    public String getGioiTinh() {
	        return gioiTinh;
	    }

	    public void setGioiTinh(String gioiTinh)  {
	        if (!gioiTinh.equalsIgnoreCase("Nam") && !gioiTinh.equalsIgnoreCase("Nữ")) {
	            throw new IllegalArgumentException("Giới tính chỉ được phép là 'Nam' hoặc 'Nữ'!");
	        }
	        this.gioiTinh = gioiTinh;
	    }


	@Override
	public int hashCode() {
		return Objects.hash(CCCD, SDT, gioiTinh, hoTen, maKH, tuoi);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhachHang other = (KhachHang) obj;
		return Objects.equals(CCCD, other.CCCD) && Objects.equals(SDT, other.SDT)
				&& Objects.equals(gioiTinh, other.gioiTinh) && Objects.equals(hoTen, other.hoTen)
				&& Objects.equals(maKH, other.maKH) && tuoi == other.tuoi;
	}
	
	
	

}
