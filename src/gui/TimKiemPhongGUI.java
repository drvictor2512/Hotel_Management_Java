package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connectDB.ConnectDB;
import dao.PhongDAO;
import entity.NhanVien;
import entity.Phong;

public class TimKiemPhongGUI extends MenuGUI implements ActionListener{
	private JTextField txtMaPhong;
	private JTextField txtTenPhong;
	private JTextField txtLoaiPhong;
	private DefaultTableModel tableModel;
	private JTable table;
	private JComboBox cboLoaiPhong;
	private JButton btnLoad;
	private JComboBox cboTrangThai;
	
	// Định nghĩa màu sắc (Hex)
		private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
		private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
		private static final Color HEADER_COLOR = Color.decode("#34495E");
		
	public TimKiemPhongGUI(NhanVien nv) {
		super("Tìm kiếm phòng", nv);
		
		JPanel pnlMain = new JPanel(new BorderLayout());
		pnlMain.setBackground(BACKGROUND_COLOR);
		
		// ==== Panel Top - Tìm kiếm ====
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBackground(PANEL_COLOR);
		pnlTop.setBorder(new EmptyBorder(30, 40, 30, 40));
		
		// Tiêu đề
		JLabel lblTitle = new JLabel("TÌM KIẾM PHÒNG", JLabel.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitle.setForeground(HEADER_COLOR);
		lblTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		pnlTop.add(lblTitle);
		pnlTop.add(Box.createRigidArea(new Dimension(0, 30)));
		
		// Panel nhập liệu chính
		JPanel pnlInput = new JPanel();
		pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.Y_AXIS));
		pnlInput.setBackground(PANEL_COLOR);
		pnlInput.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		pnlInput.setMaximumSize(new Dimension(800, 90)); // a bit taller for two rows

		// ===== Row 1: Mã phòng + Tên phòng =====
		JPanel pnlRow1 = new JPanel();
		pnlRow1.setLayout(new BoxLayout(pnlRow1, BoxLayout.X_AXIS));
		pnlRow1.setBackground(PANEL_COLOR);

		JLabel lblMaP = new JLabel("Mã phòng:");
		lblMaP.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblMaP.setPreferredSize(new Dimension(90, 35));

		txtMaPhong = new JTextField();
		txtMaPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtMaPhong.setPreferredSize(new Dimension(300, 35));
		txtMaPhong.setMaximumSize(new Dimension(300, 35));

		JLabel lblTenP = new JLabel("Tên phòng:");
		lblTenP.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblTenP.setPreferredSize(new Dimension(90, 35));

		txtTenPhong = new JTextField();
		txtTenPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTenPhong.setPreferredSize(new Dimension(300, 35));
		txtTenPhong.setMaximumSize(new Dimension(300, 35));

		pnlRow1.add(Box.createHorizontalGlue());
		pnlRow1.add(lblMaP);
		pnlRow1.add(Box.createRigidArea(new Dimension(1, 0)));
		pnlRow1.add(txtMaPhong);
		
		pnlRow1.add(Box.createRigidArea(new Dimension(40, 0)));
		
		pnlRow1.add(lblTenP);
		pnlRow1.add(Box.createRigidArea(new Dimension(1, 0)));
		pnlRow1.add(txtTenPhong);		
		pnlRow1.add(Box.createHorizontalGlue());

		// ===== Row 2: Loại phòng + Trạng thái =====
		JPanel pnlRow2 = new JPanel();
		pnlRow2.setLayout(new BoxLayout(pnlRow2, BoxLayout.X_AXIS));
		pnlRow2.setBackground(PANEL_COLOR);

		JLabel lblLoaiP = new JLabel("Loại phòng:");
		lblLoaiP.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblLoaiP.setPreferredSize(new Dimension(90, 35));

		cboLoaiPhong = new JComboBox<>(new String[]{"Tất cả", "Standard", "Superior", "VIP"});
		cboLoaiPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cboLoaiPhong.setMaximumSize(new Dimension(300, 35));
		cboLoaiPhong.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		cboLoaiPhong.setPreferredSize(new Dimension(200, 35));

