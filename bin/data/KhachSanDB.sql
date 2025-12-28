CREATE DATABASE DBKhachSan;
GO
USE DBKhachSan;
GO
CREATE TABLE LoaiNhanVien (
	maLoaiNV VARCHAR(10) PRIMARY KEY,
    tenLoaiNV NVARCHAR(50) 
);
ALTER TABLE LoaiNhanVien ADD visible BIT NOT NULL DEFAULT 1;
CREATE TABLE NhanVien (
    maNhanVien VARCHAR(10) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    hoTen NVARCHAR(100) NOT NULL,
    gioiTinh NVARCHAR(5),
    SDT VARCHAR(15),
    tuoi INT,
);
ALTER TABLE NhanVien ADD visible BIT NOT NULL DEFAULT 1;

ALTER TABLE NhanVien ADD maLoaiNV VARCHAR(10) ;
ALTER TABLE NhanVien ADD CONSTRAINT FK_NV_LNV FOREIGN KEY (maLoaiNV) REFERENCES LoaiNhanVien(maLoaiNV);

CREATE TABLE KhachHang (
    maKH VARCHAR(10) PRIMARY KEY,
    CCCD VARCHAR(12) UNIQUE NOT NULL,
    hoTen NVARCHAR(100) NOT NULL,
    tuoi INT,
    SDT VARCHAR(15),
    gioiTinh NVARCHAR(5)
);
ALTER TABLE KhachHang ADD visible BIT NOT NULL DEFAULT 1;

ALTER TABLE KhachHang ADD maNhanVien VARCHAR(10)
ALTER TABLE KhachHang ADD CONSTRAINT FK_KH_NV FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien);

CREATE TABLE LoaiPhong (
	maLoaiPhong VARCHAR(10) PRIMARY KEY,
    tenLoaiPhong NVARCHAR(50) 
);
ALTER TABLE LoaiPhong ADD visible BIT NOT NULL DEFAULT 1;
CREATE TABLE Phong (
    maPhong VARCHAR(10) PRIMARY KEY,
    tenPhong NVARCHAR(100),
    tang INT,
    trangThai NVARCHAR(20),
    donGia DECIMAL(10,2) NOT NULL,
);
ALTER TABLE Phong ADD visible BIT NOT NULL DEFAULT 1;
ALTER TABLE Phong ADD maLoaiPhong VARCHAR(10) ;
ALTER TABLE Phong ADD CONSTRAINT FK_P_LP FOREIGN KEY (maLoaiPhong) REFERENCES LoaiPhong(maLoaiPhong);
ALTER TABLE Phong ADD maNhanVien VARCHAR(10);
ALTER TABLE Phong ADD CONSTRAINT FK_P_NV FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien);

CREATE TABLE KhuyenMai (
    maKM VARCHAR(10) PRIMARY KEY,
    tenKM NVARCHAR(100) NOT NULL,
    mucGiamGia DECIMAL(5,2),
    dieuKienApDung NVARCHAR(255),
    ngayBatDau DATE,
    ngayKetThuc DATE,
);
ALTER TABLE KhuyenMai
ADD maNhanVien VARCHAR(10);
ALTER TABLE KhuyenMai ADD visible BIT NOT NULL DEFAULT 1;
ALTER TABLE KhuyenMai ADD CONSTRAINT FK_KM_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien);

CREATE TABLE DonDatPhong (
    maDatPhong VARCHAR(10) PRIMARY KEY,
    maNhanVien VARCHAR(10),
    maKH VARCHAR(10),
    maKM VARCHAR(10) NULL,
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM)
);

CREATE TABLE ChiTietDonDatPhong (
    maDatPhong VARCHAR(10),
    maPhong VARCHAR(10),
	ngayDat DATE,
    ngayNhan DATE,
    ngayTraDuKien DATE,
    tienCoc DECIMAL(10,2),
    PRIMARY KEY (maDatPhong, maPhong),
    FOREIGN KEY (maDatPhong) REFERENCES DonDatPhong(maDatPhong),
    FOREIGN KEY (maPhong) REFERENCES Phong(maPhong)
);
ALTER TABLE ChiTietDonDatPhong
ADD trangThai NVARCHAR(20) NOT NULL DEFAULT N'Đã đặt';

CREATE TABLE HoaDon (
    maHD VARCHAR(10) PRIMARY KEY,
    maKH VARCHAR(10),
    maNhanVien VARCHAR(10),
    ngayLap DATE,
    phuongThucThanhToan NVARCHAR(20),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);
ALTER TABLE HoaDon ADD tongTien DECIMAL(12,2) DEFAULT 0;

CREATE TABLE DichVu (
    maDV VARCHAR(10) PRIMARY KEY,
    tenDV NVARCHAR(100) NOT NULL, 
    giaDV DECIMAL(10,2) NOT NULL,
);
ALTER TABLE DichVu ADD visible BIT NOT NULL DEFAULT 1;
ALTER TABLE DichVu ADD maNhanVien VARCHAR(10)
ALTER TABLE DichVu
ADD CONSTRAINT FK_DV_NV
FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien);

CREATE TABLE ChiTietDichVu (
    maDatPhong VARCHAR(10),
    maDV VARCHAR(10),
    soLuong INT NOT NULL,
    PRIMARY KEY (maDatPhong, maDV),
    FOREIGN KEY (maDatPhong) REFERENCES DonDatPhong(maDatPhong),
    FOREIGN KEY (maDV) REFERENCES DichVu(maDV)
);

CREATE TABLE ChiTietHoaDon (
    maHD VARCHAR(10),
    maPhong VARCHAR(10),
    soNgay INT,
    ngayNhanPhong DATETIME,
    ngayTraPhong DATETIME,
	donGia DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (maHD, maPhong),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    FOREIGN KEY (maPhong) REFERENCES Phong(maPhong)
);

INSERT INTO LoaiNhanVien (maLoaiNV, tenLoaiNV, visible)
VALUES ('LNV01' , N'Lễ tân', 1),('LNV02', N'Quản lý', 1);
INSERT INTO NhanVien (maNhanVien, username, password, hoTen, gioiTinh, SDT, tuoi, maLoaiNV, visible)
VALUES
('NV01', 'letan1', '123456', N'Nguyễn Thanh Hoàng', 'Nam', '0901234567', 24, 'LNV01', 1),
('NV02', 'letan2', '123456', N'Đỗ Minh Thuận', 'Nam', '0902345678', 26, 'LNV01', 1),
('NV03', 'letan3', '123456', N'Hồ Văn Phúc', 'Nam', '0903456789', 25, 'LNV01', 1),
('NV04', 'quanly', 'admin123', N'Trần Văn Thắng', 'Nam', '0904567890', 28, 'LNV02', 1);
ALTER TABLE DichVu ADD isVisible BIT NOT NULL DEFAULT 1;
ALTER TABLE DichVu ADD donViTinh NVARCHAR(20);

