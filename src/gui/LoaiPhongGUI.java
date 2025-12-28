package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.LoaiNhanVienDAO;
import dao.LoaiPhongDAO;
import dao.PhongDAO;
import entity.LoaiPhong;
import entity.NhanVien;
import entity.Phong;

public class LoaiPhongGUI extends MenuGUI implements MouseListener {
    private JTextField txtLoaiPhong;
    private JButton btnThem, btnXoa, btnSua;
	private JTextField txtMaLP;
	private JButton btnKhoiPhuc;
	private DefaultTableModel modelLP;
	private JTable tblLP;
	
	private static final Color BACKGROUND_COLOR = Color.decode("#ECF0F1");
	private static final Color PANEL_COLOR = Color.decode("#FFFFFF");
	private static final Color HEADER_COLOR = Color.decode("#34495E");
	
    public LoaiPhongGUI(NhanVien nv) {
        super("Loại phòng", nv);

        JPanel pnlNoiDung = new JPanel(new BorderLayout());
        pnlNoiDung.setBackground(BACKGROUND_COLOR);
        
        
        JLabel lblTieuDe = new JLabel("QUẢN LÝ LOẠI PHÒNG", JLabel.CENTER);
        lblTieuDe.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTieuDe.setBackground(HEADER_COLOR);
        lblTieuDe.setBorder(new EmptyBorder(20, 0, 20, 0));
        

        JPanel pnlTrai = new JPanel();
        pnlTrai.setLayout(new BoxLayout(pnlTrai, BoxLayout.Y_AXIS));
        pnlTrai.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlTrai.setBackground(PANEL_COLOR);
        pnlTrai.setPreferredSize(new Dimension(350, 0));
        
        // Tiêu đề form
        JLabel lblFormTitle = new JLabel("Thông tin loại phòng");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(HEADER_COLOR);
        lblFormTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pnlTrai.add(lblFormTitle);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 20)));
        
        //Mã loại
        JLabel lblMaLoaiNV = new JLabel("Mã loại phòng:");
        lblMaLoaiNV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMaLoaiNV.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pnlTrai.add(lblMaLoaiNV);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtMaLP = new JTextField();
        txtMaLP.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMaLP.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtMaLP.setPreferredSize(new Dimension(100, 30));
        txtMaLP.setEditable(false);
        txtMaLP.setBackground(Color.decode("#ECF0F1"));
        txtMaLP.setText(LoaiPhongDAO.getNextID());
        txtMaLP.setAlignmentX(JTextField.LEFT_ALIGNMENT);
        pnlTrai.add(txtMaLP);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));
        
        //Tên loại
        JLabel lblTenLoaiNV = new JLabel("Tên loại phòng:");
        lblTenLoaiNV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTenLoaiNV.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pnlTrai.add(lblTenLoaiNV);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtLoaiPhong = new JTextField();
        txtLoaiPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLoaiPhong.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtLoaiPhong.setPreferredSize(new Dimension(100, 30));
        txtLoaiPhong.setAlignmentX(JTextField.LEFT_ALIGNMENT);
        pnlTrai.add(txtLoaiPhong);
        
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
        
        String[] tieuDe = {"Mã loại phòng", "Tên loại phòng"};
        modelLP = new DefaultTableModel(tieuDe, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblLP = new JTable(modelLP);
        tblLP.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblLP.setRowHeight(35);
        tblLP.setSelectionBackground(Color.decode("#3498DB"));
        tblLP.setSelectionForeground(Color.WHITE);
        tblLP.setGridColor(Color.decode("#BDC3C7"));
        tblLP.setShowGrid(true);
        tblLP.setIntercellSpacing(new Dimension(1, 1));
        
        // Style cho header của bảng
        JTableHeader header = tblLP.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        // Căn giữa nội dung các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblLP.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblLP.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        // Đặt độ rộng cột
        tblLP.getColumnModel().getColumn(0).setPreferredWidth(150);
        tblLP.getColumnModel().getColumn(1).setPreferredWidth(300);
        
        JScrollPane scrollPane = new JScrollPane(tblLP);
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
        
        tblLP.addMouseListener(this);
        btnThem.addActionListener(e -> themLoaiPhong());
        btnXoa.addActionListener(e -> xoaLoaiPhong());
        btnSua.addActionListener(e -> suaLoaiPhong());
        btnKhoiPhuc.addActionListener(e -> khoiPhucLoaiPhong());
    }

    private void khoiPhucLoaiPhong() {
    	ArrayList<LoaiPhong> dsAn = LoaiPhongDAO.getLoaiPhongAn();
        if (dsAn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có phòng nào bị ẩn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Tạo dialog với table
        JDialog dialogKhoiPhuc = new JDialog(this, "Danh sách loại phòng đã xóa", true);
        dialogKhoiPhuc.setLayout(new BorderLayout());
        dialogKhoiPhuc.setSize(900, 500);
        dialogKhoiPhuc.setLocationRelativeTo(this);
        
        // Tiêu đề
        JLabel lblTitle = new JLabel("Chọn loại phòng cần khôi phục", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(HEADER_COLOR);
        lblTitle.setBorder(new EmptyBorder(15, 10, 15, 10));
        
        // Table
        String[] cols = {"Mã loại phòng", "Tên loại phòng"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
        	@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
               
        JTable tblRestore = new JTable(model);
        tblRestore.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblRestore.setRowHeight(35);
        tblRestore.setSelectionBackground(Color.decode("#3498DB"));
        tblRestore.setSelectionForeground(Color.WHITE);
        tblRestore.setGridColor(Color.decode("#BDC3C7"));
        
        // Style header
        JTableHeader headerRestore = tblRestore.getTableHeader();
        headerRestore.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerRestore.setBackground(HEADER_COLOR);
        headerRestore.setForeground(Color.WHITE);
        headerRestore.setPreferredSize(new Dimension(headerRestore.getWidth(), 40));
        
        // Căn giữa
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblRestore.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        for (LoaiPhong p : dsAn) {
            model.addRow(new Object[]{
                p.getMaLoaiPhong(), p.getTenLoaiPhong()
            });
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
                String maChon = model.getValueAt(selectedRow, 0).toString();
                if (LoaiPhongDAO.restore(maChon)) {
                    JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thành công!");
                    docDuLieuDB();
                    xoaTrang();
                    dialogKhoiPhuc.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(dialogKhoiPhuc, "Vui lòng chọn loại phòng cần khôi phục!");
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
        ArrayList<LoaiPhong> ds = LoaiPhongDAO.getAllLoaiPhong();
        modelLP.setRowCount(0);
        for (LoaiPhong loai : ds) {
            modelLP.addRow(new Object[]{loai.getMaLoaiPhong(),loai.getTenLoaiPhong()});
        }
    }
	
	private void xoaTrang() {
		txtLoaiPhong.setText("");
		txtMaLP.requestFocus();
	}

    private void themLoaiPhong() {
    	String ma = txtMaLP.getText().trim();
        String ten = txtLoaiPhong.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại phòng!");
            return;
        }
        LoaiPhong lp = new LoaiPhong(ma, ten);
        if (LoaiPhongDAO.add(lp)) {
            JOptionPane.showMessageDialog(this, "Thêm loại phòng thành công!");
            docDuLieuDB();
            txtLoaiPhong.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại (có thể tên trùng)!");
        }
    }

    private void xoaLoaiPhong() {
    	int row = tblLP.getSelectedRow();
        if (row >= 0) {
            String maLoai = modelLP.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
            		"Bạn có chắc muốn xóa loại phòng này không?",
            		"Xác nhận", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
            	if(LoaiPhongDAO.hide(maLoai)) {
            		JOptionPane.showMessageDialog(this, "Xóa thành công!", 
                    		"Thông báo", JOptionPane.INFORMATION_MESSAGE);
            		docDuLieuDB();
     				xoaTrang();
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

    private void suaLoaiPhong() {
        int row = tblLP.getSelectedRow();
        if (row >= 0) {
            String tenCu = modelLP.getValueAt(row, 0).toString();
            String tenMoi = txtLoaiPhong.getText().trim();

            if (tenMoi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên mới!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (LoaiPhongDAO.update(tenCu, tenMoi)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                docDuLieuDB();
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chọn loại phòng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = tblLP.getSelectedRow();
		if(row >= 0) {
			txtMaLP.setText(tblLP.getValueAt(row, 0).toString());
			txtLoaiPhong.setText(tblLP.getValueAt(row, 0).toString());
		}
		if(e.getClickCount() == 2 && row != -1) {
			txtLoaiPhong.setText("");
		}
		
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

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
