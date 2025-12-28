package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.github.lgooddatepicker.zinternaltools.WrapLayout;
import com.toedter.calendar.JDateChooser;

import dao.HuyPhongDAO;
import dao.KhachHangDAO;
import dao.PhongDAO;
import dao.DatPhongDAO;
import entity.KhachHang;
import entity.NhanVien;
import entity.Phong;

public class HuyPhongGUI extends MenuGUI {

    private JPanel pnlPhongGrid;
    private ArrayList<JPanel> danhSachOPhong = new ArrayList<>();
    private NhanVien nv;

    private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
    private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
    private static final Color HEADER_COLOR = Color.decode("#34495E");
    private static final Color PHONG_DA_DAT_COLOR = Color.decode("#F39C12");
	private static final Color PHONG_DANG_SU_DUNG_COLOR = Color.decode("#E74C3C");
	
    public HuyPhongGUI(NhanVien nv) {
        super("Hủy phòng", nv);
        this.nv = nv;

        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(BACKGROUND_COLOR);

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(PANEL_COLOR);
        pnlTop.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTieuDe = new JLabel("QUẢN LÝ HỦY PHÒNG", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setForeground(HEADER_COLOR);

        JPanel pnlChuThich = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlChuThich.setBackground(PANEL_COLOR);

        pnlChuThich.add(createLegendItem("Phòng đã đặt", PHONG_DA_DAT_COLOR));
		pnlChuThich.add(createLegendItem("Đang sử dụng", PHONG_DANG_SU_DUNG_COLOR));

        JButton btnHuy = createStyledButton("Hủy phòng", Color.decode("#27AE60"), 150, 35);
        btnHuy.addActionListener(e -> showHuyPhongGUI());
        pnlChuThich.add(btnHuy);

        pnlTop.add(lblTieuDe, BorderLayout.NORTH);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        pnlTop.add(pnlChuThich, BorderLayout.SOUTH);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(BACKGROUND_COLOR);
        pnlCenter.setBorder(new EmptyBorder(10, 20, 10, 20));

        pnlPhongGrid = new JPanel();
        pnlPhongGrid.setLayout(new GridLayout(0, 5, 15, 15)); 
        pnlPhongGrid.setBackground(BACKGROUND_COLOR);
        pnlPhongGrid.setBorder(new EmptyBorder(20, 20, 20, 20));

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
        ArrayList<Object[]> dsDatPhong = DatPhongDAO.layTatCaDonDatPhong();
        ArrayList<Object[]> dsDonCuaPhong = new ArrayList<>();
                                
        for (Object[] dp : dsDatPhong) {
            if (dp[3].equals(maPhong)) {
                String trangThai = dp[13].toString();
                // Hiển thị cả đơn "Đã đặt" và "Đang thuê
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
		
		String[] columns = {"Mã KH", "Họ tên", "CCCD", "Tuổi", "SĐT", "Giới tính"};
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

	private JPanel createFieldRow(String labelText, JComponent comp) {
		    JPanel panel = new JPanel(new BorderLayout(10, 0));
		    panel.setBackground(PANEL_COLOR);
		    panel.setMaximumSize(new Dimension(400, 40));
		    JLabel label = new JLabel(labelText);
		    label.setPreferredSize(new Dimension(100, 35));
		    label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		    panel.add(label, BorderLayout.WEST);
		    panel.add(comp, BorderLayout.CENTER);
		    return panel;
		
		}	

	private void showHuyPhongGUI() {
	    JDialog dialog = new JDialog(this, "Quản lý hủy phòng", true);
	    dialog.setSize(1400, 800);
	    dialog.setLocationRelativeTo(this);
	    dialog.setLayout(new BorderLayout(10, 10));

	    JPanel pnlMain = new JPanel(new BorderLayout(15, 15));
	    pnlMain.setBackground(PANEL_COLOR);
	    pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));

	    JPanel pnlInfo = new JPanel();
	    pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
	    pnlInfo.setBackground(PANEL_COLOR);
	    pnlInfo.setPreferredSize(new Dimension(400, 0));
	    pnlInfo.setBorder(new EmptyBorder(10, 10, 10, 15));
	    pnlInfo.setAlignmentX(JPanel.LEFT_ALIGNMENT);

	    JLabel lblTitle = new JLabel("Thông tin khách hàng");
	    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
	    lblTitle.setForeground(HEADER_COLOR);
	    lblTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	    pnlInfo.add(lblTitle);
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 20)));