-- Cập nhật dữ liệu mẫu
UPDATE DichVu SET donViTinh = N'Chai' WHERE maDV = 'DV001'; -- Nước suối
UPDATE DichVu SET donViTinh = N'Lượt' WHERE maDV = 'DV002'; -- Hồ bơi
UPDATE DichVu SET donViTinh = N'Chai' WHERE maDV = 'DV003'; -- Coca Cola
UPDATE DichVu SET donViTinh = N'Chai' WHERE maDV = 'DV004'; -- Bia Tiger
UPDATE DichVu SET donViTinh = N'Kg' WHERE maDV = 'DV005';   -- Giặt ủi
UPDATE DichVu SET donViTinh = N'Lượt' WHERE maDV = 'DV006'; -- Đưa đón sân bay
UPDATE DichVu SET donViTinh = N'Suất' WHERE maDV = 'DV007'; -- Buffet sáng
ALTER TABLE KhachHang ADD isVisible BIT NOT NULL DEFAULT 1;
INSERT INTO KhachHang(maKH, hoTen, CCCD, tuoi, SDT, gioiTinh) VALUES
('KH001', N'Lê Văn Phúc', '012345678111', 20, '0384741558', 'Nam'),
('KH002', N'Ngô Thị Anh', '056987895412', 20, '0336897845', N'Nữ'),
('KH003', N'Trần Thanh Long', '080205008959', 19, '0854712459', 'Nam'),
('KH004', N'Nguyễn Bích Ngân', '056987885988', 24, '0358458710', 'Nam'),
('KH005', N'Trần Minh Tuấn', '036589789456', 23, '0369875412', 'Nam'),
('KH006', N'Trần Minh Hiếu', '056988745213', 21, '0356987521', 'Nam'),
('KH007', N'Nguyễn Chí Tâm', '085020569874', 25, '0358745120', 'Nam'),
('KH008', N'Hà Chí Thanh', '023658789451', 21, '0856971245', 'Nam'),
('KH009', N'Lê Chuẩn', '080205669874', 23, '0123457892', 'Nam'),
('KH010', N'Bùi Ngọc Bửu', '025698789451', 22, '0399991111', 'Nam');

INSERT INTO KhuyenMai (maKM, tenKM, mucGiamGia, dieuKienApDung, ngayBatDau, ngayKetThuc) VALUES 
('KM001', N'Giảm giá mùa hè', 10.00, N'Khách thường xuyên', '2025-06-01', '2025-06-30'),

('KM002', N'Ưu đãi trẻ em', 15.00, N'Trẻ em', '2025-05-15', '2025-07-15'),

('KM003', N'Khuyến mãi lễ Quốc khánh', 20.00, N'Ngày lễ', '2025-09-01', '2025-09-05'),

('KM004', N'Mã thưởng tháng 10', 12.50, N'Mã thưởng', '2025-10-01', '2025-10-31'),

('KM005', N'Giảm giá Giáng sinh', 25.00, N'Khách thường xuyên', '2025-12-15', '2025-12-31');

INSERT INTO DichVu (maDV, tenDV, giaDV) VALUES
('DV001', N'Nước suối', 10000),
('DV002', N'Hồ bơi', 30000),
('DV003', N'Coca Cola', 15000),
('DV004', N'Bia Tiger', 20000),
('DV005', N'Giặt ủi', 50000),
('DV006', N'Đưa đón sân bay', 150000),
('DV007', N'Buffet sáng',50000)

-- Dữ liệu bảng LoaiPhong
INSERT INTO LoaiPhong (maLoaiPhong , tenLoaiPhong)
VALUES ('LP01',N'Standard'), ('LP02', N'Superior'), ('LP03', N'VIP');

-- Dữ liệu bảng Phong
INSERT INTO Phong (maPhong, tenPhong, tang, trangThai, donGia, maNhanVien, maLoaiPhong)
VALUES 
-- Standard (LP01)
('P001', N'101', '1', N'Trống', 300000, 'NV04', 'LP01'),
('P002', N'102', '1', N'Trống', 300000, 'NV04', 'LP01'),
('P003', N'103', '1', N'Trống', 300000, 'NV04', 'LP01'),
('P004', N'104', '1', N'Trống', 300000, 'NV04', 'LP01'),
('P005', N'105', '1', N'Trống', 300000, 'NV04', 'LP01'),
('P006', N'106', '1', N'Trống', 300000, 'NV04', 'LP01'),
('P007', N'107', '1', N'Trống', 300000, 'NV04', 'LP01'),
('P008', N'108', '1', N'Trống', 300000, 'NV04', 'LP01'),

('P009', N'201', '2', N'Trống', 300000, 'NV04', 'LP01'),
('P010', N'202', '2', N'Trống', 300000, 'NV04', 'LP01'),
('P011', N'203', '2', N'Trống', 300000, 'NV04', 'LP01'),
('P012', N'204', '2', N'Trống', 300000, 'NV04', 'LP01'),
('P013', N'205', '2', N'Trống', 300000, 'NV04', 'LP01'),
('P014', N'206', '2', N'Trống', 300000, 'NV04', 'LP01'),
('P015', N'207', '2', N'Trống', 300000, 'NV04', 'LP01'),
('P016', N'208', '2', N'Trống', 300000, 'NV04', 'LP01'),

-- Superior (LP02)
('P017', N'301', '3', N'Trống', 500000, 'NV04', 'LP02'),
('P018', N'302', '3', N'Trống', 500000, 'NV04', 'LP02'),
('P019', N'303', '3', N'Trống', 500000, 'NV04', 'LP02'),
('P020', N'304', '3', N'Trống', 500000, 'NV04', 'LP02'),
('P021', N'305', '3', N'Trống', 500000, 'NV04', 'LP02'),
('P022', N'306', '3', N'Trống', 500000, 'NV04', 'LP02'),
('P023', N'307', '3', N'Trống', 500000, 'NV04', 'LP02'),
('P024', N'308', '3', N'Trống', 500000, 'NV04', 'LP02'),

