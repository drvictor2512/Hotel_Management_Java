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

import com.github.lgooddatepicker.zinternaltools.WrapLayout;
import com.toedter.calendar.JDateChooser;
import dao.DoiPhongDAO;
import dao.HuyPhongDAO;
import dao.PhongDAO;
import entity.NhanVien;
import entity.Phong;

public class DoiPhongGUI extends MenuGUI {
	private JPanel pnlPhongGrid;
	private ArrayList<JPanel> danhSachOPhong;
	private NhanVien nhanVien;
	
	// Màu sắc
	private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
	private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
	private static final Color HEADER_COLOR = Color.decode("#34495E");
	private static final Color PHONG_DA_DAT_COLOR = Color.decode("#F39C12");
	private static final Color PHONG_DANG_SU_DUNG_COLOR = Color.decode("#E74C3C");
	public DoiPhongGUI(NhanVien nv) {
		super("Đổi phòng", nv);
		this.nhanVien = nv;
		
		JPanel pnlMain = new JPanel(new BorderLayout());
		pnlMain.setBackground(BACKGROUND_COLOR);
		
		// Panel top
		JPanel pnlTop = new JPanel(new BorderLayout());
		pnlTop.setBackground(PANEL_COLOR);
		pnlTop.setBorder(new EmptyBorder(20, 30, 20, 30));
		
		JLabel lblTieuDe = new JLabel("QUẢN LÝ ĐỔI PHÒNG", SwingConstants.CENTER);
		lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTieuDe.setForeground(HEADER_COLOR);
		
		// Panel chú thích và nút đổi phòng
		JPanel pnlChuThich = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		pnlChuThich.setBackground(PANEL_COLOR);
		
		pnlChuThich.add(createLegendItem("Đã đặt", PHONG_DA_DAT_COLOR));
		pnlChuThich.add(createLegendItem("Đang sử dụng", PHONG_DANG_SU_DUNG_COLOR));
		
		
		JButton btnDoiPhongMoi = createStyledButton("Đổi phòng", Color.decode("#27AE60"), 150, 35);
		btnDoiPhongMoi.addActionListener(e -> showDoiPhongGUI());
		pnlChuThich.add(btnDoiPhongMoi);
		
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
	
	// Tải dạnh sách phòng
	private void loadDanhSachPhong() {
        pnlPhongGrid.removeAll();
        danhSachOPhong.clear();
        ArrayList<String> phongCoDonDaDat = HuyPhongDAO.layPhongCoDonDaDat();
        
        for (String maPhong : phongCoDonDaDat) {
            Phong phong = PhongDAO.getMaP(maPhong);
            if (phong != null) {
                JPanel oPhong = createOPhong(phong);
                danhSachOPhong.add(oPhong);
                pnlPhongGrid.add(oPhong);
            }
        }
        ArrayList<Phong> ds = PhongDAO.getDSPhong();
		for (int i = ds.size(); i > phongCoDonDaDat.size(); i--) {
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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(270,140));
        
        // Xác định màu dựa vào trạng thái phòng
        Color bgColor;
        if ("Đang thuê".equals(phong.getTrangThai())) {
            bgColor = PHONG_DANG_SU_DUNG_COLOR;
        } else {
            bgColor = PHONG_DA_DAT_COLOR;
        }
        
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel pnlInfo = new JPanel();
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        pnlInfo.setBackground(bgColor);
        
        JLabel lblMaPhong = new JLabel(phong.getMaPhong());
        lblMaPhong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblMaPhong.setForeground(Color.WHITE);
        lblMaPhong.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        JLabel lblTenPhong = new JLabel(phong.getTenPhong(), SwingConstants.CENTER);
        lblTenPhong.setFont(new Font("Roboto", Font.BOLD, 16));
        lblTenPhong.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lblTenPhong.setForeground(Color.WHITE);

        JLabel lblLoai = new JLabel(phong.getLoaiPhong().getTenLoaiPhong(), SwingConstants.CENTER);
        lblLoai.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lblLoai.setForeground(Color.WHITE);

        JLabel lblGia = new JLabel(String.format("%,.0f VNĐ", phong.getDonGia()), SwingConstants.CENTER);
        lblGia.setFont(new Font("Roboto", Font.BOLD, 13));
        lblGia.setForeground(Color.WHITE);
        lblGia.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        pnlInfo.add(lblMaPhong);
        pnlInfo.add(Box.createRigidArea(new Dimension(0, 5)));      
        pnlInfo.add(lblTenPhong);
        pnlInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlInfo.add(lblLoai);
        pnlInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlInfo.add(lblGia);

        panel.add(pnlInfo, BorderLayout.CENTER);

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
                showThongTinDatPhong(phong.getMaPhong());
            }
        });

        return panel;
    }
	
	private void showThongTinDatPhong(String maPhong) {
		ArrayList<Object[]> dsDatPhong = DoiPhongDAO.layTatCaDonDatPhong();
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
	
	// Chức năng đổi phòng

	private void showDoiPhongGUI() {
	    JDialog dialog = new JDialog(this, "Đổi phòng", true);
	    dialog.setSize(1200, 800);
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

	    JLabel lblTitle = new JLabel("Thông tin đổi phòng");
	    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
	    lblTitle.setForeground(HEADER_COLOR);
	    lblTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
	    pnlInfo.add(lblTitle);
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 15)));

	    // --- Thông tin đơn gốc ---
	    JLabel lblSubTitle1 = new JLabel("Thông tin đơn gốc");
	    lblSubTitle1.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    lblSubTitle1.setForeground(HEADER_COLOR);
	    pnlInfo.add(lblSubTitle1);
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 10)));

	    JTextField txtMaDP = new JTextField();
	    txtMaDP.setEditable(false);
	    txtMaDP.setBackground(Color.decode("#ECF0F1"));

	    JTextField txtMaPhongGoc = new JTextField(20);
	    txtMaPhongGoc.setEditable(false);
	    txtMaPhongGoc.setBackground(Color.decode("#ECF0F1"));

	    JTextField txtTenPhongGoc = new JTextField(20);
	    txtTenPhongGoc.setEditable(false);
	    txtTenPhongGoc.setBackground(Color.decode("#ECF0F1"));

	    JDateChooser dateNgayDat = new JDateChooser();
	    dateNgayDat.setDateFormatString("dd/MM/yyyy");
	    dateNgayDat.setEnabled(false);

	    JDateChooser dateNgayNhan = new JDateChooser();
	    dateNgayNhan.setDateFormatString("dd/MM/yyyy");
	    dateNgayNhan.setEnabled(false);

	    JDateChooser dateNgayTra = new JDateChooser();
	    dateNgayTra.setDateFormatString("dd/MM/yyyy");
	    dateNgayTra.setEnabled(false);

	    JTextField txtTienCocGoc = new JTextField(20);
	    txtTienCocGoc.setEditable(false);
	    txtTienCocGoc.setBackground(Color.decode("#ECF0F1"));

	    JTextField txtKhuyenMaiGoc = new JTextField(20);
	    txtKhuyenMaiGoc.setEditable(false);
	    txtKhuyenMaiGoc.setBackground(Color.decode("#ECF0F1"));

	    pnlInfo.add(createFieldRow("Mã đặt phòng:", txtMaDP));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Mã phòng gốc:", txtMaPhongGoc));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Tên phòng gốc:", txtTenPhongGoc));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Ngày đặt:", dateNgayDat));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Ngày nhận:", dateNgayNhan));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Ngày trả:", dateNgayTra));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Tiền cọc:", txtTienCocGoc));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Khuyến mãi:", txtKhuyenMaiGoc));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 20)));

	    // Nút chọn đơn
	    JButton btnChonDonPhong = createStyledButton("Chọn Đơn Phòng Cần Đổi", Color.decode("#3498DB"), 250, 40);
	    JPanel pnlChonDon = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
	    pnlChonDon.setBackground(PANEL_COLOR);
	    pnlChonDon.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	    pnlChonDon.setMaximumSize(new Dimension(380, 45));
	    pnlChonDon.add(btnChonDonPhong);
	    pnlInfo.add(pnlChonDon);
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 20)));

	    // Panel chọn phòng trống
	    JPanel pnlPhongTrong = new JPanel(new BorderLayout(10, 10));
	    pnlPhongTrong.setBackground(PANEL_COLOR);
	    pnlPhongTrong.setBorder(new EmptyBorder(10, 10, 10, 10));

	    JLabel lblPhongTitle = new JLabel("Chọn phòng mới thay thế");
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
	    tblPhong.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
	    tblPhong.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
	    tblPhong.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
	    
	    JScrollPane scrollPhong = new JScrollPane(tblPhong);
	    scrollPhong.setBorder(BorderFactory.createLineBorder(Color.decode("#BDC3C7"), 1));
	    scrollPhong.setPreferredSize(new Dimension(600, 500));

	    // CHỈ CHỌN 1 PHÒNG: bỏ chọn các dòng khác khi tick 1 dòng
	    tblPhong.getModel().addTableModelListener(e -> {
	        if (e.getColumn() == 0) {
	            int rowChanged = e.getFirstRow();
	            Boolean checked = (Boolean) tblPhong.getValueAt(rowChanged, 0);
	            if (checked != null && checked) {
	                for (int i = 0; i < tblPhong.getRowCount(); i++) {
	                    if (i != rowChanged) {
	                        tblPhong.setValueAt(false, i, 0);
	                    }
	                }
	            }
	        }
	    });

	    pnlPhongTrong.add(lblPhongTitle, BorderLayout.NORTH);
	    pnlPhongTrong.add(scrollPhong, BorderLayout.CENTER);

	    // Buttons
	    JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
	    pnlButtons.setBackground(PANEL_COLOR);
	    pnlButtons.setBorder(new EmptyBorder(30, 0, 0, 0));
	    pnlButtons.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	    pnlButtons.setMaximumSize(new Dimension(380, 70));

	    JButton btnXacNhanDoi = createStyledButton("Đổi phòng", Color.decode("#27AE60"), 140, 45);
	    JButton btnHuy = createStyledButton("Hủy", Color.red, 140, 45);

	    btnHuy.addActionListener(e -> dialog.dispose());
	    pnlButtons.add(btnXacNhanDoi);
	    pnlButtons.add(Box.createRigidArea(new Dimension(15, 0)));
	    pnlButtons.add(btnHuy);

	    pnlInfo.add(pnlButtons);

	    pnlMain.add(pnlInfo, BorderLayout.WEST);
	    pnlMain.add(pnlPhongTrong, BorderLayout.CENTER);
	    dialog.add(pnlMain);

	    //  LOGIC CHỌN ĐƠN PHÒNG
	    btnChonDonPhong.addActionListener(e -> {
	        showChonDonDatPhongDialog(dialog, txtMaDP, txtMaPhongGoc, txtTenPhongGoc,
	                                  dateNgayDat, dateNgayNhan, dateNgayTra,
	                                  txtTienCocGoc, txtKhuyenMaiGoc);
         // Tự động tìm phòng
	        if (dateNgayNhan.getDate() != null && dateNgayTra.getDate() != null) {
	            timPhongTrong(dateNgayNhan.getDate(), dateNgayTra.getDate(), modelPhong, txtMaPhongGoc.getText());
	        }
	    });

	    //  LOGIC ĐỔI PHÒNGa
	 
		btnXacNhanDoi.addActionListener(e -> {
			try {
				String maDP = txtMaDP.getText();
				String maPhongCu = txtMaPhongGoc.getText();

				if (maDP.isEmpty() || maPhongCu.isEmpty()) {
					JOptionPane.showMessageDialog(dialog, "Vui lòng chọn đơn phòng cần đổi!");
					return;
				}

				// Lấy phòng mới và giá phòng mới
				String maPhongMoi = null;
				double giaPhongMoi = 0;
				for (int i = 0; i < modelPhong.getRowCount(); i++) {
					Boolean checked = (Boolean) modelPhong.getValueAt(i, 0);
					if (checked != null && checked) {
						maPhongMoi = modelPhong.getValueAt(i, 1).toString();
						String strDonGia = modelPhong.getValueAt(i, 4).toString().replace(",", "");
						giaPhongMoi = Double.parseDouble(strDonGia);
						break;
					}
				}

				if (maPhongMoi == null) {
					JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một phòng mới để đổi!");
					return;
				}

				// Tính tiền cọc = 30% giá phòng mới
				double tienCocMoi = giaPhongMoi * 0.3;
				txtTienCocGoc.setText(String.format("%,.0f", tienCocMoi));

				// 1. Cập nhật phòng và tiền cọc trong DB
				boolean ok = DoiPhongDAO.capNhatPhongTrongCTDP(maDP, maPhongCu, maPhongMoi, tienCocMoi);
					
				if (ok) {
					// 3. KIỂM TRA PHÒNG CŨ
					 int soDonDangThue = DoiPhongDAO.demSoDonDangThue(maPhongCu);
			         int soDonDaDat = DoiPhongDAO.demSoDonDatConLai(maPhongCu);
			         String trangThaiMoi;
					 if (soDonDangThue > 0) {
			                // Ưu tiên: Nếu còn đơn "Đang thuê" -> Màu đỏ
			                trangThaiMoi = "Đang thuê";
			            } else if (soDonDaDat > 0) {
			                // Nếu không có "Đang thuê" nhưng còn "Đã đặt" -> Màu vàng
			                trangThaiMoi = "Đã đặt";
			            } else {
			                // Không còn đơn nào -> Màu xanh
			                trangThaiMoi = "Trống";
			            }
					 DoiPhongDAO.capNhatTinhTrangPhong(maPhongCu, trangThaiMoi);

					 int soDonDangThuePhongMoi = DoiPhongDAO.demSoDonDangThue(maPhongMoi);
					 String trangThaiPhongMoi;
					 if (soDonDangThuePhongMoi > 0) {
					     // Nếu phòng mới có đơn "Đang thuê" -> Giữ màu đỏ
					     trangThaiPhongMoi = "Đang thuê";
					 } else {
					     // Nếu không có "Đang thuê" -> Chuyển sang "Đã đặt" (màu vàng)
					     trangThaiPhongMoi = "Đã đặt";
					 }
					 DoiPhongDAO.capNhatTinhTrangPhong(maPhongMoi, trangThaiPhongMoi);
					
					JOptionPane.showMessageDialog(dialog,
							"Đổi phòng thành công từ " + maPhongCu + " sang " + maPhongMoi,
							"Thành công", JOptionPane.INFORMATION_MESSAGE);

					loadDanhSachPhong(); 
					dialog.dispose();
				} else {
					JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật chi tiết đơn đặt phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		});

	    dialog.setVisible(true);
	}

	 // Hàm logic tìm phòng trống
	
	private void timPhongTrong(Date ngayNhan, Date ngayTra, DefaultTableModel modelPhong, String maPhongGoc) {
        if (ngayNhan == null || ngayTra == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn phòng gốc trước!");
            return;
        }
        
        modelPhong.setRowCount(0);
        ArrayList<Object[]> dsPhong = DoiPhongDAO.layPhongTrongTheoNgay(
            new java.sql.Date(ngayNhan.getTime()),
            new java.sql.Date(ngayTra.getTime())
        );
        
        for (Object[] p : dsPhong) {
            // Không hiển thị phòng gốc trong danh sách phòng mới
            if (p[0].toString().equals(maPhongGoc)) {
                continue;
            }
            modelPhong.addRow(new Object[]{
                false, p[0], p[1], p[2], String.format("%,.0f", p[3])
            });
        }
	}
	
	
	private void showChonDonDatPhongDialog(JDialog parent, JTextField txtMaDP, 
	                                        JTextField txtMaPhong, JTextField txtTenPhong, 
	                                        JDateChooser dateNgayDat, JDateChooser dateNgayNhan, 
	                                        JDateChooser dateNgayTra, JTextField txtTienCoc, 
	                                        JTextField txtKhuyenMai) {
	    JDialog dialog = new JDialog(parent, "Chọn đơn đặt phòng cần đổi", true);
	    dialog.setSize(1000, 600);
	    dialog.setLocationRelativeTo(parent);
	    dialog.setLayout(new BorderLayout(10, 10));
	    
	    JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
	    pnlMain.setBackground(PANEL_COLOR);
	    pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
	    
	    JLabel lblTitle = new JLabel("Danh sách đơn đặt phòng (Trạng thái 'Đã đặt')", JLabel.CENTER);
	    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    lblTitle.setForeground(HEADER_COLOR);
	    
	    String[] columns = {"Mã ĐP", "Mã Phòng", "Tên Phòng", "Tên KH", "Ngày đặt", "Ngày nhận", "Ngày trả", "Tiền cọc", "KM"};
	    DefaultTableModel model = new DefaultTableModel(columns, 0) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false;
	        }
	    };
	    
	    JTable table = new JTable(model);
	    table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    table.setRowHeight(35);
	    table.setSelectionBackground(Color.decode("#3498DB"));
	    table.setSelectionForeground(Color.WHITE);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    JTableHeader header = table.getTableHeader();
	    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
	    header.setBackground(HEADER_COLOR);
	    header.setForeground(Color.WHITE);
	    header.setPreferredSize(new Dimension(header.getWidth(), 40));
	    table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(80);
		table.getColumnModel().getColumn(8).setPreferredWidth(80);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < columns.length; i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			
		}
	    // Đổ dữ liệu
	    ArrayList<Object[]> dsDatPhong = DoiPhongDAO.layTatCaDonDatPhong();
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    
	    for (Object[] dp : dsDatPhong) {
	        String trangThai = (String) dp[13];
	        
	        // Chỉ lấy đơn đã đặt
	        if ("Đã đặt".equals(trangThai)) {
	            String maDP = dp[0].toString();
	            String maPhong = dp[3].toString();
	            String tenKH = dp[2].toString();
	            java.sql.Date ngayDatSql = (java.sql.Date) dp[7];
	            java.sql.Date ngayNhanSql = (java.sql.Date) dp[8];
	            java.sql.Date ngayTraSql = (java.sql.Date) dp[9];
	            double tienCoc = (double) dp[10];
	            String mucGiamGiaStr = dp[11].toString(); 

	            Phong phong = DoiPhongDAO.getMaP(maPhong); 
	            String tenPhong = (phong != null) ? phong.getTenPhong() : "Không rõ";

	            model.addRow(new Object[]{
	                maDP,
	                maPhong,
	                tenPhong,
	                tenKH,
	                sdf.format(ngayDatSql),
	                sdf.format(ngayNhanSql),
	                sdf.format(ngayTraSql),
	                String.format("%,.0f", tienCoc),
	                mucGiamGiaStr + "%"
	            });
	        }
	    }
	    
	    
	    JScrollPane scroll = new JScrollPane(table);
	    scroll.setBorder(null);
	    
	    JPanel pnlButtons = new JPanel();
	    pnlButtons.setBackground(PANEL_COLOR);
	    
	    JButton btnXacNhan = createStyledButton("Xác nhận", Color.decode("#27AE60"), 120, 40);
	    JButton btnHuy = createStyledButton("Hủy", Color.decode("#95A5A6"), 120, 40);
	    
	    btnXacNhan.addActionListener(e -> {
	        int selectedRow = table.getSelectedRow();
	        if (selectedRow < 0) {
	            JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một đơn đặt phòng!");
	            return;
	        }
	        
	        String maDP = model.getValueAt(selectedRow, 0).toString();
	        String maPhong = model.getValueAt(selectedRow, 1).toString();
	        String tenPhong = model.getValueAt(selectedRow, 2).toString();
	        String strTienCoc = model.getValueAt(selectedRow, 7).toString();
	        String strKhuyenMai = model.getValueAt(selectedRow, 8).toString();
	        
	        java.sql.Date ngayDat = null;
	        java.sql.Date ngayNhan = null;
	        java.sql.Date ngayTra = null;

	        for (Object[] dp : dsDatPhong) {
	            if (dp[0].toString().equals(maDP) && dp[3].toString().equals(maPhong)) {
	                ngayDat = (java.sql.Date) dp[7];
	                ngayNhan = (java.sql.Date) dp[8];
	                ngayTra = (java.sql.Date) dp[9];
	                break;
	            }
	        }
	        
	        txtMaDP.setText(maDP);
	        txtMaPhong.setText(maPhong);
	        txtTenPhong.setText(tenPhong);
	        dateNgayDat.setDate(ngayDat);
	        dateNgayNhan.setDate(ngayNhan);
	        dateNgayTra.setDate(ngayTra);
	        txtTienCoc.setText(strTienCoc);
	        txtKhuyenMai.setText(strKhuyenMai); 
	        
	        dialog.dispose();
	    });
	    
	    btnHuy.addActionListener(e -> dialog.dispose());
	    
	    pnlButtons.add(btnXacNhan);
	    pnlButtons.add(Box.createRigidArea(new Dimension(10, 0)));
	    pnlButtons.add(btnHuy);
	    
	    pnlMain.add(lblTitle, BorderLayout.NORTH);
	    pnlMain.add(scroll, BorderLayout.CENTER);
	    pnlMain.add(pnlButtons, BorderLayout.SOUTH);
	    
	    dialog.add(pnlMain);
	    dialog.setVisible(true);
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