		JLabel lblTrangThai = new JLabel("Trạng thái:");
		lblTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblTrangThai.setPreferredSize(new Dimension(90, 35));

		cboTrangThai = new JComboBox<>(new String[]{"Tất cả", "Trống", "Đã đặt", "Đang thuê"});
		cboTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cboTrangThai.setMaximumSize(new Dimension(300, 35));
		cboTrangThai.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		cboTrangThai.setPreferredSize(new Dimension(200, 35));
		
		
		pnlRow2.add(lblLoaiP);
		pnlRow2.add(Box.createRigidArea(new Dimension(1, 0)));
		pnlRow2.add(cboLoaiPhong);
		pnlRow2.add(Box.createRigidArea(new Dimension(128, 0)));
		pnlRow2.add(lblTrangThai);
		pnlRow2.add(Box.createRigidArea(new Dimension(1, 0)));
		pnlRow2.add(cboTrangThai);
		pnlRow2.add(Box.createHorizontalGlue());

		// 2 rows
		pnlInput.add(pnlRow1);
		pnlInput.add(Box.createVerticalStrut(15));
		pnlInput.add(pnlRow2);
		
		
		pnlTop.add(pnlInput);
		pnlTop.add(Box.createRigidArea(new Dimension(0, 20)));
		
		// Panel buttons
		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
		pnlButtons.setBackground(PANEL_COLOR);
		pnlButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		pnlButtons.setMaximumSize(new Dimension(400, 50));
		
		JButton btnSearch = createStyledButton("Tìm kiếm", Color.decode("#2980B9"));
		JButton btnLoad = createStyledButton("Tải lại", Color.decode("#27AE60"));
        
        btnSearch.addActionListener(this);
        btnLoad.addActionListener(e -> DocDuLieuDB());
        
        pnlButtons.add(Box.createHorizontalGlue());
		pnlButtons.add(btnSearch);
		pnlButtons.add(Box.createRigidArea(new Dimension(15, 0)));
		pnlButtons.add(btnLoad);
		pnlButtons.add(Box.createHorizontalGlue());
		
		pnlTop.add(pnlButtons);
        
     // ==== Bảng kết quả ====
     		JPanel pnlTable = new JPanel(new BorderLayout());
     		pnlTable.setBackground(BACKGROUND_COLOR);
     		pnlTable.setBorder(new EmptyBorder(10, 20, 20, 20));
     		
     		String[] col = {"Mã phòng", "Loại phòng", "Tên phòng", "Tầng", "Trạng thái", "Đơn giá"};
     		tableModel = new DefaultTableModel(col, 0) {
     			@Override
     			public boolean isCellEditable(int row, int column) {
     				return false;
     			}
     		};
     		
     		table = new JTable(tableModel);
     		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
     		table.setRowHeight(35);
     		table.setSelectionBackground(Color.decode("#3498DB"));
     		table.setSelectionForeground(Color.WHITE);
     		table.setGridColor(Color.decode("#BDC3C7"));
     		table.setShowGrid(true);
     		table.setIntercellSpacing(new Dimension(1, 1));
     		
     		// Style cho header
     		JTableHeader header = table.getTableHeader();
     		header.setFont(new Font("Segoe UI", Font.BOLD, 14));
     		header.setBackground(HEADER_COLOR);
     		header.setForeground(Color.WHITE);
     		header.setPreferredSize(new Dimension(header.getWidth(), 40));
     		header.setReorderingAllowed(false);
     		
     		// Căn giữa tất cả các cột
     		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
     		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
     		for (int i = 0; i < col.length; i++) {
     			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
     		}
     		
     		JScrollPane scrollpane = new JScrollPane(table);
     		scrollpane.setBorder(null);
     		scrollpane.getViewport().setBackground(Color.WHITE);
     		
     		JPanel pnlTableContainer = new JPanel(new BorderLayout());
     		pnlTableContainer.setBackground(PANEL_COLOR);
     		pnlTableContainer.setBorder(new EmptyBorder(15, 15, 15, 15));
     		
