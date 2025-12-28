package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.LoaiNhanVienDAO;
import entity.LoaiNhanVien;
import entity.NhanVien;

public class LoaiNhanVienGUI extends MenuGUI implements MouseListener {
	private JTextField txtTenLoaiNV;
	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnSua;
	private JButton btnKhoiPhuc;

	private DefaultTableModel modelLNV;
	private JTable tblLNV;
	private JTextField txtmaLoaiNV;
	
	private static final Color BACKGROUND_COLOR = Color.decode("#ECF0F1");
	private static final Color PANEL_COLOR = Color.decode("#FFFFFF");
	private static final Color HEADER_COLOR = Color.decode("#34495E");
	
	public LoaiNhanVienGUI(NhanVien nv) {
		super("Quản lý loại nhân viên", nv);
		JPanel pnlNoiDung = new JPanel(new BorderLayout());
		pnlNoiDung.setBackground(BACKGROUND_COLOR);
		
        JLabel lblTieuDe = new JLabel("QUẢN LÝ LOẠI NHÂN VIÊN", JLabel.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setForeground(HEADER_COLOR);
        lblTieuDe.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Panel bên trái với BoxLayout
        JPanel pnlTrai = new JPanel();
        pnlTrai.setLayout(new BoxLayout(pnlTrai, BoxLayout.Y_AXIS));
        pnlTrai.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlTrai.setBackground(PANEL_COLOR);
        pnlTrai.setPreferredSize(new Dimension(350, 0));
        
        // Tiêu đề form
        JLabel lblFormTitle = new JLabel("Thông tin loại nhân viên");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(HEADER_COLOR);
        lblFormTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pnlTrai.add(lblFormTitle);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Mã loại nhân viên
        JLabel lblMaLoaiNV = new JLabel("Mã loại nhân viên:");
        lblMaLoaiNV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMaLoaiNV.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pnlTrai.add(lblMaLoaiNV);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtmaLoaiNV = new JTextField();
        txtmaLoaiNV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtmaLoaiNV.setPreferredSize(new Dimension(120, 30));
        txtmaLoaiNV.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtmaLoaiNV.setEditable(false);
        txtmaLoaiNV.setBackground(Color.decode("#ECF0F1"));
        txtmaLoaiNV.setText(LoaiNhanVienDAO.getNextID());
        txtmaLoaiNV.setAlignmentX(JTextField.LEFT_ALIGNMENT);
        pnlTrai.add(txtmaLoaiNV);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Tên loại nhân viên
        JLabel lblTenLoaiNV = new JLabel("Tên loại nhân viên:");
        lblTenLoaiNV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTenLoaiNV.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pnlTrai.add(lblTenLoaiNV);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtTenLoaiNV = new JTextField();
        txtTenLoaiNV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTenLoaiNV.setPreferredSize(new Dimension(120, 30));
        txtTenLoaiNV.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtTenLoaiNV.setAlignmentX(JTextField.LEFT_ALIGNMENT);
        pnlTrai.add(txtTenLoaiNV);
        
        // Thêm khoảng trống linh hoạt
        pnlTrai.add(Box.createVerticalGlue());
        
        // Panel chứa các nút - Nằm ngang
        JPanel pnlNut = new JPanel();
        pnlNut.setLayout(new BoxLayout(pnlNut, BoxLayout.Y_AXIS));
        pnlNut.setBackground(PANEL_COLOR);
        pnlNut.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        pnlNut.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Hàng 1: Thêm và Sửa
        JPanel pnlNutHang1 = new JPanel();
        pnlNutHang1.setLayout(new BoxLayout(pnlNutHang1, BoxLayout.X_AXIS));
        pnlNutHang1.setBackground(PANEL_COLOR);
        pnlNutHang1.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        pnlNutHang1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        btnThem = createStyledButton("Thêm", Color.decode("#27AE60"));
        btnSua = createStyledButton("Sửa", Color.decode("#2980B9"));
        
        pnlNutHang1.add(btnThem);
        pnlNutHang1.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlNutHang1.add(btnSua);
        
        // Hàng 2: Xóa và Khôi phục
        JPanel pnlNutHang2 = new JPanel();
        pnlNutHang2.setLayout(new BoxLayout(pnlNutHang2, BoxLayout.X_AXIS));
        pnlNutHang2.setBackground(PANEL_COLOR);
        pnlNutHang2.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        pnlNutHang2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        btnXoa = createStyledButton("Xóa", Color.decode("#E74C3C"));
        btnKhoiPhuc = createStyledButton("Khôi phục", Color.decode("#F39C12"));
        
        pnlNutHang2.add(btnXoa);
        pnlNutHang2.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlNutHang2.add(btnKhoiPhuc);
        
        pnlNut.add(pnlNutHang1);
        pnlNut.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlNut.add(pnlNutHang2);
        
        pnlTrai.add(pnlNut);
        
        // Panel giữa - Bảng dữ liệu
        JPanel pnlGiua = new JPanel(new BorderLayout());
        pnlGiua.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlGiua.setBackground(BACKGROUND_COLOR);
        
        String[] tieuDe = {"Mã loại nhân viên", "Tên loại nhân viên"};
        modelLNV = new DefaultTableModel(tieuDe, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblLNV = new JTable(modelLNV);
        tblLNV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblLNV.setRowHeight(35);
        tblLNV.setSelectionBackground(Color.decode("#3498DB"));
        tblLNV.setSelectionForeground(Color.WHITE);
        tblLNV.setGridColor(Color.decode("#BDC3C7"));
        tblLNV.setShowGrid(true);
        tblLNV.setIntercellSpacing(new Dimension(1, 1));
        
        // Style cho header của bảng
        JTableHeader header = tblLNV.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        // Căn giữa nội dung các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblLNV.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblLNV.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        // Đặt độ rộng cột
        tblLNV.getColumnModel().getColumn(0).setPreferredWidth(150);
        tblLNV.getColumnModel().getColumn(1).setPreferredWidth(300);
        
        JScrollPane scrollPane = new JScrollPane(tblLNV);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel pnlTableContainer = new JPanel(new BorderLayout());
        pnlTableContainer.setBackground(PANEL_COLOR);
        pnlTableContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlTableContainer.add(lblTieuDe, BorderLayout.NORTH);
        pnlTableContainer.add(scrollPane, BorderLayout.CENTER);
        
        pnlGiua.add(pnlTableContainer, BorderLayout.CENTER);

        pnlNoiDung.add(pnlTrai, BorderLayout.WEST);
        pnlNoiDung.add(pnlGiua, BorderLayout.CENTER);

        add(pnlNoiDung, BorderLayout.CENTER);
        setVisible(true);
        docDuLieuDB();
        
        tblLNV.addMouseListener(this);
        btnThem.addActionListener(e -> themLoaiNhanVien());
        btnXoa.addActionListener(e -> xoaLoaiNhanVien());
        btnSua.addActionListener(e -> suaLoaiNhanVien());
        btnKhoiPhuc.addActionListener(e -> khoiPhucLoaiNhanVien());
	}
	
	// Tạo button với style đẹp - To và nằm ngang
	private JButton createStyledButton(String text, Color bgColor) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
		btn.setForeground(Color.WHITE);
		btn.setBackground(bgColor);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setPreferredSize(new Dimension(150, 45));
		btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
		btn.setAlignmentX(JButton.LEFT_ALIGNMENT);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// Thêm hiệu ứng hover
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
	
	private void khoiPhucLoaiNhanVien() {
		ArrayList<LoaiNhanVien> dsAn = LoaiNhanVienDAO.getHiddenLoaiNV();
	    if (dsAn.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Không có loại nhân viên nào bị ẩn!");
	        return;
	    }
	    
	    // Tạo dialog với table
	    JDialog dialogKhoiPhuc = new JDialog(this, "Danh sách loại nhân viên đã xóa", true);
	    dialogKhoiPhuc.setLayout(new BorderLayout());
	    dialogKhoiPhuc.setSize(600, 400);
	    dialogKhoiPhuc.setLocationRelativeTo(this);
	    
	    // Tiêu đề
	    JLabel lblTitle = new JLabel("Chọn loại nhân viên cần khôi phục", JLabel.CENTER);
	    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    lblTitle.setForeground(HEADER_COLOR);
	    lblTitle.setBorder(new EmptyBorder(15, 10, 15, 10));
	    
	    // Tạo table
	    String[] columns = {"Mã loại nhân viên", "Tên loại nhân viên"};
	    DefaultTableModel modelRestore = new DefaultTableModel(columns, 0) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false;
	        }
	    };
	    
	    JTable tblRestore = new JTable(modelRestore);
	    tblRestore.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    tblRestore.setRowHeight(35);
	    tblRestore.setSelectionBackground(Color.decode("#3498DB"));
	    tblRestore.setSelectionForeground(Color.WHITE);
	    tblRestore.setGridColor(Color.decode("#BDC3C7"));
	    tblRestore.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
	    
	    // Style header
	    JTableHeader headerRestore = tblRestore.getTableHeader();
	    headerRestore.setFont(new Font("Segoe UI", Font.BOLD, 14));
	    headerRestore.setBackground(HEADER_COLOR);
	    headerRestore.setForeground(Color.WHITE);
	    headerRestore.setPreferredSize(new Dimension(headerRestore.getWidth(), 40));
	    
	    // Căn giữa
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    tblRestore.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	    tblRestore.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	    
	    // Thêm dữ liệu
	    for (LoaiNhanVien lnv : dsAn) {
	        modelRestore.addRow(new Object[]{lnv.getMaLoaiNV(), lnv.getTenLoaiNV()});
	    }
	    
	    JScrollPane scrollPane = new JScrollPane(tblRestore);
	    scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
	    
	    // Panel buttons
	    JPanel pnlButtons = new JPanel();
	    pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
	    pnlButtons.setBorder(new EmptyBorder(10, 10, 15, 10));
	    
	    JButton btnConfirm = createStyledButton("Khôi phục", Color.decode("#27AE60"));
	    JButton btnCancel = createStyledButton("Hủy", Color.decode("#95A5A6"));
	    
	    btnConfirm.addActionListener(e -> {
	        int selectedRow = tblRestore.getSelectedRow();
	        if (selectedRow >= 0) {
	            String maChon = modelRestore.getValueAt(selectedRow, 0).toString();
	            if (LoaiNhanVienDAO.restore(maChon)) {
	                JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thành công!");
	                docDuLieuDB();
	                xoaForm();
	                dialogKhoiPhuc.dispose();
	            } else {
	                JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thất bại!");
	            }
	        } else {
	            JOptionPane.showMessageDialog(dialogKhoiPhuc, "Vui lòng chọn loại nhân viên cần khôi phục!");
	        }
	    });
	    
	    btnCancel.addActionListener(e -> dialogKhoiPhuc.dispose());
	    
	    pnlButtons.add(Box.createHorizontalGlue());
	    pnlButtons.add(btnConfirm);
	    pnlButtons.add(Box.createRigidArea(new Dimension(10, 0)));
	    pnlButtons.add(btnCancel);
	    pnlButtons.add(Box.createHorizontalGlue());
	    
	    dialogKhoiPhuc.add(lblTitle, BorderLayout.NORTH);
	    dialogKhoiPhuc.add(scrollPane, BorderLayout.CENTER);
	    dialogKhoiPhuc.add(pnlButtons, BorderLayout.SOUTH);
	    
	    dialogKhoiPhuc.setVisible(true);
	}