('P025', N'401', '4', N'Trống', 500000, 'NV04', 'LP02'),
('P026', N'402', '4', N'Trống', 500000, 'NV04', 'LP02'),
('P027', N'403', '4', N'Trống', 500000, 'NV04', 'LP02'),
('P028', N'404', '4', N'Trống', 500000, 'NV04', 'LP02'),
('P029', N'405', '4', N'Trống', 500000, 'NV04', 'LP02'),
('P030', N'406', '4', N'Trống', 500000, 'NV04', 'LP02'),
('P031', N'407', '4', N'Trống', 500000, 'NV04', 'LP02'),
('P032', N'408', '4', N'Trống', 500000, 'NV04', 'LP02'),

-- VIP (LP03)
('P033', N'501', '5', N'Trống', 1000000, 'NV04', 'LP03'),
('P034', N'502', '5', N'Trống', 1000000, 'NV04', 'LP03'),
('P035', N'503', '5', N'Trống', 1000000, 'NV04', 'LP03'),
('P036', N'504', '5', N'Trống', 1000000, 'NV04', 'LP03'),
('P037', N'505', '5', N'Trống', 1000000, 'NV04', 'LP03'),
('P038', N'506', '5', N'Trống', 1000000, 'NV04', 'LP03'),
('P039', N'507', '5', N'Trống', 1000000, 'NV04', 'LP03'),
('P040', N'508', '5', N'Trống', 1000000, 'NV04', 'LP03');

-- THÁNG 1/2025 - 6 đơn
-- ============================================================================

-- Đơn 1: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP001', 'NV01', 'KH001', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP001', 'P001', '2025-01-02', '2025-01-05', '2025-01-08', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP001', 'DV001', 4), ('DP001', 'DV003', 2);

-- Tính tổng: 3 ngày * 300,000 = 900,000 - 10% = 810,000 + dịch vụ 70,000 = 880,000 + 8% thuế = 950,400
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD001', 'KH001', 'NV01', '2025-01-08', N'Tiền mặt', 950400);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD001', 'P001', 3, '2025-01-05 14:00', '2025-01-08 12:00', 300000);

-- Đơn 2: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP002', 'NV02', 'KH002', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP002', 'P002', '2025-01-08', '2025-01-12', '2025-01-15', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP002', 'DV004', 6);

-- 900,000 + 120,000 = 1,020,000 + 8% = 1,101,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD002', 'KH002', 'NV02', '2025-01-15', N'Chuyển khoản', 1101600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD002', 'P002', 3, '2025-01-12 14:00', '2025-01-15 12:00', 300000);

-- Đơn 3: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP003', 'NV03', 'KH003', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP003', 'P017', '2025-01-10', '2025-01-15', '2025-01-18', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP003', 'DV001', 8), ('DP003', 'DV007', 2);

-- 1,500,000 - 10% = 1,350,000 + 180,000 = 1,530,000 + 8% = 1,652,400
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD003', 'KH003', 'NV03', '2025-01-18', N'Tiền mặt', 1652400);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD003', 'P017', 3, '2025-01-15 14:00', '2025-01-18 12:00', 500000);

-- Đơn 4: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP004', 'NV01', 'KH004', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP004', 'P003', '2025-01-15', '2025-01-20', '2025-01-23', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP004', 'DV002', 3);

-- 900,000 + 90,000 = 990,000 + 8% = 1,069,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD004', 'KH004', 'NV01', '2025-01-23', N'Chuyển khoản', 1069200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD004', 'P003', 3, '2025-01-20 14:00', '2025-01-23 12:00', 300000);

-- Đơn 5: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP005', 'NV02', 'KH005', 'KM002');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP005', 'P018', '2025-01-20', '2025-01-25', '2025-01-28', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP005', 'DV004', 10), ('DP005', 'DV005', 2);

-- 1,500,000 - 15% = 1,275,000 + 300,000 = 1,575,000 + 8% = 1,701,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD005', 'KH005', 'NV02', '2025-01-28', N'Tiền mặt', 1701000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD005', 'P018', 3, '2025-01-25 14:00', '2025-01-28 12:00', 500000);

-- Đơn 6: VIP
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP006', 'NV03', 'KH006', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP006', 'P033', '2025-01-25', '2025-01-28', '2025-01-31', 300000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP006', 'DV001', 12), ('DP006', 'DV007', 3), ('DP006', 'DV006', 1);

-- 3,000,000 + 470,000 = 3,470,000 + 8% = 3,747,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD006', 'KH006', 'NV03', '2025-01-31', N'Chuyển khoản', 3747600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD006', 'P033', 3, '2025-01-28 14:00', '2025-01-31 12:00', 1000000);

-- ============================================================================
-- THÁNG 2/2025 - 7 đơn
-- ============================================================================

-- Đơn 7: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP007', 'NV01', 'KH007', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP007', 'P004', '2025-02-01', '2025-02-05', '2025-02-08', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP007', 'DV003', 4), ('DP007', 'DV004', 4);

-- 900,000 - 10% = 810,000 + 140,000 = 950,000 + 8% = 1,026,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD007', 'KH007', 'NV01', '2025-02-08', N'Tiền mặt', 1026000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD007', 'P004', 3, '2025-02-05 14:00', '2025-02-08 12:00', 300000);

-- Đơn 8: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP008', 'NV02', 'KH008', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP008', 'P005', '2025-02-05', '2025-02-09', '2025-02-12', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP008', 'DV001', 6);

-- 900,000 + 60,000 = 960,000 + 8% = 1,036,800
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD008', 'KH008', 'NV02', '2025-02-12', N'Chuyển khoản', 1036800);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD008', 'P005', 3, '2025-02-09 14:00', '2025-02-12 12:00', 300000);

-- Đơn 9: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP009', 'NV03', 'KH009', 'KM002');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP009', 'P019', '2025-02-10', '2025-02-15', '2025-02-18', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP009', 'DV002', 4), ('DP009', 'DV007', 3);

-- 1,500,000 - 15% = 1,275,000 + 270,000 = 1,545,000 + 8% = 1,668,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD009', 'KH009', 'NV03', '2025-02-18', N'Tiền mặt', 1668600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD009', 'P019', 3, '2025-02-15 14:00', '2025-02-18 12:00', 500000);

-- Đơn 10: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP010', 'NV01', 'KH010', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP010', 'P006', '2025-02-15', '2025-02-18', '2025-02-21', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP010', 'DV005', 2);

-- 900,000 + 100,000 = 1,000,000 + 8% = 1,080,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD010', 'KH010', 'NV01', '2025-02-21', N'Chuyển khoản', 1080000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD010', 'P006', 3, '2025-02-18 14:00', '2025-02-21 12:00', 300000);

