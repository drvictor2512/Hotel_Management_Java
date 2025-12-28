package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.toedter.calendar.JDateChooser;
import dao.DatPhongDAO;
import dao.DichVuDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.KhuyenMaiDAO;
import dao.NhanPhongDAO;
import dao.PhongDAO;
import entity.ChiTietDichVu;
import entity.ChiTietDonDatPhong;
import entity.ChiTietHoaDon;
import entity.DichVu;
import entity.DonDatPhong;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.Phong;
import utils.PDFExporter;

public class HoaDonGUI extends MenuGUI {
	private JPanel pnlPhongGrid;
	private ArrayList<JPanel> danhSachOPhong;
	private NhanVien nhanVien;
	private double tongGiam = 0;
	private double tongThue = 0;
	
	// Màu sắc
	private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
	private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
	private static final Color HEADER_COLOR = Color.decode("#34495E");
	private static final Color PHONG_TRONG_COLOR = Color.decode("#27AE60");
	private static final Color PHONG_DA_DAT_COLOR = Color.decode("#F39C12");
	private static final Color PHONG_DANG_SU_DUNG_COLOR = Color.decode("#E74C3C");
	
	public HoaDonGUI(NhanVien nv) {
		super("Lập hóa đơn", nv);
		this.nhanVien = nv;
		
		JPanel pnlMain = new JPanel(new BorderLayout());
		pnlMain.setBackground(BACKGROUND_COLOR);
		
		// Panel top
		JPanel pnlTop = new JPanel(new BorderLayout());
		pnlTop.setBackground(PANEL_COLOR);
		pnlTop.setBorder(new EmptyBorder(20, 30, 20, 30));
		
		JLabel lblTieuDe = new JLabel("QUẢN LÝ HÓA ĐƠN", SwingConstants.CENTER);
		lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTieuDe.setForeground(HEADER_COLOR);
		
		// Panel chú thích và nút đặt phòng
		JPanel pnlChuThich = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		pnlChuThich.setBackground(PANEL_COLOR);
		
		pnlChuThich.add(createLegendItem("Đã đặt", PHONG_DA_DAT_COLOR));
		pnlChuThich.add(createLegendItem("Đang sử dụng", PHONG_DANG_SU_DUNG_COLOR));
		
		
		
		JButton btnDatPhongMoi = createStyledButton("Thanh toán", Color.decode("#27AE60"), 150, 35);
		btnDatPhongMoi.addActionListener(e -> showHoaDonGUi());
		pnlChuThich.add(btnDatPhongMoi);
		
		JButton btnTimKiem = createStyledButton("Hoá đơn", Color.decode("#0C2B4E"), 150, 35);
		btnTimKiem.addActionListener(e -> showTimKiemDonDialog());
		pnlChuThich.add(btnTimKiem);
		
		pnlTop.add(lblTieuDe, BorderLayout.NORTH);
		pnlTop.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
		pnlTop.add(pnlChuThich, BorderLayout.SOUTH);
		
		// Panel grid phòng
		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.setBackground(BACKGROUND_COLOR);
		pnlCenter.setBorder(new EmptyBorder(10, 20, 10, 20));
		
		pnlPhongGrid = new JPanel();
		pnlPhongGrid.setLayout(new GridLayout(0, 5, 15, 15));
		pnlPhongGrid.setBackground(BACKGROUND_COLOR);
		pnlPhongGrid.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		danhSachOPhong = new ArrayList<>();
		
		JScrollPane scrollPane = new JScrollPane(pnlPhongGrid);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		pnlCenter.add(scrollPane, BorderLayout.CENTER);
		
		pnlMain.add(pnlTop, BorderLayout.NORTH);
		pnlMain.add(pnlCenter, BorderLayout.CENTER);
		
		add(pnlMain, BorderLayout.CENTER);
		
		loadDanhSachPhong();
		setVisible(true);
	}
	
