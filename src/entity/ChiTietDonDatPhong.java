package entity;

import java.sql.Date;
import java.time.LocalDate;

public class ChiTietDonDatPhong {
	private DonDatPhong donDatPhong;
	private Phong phong;
	private Date ngayDat;
	private Date ngayNhan;
	private Date ngayTraDuKien;
	private double tienCoc;
	public ChiTietDonDatPhong(DonDatPhong donDatPhong, Phong phong, Date ngayDat, Date ngayNhan, Date ngayTraDuKien,
			double tienCoc) {
		this.donDatPhong = donDatPhong;
		this.phong = phong;
		this.ngayDat = ngayDat;
		this.ngayNhan = ngayNhan;
		this.ngayTraDuKien = ngayTraDuKien;
		this.tienCoc = tienCoc;
	}
	public DonDatPhong getDonDatPhong() {
		return donDatPhong;
	}
	public void setDonDatPhong(DonDatPhong donDatPhong) {
		this.donDatPhong = donDatPhong;
	}
	public Phong getPhong() {
		return phong;
	}
	public void setPhong(Phong phong) {
		this.phong = phong;
	}
	public Date getNgayDat() {
		return ngayDat;
	}
	public void setNgayDat(Date ngayDat) {
		this.ngayDat = ngayDat;
	}
	public Date getNgayNhan() {
		return ngayNhan;
	}
	public void setNgayNhan(Date ngayNhan) {
		this.ngayNhan = ngayNhan;
	}
	public Date getNgayTraDuKien() {
		return ngayTraDuKien;
	}
	public void setNgayTraDuKien(Date ngayTraDuKien) {
		this.ngayTraDuKien = ngayTraDuKien;
	}
	public double getTienCoc() {
		return tienCoc;
	}
	public void setTienCoc(double tienCoc) {
		this.tienCoc = tienCoc;
	}
	
	
	
	
}