-- Đơn 11: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP011', 'NV02', 'KH001', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP011', 'P020', '2025-02-18', '2025-02-22', '2025-02-25', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP011', 'DV003', 8), ('DP011', 'DV004', 6);

-- 1,500,000 - 10% = 1,350,000 + 240,000 = 1,590,000 + 8% = 1,717,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD011', 'KH001', 'NV02', '2025-02-25', N'Tiền mặt', 1717200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD011', 'P020', 3, '2025-02-22 14:00', '2025-02-25 12:00', 500000);

-- Đơn 12: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP012', 'NV03', 'KH002', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP012', 'P007', '2025-02-22', '2025-02-25', '2025-02-28', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP012', 'DV001', 8);

-- 900,000 + 80,000 = 980,000 + 8% = 1,058,400
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD012', 'KH002', 'NV03', '2025-02-28', N'Chuyển khoản', 1058400);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD012', 'P007', 3, '2025-02-25 14:00', '2025-02-28 12:00', 300000);

-- Đơn 13: VIP
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP013', 'NV01', 'KH003', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP013', 'P034', '2025-02-25', '2025-02-28', '2025-03-03', 300000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP013', 'DV002', 6), ('DP013', 'DV005', 4), ('DP013', 'DV006', 1);

-- 3,000,000 - 10% = 2,700,000 + 680,000 = 3,380,000 + 8% = 3,650,400
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD013', 'KH003', 'NV01', '2025-03-03', N'Tiền mặt', 3650400);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD013', 'P034', 3, '2025-02-28 14:00', '2025-03-03 12:00', 1000000);

-- ============================================================================
-- THÁNG 3/2025 - 7 đơn
-- ============================================================================

-- Đơn 14: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP014', 'NV02', 'KH004', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP014', 'P008', '2025-03-01', '2025-03-05', '2025-03-08', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP014', 'DV007', 2);

-- 900,000 + 100,000 = 1,000,000 + 8% = 1,080,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD014', 'KH004', 'NV02', '2025-03-08', N'Chuyển khoản', 1080000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD014', 'P008', 3, '2025-03-05 14:00', '2025-03-08 12:00', 300000);

-- Đơn 15: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP015', 'NV03', 'KH005', 'KM002');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP015', 'P009', '2025-03-05', '2025-03-10', '2025-03-13', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP015', 'DV005', 1);

-- 900,000 - 15% = 765,000 + 50,000 = 815,000 + 8% = 880,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD015', 'KH005', 'NV03', '2025-03-13', N'Tiền mặt', 880200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD015', 'P009', 3, '2025-03-10 14:00', '2025-03-13 12:00', 300000);

-- Đơn 16: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP016', 'NV01', 'KH006', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP016', 'P021', '2025-03-10', '2025-03-15', '2025-03-18', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP016', 'DV001', 10), ('DP016', 'DV005', 2);

-- 1,500,000 + 200,000 = 1,700,000 + 8% = 1,836,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD016', 'KH006', 'NV01', '2025-03-18', N'Chuyển khoản', 1836000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD016', 'P021', 3, '2025-03-15 14:00', '2025-03-18 12:00', 500000);

-- Đơn 17: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP017', 'NV02', 'KH007', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP017', 'P010', '2025-03-15', '2025-03-18', '2025-03-21', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP017', 'DV002', 3), ('DP017', 'DV007', 2);

-- 900,000 - 10% = 810,000 + 190,000 = 1,000,000 + 8% = 1,080,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD017', 'KH007', 'NV02', '2025-03-21', N'Tiền mặt', 1080000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD017', 'P010', 3, '2025-03-18 14:00', '2025-03-21 12:00', 300000);

-- Đơn 18: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP018', 'NV03', 'KH008', 'KM002');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP018', 'P022', '2025-03-18', '2025-03-22', '2025-03-25', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP018', 'DV004', 12);

-- 1,500,000 - 15% = 1,275,000 + 240,000 = 1,515,000 + 8% = 1,636,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD018', 'KH008', 'NV03', '2025-03-25', N'Chuyển khoản', 1636200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD018', 'P022', 3, '2025-03-22 14:00', '2025-03-25 12:00', 500000);

-- Đơn 19: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP019', 'NV01', 'KH009', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP019', 'P011', '2025-03-22', '2025-03-25', '2025-03-28', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP019', 'DV004', 8);

-- 900,000 + 160,000 = 1,060,000 + 8% = 1,144,800
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD019', 'KH009', 'NV01', '2025-03-28', N'Tiền mặt', 1144800);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD019', 'P011', 3, '2025-03-25 14:00', '2025-03-28 12:00', 300000);

-- Đơn 20: VIP
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP020', 'NV02', 'KH010', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP020', 'P035', '2025-03-25', '2025-03-28', '2025-03-31', 300000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP020', 'DV001', 10), ('DP020', 'DV004', 12), ('DP020', 'DV007', 4);

-- 3,000,000 + 540,000 = 3,540,000 + 8% = 3,823,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD020', 'KH010', 'NV02', '2025-03-31', N'Chuyển khoản', 3823200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD020', 'P035', 3, '2025-03-28 14:00', '2025-03-31 12:00', 1000000);

-- ============================================================================
-- THÁNG 4/2025 - 6 đơn
-- ============================================================================

-- Đơn 21: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP021', 'NV03', 'KH001', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP021', 'P012', '2025-04-01', '2025-04-05', '2025-04-08', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP021', 'DV001', 5), ('DP021', 'DV003', 3);

-- 900,000 - 10% = 810,000 + 95,000 = 905,000 + 8% = 977,400
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD021', 'KH001', 'NV03', '2025-04-08', N'Tiền mặt', 977400);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD021', 'P012', 3, '2025-04-05 14:00', '2025-04-08 12:00', 300000);

-- Đơn 22: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP022', 'NV01', 'KH002', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP022', 'P013', '2025-04-08', '2025-04-12', '2025-04-15', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP022', 'DV002', 4);

-- 900,000 + 120,000 = 1,020,000 + 8% = 1,101,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD022', 'KH002', 'NV01', '2025-04-15', N'Chuyển khoản', 1101600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD022', 'P013', 3, '2025-04-12 14:00', '2025-04-15 12:00', 300000);

-- Đơn 23: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP023', 'NV02', 'KH003', 'KM002');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP023', 'P023', '2025-04-12', '2025-04-16', '2025-04-19', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP023', 'DV005', 3);

