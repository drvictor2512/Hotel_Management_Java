package entity;

public class DichVu {
    private String maDV;
    private String tenDV;
    private double giaDV;
    private String maNhanVien;
    private String donViTinh;  
    
    public DichVu(String maDV, String tenDV, double giaDV, String maNhanVien, String donViTinh) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.giaDV = giaDV;
        this.maNhanVien = maNhanVien;
        this.donViTinh = donViTinh;
    }

    public String getMaDV() {
        return maDV;
    }
    
    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }
    
    public String getTenDV() {
        return tenDV;
    }
    
    public void setTenDV(String tenDV) {
        this.tenDV = tenDV;
    }
    
    public double getGiaDV() {
        if(giaDV < 0) {
            throw new IllegalArgumentException("Giá dịch vụ phải lớn hơn 0!");
        }
        return giaDV;
    }
    
    public void setGiaDV(double giaDV) {
        this.giaDV = giaDV;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
    
    // THÊM MỚI
    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }
}