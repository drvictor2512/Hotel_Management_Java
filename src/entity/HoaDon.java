package entity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class HoaDon {
	private String maHD;
	private KhachHang khachHang;
	private NhanVien nhanVien;
	private Date ngayLap;
	private String phuongThucThanhToan;
	private double tongTien;
	private ArrayList<ChiTietHoaDon> chiTietHD;
	
	public HoaDon(String maHD, KhachHang khachHang, NhanVien nhanVien, Date ngayLap, String phuongThucThanhToan,
			double tongTien) {
		super();
		this.maHD = maHD;
		this.khachHang = khachHang;
		this.nhanVien = nhanVien;
		this.ngayLap = ngayLap;
		this.phuongThucThanhToan = phuongThucThanhToan;
		this.tongTien = tongTien;
	}

	

	public String getMaHD() {
		return maHD;
	}



	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}



	public KhachHang getKhachHang() {
		return khachHang;
	}



	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}



	public NhanVien getNhanVien() {
		return nhanVien;
	}



	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}



	public Date getNgayLap() {
		return ngayLap;
	}



	public void setNgayLap(Date ngayLap) {
		this.ngayLap = ngayLap;
	}



	public String getPhuongThucThanhToan() {
		return phuongThucThanhToan;
	}



	public void setPhuongThucThanhToan(String phuongThucThanhToan) {
		this.phuongThucThanhToan = phuongThucThanhToan;
	}



	public double getTongTien() {
		return tongTien;
	}



	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}



	public ArrayList<ChiTietHoaDon> getChiTietHD() {
		return chiTietHD;
	}



	public void setChiTietHD(ArrayList<ChiTietHoaDon> chiTietHD) {
		this.chiTietHD = chiTietHD;
	}



	public void themChiTiet(ChiTietHoaDon ct) {
	    if (chiTietHD == null) {
	    	chiTietHD = new ArrayList<>();
	    }
	    chiTietHD.add(ct);
	}
}