-- 1,500,000 - 15% = 1,275,000 + 150,000 = 1,425,000 + 8% = 1,539,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD023', 'KH003', 'NV02', '2025-04-19', N'Tiền mặt', 1539000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD023', 'P023', 3, '2025-04-16 14:00', '2025-04-19 12:00', 500000);

-- Đơn 24: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP024', 'NV03', 'KH004', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP024', 'P014', '2025-04-18', '2025-04-22', '2025-04-25', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP024', 'DV007', 3);

-- 900,000 + 150,000 = 1,050,000 + 8% = 1,134,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD024', 'KH004', 'NV03', '2025-04-25', N'Chuyển khoản', 1134000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD024', 'P014', 3, '2025-04-22 14:00', '2025-04-25 12:00', 300000);

-- Đơn 25: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP025', 'NV01', 'KH005', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP025', 'P024', '2025-04-22', '2025-04-26', '2025-04-29', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP025', 'DV001', 6), ('DP025', 'DV004', 4);

-- 1,500,000 - 10% = 1,350,000 + 140,000 = 1,490,000 + 8% = 1,609,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD025', 'KH005', 'NV01', '2025-04-29', N'Tiền mặt', 1609200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD025', 'P024', 3, '2025-04-26 14:00', '2025-04-29 12:00', 500000);

-- Đơn 26: VIP
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP026', 'NV02', 'KH006', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP026', 'P036', '2025-04-26', '2025-04-30', '2025-05-03', 300000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP026', 'DV002', 5), ('DP026', 'DV003', 8), ('DP026', 'DV006', 1), ('DP026', 'DV007', 3);

-- 3,000,000 - 10% = 2,700,000 + 570,000 = 3,270,000 + 8% = 3,531,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD026', 'KH006', 'NV02', '2025-05-03', N'Chuyển khoản', 3531600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD026', 'P036', 3, '2025-04-30 14:00', '2025-05-03 12:00', 1000000);

-- ============================================================================
-- THÁNG 5/2025 - 7 đơn
-- ============================================================================

-- Đơn 27: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP027', 'NV03', 'KH007', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP027', 'P015', '2025-05-01', '2025-05-05', '2025-05-08', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP027', 'DV001', 6), ('DP027', 'DV004', 4);

-- 900,000 + 140,000 = 1,040,000 + 8% = 1,123,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD027', 'KH007', 'NV03', '2025-05-08', N'Tiền mặt', 1123200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD027', 'P015', 3, '2025-05-05 14:00', '2025-05-08 12:00', 300000);

-- Đơn 28: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP028', 'NV01', 'KH008', 'KM002');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP028', 'P016', '2025-05-08', '2025-05-12', '2025-05-15', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP028', 'DV005', 2);

-- 900,000 - 15% = 765,000 + 100,000 = 865,000 + 8% = 934,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD028', 'KH008', 'NV01', '2025-05-15', N'Chuyển khoản', 934200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD028', 'P016', 3, '2025-05-12 14:00', '2025-05-15 12:00', 300000);

-- Đơn 29: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP029', 'NV02', 'KH009', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP029', 'P025', '2025-05-12', '2025-05-16', '2025-05-19', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP029', 'DV003', 5);

-- 1,500,000 + 75,000 = 1,575,000 + 8% = 1,701,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD029', 'KH009', 'NV02', '2025-05-19', N'Tiền mặt', 1701000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD029', 'P025', 3, '2025-05-16 14:00', '2025-05-19 12:00', 500000);

-- Đơn 30: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP030', 'NV03', 'KH010', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP030', 'P001', '2025-05-16', '2025-05-20', '2025-05-23', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP030', 'DV002', 3), ('DP030', 'DV007', 2);

-- 900,000 - 10% = 810,000 + 190,000 = 1,000,000 + 8% = 1,080,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD030', 'KH010', 'NV03', '2025-05-23', N'Chuyển khoản', 1080000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD030', 'P001', 3, '2025-05-20 14:00', '2025-05-23 12:00', 300000);

-- Đơn 31: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP031', 'NV01', 'KH001', 'KM002');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP031', 'P026', '2025-05-20', '2025-05-24', '2025-05-27', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP031', 'DV001', 7);

-- 1,500,000 - 15% = 1,275,000 + 70,000 = 1,345,000 + 8% = 1,452,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD031', 'KH001', 'NV01', '2025-05-27', N'Tiền mặt', 1452600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD031', 'P026', 3, '2025-05-24 14:00', '2025-05-27 12:00', 500000);

-- Đơn 32: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP032', 'NV02', 'KH002', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP032', 'P002', '2025-05-24', '2025-05-27', '2025-05-30', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP032', 'DV004', 6);

-- 900,000 + 120,000 = 1,020,000 + 8% = 1,101,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD032', 'KH002', 'NV02', '2025-05-30', N'Chuyển khoản', 1101600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD032', 'P002', 3, '2025-05-27 14:00', '2025-05-30 12:00', 300000);

-- Đơn 33: VIP
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP033', 'NV03', 'KH003', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP033', 'P037', '2025-05-27', '2025-05-30', '2025-06-02', 300000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP033', 'DV001', 12), ('DP033', 'DV004', 15), ('DP033', 'DV005', 3);

-- 3,000,000 - 10% = 2,700,000 + 570,000 = 3,270,000 + 8% = 3,531,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD033', 'KH003', 'NV03', '2025-06-02', N'Tiền mặt', 3531600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD033', 'P037', 3, '2025-05-30 14:00', '2025-06-02 12:00', 1000000);

-- ============================================================================
-- THÁNG 6/2025 - 6 đơn
-- ============================================================================

-- Đơn 34: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP034', 'NV01', 'KH004', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP034', 'P003', '2025-06-01', '2025-06-05', '2025-06-08', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP034', 'DV001', 8);

-- 900,000 - 10% = 810,000 + 80,000 = 890,000 + 8% = 961,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD034', 'KH004', 'NV01', '2025-06-08', N'Tiền mặt', 961200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD034', 'P003', 3, '2025-06-05 14:00', '2025-06-08 12:00', 300000);

-- Đơn 35: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP035', 'NV02', 'KH005', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP035', 'P004', '2025-06-08', '2025-06-12', '2025-06-15', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP035', 'DV003', 6);

-- 900,000 + 90,000 = 990,000 + 8% = 1,069,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD035', 'KH005', 'NV02', '2025-06-15', N'Chuyển khoản', 1069200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD035', 'P004', 3, '2025-06-12 14:00', '2025-06-15 12:00', 300000);