     		JLabel lblResultTitle = new JLabel("KẾT QUẢ TÌM KIẾM", JLabel.CENTER);
     		lblResultTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
     		lblResultTitle.setForeground(HEADER_COLOR);
     		lblResultTitle.setBorder(new EmptyBorder(10, 0, 15, 0));
     		
     		pnlTableContainer.add(lblResultTitle, BorderLayout.NORTH);
     		pnlTableContainer.add(scrollpane, BorderLayout.CENTER);
     		
     		pnlTable.add(pnlTableContainer, BorderLayout.CENTER);
     		
     		pnlMain.add(pnlTop, BorderLayout.NORTH);
     		pnlMain.add(pnlTable, BorderLayout.CENTER);
     		
     		add(pnlMain, BorderLayout.CENTER);
     		
     		DocDuLieuDB();
     		setVisible(true);
	}
	
	private void DocDuLieuDB() {
		ArrayList<Phong> pList;
		txtMaPhong.setText("");
		txtTenPhong.setText("");
		cboLoaiPhong.setSelectedIndex(0);
		try {
			tableModel.setRowCount(0);
			pList = PhongDAO.getDSPhong();
			for(Phong p : pList) {
				tableModel.addRow(new Object[] {p.getMaPhong(), p.getLoaiPhong().getTenLoaiPhong(), p.getTenPhong(), p.getTang(),
						p.getTrangThai(), p.getDonGia()});
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timKiemPhong();
	}
	
	
	private void timKiemPhong() {
		String maP = txtMaPhong.getText().trim();
		String tenP = txtTenPhong.getText().trim();
		String loaiP = cboLoaiPhong.getSelectedItem().toString();
		String ttP = cboTrangThai.getSelectedItem().toString();
		
		//regex check

		tableModel.setRowCount(0);	

		try (Connection con = ConnectDB.getConnection()) {
			String sql = """
						SELECT p.maPhong, p.tenPhong, p.tang, p.trangThai, p.donGia, p.maNhanVien, p.maLoaiPhong, lp.tenLoaiPhong 
						FROM Phong p
						JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
						WHERE 1=1 AND p.visible = 1
					""";
			if (!maP.isEmpty()) {
				sql += " AND p.maPhong LIKE ?";
			}
			if (!tenP.isEmpty()) {
				sql += " AND p.tenPhong LIKE ?";
			}
			if (!loaiP.equals("Tất cả")) {
				sql += " AND lp.tenLoaiPhong = ?";
			}
			if (!ttP.equals("Tất cả")) {
				sql += " AND p.trangThai = ?";
			}

			PreparedStatement stmt = con.prepareStatement(sql);

			int index = 1;
			if (!maP.isEmpty()) {
				stmt.setString(index++, "%" + maP + "%");
			}
			if (!tenP.isEmpty()) {
				stmt.setString(index++, "%" + tenP + "%");
			}
			if (!loaiP.equals("Tất cả")) {
				stmt.setString(index++, loaiP);
			}
			if (!ttP.equals("Tất cả")) {
				stmt.setString(index++, ttP);
			}

			ResultSet rs = stmt.executeQuery();

			boolean found = false;
			while (rs.next()) {
				String ma = rs.getString("maPhong");
	            String loai = rs.getString("tenLoaiPhong");
	            String ten = rs.getString("tenPhong");
	            int tang = rs.getInt("tang");
	            String tt = rs.getString("trangThai");
	            double gia = rs.getDouble("donGia");

				tableModel.addRow(new Object[] { ma, loai, ten, tang, tt, gia});
				found = true;
			}

			if (!found) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy phòng nào phù hợp!", "Kết quả",
						JOptionPane.INFORMATION_MESSAGE);
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm phòng: " + e.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private JButton createStyledButton(String text, Color bgColor) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
		btn.setForeground(Color.WHITE);
		btn.setBackground(bgColor);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setPreferredSize(new Dimension(140, 45));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// Hiệu ứng hover
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

}
