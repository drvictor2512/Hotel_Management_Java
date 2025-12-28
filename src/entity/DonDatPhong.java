package entity;

import java.util.ArrayList;

public class DonDatPhong {
	private String maDatPhong;
	private NhanVien nv;
	private KhachHang khachHang;
	private KhuyenMai khuyenMai;
	private ArrayList<ChiTietDonDatPhong> chiTietDon;
	
	public DonDatPhong(String maDatPhong, NhanVien nv, KhachHang khachHang, KhuyenMai khuyenMai) {
		this.maDatPhong = maDatPhong;
		this.nv = nv;
		this.khachHang = khachHang;
		this.khuyenMai = khuyenMai;
		this.chiTietDon = new ArrayList<>();
	}
	public String getMaDatPhong() {
		return maDatPhong;
	}
	public void setMaDatPhong(String maDatPhong) {
		this.maDatPhong = maDatPhong;
	}
	public NhanVien getNv() {
		return nv;
	}
	public void setNv(NhanVien nv) {
		this.nv = nv;
	}
	public KhachHang getKhachHang() {
		return khachHang;
	}
	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}
	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}
	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}
	public ArrayList<ChiTietDonDatPhong> getChiTietDon() {
		return chiTietDon;
	}
	public void setChiTietDon(ArrayList<ChiTietDonDatPhong> chiTietDon) {
		this.chiTietDon = chiTietDon;
	}
	public void themChiTiet(ChiTietDonDatPhong ctdp) {
	    if (chiTietDon == null) {
	        chiTietDon = new ArrayList<>();
	    }
	    chiTietDon.add(ctdp);
	}
}