-- Đơn 36: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP036', 'NV03', 'KH006', 'KM002');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP036', 'P027', '2025-06-12', '2025-06-16', '2025-06-19', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP036', 'DV007', 4), ('DP036', 'DV004', 8);

-- 1,500,000 - 15% = 1,275,000 + 360,000 = 1,635,000 + 8% = 1,765,800
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD036', 'KH006', 'NV03', '2025-06-19', N'Tiền mặt', 1765800);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD036', 'P027', 3, '2025-06-16 14:00', '2025-06-19 12:00', 500000);

-- Đơn 37: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP037', 'NV01', 'KH007', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP037', 'P005', '2025-06-18', '2025-06-22', '2025-06-25', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP037', 'DV005', 3);

-- 900,000 + 150,000 = 1,050,000 + 8% = 1,134,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD037', 'KH007', 'NV01', '2025-06-25', N'Chuyển khoản', 1134000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD037', 'P005', 3, '2025-06-22 14:00', '2025-06-25 12:00', 300000);

-- Đơn 38: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP038', 'NV02', 'KH008', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP038', 'P028', '2025-06-22', '2025-06-26', '2025-06-29', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP038', 'DV001', 9), ('DP038', 'DV005', 3);

-- 1,500,000 - 10% = 1,350,000 + 240,000 = 1,590,000 + 8% = 1,717,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD038', 'KH008', 'NV02', '2025-06-29', N'Tiền mặt', 1717200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD038', 'P028', 3, '2025-06-26 14:00', '2025-06-29 12:00', 500000);

-- Đơn 39: VIP
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP039', 'NV03', 'KH009', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP039', 'P038', '2025-06-26', '2025-06-29', '2025-07-02', 300000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP039', 'DV002', 6), ('DP039', 'DV003', 10), ('DP039', 'DV007', 4);

-- 3,000,000 + 530,000 = 3,530,000 + 8% = 3,812,400
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD039', 'KH009', 'NV03', '2025-07-02', N'Chuyển khoản', 3812400);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD039', 'P038', 3, '2025-06-29 14:00', '2025-07-02 12:00', 1000000);

-- ============================================================================
-- THÁNG 7/2025 - 6 đơn
-- ============================================================================

-- Đơn 40: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP040', 'NV01', 'KH010', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP040', 'P006', '2025-07-01', '2025-07-05', '2025-07-08', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP040', 'DV001', 6), ('DP040', 'DV003', 4);

-- 900,000 - 10% = 810,000 + 100,000 = 910,000 + 8% = 982,800
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD040', 'KH010', 'NV01', '2025-07-08', N'Tiền mặt', 982800);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD040', 'P006', 3, '2025-07-05 14:00', '2025-07-08 12:00', 300000);

-- Đơn 41: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP041', 'NV02', 'KH001', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP041', 'P007', '2025-07-08', '2025-07-12', '2025-07-15', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP041', 'DV002', 4);

-- 900,000 + 120,000 = 1,020,000 + 8% = 1,101,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD041', 'KH001', 'NV02', '2025-07-15', N'Chuyển khoản', 1101600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD041', 'P007', 3, '2025-07-12 14:00', '2025-07-15 12:00', 300000);

-- Đơn 42: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP042', 'NV03', 'KH002', 'KM002');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP042', 'P029', '2025-07-12', '2025-07-16', '2025-07-19', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP042', 'DV004', 10);

-- 1,500,000 - 15% = 1,275,000 + 200,000 = 1,475,000 + 8% = 1,593,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD042', 'KH002', 'NV03', '2025-07-19', N'Tiền mặt', 1593000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD042', 'P029', 3, '2025-07-16 14:00', '2025-07-19 12:00', 500000);

-- Đơn 43: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP043', 'NV01', 'KH003', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP043', 'P008', '2025-07-18', '2025-07-22', '2025-07-25', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP043', 'DV007', 3);

-- 900,000 + 150,000 = 1,050,000 + 8% = 1,134,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD043', 'KH003', 'NV01', '2025-07-25', N'Chuyển khoản', 1134000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD043', 'P008', 3, '2025-07-22 14:00', '2025-07-25 12:00', 300000);

-- Đơn 44: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP044', 'NV02', 'KH004', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP044', 'P030', '2025-07-22', '2025-07-26', '2025-07-29', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP044', 'DV003', 7), ('DP044', 'DV004', 5);

-- 1,500,000 - 10% = 1,350,000 + 205,000 = 1,555,000 + 8% = 1,679,400
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD044', 'KH004', 'NV02', '2025-07-29', N'Tiền mặt', 1679400);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD044', 'P030', 3, '2025-07-26 14:00', '2025-07-29 12:00', 500000);

-- Đơn 45: VIP
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP045', 'NV03', 'KH005', 'KM003');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP045', 'P039', '2025-07-26', '2025-07-29', '2025-08-01', 300000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP045', 'DV001', 15), ('DP045', 'DV004', 12), ('DP045', 'DV006', 2), ('DP045', 'DV007', 3);

-- 3,000,000 - 20% = 2,400,000 + 840,000 = 3,240,000 + 8% = 3,499,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD045', 'KH005', 'NV03', '2025-08-01', N'Chuyển khoản', 3499200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD045', 'P039', 3, '2025-07-29 14:00', '2025-08-01 12:00', 1000000);

-- ============================================================================
-- THÁNG 8/2025 - 7 đơn
-- ============================================================================

-- Đơn 46: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP046', 'NV01', 'KH006', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP046', 'P009', '2025-08-01', '2025-08-05', '2025-08-08', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP046', 'DV001', 5), ('DP046', 'DV004', 4);

-- 900,000 + 130,000 = 1,030,000 + 8% = 1,112,400
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD046', 'KH006', 'NV01', '2025-08-08', N'Tiền mặt', 1112400);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD046', 'P009', 3, '2025-08-05 14:00', '2025-08-08 12:00', 300000);

-- Đơn 47: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP047', 'NV02', 'KH007', 'KM002');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP047', 'P010', '2025-08-08', '2025-08-12', '2025-08-15', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP047', 'DV005', 2);

-- 900,000 - 15% = 765,000 + 100,000 = 865,000 + 8% = 934,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD047', 'KH007', 'NV02', '2025-08-15', N'Chuyển khoản', 934200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD047', 'P010', 3, '2025-08-12 14:00', '2025-08-15 12:00', 300000);

-- Đơn 48: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP048', 'NV03', 'KH008', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP048', 'P031', '2025-08-12', '2025-08-16', '2025-08-19', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP048', 'DV002', 5), ('DP048', 'DV007', 3);

