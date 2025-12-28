package entity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

public class ChiTietHoaDon {
	private HoaDon hoaDon;
	private Phong phong;
	private int soNgay;
	private Date ngayNhanPhong;
	private Date ngayTraPhong;
	private double donGia;
	
	public ChiTietHoaDon(HoaDon hoaDon, Phong phong, int soNgay, Date ngayNhanPhong, Date ngayTraPhong, double donGia) {
		super();
		this.hoaDon = hoaDon;
		this.phong = phong;
		this.soNgay = soNgay;
		this.ngayNhanPhong = ngayNhanPhong;
		this.ngayTraPhong = ngayTraPhong;
		this.donGia = donGia;
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public Phong getPhong() {
		return phong;
	}

	public void setPhong(Phong phong) {
		this.phong = phong;
	}

	public int getSoNgay() {
		return soNgay;
	}

	public void setSoNgay(int soNgay) {
		this.soNgay = soNgay;
	}

	public Date getNgayNhanPhong() {
		return ngayNhanPhong;
	}

	public void setNgayNhanPhong(Date ngayNhanPhong) {
		this.ngayNhanPhong = ngayNhanPhong;
	}

	public Date getNgayTraPhong() {
		return ngayTraPhong;
	}

	public void setNgayTraPhong(Date ngayTraPhong) {
		this.ngayTraPhong = ngayTraPhong;
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}

	@Override
	public String toString() {
		return "ChiTietHoaDon [hoaDon=" + hoaDon + ", phong=" + phong + ", soNgay=" + soNgay + ", ngayNhanPhong="
				+ ngayNhanPhong + ", ngayTraPhong=" + ngayTraPhong + ", donGia=" + donGia + "]";
	}
	
	
}
