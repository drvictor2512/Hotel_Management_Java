package entity;

import java.util.Objects;

public class Phong {
    private final String maPhong;
    private String tenPhong;
    private int tang;
    private String trangThai;
    private double donGia;
    private LoaiPhong loaiPhong;
    private String maNhanVien;
	public Phong(String maPhong, String tenPhong, int tang, String trangThai, double donGia, LoaiPhong loaiPhong,
			String maNhanVien) {
		this.maPhong = maPhong;
		this.tenPhong = tenPhong;
		this.tang = tang;
		this.trangThai = trangThai;
		this.donGia = donGia;
		this.loaiPhong = loaiPhong;
		this.maNhanVien = maNhanVien;
	}

	public String getTenPhong() {
		return tenPhong;
	}

	public void setTenPhong(String tenPhong) {
		this.tenPhong = tenPhong;
	}

	public int getTang() {
		return tang;
	}

	public void setTang(int tang) {
		this.tang = tang;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}


	public LoaiPhong getLoaiPhong() {
		return loaiPhong;
	}

	public void setLoaiPhong(LoaiPhong loaiPhong) {
		this.loaiPhong = loaiPhong;
	}

	public String getMaPhong() {
		return maPhong;
	}
	
	public String getMaNhanVien() {
		return maNhanVien;
	}
	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}
	@Override
    public int hashCode() {
        return Objects.hash(maPhong);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Phong other = (Phong) obj;
        return Objects.equals(maPhong, other.maPhong);
    }

    @Override
    public String toString() {
        return String.format("Phong[maPhong=%s, tenPhong=%s, tang=%d, trangThai=%s, donGia=%.2f]",
                maPhong, tenPhong, tang, trangThai, donGia);
    }
}