-- 1,500,000 + 250,000 = 1,750,000 + 8% = 1,890,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD048', 'KH008', 'NV03', '2025-08-19', N'Tiền mặt', 1890000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD048', 'P031', 3, '2025-08-16 14:00', '2025-08-19 12:00', 500000);

-- Đơn 49: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP049', 'NV01', 'KH009', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP049', 'P011', '2025-08-18', '2025-08-22', '2025-08-25', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP049', 'DV001', 8), ('DP049', 'DV003', 2);

-- 900,000 - 10% = 810,000 + 100,000 = 910,000 + 8% = 982,800
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD049', 'KH009', 'NV01', '2025-08-25', N'Chuyển khoản', 982800);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD049', 'P011', 3, '2025-08-22 14:00', '2025-08-25 12:00', 300000);

-- Đơn 50: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP050', 'NV02', 'KH010', 'KM002');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP050', 'P032', '2025-08-22', '2025-08-26', '2025-08-29', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP050', 'DV004', 8), ('DP050', 'DV005', 2);

-- 1,500,000 - 15% = 1,275,000 + 260,000 = 1,535,000 + 8% = 1,657,800
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD050', 'KH010', 'NV02', '2025-08-29', N'Tiền mặt', 1657800);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD050', 'P032', 3, '2025-08-26 14:00', '2025-08-29 12:00', 500000);

-- Đơn 51: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP051', 'NV03', 'KH001', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP051', 'P012', '2025-08-26', '2025-08-29', '2025-09-01', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP051', 'DV002', 3), ('DP051', 'DV007', 2);

-- 900,000 + 190,000 = 1,090,000 + 8% = 1,177,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD051', 'KH001', 'NV03', '2025-09-01', N'Chuyển khoản', 1177200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD051', 'P012', 3, '2025-08-29 14:00', '2025-09-01 12:00', 300000);

-- Đơn 52: VIP
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP052', 'NV01', 'KH002', 'KM001');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP052', 'P040', '2025-08-29', '2025-09-01', '2025-09-04', 300000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP052', 'DV002', 5), ('DP052', 'DV003', 8), ('DP052', 'DV005', 4);

-- 3,000,000 - 10% = 2,700,000 + 470,000 = 3,170,000 + 8% = 3,423,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD052', 'KH002', 'NV01', '2025-09-04', N'Tiền mặt', 3423600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD052', 'P040', 3, '2025-09-01 14:00', '2025-09-04 12:00', 1000000);

-- ============================================================================
-- THÁNG 9/2025 - 6 đơn
-- ============================================================================

-- Đơn 53: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP053', 'NV02', 'KH003', 'KM003');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP053', 'P013', '2025-09-01', '2025-09-05', '2025-09-08', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP053', 'DV001', 6);

-- 900,000 - 20% = 720,000 + 60,000 = 780,000 + 8% = 842,400
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD053', 'KH003', 'NV02', '2025-09-08', N'Tiền mặt', 842400);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD053', 'P013', 3, '2025-09-05 14:00', '2025-09-08 12:00', 300000);

-- Đơn 54: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP054', 'NV03', 'KH004', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP054', 'P014', '2025-09-08', '2025-09-12', '2025-09-15', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP054', 'DV003', 4), ('DP054', 'DV004', 4);

-- 900,000 + 140,000 = 1,040,000 + 8% = 1,123,200
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD054', 'KH004', 'NV03', '2025-09-15', N'Chuyển khoản', 1123200);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD054', 'P014', 3, '2025-09-12 14:00', '2025-09-15 12:00', 300000);

-- Đơn 55: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP055', 'NV01', 'KH005', 'KM003');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP055', 'P017', '2025-09-12', '2025-09-16', '2025-09-19', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP055', 'DV004', 10), ('DP055', 'DV001', 6);

-- 1,500,000 - 20% = 1,200,000 + 260,000 = 1,460,000 + 8% = 1,576,800
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD055', 'KH005', 'NV01', '2025-09-19', N'Tiền mặt', 1576800);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD055', 'P017', 3, '2025-09-16 14:00', '2025-09-19 12:00', 500000);

-- Đơn 56: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP056', 'NV02', 'KH006', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP056', 'P015', '2025-09-18', '2025-09-22', '2025-09-25', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP056', 'DV005', 3);

-- 900,000 + 150,000 = 1,050,000 + 8% = 1,134,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD056', 'KH006', 'NV02', '2025-09-25', N'Chuyển khoản', 1134000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD056', 'P015', 3, '2025-09-22 14:00', '2025-09-25 12:00', 300000);

-- Đơn 57: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP057', 'NV03', 'KH007', 'KM003');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP057', 'P018', '2025-09-22', '2025-09-26', '2025-09-29', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP057', 'DV002', 4), ('DP057', 'DV007', 3);

-- 1,500,000 - 20% = 1,200,000 + 270,000 = 1,470,000 + 8% = 1,587,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD057', 'KH007', 'NV03', '2025-09-29', N'Tiền mặt', 1587600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD057', 'P018', 3, '2025-09-26 14:00', '2025-09-29 12:00', 500000);

-- Đơn 58: VIP
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP058', 'NV01', 'KH008', 'KM003');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP058', 'P033', '2025-09-26', '2025-09-29', '2025-10-02', 300000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP058', 'DV001', 8), ('DP058', 'DV004', 10), ('DP058', 'DV007', 3);

-- 3,000,000 - 20% = 2,400,000 + 510,000 = 2,910,000 + 8% = 3,142,800
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD058', 'KH008', 'NV01', '2025-10-02', N'Chuyển khoản', 3142800);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD058', 'P033', 3, '2025-09-29 14:00', '2025-10-02 12:00', 1000000);

-- ============================================================================
-- THÁNG 10/2025 - 6 đơn
-- ============================================================================

-- Đơn 59: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP059', 'NV02', 'KH009', 'KM004');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP059', 'P016', '2025-10-01', '2025-10-05', '2025-10-08', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP059', 'DV001', 5), ('DP059', 'DV003', 3);

-- 900,000 - 12.5% = 787,500 + 95,000 = 882,500 + 8% = 953,100
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD059', 'KH009', 'NV02', '2025-10-08', N'Tiền mặt', 953100);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD059', 'P016', 3, '2025-10-05 14:00', '2025-10-08 12:00', 300000);

-- Đơn 60: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP060', 'NV03', 'KH010', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP060', 'P001', '2025-10-08', '2025-10-12', '2025-10-15', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP060', 'DV002', 4);