	private void showTimKiemDonDialog() {
		JDialog dialog = new JDialog(this, "Tìm kiếm hóa đơn", true);
		dialog.setSize(1400, 700);
		dialog.setLocationRelativeTo(this);
		
		JPanel pnlMain = new JPanel(new BorderLayout(15, 15));
		pnlMain.setBackground(PANEL_COLOR);
		pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		// Panel tìm kiếm
		JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		pnlTimKiem.setBackground(PANEL_COLOR);
		pnlTimKiem.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(HEADER_COLOR, 2),
			"Tìm kiếm",
			0, 0,
			new Font("Segoe UI", Font.BOLD, 14),
			HEADER_COLOR
		));
		
		JLabel lblMaDon = new JLabel("Mã hóa đơn:");
		lblMaDon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		JTextField txtMaDon = new JTextField(15);
		txtMaDon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtMaDon.setPreferredSize(new Dimension(150, 35));
		
		JLabel lblSDT = new JLabel("SĐT khách hàng:");
		lblSDT.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		JTextField txtSDT = new JTextField(15);
		txtSDT.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtSDT.setPreferredSize(new Dimension(150, 35));
		
		JButton btnTimKiem = createStyledButton("Tìm kiếm", Color.decode("#3498DB"), 120, 35);
		JButton btnLamMoi = createStyledButton("Làm mới", Color.decode("#95A5A6"), 120, 35);
		
		pnlTimKiem.add(lblMaDon);
		pnlTimKiem.add(txtMaDon);
		pnlTimKiem.add(Box.createRigidArea(new Dimension(20, 0)));
		pnlTimKiem.add(lblSDT);
		pnlTimKiem.add(txtSDT);
		pnlTimKiem.add(Box.createRigidArea(new Dimension(20, 0)));
		pnlTimKiem.add(btnTimKiem);
		pnlTimKiem.add(btnLamMoi);
		
		// Bảng kết quả
		String[] columns = {"Mã HĐ", "NV lập HĐ", "Khách hàng", "SĐT", "Phòng", "Loại phòng", "Ngày nhận", "Ngày trả",
							"Số ngày", "Phương thức", "Thanh toán"};
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		JTable table = new JTable(model);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		table.setRowHeight(40);
		table.setSelectionBackground(Color.decode("#3498DB"));
		table.setSelectionForeground(Color.WHITE);
		
		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Segoe UI", Font.BOLD, 13));
		header.setBackground(HEADER_COLOR);
		header.setForeground(Color.WHITE);
		header.setPreferredSize(new Dimension(header.getWidth(), 45));
		
		// Center alignment
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < columns.length; i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			
		}
		
		// Thiết lập độ rộng cột
		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(130);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(90);
		table.getColumnModel().getColumn(6).setPreferredWidth(90);
		table.getColumnModel().getColumn(7).setPreferredWidth(90);
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(10).setPreferredWidth(100);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#BDC3C7"), 1));
		
		// Label thống kê
		JLabel lblThongKe = new JLabel("Tổng số đơn: 0");
		lblThongKe.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblThongKe.setForeground(HEADER_COLOR);
		lblThongKe.setBorder(new EmptyBorder(10, 5, 5, 5));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		// Load tất cả đơn khi mở dialog
		loadAllDonDatPhong(model, lblThongKe, sdf);
		
		// Xử lý nút tìm kiếm
		btnTimKiem.addActionListener(e -> {
			String maDon = txtMaDon.getText().trim();
			String sdt = txtSDT.getText().trim();
			
			ArrayList<Object[]> ketQua = new ArrayList<>();
			
			if (!maDon.isEmpty() && !sdt.isEmpty()) {
				// Tìm theo cả 2 tiêu chí
				ArrayList<Object[]> kqMa = HoaDonDAO.layTatCaHoaDonTheoMa(maDon);
				ArrayList<Object[]> kqSDT = HoaDonDAO.layTatCaHoaDonTheoSDT(sdt);
				
				// Lấy giao của 2 kết quả
				for (Object[] donMa : kqMa) {
					for (Object[] donSDT : kqSDT) {
						if (donMa[0].equals(donSDT[0])) { // So sánh mã đặt phòng
							ketQua.add(donMa);
							break;
						}
					}
				}
			} else if (!maDon.isEmpty()) {
				// Chỉ tìm theo mã đơn
				ketQua = HoaDonDAO.layTatCaHoaDonTheoMa(maDon);
			} else if (!sdt.isEmpty()) {
				// Chỉ tìm theo SĐT
				ketQua = HoaDonDAO.layTatCaHoaDonTheoSDT(sdt);
			} else {
				JOptionPane.showMessageDialog(dialog, 
					"Vui lòng nhập ít nhất một tiêu chí tìm kiếm!",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Hiển thị kết quả
			model.setRowCount(0);
			for (Object[] don : ketQua) {
				
				model.addRow(new Object[]{
						don[0],   // maHD
						don[1],   // hoTenNV
						don[2],   // hoTenKH
						don[3],   // SDT
						don[4],   // tenPhong
						don[5],   // tenLoaiPhong
				        sdf.format((java.sql.Date) don[6]),   // ngayNhanPhong
				        sdf.format((java.sql.Date) don[7]),   // ngayTraPhong
				        don[8],   // soNgay
				        don[9],   // phuongThucThanhToan
				        don[10]   // donGia
				});
			}
			
			lblThongKe.setText("Tổng số đơn: " + ketQua.size());
			
			if (ketQua.isEmpty()) {
				JOptionPane.showMessageDialog(dialog, 
					"Không tìm thấy hóa đơn nào!",
					"Thông báo", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		// Xử lý nút làm mới
		btnLamMoi.addActionListener(e -> {
			txtMaDon.setText("");
			txtSDT.setText("");
			loadAllDonDatPhong(model, lblThongKe, sdf);
		});
		
		// Panel bottom
		JPanel pnlBottom = new JPanel(new BorderLayout());
		pnlBottom.setBackground(PANEL_COLOR);
		
		JButton btnDong = createStyledButton("Đóng", Color.decode("#E74C3C"), 120, 40);
		btnDong.addActionListener(e -> dialog.dispose());
		
		JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlButton.setBackground(PANEL_COLOR);
		pnlButton.add(btnDong);
		
		pnlBottom.add(lblThongKe, BorderLayout.WEST);
		pnlBottom.add(pnlButton, BorderLayout.CENTER);
		
		pnlMain.add(pnlTimKiem, BorderLayout.NORTH);
		pnlMain.add(scrollPane, BorderLayout.CENTER);
		pnlMain.add(pnlBottom, BorderLayout.SOUTH);
		
		dialog.add(pnlMain);
		dialog.setVisible(true);
	}
	
	private void loadAllDonDatPhong(DefaultTableModel model, JLabel lblThongKe, SimpleDateFormat sdf) {
		model.setRowCount(0);
		ArrayList<Object[]> tatCaDon = HoaDonDAO.layTatCaHoaDon();
		
		for (Object[] don : tatCaDon) {				
			model.addRow(new Object[]{
					don[0],   // maHD
					don[1],   // hoTenNV
					don[2],   // hoTenKH
					don[3],   // SDT
					don[4],   // tenPhong
					don[5],   // tenLoaiPhong
			        sdf.format((java.sql.Date) don[6]),   // ngayNhanPhong
			        sdf.format((java.sql.Date) don[7]),   // ngayTraPhong
			        don[8],   // soNgay
			        don[9],   // phuongThucThanhToan
			        don[10]   // donGia
			    });
		}
		
		lblThongKe.setText("Tổng số đơn: " + tatCaDon.size());
	}
	
	private JPanel createLegendItem(String text, Color color) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		panel.setBackground(PANEL_COLOR);
		
		JPanel colorBox = new JPanel();
		colorBox.setBackground(color);
		colorBox.setPreferredSize(new Dimension(20, 20));
		colorBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		
		panel.add(colorBox);
		panel.add(label);
		
		return panel;
	}
	
	private void loadDanhSachPhong() {
		pnlPhongGrid.removeAll();
		danhSachOPhong.clear();
		
		ArrayList<Phong> dsPhong = PhongDAO.getDSPhongDat();
		
		for (Phong phong : dsPhong) {
			JPanel oPhong = createOPhong(phong);
			danhSachOPhong.add(oPhong);
			pnlPhongGrid.add(oPhong);					
		}
		
		//Create phòng ma để GridLayout kéo giãn ô phòng ra
		ArrayList<Phong> ds = PhongDAO.getDSPhong();
		for (int i = ds.size(); i > dsPhong.size(); i--) {
				JPanel oPhong = new JPanel();

				oPhong.setLayout(new BorderLayout(5, 5));
				oPhong.setPreferredSize(new Dimension(150, 120));
				oPhong.setBorder(new EmptyBorder(10, 10, 10, 10));
					
				oPhong.setBackground(BACKGROUND_COLOR);
				pnlPhongGrid.add(oPhong);	
		}
			
		pnlPhongGrid.revalidate();
		pnlPhongGrid.repaint();
		
	}
	
	private JPanel createOPhong(Phong phong) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		panel.setPreferredSize(new Dimension(150, 120));
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.GRAY, 2, true),
			new EmptyBorder(10, 10, 10, 10)
		));
		panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// Xác định màu dựa trên tình trạng
		Color bgColor;
		String tinhTrang = phong.getTrangThai();
		if ("Trống".equals(tinhTrang)) {
			bgColor = PHONG_TRONG_COLOR;
		} else if ("Đã đặt".equals(tinhTrang)) {
			bgColor = PHONG_DA_DAT_COLOR;
		} else {
			bgColor = PHONG_DANG_SU_DUNG_COLOR;
		}
		panel.setBackground(bgColor);
		
		// Thông tin phòng
		JPanel pnlInfo = new JPanel();
		pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
		pnlInfo.setBackground(bgColor);
		
		JLabel lblMaPhong = new JLabel(phong.getMaPhong());
		lblMaPhong.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblMaPhong.setForeground(Color.WHITE);
		lblMaPhong.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		JLabel lblTenPhong = new JLabel(phong.getTenPhong());
		lblTenPhong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblTenPhong.setForeground(Color.WHITE);
		lblTenPhong.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		JLabel lblLoaiPhong = new JLabel(phong.getLoaiPhong().getTenLoaiPhong());
		lblLoaiPhong.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		lblLoaiPhong.setForeground(Color.WHITE);
		lblLoaiPhong.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		JLabel lblGia = new JLabel(String.format("%,.0f VNĐ", phong.getDonGia()));
		lblGia.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblGia.setForeground(Color.WHITE);
		lblGia.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		pnlInfo.add(lblMaPhong);
		pnlInfo.add(Box.createRigidArea(new Dimension(0, 5)));
		pnlInfo.add(lblTenPhong);
		pnlInfo.add(Box.createRigidArea(new Dimension(0, 3)));
		pnlInfo.add(lblLoaiPhong);
		pnlInfo.add(Box.createRigidArea(new Dimension(0, 5)));
		pnlInfo.add(lblGia);
		
		panel.add(pnlInfo, BorderLayout.CENTER);
		
		// Thêm hiệu ứng hover
		panel.addMouseListener(new MouseAdapter() {
			Color originalColor = bgColor;
			
			@Override
			public void mouseEntered(MouseEvent e) {
				panel.setBackground(bgColor.brighter());
				pnlInfo.setBackground(bgColor.brighter());
				panel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.WHITE, 3, true),
					new EmptyBorder(10, 10, 10, 10)
				));
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				panel.setBackground(originalColor);
				pnlInfo.setBackground(originalColor);
				panel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.GRAY, 2, true),
					new EmptyBorder(10, 10, 10, 10)
				));
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if ("Đã đặt".equals(phong.getTrangThai()) || "Đang thuê".equals(phong.getTrangThai())) {
					showThongTinDatPhong(phong.getMaPhong());
				} else {
					return;
				}
			}
		});
		
		return panel;
	}
	
	private void showThongTinDatPhong(String maPhong) {
		ArrayList<Object[]> dsDatPhong = DatPhongDAO.layTatCaDonDatPhong();
		ArrayList<Object[]> dsDonCuaPhong = new ArrayList<>();
		
		for (Object[] dp : dsDatPhong) {
			if (dp[3].equals(maPhong)) {
				String trangThai = dp[13].toString();
				if ("Đã đặt".equals(trangThai) ||"Đang thuê".equals(trangThai)) {
					dsDonCuaPhong.add(dp);
				}
			}
		}
		
		if (dsDonCuaPhong.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin đặt phòng!");
			return;
		}
		
		JDialog dialog = new JDialog(this, "Thông tin đặt phòng - " + maPhong, true);
		dialog.setSize(1200, 600);
		dialog.setLocationRelativeTo(this);
		
		JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
		pnlMain.setBackground(PANEL_COLOR);
		pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JLabel lblTitle = new JLabel("Danh sách đơn đặt phòng " + maPhong + " (" + dsDonCuaPhong.size() + " đơn)", JLabel.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitle.setForeground(HEADER_COLOR);
		lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
		
		// Tạo bảng hiển thị
		String[] columns = {"Mã ĐP", "Khách hàng", "Ngày đặt", "Ngày nhận", "Ngày trả", "Tiền cọc", "KM(%)", "Dịch vụ", "Trạng thái đơn"};
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		JTable table = new JTable(model);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		table.setRowHeight(40);
		table.setSelectionBackground(Color.decode("#3498DB"));
		table.setSelectionForeground(Color.WHITE);
		
		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Segoe UI", Font.BOLD, 13));
		header.setBackground(HEADER_COLOR);
		header.setForeground(Color.WHITE);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < columns.length; i++) {
			if (i != 7) { // Trừ cột dịch vụ
				table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		// Thêm dữ liệu vào bảng
		for (Object[] thongTin : dsDonCuaPhong) {
			model.addRow(new Object[]{
				thongTin[0],  // Mã ĐP
				thongTin[2],  // Tên KH
				sdf.format((java.sql.Date) thongTin[7]),  // Ngày đặt
				sdf.format((java.sql.Date) thongTin[8]),  // Ngày nhận
				sdf.format((java.sql.Date) thongTin[9]),  // Ngày trả
				String.format("%,.0f VNĐ", thongTin[10]), // Tiền cọc
				thongTin[11],  // Khuyến mãi
				thongTin[12], // Dịch vụ
				thongTin[13]
			});
		}
		
		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(60);
		table.getColumnModel().getColumn(7).setPreferredWidth(200);
		table.getColumnModel().getColumn(8).setPreferredWidth(120);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#BDC3C7"), 1));
		
		JButton btnDong = createStyledButton("Đóng", Color.decode("#95A5A6"), 120, 40);
		btnDong.addActionListener(e -> dialog.dispose());
		
		JPanel pnlButton = new JPanel();
		pnlButton.setBackground(PANEL_COLOR);
		pnlButton.add(btnDong);
		
		pnlMain.add(lblTitle, BorderLayout.NORTH);
		pnlMain.add(scrollPane, BorderLayout.CENTER);
		pnlMain.add(pnlButton, BorderLayout.SOUTH);
		
		dialog.add(pnlMain);
		dialog.setVisible(true);
	}
	
	private JButton createStyledButton(String text, Color bgColor, int width, int height) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btn.setForeground(Color.WHITE);
		btn.setBackground(bgColor);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setPreferredSize(new Dimension(width, height));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		btn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				btn.setBackground(bgColor.brighter());
			}
			public void mouseExited(MouseEvent evt) {
				btn.setBackground(bgColor);
			}
		});
		
		return btn;
	}
	
	private void showHoaDonGUi() {
	    JDialog dialog = new JDialog(this, "Lập Hóa Đơn", true);
	    dialog.setSize(1500, 900);
	    dialog.setLocationRelativeTo(this);
	    dialog.setLayout(new BorderLayout(10, 10));
	    
	    JPanel pnlMain = new JPanel(new BorderLayout(15, 15));
	    pnlMain.setBackground(PANEL_COLOR);
	    pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
	    
	    // Panel thông tin 
	    JPanel pnlInfo = new JPanel();
	    pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
	    pnlInfo.setBackground(PANEL_COLOR);
	    pnlInfo.setPreferredSize(new Dimension(400, 0));
	    pnlInfo.setBorder(new EmptyBorder(10, 10, 10, 15));
	    pnlInfo.setAlignmentX(JPanel.LEFT_ALIGNMENT);

	    JLabel lblTitle = new JLabel("Thông tin hóa đơn");
	    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
	    lblTitle.setForeground(HEADER_COLOR);
	    lblTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
	    pnlInfo.add(lblTitle);
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 20)));
	    
	    JTextField txtMaHD = new JTextField(HoaDonDAO.getNextId());
	    txtMaHD.setEditable(false);
	    txtMaHD.setBackground(Color.decode("#ECF0F1"));
	    
	    JTextField txtMaKH = new JTextField(20);
	    txtMaKH.setEditable(false);
	    txtMaKH.setBackground(Color.decode("#ECF0F1"));
	    
	    JTextField txtTenKH = new JTextField(20);
	    txtTenKH.setEditable(false);
	    txtTenKH.setBackground(Color.decode("#ECF0F1"));
	    
	    JButton btnChonKH = createStyledButton("Chọn khách hàng", Color.decode("#3498DB"), 200, 35);
	    
	    JTextField txtMaDP = new JTextField(20);
	    txtMaDP.setEditable(false);
	    txtMaDP.setBackground(Color.decode("#ECF0F1"));	    
	    
	    JButton btnChonDP = createStyledButton("Chọn đơn đặt phòng", Color.decode("#3498DB"), 200, 35);
	    btnChonDP.setEnabled(false);
	    
	    final String[] maKHSelected = new String[1];	   
	    
	    JDateChooser dateNgayLap = new JDateChooser();
	    dateNgayLap.setDateFormatString("dd/MM/yyyy");
	    dateNgayLap.setPreferredSize(new Dimension(200, 35));
	    dateNgayLap.setDate(new Date());
	    
	    String[] phuongThuc = {"Tiền mặt", "Chuyển khoản"};
	    JComboBox<String> cboPhuongThuc = new JComboBox<>(phuongThuc);
	    cboPhuongThuc.setBackground(Color.decode("#ECF0F1"));
	    
	    JTextField txtDichVu = new JTextField(20);
	    txtDichVu.setEditable(false);
	    txtDichVu.setBackground(Color.decode("#ECF0F1"));
	    txtDichVu.setText("0");
	    
	    JTextField txtTongTien = new JTextField(20);
	    txtTongTien.setEditable(false);
	    txtTongTien.setBackground(Color.decode("#ECF0F1"));
	    
	    JTextField txtTienTra = new JTextField(20);
	    txtTienTra.setEditable(true);
	    txtTienTra.setBackground(Color.decode("#ECF0F1"));
	    txtTienTra.setText("0");	    
	    
	    JTextField txtTienDu = new JTextField(20);
	    txtTienDu.setEditable(false);
	    txtTienDu.setBackground(Color.decode("#ECF0F1"));
	    
	    //Tính tiền dư ngay khi tiền trả được nhập
	    txtTienTra.addFocusListener(new java.awt.event.FocusAdapter() {
	        @Override
	        public void focusLost(java.awt.event.FocusEvent e) {
	        	Double tong = Double.parseDouble(txtTongTien.getText());
	            Double tra = Double.parseDouble(txtTienTra.getText());
	            Double du = tra - tong;
	            txtTienDu.setText(Double.toString(du));
	        }
	    });
	    

	    // Sử dụng phương pháp mới để tạo các field row
	    pnlInfo.add(createFieldRow("Mã hóa đơn:", txtMaHD));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Mã KH:", txtMaKH));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Tên KH:", txtTenKH));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    
	    JPanel pnlChonKH = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    pnlChonKH.setBackground(PANEL_COLOR);
	    pnlChonKH.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	    pnlChonKH.setMaximumSize(new Dimension(380, 45));
	    pnlChonKH.add(Box.createRigidArea(new Dimension(110, 0)));
	    pnlChonKH.add(btnChonKH);
	    pnlInfo.add(pnlChonKH);
	    
	    pnlInfo.add(createFieldRow("Mã đặt phòng:", txtMaDP));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    
	    JPanel pnlChonDP = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    pnlChonDP.setBackground(PANEL_COLOR);
	    pnlChonDP.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	    pnlChonDP.setMaximumSize(new Dimension(380, 45));
	    pnlChonDP.add(Box.createRigidArea(new Dimension(110, 0)));
	    pnlChonDP.add(btnChonDP);
	    pnlInfo.add(pnlChonDP);
	    
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Ngày lập:", dateNgayLap));
	    
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Phương thức:", cboPhuongThuc));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    
	    pnlInfo.add(createFieldRow("Tổng dịch vụ:", txtDichVu));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Tổng tiền:", txtTongTien));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Tiền trả:", txtTienTra));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));	    
	    pnlInfo.add(createFieldRow("Tiền thối:", txtTienDu));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    
	    
	    // Panel chọn phòng trống
	    JPanel pnlPhongTrong = new JPanel(new BorderLayout(10, 10));
	    pnlPhongTrong.setBackground(PANEL_COLOR);
	    pnlPhongTrong.setBorder(new EmptyBorder(10, 10, 10, 10));
	    
	    JLabel lblPhongTitle = new JLabel("Chọn phòng (nhập ngày nhận & trả trước)");
	    lblPhongTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    lblPhongTitle.setForeground(HEADER_COLOR);
	    lblPhongTitle.setBorder(new EmptyBorder(5, 5, 10, 5));
	    
	    String[] columns = {"Chọn", "Mã phòng", "Tên phòng", "Số ngày", "Ngày nhận", "Ngày trả", "Đơn giá"};
	    DefaultTableModel modelPhong = new DefaultTableModel(columns, 0) {
	        @Override
	        public Class<?> getColumnClass(int column) {
	            return column == 0 ? Boolean.class : String.class;
	        }
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return column == 0;
	        }
	    };
	    
	    JTable tblPhong = new JTable(modelPhong);
	    tblPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    tblPhong.setRowHeight(40);
	    tblPhong.setSelectionBackground(Color.decode("#3498DB"));
	    tblPhong.setSelectionForeground(Color.WHITE);
	    
	    btnChonKH.addActionListener(e -> {
	        showChonKhachHangDialog(dialog, maKHSelected, txtMaKH, txtTenKH);
	        txtMaDP.setText("");
	        modelPhong.setRowCount(0);
	        btnChonDP.setEnabled(true);
	    });
	    
	    btnChonDP.addActionListener(e -> {
	        showChonDatPhongDialog(dialog, maKHSelected, txtMaDP, txtMaKH);
	        
	        //Tiền dịch vụ
            ArrayList<Object[]> dsDV = DichVuDAO.getDichVuTheoMaDP(txtMaDP.getText());
            double tongDV = 0;
            for (Object[] dv : dsDV) {
            	double tienDV = Double.parseDouble(dv[1].toString());
            	int soLuongDV = Integer.parseInt(dv[2].toString());
            	
            	tongDV += tienDV * soLuongDV;
            }
            String str = String.format("%.0f", tongDV);
            txtDichVu.setText(str);
            
	        modelPhong.setRowCount(0);
	    });
	    
	    // Thiết lập độ rộng cột
	    tblPhong.getColumnModel().getColumn(0).setPreferredWidth(50);
	    tblPhong.getColumnModel().getColumn(0).setMaxWidth(60);
	    tblPhong.getColumnModel().getColumn(1).setPreferredWidth(80);
	    tblPhong.getColumnModel().getColumn(2).setPreferredWidth(80);
	    tblPhong.getColumnModel().getColumn(3).setPreferredWidth(70);
	    tblPhong.getColumnModel().getColumn(4).setPreferredWidth(100);
	    tblPhong.getColumnModel().getColumn(5).setPreferredWidth(100);
	    tblPhong.getColumnModel().getColumn(6).setPreferredWidth(120);
	    
	    JTableHeader header = tblPhong.getTableHeader();
	    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
	    header.setBackground(HEADER_COLOR);
	    header.setForeground(Color.WHITE);
	    header.setPreferredSize(new Dimension(header.getWidth(), 45));
	    
	    // Center alignment cho các cột trừ tên phòng
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    tblPhong.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	    tblPhong.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
	    
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    tblPhong.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
	    
	    JScrollPane scrollPhong = new JScrollPane(tblPhong);
	    scrollPhong.setBorder(BorderFactory.createLineBorder(Color.decode("#BDC3C7"), 1));
	    scrollPhong.setPreferredSize(new Dimension(600, 500));
	    
	    JButton btnTimPhong = createStyledButton("Tìm phòng đang thuê", Color.decode("#3498DB"), 230, 45);
	    btnTimPhong.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    btnTimPhong.addActionListener(e -> {	        
	        modelPhong.setRowCount(0);
	        ArrayList<Object[]> dsPhong = DatPhongDAO.layPhongDaDatTheoDPVaKH(
	        		txtMaKH.getText().trim(),
		            txtMaDP.getText().trim(),
		            "Đang thuê"
	        );
	        
	        if (dsPhong.size() == 0) {
	        	JOptionPane.showMessageDialog(dialog, "Khách hàng này không có phòng đang thuê!");
                return;
	        }        
	        
	        for (Object[] p : dsPhong) {
	        	LocalDate nhan = ((java.sql.Date) p[5]).toLocalDate();
	        	LocalDate lap = ((java.sql.Date) p[6]).toLocalDate();
	        	
	        	LocalDate homNay = dateNgayLap.getDate().toInstant()
	                    .atZone(ZoneId.systemDefault())
	                    .toLocalDate();
	            
	            long soNgay = ChronoUnit.DAYS.between(nhan, lap);
	            String tongNgay = String.valueOf(soNgay);
	            
	            
	            if (soNgay < 0) {
		        	JOptionPane.showMessageDialog(dialog, "Không thể trả phòng trước ngày nhận!");
	                return;
		        } 	  
	            
	            if (homNay.isAfter(lap)) {
	            	long ngayTre = ChronoUnit.DAYS.between(lap, homNay);
	            	tongNgay = tongNgay + " + " + String.valueOf(ngayTre);
	            }
	            
	            modelPhong.addRow(new Object[]{
	                false,
	                p[0],
	                p[1],
	                tongNgay,
	                p[5],
	                p[6],
	                String.format("%,.0f", p[3])
	            });
	            	            
	        }
	        
	        // Tính tổng tiền khi có thay đổi checkbox

	        tblPhong.getModel().addTableModelListener(ev -> {
	            tongGiam = 0;
	            tongThue = 0;
	            double tongTienPhong = 0;
	            
	            for (int i = 0; i < modelPhong.getRowCount(); i++) {
	                Boolean checked = (Boolean) modelPhong.getValueAt(i, 0);
	                if (checked != null && checked) {
	                    
	                    String giaStr = modelPhong.getValueAt(i, 6).toString().replace(",", "");
	                    double donGiaPhong = Double.parseDouble(giaStr);
	                    
	                    String soNgayStr = modelPhong.getValueAt(i, 3).toString();
	                    long soNgayThue = 0;
	                    long soNgayTre = 0;
	                    
	                    // Tách số ngày thuê và số ngày trễ (nếu có)
	                    if (soNgayStr.contains("+")) {
	                        String[] parts = soNgayStr.split("\\s*\\+\\s*");
	                        soNgayThue = Long.parseLong(parts[0].trim());
	                        soNgayTre = Long.parseLong(parts[1].trim());
	                    } else {
	                        soNgayThue = Long.parseLong(soNgayStr.trim());
	                    }
	                    
	                    // Lấy thông tin đơn đặt phòng
	                    ArrayList<Object[]> dp = DatPhongDAO.timDonDatPhongTheoMa(txtMaDP.getText());
	                    if (dp.isEmpty()) continue;
	                    
	                    double tienCoc = (Double) dp.get(0)[10];
	                    double mucGiamGia = Double.parseDouble(dp.get(0)[11].toString());
	                    
	                    // Tính tiền thuê phòng sau khi giảm giá
	                    double tienGiamGia = donGiaPhong * soNgayThue * (mucGiamGia / 100);
	                    tongGiam += tienGiamGia;
	                    
	                    double tienPhongSauGiam = (donGiaPhong * soNgayThue) - tienGiamGia;
	                    
	                    // Tính tiền trễ (không áp dụng giảm giá)
	                    double tienTre = soNgayTre * donGiaPhong;
	                    
	                    // Tổng tiền của phòng này = tiền thuê sau giảm - tiền cọc + tiền trễ
	                    double tongTienPhongNay = tienPhongSauGiam - tienCoc + tienTre;
	                    tongTienPhong += tongTienPhongNay;
	                }
	            }
	            
	            // Tính thuế 8%
	            tongThue = tongTienPhong * 0.08;
	            
	            // Lấy tiền dịch vụ
	            double tongDichVu = 0;
	            try {
	                tongDichVu = Double.parseDouble(txtDichVu.getText().replace(",", ""));
	            } catch (NumberFormatException ex) {
	                tongDichVu = 0;
	            }
	            
	            // Tổng tiền = tiền phòng + thuế + dịch vụ
	            double tongTien = tongTienPhong + tongThue + tongDichVu;
	            
	            txtTongTien.setText(String.format("%.0f", tongTien));
	        });
	    });
	    
	    JPanel pnlBtnTim = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    pnlBtnTim.setBackground(PANEL_COLOR);
	    pnlBtnTim.add(btnTimPhong);
	    
	    pnlPhongTrong.add(lblPhongTitle, BorderLayout.NORTH);
	    pnlPhongTrong.add(scrollPhong, BorderLayout.CENTER);
	    pnlPhongTrong.add(pnlBtnTim, BorderLayout.SOUTH);
	    
	    
	    // Panel buttons - FIXED
	    JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
	    pnlButtons.setBackground(PANEL_COLOR);
	    pnlButtons.setBorder(new EmptyBorder(30, 0, 0, 0));
	    pnlButtons.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	    pnlButtons.setMaximumSize(new Dimension(380, 70));
	    
	    JButton btnLapHD = createStyledButton("Lập hóa đơn", Color.decode("#27AE60"), 140, 45);
	    JButton btnHuy = createStyledButton("Hủy", Color.red, 140, 45);
	    
	    btnHuy.addActionListener(e -> dialog.dispose());
	    
	    
	    btnLapHD.addActionListener(e -> {
	        try {
	            String maHD = HoaDonDAO.getNextId();
	            String maKH = txtMaKH.getText();
	            String maDP = txtMaDP.getText();
	            Double tienTra = Double.parseDouble(txtTienTra.getText());
	            Double tongTien = Double.parseDouble(txtTongTien.getText());
	            // Check lỗi
	            if (maKH == null || maKH.isEmpty()) {
	                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn khách hàng!");
	                return;
	            }
	            
	            if (maDP == null || maDP.isEmpty()) {
	                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn mã đặt phòng!");
	                return;
	            }
	            
	            if (tienTra == 0 || tienTra < tongTien) {
	                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đúng tiền trả!");
	                return;
	            }
	            
	            ArrayList<String> phongDaChon = new ArrayList<>();	            
	            
	            for (int i = 0; i < modelPhong.getRowCount(); i++) {
	                Boolean checked = (Boolean) modelPhong.getValueAt(i, 0);
	                if (checked != null && checked) {	                	                	
	                    phongDaChon.add(modelPhong.getValueAt(i, 1).toString());
	                }
	            }
	            
	            if (phongDaChon.isEmpty()) {
	                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ít nhất một phòng!");
	                return;
	            }
	            
	            Date ngayLap = dateNgayLap.getDate();
	            
	            if (ngayLap == null) {
	                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn đầy đủ ngày!");
	                return;
	            }	            
	            java.sql.Date sqlNgayLap = new java.sql.Date(ngayLap.getTime());
	            KhachHang kh = KhachHangDAO.getKhachHangbyID(maKH);
	            HoaDon hd = new HoaDon(maHD, kh, nhanVien, sqlNgayLap, (String) cboPhuongThuc.getSelectedItem(), tongTien);
	            
	            // Thêm chi tiết cho từng phòng 
	           
	            for (int i = 0; i < modelPhong.getRowCount(); i++) {
	                Boolean checked = (Boolean) modelPhong.getValueAt(i, 0);
	                if (checked != null && checked) {

	                    String maPhong = modelPhong.getValueAt(i, 1).toString();
	                    Phong phong = PhongDAO.getMaP(maPhong);
	                    long soNgay = 0;
	                    if (modelPhong.getValueAt(i, 3).toString().indexOf("+") != -1) {
	                    	String[] ngay = modelPhong.getValueAt(i, 3).toString().split(" \\+ ");
	                    	long a = Integer.parseInt(ngay[0].trim());
	                    	long b = Integer.parseInt(ngay[1].trim());
	                    	soNgay = a + b;
	                    }
	                    else {
	                    	soNgay = Long.parseLong(modelPhong.getValueAt(i, 3).toString());
	                    }
	                    
	                    double donGia = Double.parseDouble(modelPhong.getValueAt(i, 6).toString().replace(",", ""));
	                    java.sql.Date ngayNhan = (java.sql.Date) modelPhong.getValueAt(i, 4);
	                    double thanhTien = soNgay * donGia - (donGia * 0.3);

	                    ChiTietHoaDon cthd = new ChiTietHoaDon(
	                        hd,
	                        phong,
	                        (int) soNgay,
	                        ngayNhan,
		                    sqlNgayLap,
	                        thanhTien
	                    );
	                    
	                    HoaDonDAO.HoanThienDonDatPhong(maPhong, maDP, "Hoàn thiện");

	                    hd.themChiTiet(cthd);
	                }
	            }
	            	            
	            boolean ok = HoaDonDAO.themHoaDon(hd);
	            	      
	            
	            if (ok) {
	                // Cập nhật trạng thái tất cả các phòng
	                for (String maPhong : phongDaChon) {
	                	if (!PhongDAO.ktraConDatPhong(maPhong)) {
	                		PhongDAO.capNhatTinhTrangPhong(maPhong, "Trống");
	                	}
	                	else 
	                		PhongDAO.capNhatTinhTrangPhong(maPhong, "Đã đặt");
	                }
	                   
	                JOptionPane.showMessageDialog(dialog, 
	                    "Lập hóa đơn thành công cho " + phongDaChon.size() + " phòng!",
	                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
	                	                
	                
	                if(DatPhongDAO.ktraTrangThaiDatPhong(maDP)) {
	                	JOptionPane.showMessageDialog(dialog, 
	    	                    "Lập hết hóa đơn thành công cho quý khách " + txtTenKH.getText() + "!",
	    	                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
	                }
	                
	                ArrayList<Object[]> dsCT = HoaDonDAO.layTatCaChiTietHoaDon(maHD);
	                
	                showXuatHD(nv, maHD, kh.getHoTen(), nv.getMaNhanVien(), ngayLap, dsCT,
	                		Double.parseDouble(txtTongTien.getText()), Double.parseDouble(txtTienTra.getText()),
	                		Double.parseDouble(txtTienDu.getText()), cboPhuongThuc.getSelectedItem().toString(),
	                		Double.parseDouble(txtDichVu.getText()), maDP);
	                
	                loadDanhSachPhong();
	                dialog.dispose();
	            } else {
	                JOptionPane.showMessageDialog(dialog, "Lỗi khi lập hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            }
	            
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	   
	    
	    pnlButtons.add(btnLapHD);
	    pnlButtons.add(Box.createRigidArea(new Dimension(15, 0)));
	    pnlButtons.add(btnHuy);
	    
	    pnlInfo.add(pnlButtons);

	    pnlMain.add(pnlInfo, BorderLayout.WEST);
	    pnlMain.add(pnlPhongTrong, BorderLayout.CENTER);
	    
	    dialog.add(pnlMain);
	    dialog.setVisible(true);
	}

	
	private void showChonKhachHangDialog(JDialog parent, String[] maKHSelected, JTextField txtMaKH, JTextField txtTenKH) {
		JDialog dialogKH = new JDialog(parent, "Chọn khách hàng", true);
		dialogKH.setSize(1000, 500);
		dialogKH.setLocationRelativeTo(parent);
		dialogKH.setLayout(new BorderLayout(10, 10));
		
		JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
		pnlMain.setBackground(PANEL_COLOR);
		pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JLabel lblTitle = new JLabel("Danh sách khách hàng", JLabel.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTitle.setForeground(HEADER_COLOR);
		
		String[] columns = {"Mã KH", "Họ tên", "CCCD", "Tuổi", "SĐT", "Giới tính", "Nhân viên"};
		DefaultTableModel modelKH = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		JTable tblKH = new JTable(modelKH);
		tblKH.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tblKH.setRowHeight(35);
		tblKH.setSelectionBackground(Color.decode("#3498DB"));
		tblKH.setSelectionForeground(Color.WHITE);
		tblKH.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JTableHeader header = tblKH.getTableHeader();
		header.setFont(new Font("Segoe UI", Font.BOLD, 14));
		header.setBackground(HEADER_COLOR);
		header.setForeground(Color.WHITE);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < columns.length; i++) {
			tblKH.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		ArrayList<KhachHang> dsKH = KhachHangDAO.getAllKhachHangDangThue();
		for (KhachHang kh : dsKH) {
			modelKH.addRow(new Object[]{
				kh.getMaKH(),
				kh.getHoTen(),
				kh.getCCCD(),
				kh.getTuoi(),
				kh.getSDT(),
				kh.getGioiTinh(),
				kh.getMaNhanVien()
			});
		}
		
		JScrollPane scroll = new JScrollPane(tblKH);
		scroll.setBorder(null);
		
		JPanel pnlButtons = new JPanel();
		pnlButtons.setBackground(PANEL_COLOR);
		
		JButton btnXacNhan = createStyledButton("Xác nhận", Color.decode("#27AE60"), 120, 40);
		JButton btnHuy = createStyledButton("Hủy", Color.decode("#95A5A6"), 120, 40);
		
		btnXacNhan.addActionListener(e -> {
			int selectedRow = tblKH.getSelectedRow();
			if (selectedRow < 0) {
				JOptionPane.showMessageDialog(dialogKH, "Vui lòng chọn một khách hàng!");
				return;
			}
			
			String maKH = modelKH.getValueAt(selectedRow, 0).toString();
			String tenKH = modelKH.getValueAt(selectedRow, 1).toString();
			maKHSelected[0] = maKH;
			txtMaKH.setText(maKH);
			txtTenKH.setText(tenKH);
			dialogKH.dispose();
		});
		
		btnHuy.addActionListener(e -> dialogKH.dispose());
		
		pnlButtons.add(btnXacNhan);
		pnlButtons.add(Box.createRigidArea(new Dimension(10, 0)));
		pnlButtons.add(btnHuy);
		
		pnlMain.add(lblTitle, BorderLayout.NORTH);
		pnlMain.add(scroll, BorderLayout.CENTER);
		pnlMain.add(pnlButtons, BorderLayout.SOUTH);
		
		dialogKH.add(pnlMain);
		dialogKH.setVisible(true);
	}	
	
	private void showChonDatPhongDialog(JDialog parent, String[] maKHSelected, JTextField txtMaDP, JTextField txtMaKH) {
		String maKH = txtMaKH.getText().trim();

		JDialog dialogKH = new JDialog(parent, "Chọn đơn đặt phòng", true);
		dialogKH.setSize(1000, 500);
		dialogKH.setLocationRelativeTo(parent);
		dialogKH.setLayout(new BorderLayout(10, 10));
		
		JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
		pnlMain.setBackground(PANEL_COLOR);
		pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JLabel lblTitle = new JLabel("Danh sách đơn đặt phòng", JLabel.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTitle.setForeground(HEADER_COLOR);
		
		String[] columns = {"Mã DP", "Mã NV", "Mã KH", "Mã KM"};
		DefaultTableModel modelDP = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		JTable tblDP = new JTable(modelDP);
		tblDP.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tblDP.setRowHeight(35);
		tblDP.setSelectionBackground(Color.decode("#3498DB"));
		tblDP.setSelectionForeground(Color.WHITE);
		tblDP.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JTableHeader header = tblDP.getTableHeader();
		header.setFont(new Font("Segoe UI", Font.BOLD, 14));
		header.setBackground(HEADER_COLOR);
		header.setForeground(Color.WHITE);
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < columns.length; i++) {
			tblDP.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		
		ArrayList<Object[]> dsDP = DatPhongDAO.getDonDatPhongDangThue(maKH);
		for (Object[] dp : dsDP) {
			modelDP.addRow(new Object[]{
				dp[0],
				dp[1],
				dp[2],
				dp[3]	
			});
		}
		
		JScrollPane scroll = new JScrollPane(tblDP);
		scroll.setBorder(null);
		
		JPanel pnlButtons = new JPanel();
		pnlButtons.setBackground(PANEL_COLOR);
		
		JButton btnXacNhan = createStyledButton("Xác nhận", Color.decode("#27AE60"), 120, 40);
		JButton btnHuy = createStyledButton("Hủy", Color.decode("#95A5A6"), 120, 40);
		
		btnXacNhan.addActionListener(e -> {
			int selectedRow = tblDP.getSelectedRow();
			if (selectedRow < 0) {
				JOptionPane.showMessageDialog(dialogKH, "Vui lòng chọn một đơn đặt phòng!");
				return;
			}
			
			String maDP = modelDP.getValueAt(selectedRow, 0).toString();
			maKHSelected[0] = maDP;
			txtMaDP.setText(maDP);
			dialogKH.dispose();
		});
		
		btnHuy.addActionListener(e -> dialogKH.dispose());
		
		pnlButtons.add(btnXacNhan);
		pnlButtons.add(Box.createRigidArea(new Dimension(10, 0)));
		pnlButtons.add(btnHuy);
		
		pnlMain.add(lblTitle, BorderLayout.NORTH);
		pnlMain.add(scroll, BorderLayout.CENTER);
		pnlMain.add(pnlButtons, BorderLayout.SOUTH);
		
		dialogKH.add(pnlMain);
		dialogKH.setVisible(true);
	}
	
	private JPanel createFieldRow(String labelText, JComponent comp) {
	    JPanel panel = new JPanel(new BorderLayout(10, 0));
	    panel.setBackground(PANEL_COLOR);
	    panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	    panel.setMaximumSize(new Dimension(380, 40)); // Tăng width lên
	    
	    JLabel label = new JLabel(labelText);
	    label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    label.setPreferredSize(new Dimension(100, 35));
	    
	    if (comp instanceof JTextField) {
	        JTextField txt = (JTextField) comp;
	        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        txt.setPreferredSize(new Dimension(250, 35)); // Tăng width textfield
	    } else if (comp instanceof JDateChooser) {
	        comp.setPreferredSize(new Dimension(250, 35)); // Tăng width date chooser
	    }
	    
	    panel.add(label, BorderLayout.WEST);
	    panel.add(comp, BorderLayout.CENTER);
	    
	    return panel;
	}
	
	private void showXuatHD(
	        NhanVien nv, String maHD, String tenKH, String maNV, Date ngayLap,
	        ArrayList<Object[]> dsCTHD, // danh sách chi tiết hóa đơn
	        double tongTien, double tienTra, double tienThoi, String phuongThuc,
	        double tienDV, String maDP
	        ) {

	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	    // ====== DIALOG ======
	    JDialog dialog = new JDialog(this, "Chi tiết hóa đơn", true);
	    dialog.setSize(800, 900);
	    dialog.setLocationRelativeTo(this);
	    dialog.setLayout(new BorderLayout(10, 10));
	    dialog.getContentPane().setBackground(Color.WHITE);

	    // ====== HEADER ======
	    JPanel pnlHeader = new JPanel(new BorderLayout());
	    pnlHeader.setBackground(Color.WHITE);
	    pnlHeader.setBorder(new EmptyBorder(20, 30, 15, 30));

	    JPanel pnlHotel = new JPanel();
	    pnlHotel.setLayout(new BoxLayout(pnlHotel, BoxLayout.Y_AXIS));
	    pnlHotel.setBackground(Color.WHITE);
	    
	    Font fontHotelTitle = new Font("Roboto", Font.BOLD, 14);
	    Font fontHotelInfo = new Font("Roboto", Font.PLAIN, 12);
	    
	    JLabel lblHotelName = new JLabel("KHÁCH SẠN VICTORY");
	    lblHotelName.setFont(fontHotelTitle);
	    
	    JLabel lblAddress = new JLabel("Địa chỉ: An Nhơn, TP. Hồ Chí Minh");
	    lblAddress.setFont(fontHotelInfo);
	    
	    JLabel lblHotline = new JLabel("Hotline: 0939 799 999");
	    lblHotline.setFont(fontHotelInfo);
	    
	    pnlHotel.add(lblHotelName);
	    pnlHotel.add(Box.createVerticalStrut(5));
	    pnlHotel.add(lblAddress);
	    pnlHotel.add(Box.createVerticalStrut(5));
	    pnlHotel.add(lblHotline);

	    JLabel lblTitle = new JLabel("HÓA ĐƠN THANH TOÁN", SwingConstants.CENTER);
	    lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
	    lblTitle.setForeground(new Color(0, 102, 204));

	    pnlHeader.add(pnlHotel, BorderLayout.WEST);
	    pnlHeader.add(lblTitle, BorderLayout.CENTER);
	    dialog.add(pnlHeader, BorderLayout.NORTH);

	    // ====== THÔNG TIN CHUNG ======
	    JPanel pnlMain = new JPanel();
	    pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
	    pnlMain.setBackground(Color.WHITE);
	    pnlMain.setBorder(new EmptyBorder(15, 40, 20, 40));

	    Font fontLabel = new Font("Roboto", Font.PLAIN, 15);
	    Font fontValue = new Font("Roboto", Font.BOLD, 15);

	    pnlMain.add(createInfoLine("Mã hóa đơn:", maHD, fontLabel, fontValue));
	    pnlMain.add(Box.createVerticalStrut(8));
	    
	    pnlMain.add(createInfoLine("Khách hàng:", tenKH, fontLabel, fontValue));
	    pnlMain.add(Box.createVerticalStrut(8));
	    
	    pnlMain.add(createInfoLine("Nhân viên lập:", maNV, fontLabel, fontValue));
	    pnlMain.add(Box.createVerticalStrut(8));
	    
	    pnlMain.add(createInfoLine("Ngày lập:", sdf.format(ngayLap), fontLabel, fontValue));
	    pnlMain.add(Box.createVerticalStrut(8));
	    
	    pnlMain.add(createInfoLine("Phương thức:", phuongThuc, fontLabel, fontValue));

	    pnlMain.add(Box.createVerticalStrut(15));
	    pnlMain.add(new JSeparator());
	    pnlMain.add(Box.createVerticalStrut(15));

	    // ====== TẠO BẢNG CHI TIẾT PHÒNG ======
	    String[] cols = {
	            "Phòng", "Số ngày",
	            "Ngày nhận", "Ngày trả", "Đơn giá",
	            "Tiền cọc", "Thanh toán"
	    };

	    DefaultTableModel model = new DefaultTableModel(cols, 0) {
	        @Override
	        public boolean isCellEditable(int r, int c) {
	            return false;
	        }
	    };

	    JTable table = new JTable(model);
	    table.setRowHeight(35);
	    table.setFont(new Font("Roboto", Font.PLAIN, 13));
	    table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 13));
	    table.getTableHeader().setBackground(new Color(40, 80, 130));
	    table.getTableHeader().setForeground(Color.WHITE);
	    table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 40));

	    // Center alignment cho tất cả các cột
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    for (int i = 0; i < cols.length; i++) {
	        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
	    }

	    // === THÊM DỮ LIỆU CÁC CHI TIẾT PHÒNG ===
	    for (Object[] ct : dsCTHD) {
	        String maP = String.valueOf(ct[1]);
	        double donGia = PhongDAO.getMaP(maP).getDonGia();
	        double coc = donGia * 0.3;
	        model.addRow(new Object[]{
	                ct[1],
	                ct[2],
	                ct[3],
	                ct[4],
	                String.format("%,.0f", donGia),
	                String.format("%,.0f", coc),
	                String.format("%,.0f", ct[5])
	        });
	    }

	    JScrollPane scroll = new JScrollPane(table);
	    scroll.setPreferredSize(new Dimension(700, 180));
	    scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
	    pnlMain.add(scroll);

	    pnlMain.add(Box.createVerticalStrut(20));
	    pnlMain.add(new JSeparator());
	    pnlMain.add(Box.createVerticalStrut(20));
	    
	    // ====== TẠO BẢNG CHI TIẾT DỊCH VỤ ======
	    String[] cols1 = {
	            "Dịch vụ", "Số lượng", "Đơn giá", "Thanh toán"
	    };

	    DefaultTableModel model1 = new DefaultTableModel(cols1, 0) {
	        @Override
	        public boolean isCellEditable(int r, int c) {
	            return false;
	        }
	    };

	    JTable table1 = new JTable(model1);
	    table1.setRowHeight(35);
	    table1.setFont(new Font("Roboto", Font.PLAIN, 13));
	    table1.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 13));
	    table1.getTableHeader().setBackground(new Color(40, 80, 130));
	    table1.getTableHeader().setForeground(Color.WHITE);
	    table1.getTableHeader().setPreferredSize(new Dimension(table1.getTableHeader().getWidth(), 40));

	    // Center alignment
	    DefaultTableCellRenderer centerRenderer1 = new DefaultTableCellRenderer();
	    centerRenderer1.setHorizontalAlignment(JLabel.CENTER);
	    
	    DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
	    leftRenderer.setHorizontalAlignment(JLabel.LEFT);
	    
	    table1.getColumnModel().getColumn(0).setCellRenderer(leftRenderer); // Tên dịch vụ căn trái
	    for (int i = 1; i < cols1.length; i++) {
	        table1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer1);
	    }

	    // === THÊM DỮ LIỆU CÁC CHI TIẾT DỊCH VỤ ===
	    ArrayList<Object[]> dsDV = DichVuDAO.getDichVuTheoMaDP(maDP);
	    for (Object[] dv : dsDV) {
	        double donGia = Double.parseDouble(dv[1].toString());
	        int soLuong = Integer.parseInt(dv[2].toString());
	        double tong = donGia * soLuong;
	        model1.addRow(new Object[]{
	                dv[0],
	                dv[2],
	                String.format("%,.0f", donGia),
	                String.format("%,.0f", tong)
	        });
	    }

	    JScrollPane scroll1 = new JScrollPane(table1);
	    scroll1.setPreferredSize(new Dimension(700, 180));
	    scroll1.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
	    pnlMain.add(scroll1);

	    pnlMain.add(Box.createVerticalStrut(20));
	    pnlMain.add(new JSeparator());
	    pnlMain.add(Box.createVerticalStrut(20));

	    // ====== TỔNG TIỀN ======
	    Font fontSummary = new Font("Roboto", Font.PLAIN, 15);
	    Font fontSummaryValue = new Font("Roboto", Font.BOLD, 15);
	    
	    pnlMain.add(createInfoLine("Tiền dịch vụ:", String.format("%,.0f VNĐ", tienDV), fontSummary, fontSummaryValue));
	    pnlMain.add(Box.createVerticalStrut(8));
	    
	    pnlMain.add(createInfoLine("Khuyến mãi:", String.format("-%,.0f VNĐ", tongGiam), fontSummary, fontSummaryValue));
	    pnlMain.add(Box.createVerticalStrut(8));
	    
	    pnlMain.add(createInfoLine("Tiền thuế:", String.format("%,.0f VNĐ", tongThue), fontSummary, fontSummaryValue));
	    pnlMain.add(Box.createVerticalStrut(15));

	    // TỔNG CỘNG
	    JPanel pnlTotal = new JPanel(new BorderLayout());
	    pnlTotal.setBackground(Color.WHITE);
	    pnlTotal.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(200, 200, 200)),
	        new EmptyBorder(12, 0, 12, 0)
	    ));
	    
	    JLabel lblTong = new JLabel("TỔNG CỘNG:");
	    lblTong.setFont(new Font("Roboto", Font.BOLD, 18));

	    JLabel lblTienTong = new JLabel(String.format("%,.0f VNĐ", tongTien));
	    lblTienTong.setFont(new Font("Roboto", Font.BOLD, 20));
	    lblTienTong.setForeground(new Color(220, 20, 60));

	    pnlTotal.add(lblTong, BorderLayout.WEST);
	    pnlTotal.add(lblTienTong, BorderLayout.EAST);
	    pnlMain.add(pnlTotal);
	    
	    pnlMain.add(Box.createVerticalStrut(15));
	    
	    pnlMain.add(createInfoLine("Khách trả:", String.format("%,.0f VNĐ", tienTra), fontSummary, fontSummaryValue));
	    pnlMain.add(Box.createVerticalStrut(8));
	    
	    pnlMain.add(createInfoLine("Tiền thối:", String.format("%,.0f VNĐ", tienThoi), fontSummary, fontSummaryValue));

	    pnlMain.add(Box.createVerticalStrut(25));
	    
	    JLabel lblThanks = new JLabel("Cảm ơn quý khách đã sử dụng dịch vụ!", SwingConstants.CENTER);
	    lblThanks.setFont(new Font("Roboto", Font.ITALIC, 14));
	    lblThanks.setForeground(new Color(100, 100, 100));
	    pnlMain.add(lblThanks);

	    JScrollPane scrollMain = new JScrollPane(pnlMain);
	    scrollMain.setBorder(null);
	    scrollMain.getVerticalScrollBar().setUnitIncrement(16);
	    dialog.add(scrollMain, BorderLayout.CENTER);

	    // ====== BUTTON ======
	    JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
	    pnlButtons.setBackground(Color.WHITE);
	    pnlButtons.setBorder(new EmptyBorder(10, 0, 10, 0));

	    JButton btnXuat = new JButton("Xuất PDF");
	    btnXuat.setBackground(new Color(0, 102, 204));
	    btnXuat.setForeground(Color.WHITE);
	    btnXuat.setFont(new Font("Roboto", Font.BOLD, 15));
	    btnXuat.setPreferredSize(new Dimension(160, 45));
	    btnXuat.setFocusPainted(false);
	    btnXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    btnXuat.addActionListener(e -> {
	        boolean success = PDFExporter.xuatHoaDonPDF(
	            maHD, tenKH, maNV, ngayLap, dsCTHD, 
	            tongTien, tienTra, tienThoi, phuongThuc, dsDV,
	            tienDV, tongGiam, tongThue
	        );
	        
	        if (success) {
	            JOptionPane.showMessageDialog(dialog, 
	                "Xuất hóa đơn PDF thành công!\nFile: hoadon/HoaDon_" + maHD + ".pdf",
	                "Thành công", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            JOptionPane.showMessageDialog(dialog, 
	                "Lỗi khi xuất hóa đơn PDF!", 
	                "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	    
	    JButton btnClose = new JButton("Đóng");
	    btnClose.setBackground(new Color(231, 76, 60));
	    btnClose.setForeground(Color.WHITE);
	    btnClose.setFont(new Font("Roboto", Font.BOLD, 15));
	    btnClose.setPreferredSize(new Dimension(160, 45));
	    btnClose.setFocusPainted(false);
	    btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    btnClose.addActionListener(e -> dialog.dispose());

	    pnlButtons.add(btnXuat);
	    pnlButtons.add(btnClose);

	    dialog.add(pnlButtons, BorderLayout.SOUTH);
	    dialog.setVisible(true);
	}

	// Phương thức hỗ trợ tạo dòng thông tin
	private JPanel createInfoLine(String label, String value, Font fontLabel, Font fontValue) {
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.setBackground(Color.WHITE);
	    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
	    
	    JLabel lblLabel = new JLabel(label);
	    lblLabel.setFont(fontLabel);
	    
	    JLabel lblValue = new JLabel(value);
	    lblValue.setFont(fontValue);
	    lblValue.setHorizontalAlignment(JLabel.RIGHT);
	    
	    panel.add(lblLabel, BorderLayout.WEST);
	    panel.add(lblValue, BorderLayout.EAST);
	    
	    return panel;
	}
		
}