	    JTextField txtMaKH = new JTextField(20);
	    txtMaKH.setEditable(false);
	    txtMaKH.setBackground(Color.decode("#ECF0F1"));

	    JTextField txtTenKH = new JTextField(20);
	    txtTenKH.setEditable(false);
	    txtTenKH.setBackground(Color.decode("#ECF0F1"));

	    JButton btnChonKH = createStyledButton("Chọn khách hàng", Color.decode("#3498DB"), 200, 35);
	    final String[] maKHSelected = new String[1];

	    btnChonKH.addActionListener(e -> {
	        showChonKhachHangDialog(dialog, maKHSelected, txtMaKH, txtTenKH);
	    });

	    pnlInfo.add(createFieldRow("Mã KH:", txtMaKH));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));
	    pnlInfo.add(createFieldRow("Tên KH:", txtTenKH));
	    pnlInfo.add(Box.createRigidArea(new Dimension(0, 12)));

	    JPanel pnlChonKH = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    pnlChonKH.setBackground(PANEL_COLOR);
	    pnlChonKH.setMaximumSize(new Dimension(380, 45));
	    pnlChonKH.add(Box.createRigidArea(new Dimension(110, 0)));
	    pnlChonKH.add(btnChonKH);
	    pnlInfo.add(pnlChonKH);

	    String[] columns = {"Chọn", "Mã đơn", "Mã phòng", "Tên phòng", "Loại phòng","Ngày đặt", "Ngày nhận", "Ngày trả", "Tiền cọc"};
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
	    
	    JTableHeader header = tblPhong.getTableHeader();
	    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
	    header.setBackground(HEADER_COLOR);
	    header.setForeground(Color.WHITE);
	    header.setPreferredSize(new Dimension(header.getWidth(), 45));
	    
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    
	    
	    tblPhong.getColumnModel().getColumn(0).setPreferredWidth(50);
	    tblPhong.getColumnModel().getColumn(0).setMaxWidth(60);
	    
	    tblPhong.getColumnModel().getColumn(1).setPreferredWidth(80);
	    tblPhong.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	    
	    tblPhong.getColumnModel().getColumn(2).setPreferredWidth(100);
	    tblPhong.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
	    
	    tblPhong.getColumnModel().getColumn(3).setPreferredWidth(150);
	    tblPhong.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
	    
	    tblPhong.getColumnModel().getColumn(4).setPreferredWidth(120);
	    tblPhong.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
	    
	    tblPhong.getColumnModel().getColumn(5).setPreferredWidth(100);
	    tblPhong.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
	    
	    tblPhong.getColumnModel().getColumn(6).setPreferredWidth(100);
	    tblPhong.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
	    
	    tblPhong.getColumnModel().getColumn(7).setPreferredWidth(100);
	    tblPhong.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
	    
	    tblPhong.getColumnModel().getColumn(8).setPreferredWidth(120);
	    tblPhong.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
	    
	    JScrollPane scrollPhong = new JScrollPane(tblPhong);
	    scrollPhong.setPreferredSize(new Dimension(900, 500));
	    scrollPhong.setBorder(BorderFactory.createLineBorder(Color.decode("#BDC3C7"), 1));

	    JButton btnTimPhong = createStyledButton("Tìm phòng đã đặt", Color.decode("#3498DB"), 180, 40);
	    btnTimPhong.addActionListener(e -> {
	        if (maKHSelected[0] == null || maKHSelected[0].isEmpty()) {
	            JOptionPane.showMessageDialog(dialog, "Vui lòng chọn khách hàng!");
	            return;
	        }

	        modelPhong.setRowCount(0);
	        ArrayList<Object[]> dsPhong = HuyPhongDAO.layPhongDaDatTheoKH(maKHSelected[0]);

	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        for (Object[] p : dsPhong) {
	            String maDatPhong = p[0].toString();
	            String maPhong = p[1].toString();
	            String tenPhong = p[2].toString();
	            String loaiPhong = p[3].toString();
	            String ngayDat = p[4] != null ? sdf.format(p[4]) : "";
	            String ngayNhan = p[5] != null ? sdf.format(p[5]) : "";
	            String ngayTra = p[6] != null ? sdf.format(p[6]) : "";
	            double tien = p[7] != null ? ((Number)p[7]).doubleValue() : 0;
	            String tienCoc = String.format("%,.0f VNĐ", tien);

	            modelPhong.addRow(new Object[]{
	                false,     
	                maDatPhong, 
	                maPhong,    
	                tenPhong,   
	                loaiPhong,  
	                ngayDat,
	                ngayNhan,
	                ngayTra,
	                tienCoc
	            });
	        }
	    });

	    JPanel pnlBtnTim = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    pnlBtnTim.setBackground(PANEL_COLOR);
	    pnlBtnTim.add(btnTimPhong);

	    JPanel pnlTable = new JPanel(new BorderLayout());
	    pnlTable.add(scrollPhong, BorderLayout.CENTER);
	    pnlTable.add(pnlBtnTim, BorderLayout.SOUTH);

	    JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
	    pnlButtons.setBackground(PANEL_COLOR);
	    pnlButtons.setBorder(new EmptyBorder(30, 0, 0, 0));
	    pnlButtons.setMaximumSize(new Dimension(380, 70));

	    JButton btnHuyPhong = createStyledButton("Hủy phòng", Color.decode("#27AE60"), 140, 45);
	    JButton btnThoat = createStyledButton("Thoát", Color.RED, 140, 45);
	    btnThoat.addActionListener(e -> dialog.dispose());

	    btnHuyPhong.addActionListener(e -> {
	        ArrayList<String> dsMaDatPhong = new ArrayList<>();
	        ArrayList<String> dsMaPhong = new ArrayList<>();

	        // Lấy các phòng được chọn
	        for (int i = 0; i < modelPhong.getRowCount(); i++) {
	            Boolean checked = (Boolean) modelPhong.getValueAt(i, 0);
	            if (checked != null && checked) {
	                dsMaDatPhong.add(modelPhong.getValueAt(i, 1).toString()); 
	                dsMaPhong.add(modelPhong.getValueAt(i, 2).toString());   
	            }
	        }

	        if (dsMaDatPhong.isEmpty()) {
	            JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ít nhất một phòng để hủy!");
	            return;
	        }

	        int confirm = JOptionPane.showConfirmDialog(dialog,
	                "Bạn có chắc muốn hủy " + dsMaDatPhong.size() + " phòng không?",
	                "Xác nhận", JOptionPane.YES_NO_OPTION);

	        if (confirm == JOptionPane.YES_OPTION) {
	            boolean allSuccess = true;
	            
	            for (int i = 0; i < dsMaDatPhong.size(); i++) {
	                String maDatPhong = dsMaDatPhong.get(i);
	                String maPhong = dsMaPhong.get(i);

	                boolean ok = HuyPhongDAO.xoaPhongTrongDon(maDatPhong, maPhong);

	                if (ok) {
	                    boolean conDonDangThue = HuyPhongDAO.conDonDangThue(maPhong);
	                    boolean conDonDaDat = HuyPhongDAO.conDonDangDatPhong(maPhong);
	                    
	                    if (conDonDangThue) {
	                        PhongDAO.capNhatTinhTrangPhong(maPhong, "Đang thuê");
	                    } else if (conDonDaDat) {
	                        PhongDAO.capNhatTinhTrangPhong(maPhong, "Đã đặt");
	                    } else {
	                        PhongDAO.capNhatTinhTrangPhong(maPhong, "Trống");
	                    }
	                }  else {
	                    allSuccess = false;
	                }
	            }

	            if (allSuccess) {
	                JOptionPane.showMessageDialog(dialog, "Hủy phòng thành công!");
	            } else {
	                JOptionPane.showMessageDialog(dialog, "Hủy phòng không thành công!");
	            }
	            
	            modelPhong.setRowCount(0);
	            loadDanhSachPhong(); 
	            
	            // Reset các field
	            txtMaKH.setText("");
	            txtTenKH.setText("");
	            maKHSelected[0] = null;
	            dialog.dispose();
	        }
	    });

	    pnlButtons.add(btnHuyPhong);
	    pnlButtons.add(btnThoat);
	    pnlInfo.add(pnlButtons);

	    pnlMain.add(pnlInfo, BorderLayout.WEST);
	    pnlMain.add(pnlTable, BorderLayout.CENTER);

	    dialog.add(pnlMain);
	    dialog.setVisible(true);
	}


}
