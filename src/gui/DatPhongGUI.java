package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import dao.KhachHangDAO;
import dao.KhuyenMaiDAO;
import dao.PhongDAO;
import entity.ChiTietDichVu;
import entity.ChiTietDonDatPhong;
import entity.DichVu;
import entity.DonDatPhong;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.Phong;

public class DatPhongGUI extends MenuGUI {
	private JPanel pnlPhongGrid;
	private ArrayList<JPanel> danhSachOPhong;
	private NhanVien nhanVien;
	
	// Màu sắc
	private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
	private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
	private static final Color HEADER_COLOR = Color.decode("#34495E");
	private static final Color PHONG_TRONG_COLOR = Color.decode("#27AE60");
	private static final Color PHONG_DA_DAT_COLOR = Color.decode("#F39C12");
	private static final Color PHONG_DANG_SU_DUNG_COLOR = Color.decode("#E74C3C");
	
	public DatPhongGUI(NhanVien nv) {
		super("Đặt phòng", nv);
		this.nhanVien = nv;
		
		JPanel pnlMain = new JPanel(new BorderLayout());
		pnlMain.setBackground(BACKGROUND_COLOR);
		
		// Panel top
		JPanel pnlTop = new JPanel(new BorderLayout());
		pnlTop.setBackground(PANEL_COLOR);
		pnlTop.setBorder(new EmptyBorder(20, 30, 20, 30));
		
		JLabel lblTieuDe = new JLabel("QUẢN LÝ ĐẶT PHÒNG", SwingConstants.CENTER);
		lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTieuDe.setForeground(HEADER_COLOR);
		
		// Panel chú thích và nút
		JPanel pnlChuThich = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		pnlChuThich.setBackground(PANEL_COLOR);
		
		pnlChuThich.add(createLegendItem("Phòng trống", PHONG_TRONG_COLOR));
		pnlChuThich.add(createLegendItem("Đã đặt", PHONG_DA_DAT_COLOR));
		pnlChuThich.add(createLegendItem("Đang sử dụng", PHONG_DANG_SU_DUNG_COLOR));
		
		JButton btnDatPhongMoi = createStyledButton("Đặt phòng", Color.decode("#27AE60"), 150, 35);
		btnDatPhongMoi.addActionListener(e -> showDatPhongGUI());
		pnlChuThich.add(btnDatPhongMoi);
		
		JButton btnTimKiem = createStyledButton("Đơn đặt phòng", Color.decode("#0C2B4E"), 150, 35);
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
		JDialog dialog = new JDialog(this, "Tìm kiếm đơn đặt phòng", true);
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
		
		JLabel lblMaDon = new JLabel("Mã đặt phòng:");
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
		String[] columns = {"Mã ĐP", "Khách hàng", "SĐT", "Phòng", "Loại phòng", 
							"Ngày đặt", "Ngày nhận", "Ngày trả", "Tiền cọc", "KM(%)", "Dịch vụ", "Trạng thái"};
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
			if (i != 10) { // Trừ cột dịch vụ
				table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}
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
		table.getColumnModel().getColumn(9).setPreferredWidth(60);
		table.getColumnModel().getColumn(10).setPreferredWidth(150);
		table.getColumnModel().getColumn(11).setPreferredWidth(100);
		
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
				ArrayList<Object[]> kqMa = DatPhongDAO.timDonDatPhongTheoMa(maDon);
				ArrayList<Object[]> kqSDT = DatPhongDAO.timDonDatPhongTheoSDT(sdt);
				
				// Lấy giao của 2 kết quả
				for (Object[] donMa : kqMa) {
					for (Object[] donSDT : kqSDT) {
						if (donMa[0].equals(donSDT[0])) {
							ketQua.add(donMa);
							break;
						}
					}
				}
			} else if (!maDon.isEmpty()) {
				// Chỉ tìm theo mã đơn
				ketQua = DatPhongDAO.timDonDatPhongTheoMa(maDon);
			} else if (!sdt.isEmpty()) {
				// Chỉ tìm theo SĐT
				ketQua = DatPhongDAO.timDonDatPhongTheoSDT(sdt);
			} else {
				JOptionPane.showMessageDialog(dialog, 
					"Vui lòng nhập ít nhất một tiêu chí tìm kiếm!",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Hiển thị kết quả
			model.setRowCount(0);
			for (Object[] don : ketQua) {
				// Lấy SĐT khách hàng
				KhachHang kh = KhachHangDAO.getKhachHangbyID(don[1].toString());
				String sdtKH = (kh != null) ? kh.getSDT() : "";
				
				model.addRow(new Object[]{
					don[0],  // Mã ĐP
					don[2],  // Tên KH
					sdtKH,   // SĐT
					don[3],  // Mã phòng
					don[5],  // Loại phòng
					sdf.format((java.sql.Date) don[7]),  // Ngày đặt
					sdf.format((java.sql.Date) don[8]),  // Ngày nhận
					sdf.format((java.sql.Date) don[9]),  // Ngày trả
					String.format("%,.0f VNĐ", don[10]), // Tiền cọc
					don[11], // Khuyến mãi
					don[12], // Dịch vụ
					don[13]  // Trạng thái
				});
			}
			
			lblThongKe.setText("Tổng số đơn: " + ketQua.size());
			
			if (ketQua.isEmpty()) {
				JOptionPane.showMessageDialog(dialog, 
					"Không tìm thấy đơn đặt phòng nào!",
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
		ArrayList<Object[]> tatCaDon = DatPhongDAO.layTatCaDonDatPhong();
		
		for (Object[] don : tatCaDon) {
			// Lấy SĐT khách hàng
			KhachHang kh = KhachHangDAO.getKhachHangbyID(don[1].toString());
			String sdtKH = (kh != null) ? kh.getSDT() : "";
			
			model.addRow(new Object[]{
				don[0],  // Mã ĐP
				don[2],  // Tên KH
				sdtKH,   // SĐT
				don[3],  // Mã phòng
				don[5],  // Loại phòng
				sdf.format((java.sql.Date) don[7]),  // Ngày đặt
				sdf.format((java.sql.Date) don[8]),  // Ngày nhận
				sdf.format((java.sql.Date) don[9]),  // Ngày trả
				String.format("%,.0f VNĐ", don[10]), // Tiền cọc
				don[11], // Khuyến mãi
				don[12], // Dịch vụ
				don[13]  // Trạng thái
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
		
		ArrayList<Phong> dsPhong = PhongDAO.getDSPhong();
		
		for (Phong phong : dsPhong) {
			JPanel oPhong = createOPhong(phong);
			danhSachOPhong.add(oPhong);
			pnlPhongGrid.add(oPhong);
		}
		
		pnlPhongGrid.revalidate();
		pnlPhongGrid.repaint();
	}
	
	private JPanel createOPhong(Phong phong) {
		JPanel pnlOphong = new JPanel();
		pnlOphong.setLayout(new BorderLayout(5, 5));
		pnlOphong.setPreferredSize(new Dimension(150, 120));
		pnlOphong.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.GRAY, 2, true),
			new EmptyBorder(10, 10, 10, 10)
		));
		pnlOphong.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
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
		pnlOphong.setBackground(bgColor);
		
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
		
		pnlOphong.add(pnlInfo, BorderLayout.CENTER);
		
		// Thêm hiệu ứng hover
		pnlOphong.addMouseListener(new MouseAdapter() {
			Color originalColor = bgColor;
			
			@Override
			public void mouseEntered(MouseEvent e) {
				pnlOphong.setBackground(bgColor.brighter());
				pnlInfo.setBackground(bgColor.brighter());
				pnlOphong.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.WHITE, 3, true),
					new EmptyBorder(10, 10, 10, 10)
				));
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				pnlOphong.setBackground(originalColor);
				pnlInfo.setBackground(originalColor);
				pnlOphong.setBorder(BorderFactory.createCompoundBorder(
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
		
		return pnlOphong;
	}
	
	private void showThongTinDatPhong(String maPhong) {
		ArrayList<Object[]> dsDatPhong = DatPhongDAO.layTatCaDonDatPhong();
		ArrayList<Object[]> dsDonCuaPhong = new ArrayList<>();
		
		for (Object[] dp : dsDatPhong) {
			if (dp[3].equals(maPhong)) {
				String trangThai = dp[13].toString();
				if ("Đã đặt".equals(trangThai) || "Đang thuê".equals(trangThai)) {
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
	
	private void showDatPhongGUI() {
	    JDialog dialog = new JDialog(this, "Đặt nhiều phòng", true);
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

	    JLabel lblTitle = new JLabel("Thông tin đặt phòng");
	    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
	    lblTitle.setForeground(HEADER_COLOR);
	    lblTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
	    pnlInfo.add(lblTitle);
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 20)));
	    
	    JTextField txtMaDP = new JTextField(DatPhongDAO.getNextId());
	    txtMaDP.setEditable(false);
	    txtMaDP.setBackground(Color.decode("#ECF0F1"));
	    
	    JTextField txtMaKH = new JTextField(20);
	    txtMaKH.setEditable(false);
	    txtMaKH.setBackground(Color.decode("#ECF0F1"));
	    
	    JTextField txtTenKH = new JTextField(20);
	    txtTenKH.setEditable(false);
	    txtTenKH.setBackground(Color.decode("#ECF0F1"));
	    
	    JButton btnChonKH = createStyledButton("Chọn khách hàng", Color.decode("#3498DB"), 200, 35);
	    
	    final String[] maKHSelected = new String[1];
	    JTextField txtKhuyenMai = new JTextField(20);
	    JButton btnChonKM = createStyledButton("Chọn khuyến mãi", Color.decode("#3498DB"), 200, 35);
	    JDateChooser dateNgayDat = new JDateChooser();
	    dateNgayDat.setDateFormatString("dd/MM/yyyy");
	    dateNgayDat.setPreferredSize(new Dimension(200, 35));
	    
	    JDateChooser dateNgayNhan = new JDateChooser();
	    dateNgayNhan.setDateFormatString("dd/MM/yyyy");
	    dateNgayNhan.setPreferredSize(new Dimension(200, 35));
	    
	    JDateChooser dateNgayTra = new JDateChooser();
	    dateNgayTra.setDateFormatString("dd/MM/yyyy");
	    dateNgayTra.setPreferredSize(new Dimension(200, 35));
	    
	    JTextField txtTienCoc = new JTextField(20);
	    txtTienCoc.setEditable(false);
	    txtTienCoc.setBackground(Color.decode("#ECF0F1"));
	    
	    btnChonKH.addActionListener(e -> {
	        showChonKhachHangDialog(dialog, maKHSelected, txtMaKH, txtTenKH);
	    });
	    btnChonKM.addActionListener(e -> {
	    	showChonKhuyenMaiDialog(dialog, txtKhuyenMai, dateNgayNhan, dateNgayTra);
	    });
	    // Sử dụng phương pháp mới để tạo các field row
	    pnlInfo.add(createFieldRow("Mã đặt phòng:", txtMaDP));
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
	    
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Ngày đặt:", dateNgayDat));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Ngày nhận:", dateNgayNhan));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Ngày trả:", dateNgayTra));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Tiền cọc:", txtTienCoc));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Khuyến mãi:", txtKhuyenMai));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 20)));
	    
	    JPanel pnlChonKM = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    pnlChonKM.setBackground(PANEL_COLOR);
	    pnlChonKM.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	    pnlChonKM.setMaximumSize(new Dimension(380, 45));
	    pnlChonKM.add(Box.createRigidArea(new Dimension(110, 0)));
	    pnlChonKM.add(btnChonKM);
	    pnlInfo.add(pnlChonKM);
	    
	    // Panel chọn phòng trống
	    JPanel pnlPhongTrong = new JPanel(new BorderLayout(10, 10));
	    pnlPhongTrong.setBackground(PANEL_COLOR);
	    pnlPhongTrong.setBorder(new EmptyBorder(10, 10, 10, 10));
	    
	    JLabel lblPhongTitle = new JLabel("Chọn phòng (nhập ngày nhận & trả trước)");
	    lblPhongTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    lblPhongTitle.setForeground(HEADER_COLOR);
	    lblPhongTitle.setBorder(new EmptyBorder(5, 5, 10, 5));
	    
	    String[] columns = {"Chọn", "Mã phòng", "Tên phòng", "Loại phòng", "Đơn giá"};
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
	    
	    // Thiết lập độ rộng cột
	    tblPhong.getColumnModel().getColumn(0).setPreferredWidth(50);
	    tblPhong.getColumnModel().getColumn(0).setMaxWidth(60);
	    tblPhong.getColumnModel().getColumn(1).setPreferredWidth(100);
	    tblPhong.getColumnModel().getColumn(2).setPreferredWidth(150);
	    tblPhong.getColumnModel().getColumn(3).setPreferredWidth(150);
	    tblPhong.getColumnModel().getColumn(4).setPreferredWidth(120);
	    
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
	    
	    JButton btnTimPhong = createStyledButton("Tìm phòng trống", Color.decode("#3498DB"), 180, 45);
	    btnTimPhong.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    btnTimPhong.addActionListener(e -> {
	        Date ngayNhan = dateNgayNhan.getDate();
	        Date ngayTra = dateNgayTra.getDate();
	        Date ngayDat = dateNgayDat.getDate();
	        
	        if (ngayDat == null || ngayNhan == null || ngayTra == null) {
	            JOptionPane.showMessageDialog(dialog, "Vui lòng chọn đủ ngày đặt, ngày nhận và ngày trả!");
	            return;
	        }
	        if(ngayDat.after(ngayNhan)) {
	        	JOptionPane.showMessageDialog(dialog, "Ngày đặt phải trước ngày nhận!");
	            return;
	        }
	        if (ngayTra.before(ngayNhan)) {
	            JOptionPane.showMessageDialog(dialog, "Ngày trả phải sau ngày nhận!");
	            return;
	        }
	        
	        modelPhong.setRowCount(0);
	        ArrayList<Object[]> dsPhong = PhongDAO.layPhongTrongTheoNgay(
	            new java.sql.Date(ngayNhan.getTime()),
	            new java.sql.Date(ngayTra.getTime())
	        );
	        
	        for (Object[] p : dsPhong) {
	            modelPhong.addRow(new Object[]{
	                false,
	                p[0],
	                p[1],
	                p[2],
	                String.format("%,.0f", p[3])
	            });
	        }
	        
	        // Tính tiền cọc khi có thay đổi checkbox
	        tblPhong.getModel().addTableModelListener(ev -> {
	            double tongTienCoc = 0;
	            for (int i = 0; i < modelPhong.getRowCount(); i++) {
	                Boolean checked = (Boolean) modelPhong.getValueAt(i, 0);
	                if (checked != null && checked) {
	                    String giaStr = modelPhong.getValueAt(i, 4).toString().replace(",", "");
	                    double gia = Double.parseDouble(giaStr);
	                    tongTienCoc += gia * 0.3;
	                }
	            }
	            txtTienCoc.setText(String.format("%.0f", tongTienCoc));
	        });
	    });
	    
	    JPanel pnlBtnTim = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    pnlBtnTim.setBackground(PANEL_COLOR);
	    pnlBtnTim.add(btnTimPhong);
	    
	    pnlPhongTrong.add(lblPhongTitle, BorderLayout.NORTH);
	    pnlPhongTrong.add(scrollPhong, BorderLayout.CENTER);
	    pnlPhongTrong.add(pnlBtnTim, BorderLayout.SOUTH);
	    
	    // Panel dịch vụ
	    JPanel pnlDichVu = createPanelDichVu();
	    pnlDichVu.setPreferredSize(new Dimension(350, 0));
	    
	    // Panel buttons - FIXED
	    JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
	    pnlButtons.setBackground(PANEL_COLOR);
	    pnlButtons.setBorder(new EmptyBorder(30, 0, 0, 0));
	    pnlButtons.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	    pnlButtons.setMaximumSize(new Dimension(380, 70));
	    
	    JButton btnDatPhong = createStyledButton("Đặt phòng", Color.decode("#27AE60"), 140, 45);
	    JButton btnHuy = createStyledButton("Hủy", Color.red, 140, 45);
	    
	    btnHuy.addActionListener(e -> dialog.dispose());
	    
	    btnDatPhong.addActionListener(e -> {
	        try {
	            String maDP = DatPhongDAO.getNextId();
	            String maKH = maKHSelected[0];
	            
	            if (maKH == null || maKH.isEmpty()) {
	                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn khách hàng!");
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
	            
	            Date ngayDat = dateNgayDat.getDate();
	            Date ngayNhan = dateNgayNhan.getDate();
	            Date ngayTra = dateNgayTra.getDate();
	            
	            if (ngayDat == null || ngayNhan == null || ngayTra == null) {
	                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn đầy đủ ngày!");
	                return;
	            }
	            
	            double tienCoc = Double.parseDouble(txtTienCoc.getText().trim());
	            String maKM = txtKhuyenMai.getText().trim();
	            
	            KhachHang kh = KhachHangDAO.getKhachHangbyID(maKH);
	            KhuyenMai km = (!maKM.isEmpty()) ? KhuyenMaiDAO.getKhuyenMaiById(maKM) : null;
	            DonDatPhong dp = new DonDatPhong(maDP, nhanVien, kh, km);
	            
	            // Thêm chi tiết cho từng phòng
	            for (String maPhong : phongDaChon) {
	                Phong phong = PhongDAO.getMaP(maPhong);
	                ChiTietDonDatPhong ctdp = new ChiTietDonDatPhong(
	                    dp, phong,
	                    new java.sql.Date(ngayDat.getTime()),
	                    new java.sql.Date(ngayNhan.getTime()),
	                    new java.sql.Date(ngayTra.getTime()),
	                    tienCoc / phongDaChon.size()
	                );
	                dp.themChiTiet(ctdp);
	            }
	            
	            boolean ok = DatPhongDAO.themDonDatPhong(dp);
	            
	            if (ok) {
	                ArrayList<DichVu> dsDichVu = DichVuDAO.getAllDichVu();
	                
	                JPanel pnlDichVuContent = (JPanel) ((JScrollPane) pnlDichVu.getComponent(1)).getViewport().getView();
	                
	                int dichVuIndex = 0;
	                for (Component comp : pnlDichVuContent.getComponents()) {
	                    if (comp instanceof JPanel) {
	                        JPanel pnlItem = (JPanel) comp;
	                        
	                        JCheckBox chk = null;
	                        JSpinner spn = null;
	                        
	                        for (Component child : pnlItem.getComponents()) {
	                            if (child instanceof JPanel) {
	                                JPanel innerPanel = (JPanel) child;
	                                for (Component innerChild : innerPanel.getComponents()) {
	                                    if (innerChild instanceof JCheckBox) {
	                                        chk = (JCheckBox) innerChild;
	                                    } else if (innerChild instanceof JPanel) {
	                                        JPanel spinnerPanel = (JPanel) innerChild;
	                                        for (Component spinnerChild : spinnerPanel.getComponents()) {
	                                            if (spinnerChild instanceof JSpinner) {
	                                                spn = (JSpinner) spinnerChild;
	                                            }
	                                        }
	                                    }
	                                }
	                            }
	                        }
	                        
	                        if (chk != null && spn != null && chk.isSelected() && dichVuIndex < dsDichVu.size()) {
	                            DichVu dv = dsDichVu.get(dichVuIndex);
	                            int soLuong = (int) spn.getValue();
	                            ChiTietDichVu newdv = new ChiTietDichVu(maDP, dv.getMaDV(), soLuong);
	                            DichVuDAO.themChiTietDichVu(newdv);
	                        }
	                        dichVuIndex++;
	                    }
	                }
	                
	                //  CẬP NHẬT TRẠNG THÁI PHÒNG ĐÚNG
	                for (String maPhong : phongDaChon) {
	                    // Kiểm tra xem phòng có đang được thuê không
	                    if (DatPhongDAO.ktraPhongDangThue(maPhong)) {
	                        // Nếu phòng đang thuê thì GIỮ NGUYÊN trạng thái "Đang thuê"
	                        PhongDAO.capNhatTinhTrangPhong(maPhong, "Đang thuê");
	                    } else {
	                        // Nếu không thì cập nhật thành "Đã đặt"
	                        PhongDAO.capNhatTinhTrangPhong(maPhong, "Đã đặt");
	                    }
	                }
	                
	                JOptionPane.showMessageDialog(dialog, 
	                    "Đặt thành công " + phongDaChon.size() + " phòng!",
	                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
	                
	                loadDanhSachPhong();
	                dialog.dispose();
	            } else {
	                JOptionPane.showMessageDialog(dialog, "Lỗi khi đặt phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            }
	            
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	    
	    pnlButtons.add(btnDatPhong);
	    pnlButtons.add(Box.createRigidArea(new Dimension(15, 0)));
	    pnlButtons.add(btnHuy);
	    
	    pnlInfo.add(pnlButtons);

	    pnlMain.add(pnlInfo, BorderLayout.WEST);
	    pnlMain.add(pnlPhongTrong, BorderLayout.CENTER);
	    pnlMain.add(pnlDichVu, BorderLayout.EAST);
	    
	    dialog.add(pnlMain);
	    dialog.setVisible(true);
	}

	private JPanel createPanelDichVu() {
	    JPanel pnlDichVuContainer = new JPanel(new BorderLayout());
	    pnlDichVuContainer.setBackground(PANEL_COLOR);
	    pnlDichVuContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
	    
	    JLabel lblDichVuTitle = new JLabel("Dịch vụ bổ sung");
	    lblDichVuTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    lblDichVuTitle.setForeground(HEADER_COLOR);
	    lblDichVuTitle.setBorder(new EmptyBorder(5, 5, 10, 5));
	    
	    JPanel pnlDichVu = new JPanel();
	    pnlDichVu.setLayout(new BoxLayout(pnlDichVu, BoxLayout.Y_AXIS));
	    pnlDichVu.setBackground(PANEL_COLOR);
	    pnlDichVu.setBorder(new EmptyBorder(10, 10, 10, 10));
	    
	    ArrayList<DichVu> dsDichVu = DichVuDAO.getAllDichVu();
	    
	    for (DichVu dv : dsDichVu) {
	        JPanel pnlItem = new JPanel();
	        pnlItem.setLayout(new BoxLayout(pnlItem, BoxLayout.Y_AXIS));
	        pnlItem.setBackground(Color.WHITE);
	        pnlItem.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(Color.decode("#BDC3C7"), 1),
	            new EmptyBorder(10, 15, 10, 15)
	        ));
	        pnlItem.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	        pnlItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
	        
	        // Hàng 1: Checkbox + Tên DV
	        JPanel pnlRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
	        pnlRow1.setBackground(Color.WHITE);
	        
	        JCheckBox chk = new JCheckBox();
	        chk.setBackground(Color.WHITE);
	        chk.setFocusPainted(false);
	        chk.setCursor(new Cursor(Cursor.HAND_CURSOR));
	        
	        JLabel lblTenDV = new JLabel(dv.getTenDV());
	        lblTenDV.setFont(new Font("Segoe UI", Font.BOLD, 14));
	        
	        pnlRow1.add(chk);
	        pnlRow1.add(lblTenDV);
	        
	        // Hàng 2: Giá + Đơn vị + Số lượng
	        JPanel pnlRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
	        pnlRow2.setBackground(Color.WHITE);
	        
	        JLabel lblGiaDV = new JLabel(String.format("%,.0f VNĐ/%s", dv.getGiaDV(), dv.getDonViTinh()));
	        lblGiaDV.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	        lblGiaDV.setForeground(Color.decode("#27AE60"));
	        
	        JPanel pnlSoLuong = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
	        pnlSoLuong.setBackground(Color.WHITE);
	        
	        JLabel lblSL = new JLabel("SL:");
	        lblSL.setFont(new Font("Segoe UI", Font.PLAIN, 11));
	        
	        JSpinner spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
	        spnSoLuong.setEnabled(false);
	        spnSoLuong.setPreferredSize(new Dimension(60, 25));
	        spnSoLuong.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	        
	        JComponent editor = spnSoLuong.getEditor();
	        if (editor instanceof JSpinner.DefaultEditor) {
	            JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
	            spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
	        }
	        
	        JLabel lblDonVi = new JLabel(dv.getDonViTinh());
	        lblDonVi.setFont(new Font("Segoe UI", Font.ITALIC, 11));
	        lblDonVi.setForeground(Color.GRAY);
	        
	        pnlSoLuong.add(lblSL);
	        pnlSoLuong.add(spnSoLuong);
	        pnlSoLuong.add(lblDonVi);
	        
	        pnlRow2.add(lblGiaDV);
	        pnlRow2.add(Box.createHorizontalGlue());
	        pnlRow2.add(pnlSoLuong);
	        
	        chk.addActionListener(ev -> {
	            spnSoLuong.setEnabled(chk.isSelected());
	            Color bgColor = chk.isSelected() ? Color.decode("#E3F2FD") : Color.WHITE;
	            Color borderColor = chk.isSelected() ? Color.decode("#2196F3") : Color.decode("#BDC3C7");
	            
	            pnlItem.setBackground(bgColor);
	            pnlItem.setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createLineBorder(borderColor, chk.isSelected() ? 2 : 1),
	                new EmptyBorder(10, 15, 10, 15)
	            ));
	            pnlRow1.setBackground(bgColor);
	            pnlRow2.setBackground(bgColor);
	            pnlSoLuong.setBackground(bgColor);
	        });
	        
	        pnlItem.add(pnlRow1);
	        pnlItem.add(pnlRow2);
	        
	        pnlDichVu.add(pnlItem);
	        pnlDichVu.add(Box.createRigidArea(new Dimension(0, 8)));
	    }
	    
	    JScrollPane scrollDV = new JScrollPane(pnlDichVu);
	    scrollDV.setBorder(BorderFactory.createLineBorder(Color.decode("#BDC3C7"), 1));
	    scrollDV.getVerticalScrollBar().setUnitIncrement(16);
	    
	    pnlDichVuContainer.add(lblDichVuTitle, BorderLayout.NORTH);
	    pnlDichVuContainer.add(scrollDV, BorderLayout.CENTER);
	    
	    return pnlDichVuContainer;
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
		
		ArrayList<KhachHang> dsKH = KhachHangDAO.getAllKhachHang();
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
	private void showChonKhuyenMaiDialog(JDialog parent, JTextField txtKhuyenMai, JDateChooser dateNgayNhan, JDateChooser dateNgayTra) {
	    // Kiểm tra ngày nhận và ngày trả đã được chọn chưa
	    Date ngayNhan = dateNgayNhan.getDate();
	    Date ngayTra = dateNgayTra.getDate();
	    
	    if (ngayNhan == null || ngayTra == null) {
	        JOptionPane.showMessageDialog(parent, 
	            "Vui lòng chọn ngày nhận và ngày trả trước khi chọn khuyến mãi!",
	            "Thông báo", JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    
	    JDialog dialogKM = new JDialog(parent, "Chọn khuyến mãi", true);
	    dialogKM.setSize(1000, 500);
	    dialogKM.setLocationRelativeTo(parent);
	    dialogKM.setLayout(new BorderLayout(10, 10));
	    
	    JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
	    pnlMain.setBackground(PANEL_COLOR);
	    pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
	    
	    JLabel lblTitle = new JLabel("Danh sách khuyến mãi", JLabel.CENTER);
	    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    lblTitle.setForeground(HEADER_COLOR);
	    
	    String[] columns = {"Mã KM", "Tên KM", "Mức giảm giá", "Điều kiện", "Ngày bắt đầu", "Ngày kết thúc"};
	    DefaultTableModel modelKM = new DefaultTableModel(columns, 0) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false;
	        }
	    };
	    
	    JTable tblKM = new JTable(modelKM);
	    tblKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    tblKM.setRowHeight(35);
	    tblKM.setSelectionBackground(Color.decode("#3498DB"));
	    tblKM.setSelectionForeground(Color.WHITE);
	    tblKM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    JTableHeader header = tblKM.getTableHeader();
	    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
	    header.setBackground(HEADER_COLOR);
	    header.setForeground(Color.WHITE);
	    header.setPreferredSize(new Dimension(header.getWidth(), 40));
	    
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    for (int i = 0; i < columns.length; i++) {
	        tblKM.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
	    }
	    tblKM.getColumnModel().getColumn(1).setPreferredWidth(120);
	    
	    // Chuyển đổi Date sang LocalDate
	    LocalDate ngayNhanLD = new java.sql.Date(ngayNhan.getTime()).toLocalDate();
	    LocalDate ngayTraLD = new java.sql.Date(ngayTra.getTime()).toLocalDate();
	    
	    // Lấy danh sách khuyến mãi phù hợp
	    ArrayList<KhuyenMai> dsKM = KhuyenMaiDAO.getKhuyenMaiTheoKhoangNgay(ngayNhanLD, ngayTraLD);
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    
	    if (dsKM.isEmpty()) {
	        JOptionPane.showMessageDialog(parent, 
	            "Không có khuyến mãi nào phù hợp với khoảng thời gian đặt phòng!",
	            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	        dialogKM.dispose();
	        return;
	    }
	    
	    for (KhuyenMai km : dsKM) {
	        modelKM.addRow(new Object[]{
	            km.getMaKM(),
	            km.getTenKM(),
	            String.format("%.0f%%", km.getMucGiamGia()),
	            km.getDieuKienApDung(),
	            sdf.format(java.sql.Date.valueOf(km.getNgayBatDau())),
	            sdf.format(java.sql.Date.valueOf(km.getNgayKetThuc()))
	        });
	    }
	    
	    JScrollPane scroll = new JScrollPane(tblKM);
	    scroll.setBorder(null);
	    
	    JPanel pnlButtons = new JPanel();
	    pnlButtons.setBackground(PANEL_COLOR);
	    
	    JButton btnXacNhan = createStyledButton("Xác nhận", Color.decode("#27AE60"), 120, 40);
	    JButton btnHuy = createStyledButton("Hủy", Color.decode("#95A5A6"), 120, 40);
	    
	    btnXacNhan.addActionListener(e -> {
	        int selectedRow = tblKM.getSelectedRow();
	        if (selectedRow < 0) {
	            JOptionPane.showMessageDialog(dialogKM, "Vui lòng chọn một khuyến mãi!");
	            return;
	        }
	        
	        String maKM = modelKM.getValueAt(selectedRow, 0).toString();
	        txtKhuyenMai.setText(maKM);
	        dialogKM.dispose();
	    });
	    
	    btnHuy.addActionListener(e -> dialogKM.dispose());
	    
	    pnlButtons.add(btnXacNhan);
	    pnlButtons.add(Box.createRigidArea(new Dimension(10, 0)));
	    pnlButtons.add(btnHuy);
	    
	    pnlMain.add(lblTitle, BorderLayout.NORTH);
	    pnlMain.add(scroll, BorderLayout.CENTER);
	    pnlMain.add(pnlButtons, BorderLayout.SOUTH);
	    
	    dialogKM.add(pnlMain);
	    dialogKM.setVisible(true);
	}
	private JPanel createFieldRow(String labelText, JComponent comp) {
	    JPanel panel = new JPanel(new BorderLayout(10, 0));
	    panel.setBackground(PANEL_COLOR);
	    panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	    panel.setMaximumSize(new Dimension(380, 40)); 
	    
	    JLabel label = new JLabel(labelText);
	    label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    label.setPreferredSize(new Dimension(100, 35));
	    
	    if (comp instanceof JTextField) {
	        JTextField txt = (JTextField) comp;
	        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        txt.setPreferredSize(new Dimension(250, 35));
	    } else if (comp instanceof JDateChooser) {
	        comp.setPreferredSize(new Dimension(250, 35));
	    }
	    
	    panel.add(label, BorderLayout.WEST);
	    panel.add(comp, BorderLayout.CENTER);
	    
	    return panel;
	}
}