-- 900,000 + 120,000 = 1,020,000 + 8% = 1,101,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD060', 'KH010', 'NV03', '2025-10-15', N'Chuyển khoản', 1101600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD060', 'P001', 3, '2025-10-12 14:00', '2025-10-15 12:00', 300000);

-- Đơn 61: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP061', 'NV01', 'KH001', 'KM004');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP061', 'P019', '2025-10-12', '2025-10-16', '2025-10-19', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP061', 'DV005', 3), ('DP061', 'DV002', 4), ('DP061', 'DV007', 3);

-- 1,500,000 - 12.5% = 1,312,500 + 420,000 = 1,732,500 + 8% = 1,871,100
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD061', 'KH001', 'NV01', '2025-10-19', N'Tiền mặt', 1871100);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD061', 'P019', 3, '2025-10-16 14:00', '2025-10-19 12:00', 500000);

-- Đơn 62: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP062', 'NV02', 'KH002', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP062', 'P002', '2025-10-18', '2025-10-22', '2025-10-25', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP062', 'DV007', 3);

-- 900,000 + 150,000 = 1,050,000 + 8% = 1,134,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD062', 'KH002', 'NV02', '2025-10-25', N'Chuyển khoản', 1134000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD062', 'P002', 3, '2025-10-22 14:00', '2025-10-25 12:00', 300000);

-- Đơn 63: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP063', 'NV03', 'KH003', 'KM004');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP063', 'P020', '2025-10-22', '2025-10-26', '2025-10-29', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP063', 'DV001', 8), ('DP063', 'DV003', 5), ('DP063', 'DV004', 6);

-- 1,500,000 - 12.5% = 1,312,500 + 275,000 = 1,587,500 + 8% = 1,714,500
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD063', 'KH003', 'NV03', '2025-10-29', N'Tiền mặt', 1714500);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD063', 'P020', 3, '2025-10-26 14:00', '2025-10-29 12:00', 500000);

-- Đơn 64: VIP
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP064', 'NV01', 'KH004', 'KM004');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP064', 'P034', '2025-10-26', '2025-10-29', '2025-11-01', 300000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP064', 'DV002', 5), ('DP064', 'DV003', 8), ('DP064', 'DV005', 4);

-- 3,000,000 - 12.5% = 2,625,000 + 470,000 = 3,095,000 + 8% = 3,342,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD064', 'KH004', 'NV01', '2025-11-01', N'Chuyển khoản', 3342600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD064', 'P034', 3, '2025-10-29 14:00', '2025-11-01 12:00', 1000000);

-- ============================================================================
-- THÁNG 11/2025 - 6 đơn (CUỐI CÙNG)
-- ============================================================================

-- Đơn 65: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP065', 'NV02', 'KH005', 'KM005');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP065', 'P003', '2025-11-01', '2025-11-05', '2025-11-08', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP065', 'DV001', 5), ('DP065', 'DV007', 3);

-- 900,000 - 25% = 675,000 + 200,000 = 875,000 + 8% = 945,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD065', 'KH005', 'NV02', '2025-11-08', N'Tiền mặt', 945000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD065', 'P003', 3, '2025-11-05 14:00', '2025-11-08 12:00', 300000);

-- Đơn 66: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP066', 'NV03', 'KH006', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP066', 'P004', '2025-11-08', '2025-11-12', '2025-11-15', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP066', 'DV004', 6);

-- 900,000 + 120,000 = 1,020,000 + 8% = 1,101,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD066', 'KH006', 'NV03', '2025-11-15', N'Chuyển khoản', 1101600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD066', 'P004', 3, '2025-11-12 14:00', '2025-11-15 12:00', 300000);

-- Đơn 67: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP067', 'NV01', 'KH007', 'KM005');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP067', 'P021', '2025-11-12', '2025-11-16', '2025-11-19', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP067', 'DV002', 4), ('DP067', 'DV007', 3);

-- 1,500,000 - 25% = 1,125,000 + 270,000 = 1,395,000 + 8% = 1,506,600
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD067', 'KH007', 'NV01', '2025-11-19', N'Tiền mặt', 1506600);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD067', 'P021', 3, '2025-11-16 14:00', '2025-11-19 12:00', 500000);

-- Đơn 68: Standard
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP068', 'NV02', 'KH008', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP068', 'P005', '2025-11-18', '2025-11-22', '2025-11-25', 90000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP068', 'DV005', 3);

-- 900,000 + 150,000 = 1,050,000 + 8% = 1,134,000
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD068', 'KH008', 'NV02', '2025-11-25', N'Chuyển khoản', 1134000);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD068', 'P005', 3, '2025-11-22 14:00', '2025-11-25 12:00', 300000);

-- Đơn 69: Superior
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP069', 'NV03', 'KH009', NULL);

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP069', 'P022', '2025-11-22', '2025-11-26', '2025-11-29', 150000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP069', 'DV001', 6), ('DP069', 'DV005', 2);

-- 1,500,000 + 160,000 = 1,660,000 + 8% = 1,792,800
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD069', 'KH009', 'NV03', '2025-11-29', N'Tiền mặt', 1792800);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD069', 'P022', 3, '2025-11-26 14:00', '2025-11-29 12:00', 500000);

-- Đơn 70: VIP
INSERT INTO DonDatPhong (maDatPhong, maNhanVien, maKH, maKM) VALUES
('DP070', 'NV01', 'KH010', 'KM005');

INSERT INTO ChiTietDonDatPhong (maDatPhong, maPhong, ngayDat, ngayNhan, ngayTraDuKien, tienCoc, trangThai) VALUES
('DP070', 'P035', '2025-11-26', '2025-11-29', '2025-12-02', 300000, N'Hoàn thiện');

INSERT INTO ChiTietDichVu (maDatPhong, maDV, soLuong) VALUES
('DP070', 'DV001', 8), ('DP070', 'DV004', 10), ('DP070', 'DV007', 3);

-- 3,000,000 - 25% = 2,250,000 + 510,000 = 2,760,000 + 8% = 2,980,800
INSERT INTO HoaDon (maHD, maKH, maNhanVien, ngayLap, phuongThucThanhToan, tongTien) VALUES
('HD070', 'KH010', 'NV01', '2025-12-02', N'Chuyển khoản', 2980800);

INSERT INTO ChiTietHoaDon (maHD, maPhong, soNgay, ngayNhanPhong, ngayTraPhong, donGia) VALUES
('HD070', 'P035', 3, '2025-11-29 14:00', '2025-12-02 12:00', 1000000);


