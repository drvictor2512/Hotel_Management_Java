package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import dao.NhanVienDAO;
import entity.NhanVien;

public class TimKiemNhanVienGUI extends MenuGUI {
	private JTextField txtMaNhanVien;
	private DefaultTableModel tableModel;
	private JTable table;
	
	// Định nghĩa màu sắc (Hex)
	private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
	private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
	private static final Color HEADER_COLOR = Color.decode("#34495E");
	
	public TimKiemNhanVienGUI(NhanVien nv) {
		super("Tìm kiếm nhân viên", nv);
		
		JPanel pnlMain = new JPanel(new BorderLayout());
		pnlMain.setBackground(BACKGROUND_COLOR);
		
		// ==== Panel Top - Tìm kiếm ====
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBackground(PANEL_COLOR);
		pnlTop.setBorder(new EmptyBorder(30, 40, 30, 40));
		
		// Tiêu đề
		JLabel lblTitle = new JLabel("TÌM KIẾM NHÂN VIÊN", JLabel.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitle.setForeground(HEADER_COLOR);
		lblTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		pnlTop.add(lblTitle);
		pnlTop.add(Box.createRigidArea(new Dimension(0, 30)));
		
		// Panel nhập liệu
		JPanel pnlInput = new JPanel();
		pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.X_AXIS));
		pnlInput.setBackground(PANEL_COLOR);
		pnlInput.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		pnlInput.setMaximumSize(new Dimension(600, 40));
		
		JLabel lblMaNV = new JLabel("Mã nhân viên:");
		lblMaNV.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblMaNV.setPreferredSize(new Dimension(120, 35));
		
		txtMaNhanVien = new JTextField();
		txtMaNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtMaNhanVien.setPreferredSize(new Dimension(300, 35));
		txtMaNhanVien.setMaximumSize(new Dimension(300, 35));
		
		pnlInput.add(Box.createHorizontalGlue());
		pnlInput.add(lblMaNV);
		pnlInput.add(Box.createRigidArea(new Dimension(10, 0)));
		pnlInput.add(txtMaNhanVien);
		pnlInput.add(Box.createHorizontalGlue());
		
		pnlTop.add(pnlInput);
		pnlTop.add(Box.createRigidArea(new Dimension(0, 20)));
		
		// Panel buttons
		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
		pnlButtons.setBackground(PANEL_COLOR);
		pnlButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		pnlButtons.setMaximumSize(new Dimension(400, 50));
		
		JButton btnSearch = createStyledButton("Tìm kiếm", Color.decode("#2980B9"));
		JButton btnReload = createStyledButton("Tải lại", Color.decode("#27AE60"));
		
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String maNV = txtMaNhanVien.getText().trim();
				if (maNV.isEmpty()) {
					return;
				}
				try {
					NhanVien nv = NhanVienDAO.getMaNV(maNV);
					if(nv != null) {
						tableModel.setRowCount(0);
						tableModel.addRow(new Object[] {
							nv.getMaNhanVien(), 
							nv.getHoTen(),
							nv.getSDT(),
							nv.getTuoi(), 
							nv.getGioiTinh(), 
							nv.getLnv().getTenLoaiNV(),
							nv.getUsername(), 
							nv.getPassword()
						});
						table.setRowSelectionInterval(0, 0);
					} else {
						tableModel.setRowCount(0);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		btnReload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DocDuLieuDB();
				txtMaNhanVien.setText("");
			}
		});
		
		pnlButtons.add(Box.createHorizontalGlue());
		pnlButtons.add(btnSearch);
		pnlButtons.add(Box.createRigidArea(new Dimension(15, 0)));
		pnlButtons.add(btnReload);
		pnlButtons.add(Box.createHorizontalGlue());
		
		pnlTop.add(pnlButtons);
		
		// ==== Bảng kết quả ====
		JPanel pnlTable = new JPanel(new BorderLayout());
		pnlTable.setBackground(BACKGROUND_COLOR);
		pnlTable.setBorder(new EmptyBorder(10, 20, 20, 20));
		
		String[] col = {"Mã NV", "Họ tên", "Số ĐT", "Tuổi", "Giới tính", "Loại NV", "Tài khoản", "Mật khẩu"};
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
	
	// Tạo button với style đẹp
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
	
	private void DocDuLieuDB() {
		ArrayList<NhanVien> nvList;
		try {
			tableModel.setRowCount(0);
			nvList = NhanVienDAO.getAll();
			for(NhanVien nv : nvList) {
				tableModel.addRow(new Object[] {
					nv.getMaNhanVien(), 
					nv.getHoTen(), 
					nv.getSDT(),
					nv.getTuoi(), 
					nv.getGioiTinh(), 
					nv.getLnv().getTenLoaiNV(), 
					nv.getUsername(),
					nv.getPassword()
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}