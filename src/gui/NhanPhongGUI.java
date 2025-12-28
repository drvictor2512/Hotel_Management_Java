package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
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
import dao.NhanPhongDAO;
import dao.PhongDAO;
import entity.ChiTietDichVu;
import entity.ChiTietDonDatPhong;
import entity.DichVu;
import entity.DonDatPhong;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.Phong;

public class NhanPhongGUI extends MenuGUI {
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
	
	public NhanPhongGUI(NhanVien nv) {
		super("Nhận phòng", nv);
		this.nhanVien = nv;
		
		JPanel pnlMain = new JPanel(new BorderLayout());
		pnlMain.setBackground(BACKGROUND_COLOR);
		
		// Panel top
		JPanel pnlTop = new JPanel(new BorderLayout());
		pnlTop.setBackground(PANEL_COLOR);
		pnlTop.setBorder(new EmptyBorder(20, 30, 20, 30));
		
		JLabel lblTieuDe = new JLabel("QUẢN LÝ NHẬN PHÒNG", SwingConstants.CENTER);
		lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTieuDe.setForeground(HEADER_COLOR);
		
		// Panel chú thích và nút đặt phòng
		JPanel pnlChuThich = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		pnlChuThich.setBackground(PANEL_COLOR);
		
		pnlChuThich.add(createLegendItem("Đã đặt", PHONG_DA_DAT_COLOR));
		pnlChuThich.add(createLegendItem("Đang sử dụng", PHONG_DANG_SU_DUNG_COLOR));
		
		JButton btnDatPhongMoi = createStyledButton("Nhận phòng", Color.decode("#27AE60"), 150, 35);
		btnDatPhongMoi.addActionListener(e -> showNhanPhongGUI());
		pnlChuThich.add(btnDatPhongMoi);
		
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
		
		//Create phòng ma vì GridLayout kéo giãn ô phòng ra nếu không có đủ 40 ô why the fuck does it do that
		ArrayList<Phong> ds = PhongDAO.getDSPhong();
		for (int i = ds.size(); i > dsPhong.size(); i--) {
			JPanel oPhong = new JPanel();

			oPhong.setLayout(new BorderLayout(5, 5));
			oPhong.setPreferredSize(new Dimension(150, 120));
			oPhong.setBorder(
				new EmptyBorder(10, 10, 10, 10)
			);
			
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
			String trangThai = dp[13].toString();
			if ((dp[3].equals(maPhong) && "Đã đặt".equals(trangThai)) || (dp[3].equals(maPhong) && "Đang thuê".equals(trangThai))) {
				dsDonCuaPhong.add(dp);
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
	
	private void showNhanPhongGUI() {
	    JDialog dialog = new JDialog(this, "Nhận nhiều phòng", true);
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

	    JLabel lblTitle = new JLabel("Thông tin nhận phòng");
	    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
	    lblTitle.setForeground(HEADER_COLOR);
	    lblTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
	    pnlInfo.add(lblTitle);
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 20)));
	    	    
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
	    
	    JDateChooser dateNgayNhan = new JDateChooser();
	    dateNgayNhan.setDateFormatString("dd/MM/yyyy");
	    dateNgayNhan.setPreferredSize(new Dimension(200, 35));
	    
	    JDateChooser dateNgayTra = new JDateChooser();
	    dateNgayTra.setDateFormatString("dd/MM/yyyy");
	    dateNgayTra.setPreferredSize(new Dimension(200, 35));

	    // Sử dụng phương pháp mới để tạo các field row
	    
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
	    
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 62)));
	    pnlInfo.add(createFieldRow("Ngày nhận:", dateNgayNhan));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Ngày trả:", dateNgayTra));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));

	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 20)));
	    
	    JPanel pnlChonKM = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    pnlChonKM.setBackground(PANEL_COLOR);
	    pnlChonKM.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	    pnlChonKM.setMaximumSize(new Dimension(380, 45));
	    pnlChonKM.add(Box.createRigidArea(new Dimension(180, 0)));

	    pnlInfo.add(pnlChonKM);
	    
	    // Panel chọn phòng trống
	    JPanel pnlPhongTrong = new JPanel(new BorderLayout(10, 10));
	    pnlPhongTrong.setBackground(PANEL_COLOR);
	    pnlPhongTrong.setBorder(new EmptyBorder(10, 10, 10, 10));
	    
	    JLabel lblPhongTitle = new JLabel("Chọn phòng (chọn khách hàng trước & chọn đơn đặt phòng)");
	    lblPhongTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    lblPhongTitle.setForeground(HEADER_COLOR);
	    lblPhongTitle.setBorder(new EmptyBorder(5, 5, 10, 5));
	    
	    String[] columns = {"Chọn", "Mã phòng", "Tên phòng", "Loại phòng", "Đơn giá", "Ngày đặt", "Ngày nhận", "Ngày trả dự kiến"};
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
	    tblPhong.getColumnModel().getColumn(1).setPreferredWidth(80);
	    tblPhong.getColumnModel().getColumn(2).setPreferredWidth(100);
	    tblPhong.getColumnModel().getColumn(3).setPreferredWidth(70);
	    tblPhong.getColumnModel().getColumn(4).setPreferredWidth(80);
	    tblPhong.getColumnModel().getColumn(5).setPreferredWidth(90);
	    tblPhong.getColumnModel().getColumn(6).setPreferredWidth(90);
	    tblPhong.getColumnModel().getColumn(6).setPreferredWidth(90);
	    
	    JTableHeader header = tblPhong.getTableHeader();
	    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
	    header.setBackground(HEADER_COLOR);
	    header.setForeground(Color.WHITE);
	    header.setPreferredSize(new Dimension(header.getWidth(), 45));
	    
	    // Center alignment cho các cột
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    tblPhong.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	    tblPhong.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
	    tblPhong.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
	    tblPhong.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
	    tblPhong.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
	    tblPhong.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
	    tblPhong.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
	    
	    
	    JScrollPane scrollPhong = new JScrollPane(tblPhong);
	    scrollPhong.setBorder(BorderFactory.createLineBorder(Color.decode("#BDC3C7"), 1));
	    scrollPhong.setPreferredSize(new Dimension(600, 500));
	    
	    btnChonKH.addActionListener(e -> {
	        showChonKhachHangDialog(dialog, maKHSelected, txtMaKH, txtTenKH);
	        txtMaDP.setText("");
	        modelPhong.setRowCount(0);
	        btnChonDP.setEnabled(true);
	    });
	    
	    btnChonDP.addActionListener(e -> {
	        showChonDatPhongDialog(dialog, maKHSelected, txtMaDP, txtMaKH);
	        modelPhong.setRowCount(0);
	    });
	    
	    JButton btnTimPhong = createStyledButton("Tìm phòng đã đặt", Color.decode("#3498DB"), 180, 45);
	    btnTimPhong.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    btnTimPhong.addActionListener(e -> {
	        
	        if (txtMaKH.getText().trim() == null) {
	            JOptionPane.showMessageDialog(dialog, "Vui lòng chọn khách hàng!");
	            return;
	        }
	        
	        if (txtMaDP.getText().trim() == null) {
	            JOptionPane.showMessageDialog(dialog, "Vui lòng chọn đơn đặt phòng!");
	            return;
	        }
	        
	        
	        modelPhong.setRowCount(0);
	        ArrayList<Object[]> dsPhong = DatPhongDAO.layPhongDaDatTheoDPVaKH(
	            txtMaKH.getText().trim(),
	            txtMaDP.getText().trim(),
	            "Đã đặt"
	        );
	        
	        if (dsPhong.size() == 0) {
	        	JOptionPane.showMessageDialog(dialog, "Không có phòng nào có thể nhận được!");
	            return;
	        }
	        
	        for (Object[] p : dsPhong) {
	            modelPhong.addRow(new Object[]{
	                false,
	                p[0],
	                p[1],
	                p[2],
	                String.format("%,.0f" + "đ", p[3]),
	                p[4],
	                p[5],
	                p[6]	                
	            });
	        }
	        
	        // Set ngày
	        tblPhong.getModel().addTableModelListener(ev -> {
	        	for (int i = 0; i < modelPhong.getRowCount(); i++) {
	                Boolean checked = (Boolean) modelPhong.getValueAt(i, 0);
	                if (checked != null && checked) {
	                    Date nhan = (Date) modelPhong.getValueAt(i, 6);
	                    Date tra = (Date) modelPhong.getValueAt(i, 7);
	                    
	                    dateNgayNhan.setDate(nhan);
	                    dateNgayTra.setDate(tra);
	                }
	            }
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
	    
	    JButton btnNhanPhong = createStyledButton("Nhận phòng", Color.decode("#27AE60"), 140, 45);
	    JButton btnHuy = createStyledButton("Hủy", Color.red, 140, 45);
	    
	    btnHuy.addActionListener(e -> dialog.dispose());
	    
	    btnNhanPhong.addActionListener(e -> {
	        try {
	        	String maKH = txtMaKH.getText();
	        	
	        	if (maKH == null) {
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
	            
	            if (dateNgayNhan == null || dateNgayTra == null) {
	        		JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày nhận và ngày trả!");
	                return;
	        	}
	            	            
	                       
	            for (String maPhong : phongDaChon) {
                    PhongDAO.capNhatTinhTrangPhong(maPhong, "Đang thuê");
                    NhanPhongDAO.NhanDonDatPhong(maPhong, txtMaDP.getText(), "Đang thuê");
                }
	            
	            for (int i = 0; i < modelPhong.getRowCount(); i++) {
	                Boolean checked = (Boolean) modelPhong.getValueAt(i, 0);
	                if (checked != null && checked) {
	                    String maChon = modelPhong.getValueAt(i, 1).toString();
	                    java.util.Date ngayTra = dateNgayTra.getDate();
	                    if (ngayTra != null) {
	                        java.sql.Date tra = new java.sql.Date(ngayTra.getTime());
	                        DatPhongDAO.capNhatNgayTra(maChon, tra);
	                    }
	                }
	            }
                
                JOptionPane.showMessageDialog(dialog, 
                    "Nhận thành công " + phongDaChon.size() + " phòng!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
                loadDanhSachPhong();
                dialog.dispose();
                
	            
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	    
	    pnlButtons.add(btnNhanPhong);
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
		
		String[] columns = {"Mã KH", "Họ tên", "CCCD", "Tuổi", "SĐT", "Giới tính", "Ngày đặt"};
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
		
		ArrayList<KhachHang> dsKH = KhachHangDAO.getAllKhachHangCoDatPhong();
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
		
		
		ArrayList<Object[]> dsDP = DatPhongDAO.getDonDatPhongDaDat(maKH);
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
}