	private void docDuLieuDB() {
        ArrayList<LoaiNhanVien> ds = LoaiNhanVienDAO.getAllLoaiNV();
        modelLNV.setRowCount(0);
        for (LoaiNhanVien lnv : ds) {
            modelLNV.addRow(new Object[]{lnv.getMaLoaiNV(), lnv.getTenLoaiNV()});
        }
    }

	private void suaLoaiNhanVien() {
		int row = tblLNV.getSelectedRow();
        if (row >= 0) {
            String maCu = modelLNV.getValueAt(row, 0).toString();
            String tenMoi = txtTenLoaiNV.getText().trim();

            if (tenMoi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên mới!", 
                		"Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (LoaiNhanVienDAO.update(maCu, tenMoi)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", 
                		"Thông báo", JOptionPane.INFORMATION_MESSAGE);
                docDuLieuDB();
                xoaForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", 
                		"Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chọn loại nhân viên để sửa!", 
            		"Thông báo", JOptionPane.WARNING_MESSAGE);
        }
	}

	private void xoaLoaiNhanVien() {
		int row = tblLNV.getSelectedRow();
        if (row >= 0) {
            String maLoai = modelLNV.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
            		"Bạn có chắc muốn xóa loại nhân viên này không?",
            		"Xác nhận", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
            	if(LoaiNhanVienDAO.hide(maLoai)) {
            		JOptionPane.showMessageDialog(this, "Xóa thành công!", 
                    		"Thông báo", JOptionPane.INFORMATION_MESSAGE);
            		docDuLieuDB();
     				xoaForm();
            	} else {
            		JOptionPane.showMessageDialog(this, "Xóa thất bại!", 
                    		"Lỗi", JOptionPane.ERROR_MESSAGE);
            	}
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chọn loại nhân viên cần xóa!", 
            		"Thông báo", JOptionPane.WARNING_MESSAGE);
        }
	}

	private void themLoaiNhanVien() {
		String ma = txtmaLoaiNV.getText().trim();
		String ten = txtTenLoaiNV.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại nhân viên!", 
            		"Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        LoaiNhanVien lnv = new LoaiNhanVien(ma, ten);
        if (LoaiNhanVienDAO.add(lnv)) {
            JOptionPane.showMessageDialog(this, "Thêm loại nhân viên thành công!", 
            		"Thông báo", JOptionPane.INFORMATION_MESSAGE);
            docDuLieuDB();
            xoaForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại (có thể tên trùng)!", 
            		"Lỗi", JOptionPane.ERROR_MESSAGE);
        }
	}
	
	private void xoaForm() {
		txtTenLoaiNV.setText("");
		txtmaLoaiNV.setText(LoaiNhanVienDAO.getNextID());
		tblLNV.clearSelection();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int row = tblLNV.getSelectedRow();
		if(row >= 0) {
			txtmaLoaiNV.setText(tblLNV.getValueAt(row, 0).toString());
			txtTenLoaiNV.setText(tblLNV.getValueAt(row, 1).toString()); 
		}
		if(e.getClickCount() == 2 && row != -1) {
			xoaForm();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
}