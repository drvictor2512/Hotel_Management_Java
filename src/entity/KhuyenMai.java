package entity;

import java.time.LocalDate;
import java.util.Objects;

public class KhuyenMai {
	private final String maKM;
	private String tenKM;
	private double mucGiamGia;
	private String dieuKienApDung;
	private LocalDate ngayBatDau;
	private LocalDate ngayKetThuc;
	private boolean visible; // <-- THÊM THUỘC TÍNH NÀY

	public KhuyenMai(String maKM) {
		// Cập nhật lại để gọi hàm khởi tạo chính, mặc định visible = true
		this(maKM, "", 0, "", LocalDate.now(), LocalDate.now(), true);
	}

	public KhuyenMai(String maKM, String tenKM, double mucGiamGia, String dieuKienApDung, LocalDate ngayBatDau,
			LocalDate ngayKetThuc) {
		// Cập nhật lại để gọi hàm khởi tạo chính, mặc định visible = true
		this(maKM, tenKM, mucGiamGia, dieuKienApDung, ngayBatDau, ngayKetThuc, true);
	}

	// HÀM KHỞI TẠO ĐẦY ĐỦ (Fix lỗi cho DAO)
	public KhuyenMai(String maKM, String tenKM, double mucGiamGia, String dieuKienApDung, LocalDate ngayBatDau,
			LocalDate ngayKetThuc, boolean visible) {
		super();
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.mucGiamGia = mucGiamGia;
		this.dieuKienApDung = dieuKienApDung;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.visible = visible; // <-- THÊM DÒNG NÀY
	}

	public String getTenKM() {
		return tenKM;
	}

	public void setTenKM(String tenKM) {
		this.tenKM = tenKM;
	}

	public double getMucGiamGia() {
		return mucGiamGia;
	}

	public void setMucGiamGia(double mucGiamGia) {
		this.mucGiamGia = mucGiamGia;
	}

	public String getDieuKienApDung() {
		return dieuKienApDung;
	}

	public void setDieuKienApDung(String dieuKienApDung) {
		this.dieuKienApDung = dieuKienApDung;
	}

	public LocalDate getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(LocalDate ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public LocalDate getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(LocalDate ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	public String getMaKM() {
		return maKM;
	}

	// THÊM GETTER VÀ SETTER CHO VISIBLE
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	// ---------------------------------

	@Override
	public int hashCode() {
		return Objects.hash(maKM);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhuyenMai other = (KhuyenMai) obj;
		return Objects.equals(maKM, other.maKM);
	}

	@Override
	public String toString() {
		// Cập nhật lại toString
		return "khuyenMai [maKM=" + maKM + ", tenKM=" + tenKM + ", mucGiamGia=" + mucGiamGia + ", dieuKienApDung="
				+ dieuKienApDung + ", ngayBatDau=" + ngayBatDau + ", ngayKetThuc=" + ngayKetThuc + ", visible="
				+ visible + "]";
	}
}