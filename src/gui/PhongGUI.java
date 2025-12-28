package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import dao.LoaiPhongDAO;
import dao.NhanVienDAO;
import dao.PhongDAO;
import entity.LoaiPhong;
import entity.NhanVien;
import entity.Phong;

public class PhongGUI extends MenuGUI implements ActionListener, MouseListener {

    private JTable tblPhong;
    private DefaultTableModel modelPhong;

    private JTextField txtMaPhong, txtTenPhong, txtGia;
    private JComboBox<String> cboLoaiPhong, cboTang, cboTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnKhoiPhuc;
    private JPanel pnlNoiDung, pnlTrai, pnlGiua;

    private ArrayList<LoaiPhong> dsLoaiPhong = new ArrayList<>();

    // Màu sắc chủ đạo
    private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
    private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
    private static final Color HEADER_COLOR = Color.decode("#34495E");

    public PhongGUI(NhanVien nv) {
        super("Phòng", nv);

        pnlNoiDung = new JPanel(new BorderLayout());
        pnlNoiDung.setBackground(BACKGROUND_COLOR);

        // ==== PANEL TRÁI (Form nhập liệu) ====
        pnlTrai = new JPanel();
        pnlTrai.setLayout(new BoxLayout(pnlTrai, BoxLayout.Y_AXIS));
        pnlTrai.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlTrai.setBackground(PANEL_COLOR);
        pnlTrai.setPreferredSize(new Dimension(350, 0));
        
        // Tiêu đề
        JLabel lblFormTitle = new JLabel("Thông tin phòng");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(HEADER_COLOR);
        lblFormTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pnlTrai.add(lblFormTitle);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 20)));

        // Mã phòng
        addFormField(pnlTrai, "Mã phòng:");
        txtMaPhong = createTextField();
        txtMaPhong.setEditable(false);
        txtMaPhong.setBackground(Color.decode("#ECF0F1"));
        txtMaPhong.setText(PhongDAO.getNextPhongId());
        pnlTrai.add(txtMaPhong);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tên phòng
        addFormField(pnlTrai, "Tên phòng:");
        txtTenPhong = createTextField();
        pnlTrai.add(txtTenPhong);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Loại phòng
        addFormField(pnlTrai, "Loại phòng:");
        cboLoaiPhong = new JComboBox<>();
        cboLoaiPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboLoaiPhong.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cboLoaiPhong.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        pnlTrai.add(cboLoaiPhong);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tầng
        addFormField(pnlTrai, "Tầng:");
        cboTang = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        cboTang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboTang.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cboTang.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        pnlTrai.add(cboTang);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Giá
        addFormField(pnlTrai, "Giá phòng:");
        txtGia = createTextField();
        pnlTrai.add(txtGia);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Trạng thái
        addFormField(pnlTrai, "Trạng thái:");
        cboTrangThai = new JComboBox<>(new String[]{"Trống", "Đã đặt", "Đang thuê"});
        cboTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboTrangThai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cboTrangThai.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        pnlTrai.add(cboTrangThai);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Thêm khoảng trống linh hoạt
        pnlTrai.add(Box.createVerticalGlue());

     // ==== Panel chứa các nút - Nằm ngang 2 hàng ====
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
        pnlNoiDung.add(pnlTrai, BorderLayout.WEST);

        // ==== BẢNG PHÒNG ====
        pnlGiua = new JPanel(new BorderLayout());
        pnlGiua.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlGiua.setBackground(BACKGROUND_COLOR);

        String[] tieuDeCot = {"Mã phòng", "Tên phòng", "Loại phòng", "Tầng", "Giá", "Trạng thái"};
        modelPhong = new DefaultTableModel(tieuDeCot, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tblPhong = new JTable(modelPhong);
        tblPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblPhong.setRowHeight(35);
        tblPhong.setSelectionBackground(Color.decode("#3498DB"));
        tblPhong.setSelectionForeground(Color.WHITE);
        tblPhong.setGridColor(Color.decode("#BDC3C7"));
        tblPhong.setShowGrid(true);
        tblPhong.setIntercellSpacing(new Dimension(1, 1));
        tblPhong.addMouseListener(this);
        
        // Style header bảng
        JTableHeader header = tblPhong.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tieuDeCot.length; i++) {
            tblPhong.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        tblPhong.getColumnModel().getColumn(1).setPreferredWidth(100);
        JScrollPane scr = new JScrollPane(tblPhong);
        scr.setBorder(null);
        scr.getViewport().setBackground(Color.WHITE);

        JLabel lblTieuDe = new JLabel("QUẢN LÝ PHÒNG", JLabel.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setForeground(HEADER_COLOR);
        lblTieuDe.setBorder(new EmptyBorder(20, 0, 20, 0));

        JPanel pnlTableContainer = new JPanel(new BorderLayout());
        pnlTableContainer.setBackground(PANEL_COLOR);
        pnlTableContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlTableContainer.add(lblTieuDe, BorderLayout.NORTH);
        pnlTableContainer.add(scr, BorderLayout.CENTER);

        pnlGiua.add(pnlTableContainer, BorderLayout.CENTER);
        pnlNoiDung.add(pnlGiua, BorderLayout.CENTER);

        // ==== SỰ KIỆN ====
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnKhoiPhuc.addActionListener(this);

        // ==== KHỞI TẠO ====
        setLayout(new BorderLayout());
        add(pnlNoiDung, BorderLayout.CENTER);

        loadLoaiPhong();
        DocDuLieuDB();
        setVisible(true);
    }

    private void addFormField(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    // Tạo JTextField với style đồng nhất
    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txt.setPreferredSize(new Dimension(100, 30));
        txt.setAlignmentX(JTextField.LEFT_ALIGNMENT);
        return txt;
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

    private void loadLoaiPhong() {
        cboLoaiPhong.removeAllItems();
        dsLoaiPhong = LoaiPhongDAO.getAllLoaiPhong();
        for (LoaiPhong lp : dsLoaiPhong) {
            cboLoaiPhong.addItem(lp.getTenLoaiPhong());
        }
    }

    private void DocDuLieuDB() {
        modelPhong.setRowCount(0);
        ArrayList<Phong> ds = PhongDAO.getDSPhong();
        for (Phong p : ds) {
            modelPhong.addRow(new Object[]{
                p.getMaPhong(), p.getTenPhong(), p.getLoaiPhong().getTenLoaiPhong(),
                p.getTang(), p.getDonGia(), p.getTrangThai()
            });
        }
    }

    private void ThemActions() {
        if (txtTenPhong.getText().trim().isEmpty() || txtGia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ma = PhongDAO.getNextPhongId();
        String ten = txtTenPhong.getText().trim();
        String trangThai = (String) cboTrangThai.getSelectedItem();
        int tang = Integer.parseInt((String) cboTang.getSelectedItem());
        double gia;

        try {
            gia = Double.parseDouble(txtGia.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Giá không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LoaiPhong loai = dsLoaiPhong.get(cboLoaiPhong.getSelectedIndex());
        Phong p = new Phong(ma, ten, tang, trangThai, gia, loai, nv.getMaNhanVien());

        if (PhongDAO.add(p)) {
            JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
            DocDuLieuDB();
            xoaTrang();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void SuaActions() {
        int row = tblPhong.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn một phòng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String ma = txtMaPhong.getText().trim();
            String ten = txtTenPhong.getText().trim();
            String trangThai = (String) cboTrangThai.getSelectedItem();
            int tang = Integer.parseInt((String) cboTang.getSelectedItem());
            double gia = Double.parseDouble(txtGia.getText().trim());
            LoaiPhong loai = dsLoaiPhong.get(cboLoaiPhong.getSelectedIndex());

            Phong p = new Phong(ma, ten, tang, trangThai, gia, loai, nv.getMaNhanVien());
            if (PhongDAO.update(p)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                DocDuLieuDB();
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void XoaActions() {
        int row = tblPhong.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn phòng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ma = tblPhong.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa phòng " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (PhongDAO.hide(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                DocDuLieuDB();
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xoaTrang() {
        txtMaPhong.setText(PhongDAO.getNextPhongId());
        txtTenPhong.setText("");
        txtGia.setText("");
        cboLoaiPhong.setSelectedIndex(0);
        cboTang.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        tblPhong.clearSelection();
    }

    private void KhoiPhucActions() {
        ArrayList<Phong> dsAn = PhongDAO.getPhongAn();
        if (dsAn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có phòng nào bị ẩn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Tạo dialog với table
        JDialog dialogKhoiPhuc = new JDialog(this, "Danh sách phòng đã xóa", true);
        dialogKhoiPhuc.setLayout(new BorderLayout());
        dialogKhoiPhuc.setSize(900, 500);
        dialogKhoiPhuc.setLocationRelativeTo(this);
        
        // Tiêu đề
        JLabel lblTitle = new JLabel("Chọn phòng cần khôi phục", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(HEADER_COLOR);
        lblTitle.setBorder(new EmptyBorder(15, 10, 15, 10));
        
        // Table
        String[] cols = {"Mã phòng", "Tên phòng", "Loại phòng", "Tầng", "Giá", "Trạng thái"};
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
        
        for (Phong p : dsAn) {
            model.addRow(new Object[]{
                p.getMaPhong(), p.getTenPhong(), p.getLoaiPhong().getTenLoaiPhong(),
                p.getTang(), p.getDonGia(), p.getTrangThai()
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
                if (PhongDAO.restore(maChon)) {
                    JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thành công!");
                    DocDuLieuDB();
                    xoaTrang();
                    dialogKhoiPhuc.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(dialogKhoiPhuc, "Vui lòng chọn phòng cần khôi phục!");
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == btnThem) ThemActions();
        else if (o == btnSua) SuaActions();
        else if (o == btnXoa) XoaActions();
        else if (o == btnKhoiPhuc) KhoiPhucActions();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tblPhong.getSelectedRow();
        if (row == -1) return;

        txtMaPhong.setText(tblPhong.getValueAt(row, 0).toString());
        txtTenPhong.setText(tblPhong.getValueAt(row, 1).toString());
        cboLoaiPhong.setSelectedItem(tblPhong.getValueAt(row, 2).toString());
        cboTang.setSelectedItem(tblPhong.getValueAt(row, 3).toString());
        txtGia.setText(tblPhong.getValueAt(row, 4).toString());
        cboTrangThai.setSelectedItem(tblPhong.getValueAt(row, 5).toString());
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