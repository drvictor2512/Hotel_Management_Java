package entity;

import java.util.Objects;

public class ChiTietDichVu {
    private String maDP;
    private String maDV;
    private int soLuong;
    public ChiTietDichVu(String maDP, String maDV, int soLuong) {
		this.maDP = maDP;
		this.maDV = maDV;
		this.soLuong = soLuong;
	}

	public String getMaDP() {
		return maDP;
	}

	public void setMaDP(String maDP) {
		this.maDP = maDP;
	}

	public String getMaDV() {
		return maDV;
	}

	public void setMaDV(String maDV) {
		this.maDV = maDV;
	}

	public ChiTietDichVu() {
    	super();
    }

	public int getSoLuong() {
		
		return soLuong;
	}

	public void setSoLuong(int soLuong)  {
		if (soLuong <0 ) {
			throw new IllegalArgumentException("Số lượng phải lớn hơn hoặc bằng 0!");
		}
		this.soLuong = soLuong;
	}
 }
