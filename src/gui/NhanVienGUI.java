package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import dao.LoaiNhanVienDAO;
import dao.NhanVienDAO;
import entity.LoaiNhanVien;
import entity.NhanVien;

public class NhanVienGUI extends MenuGUI implements ActionListener, MouseListener {

    private JTable tblNhanVien;
    private DefaultTableModel modelNhanVien;

    private JTextField txtMaNV, txtTenNV, txtSoDT, txtTuoi, txtUsername, txtPassw;
    private JComboBox<String> cboGioiTinh, cboLoaiNV;
    private JButton btnThem, btnXoa, btnSua, btnKhoiPhuc;
    private JPanel pnlNoiDung, pnlTrai, pnlGiua;

    private ArrayList<LoaiNhanVien> dsLoaiNV = new ArrayList<>();
    
    // Định nghĩa màu sắc (Hex)
    private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
	private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
    private static final Color HEADER_COLOR = Color.decode("#34495E");

    public NhanVienGUI(NhanVien nv) {
        super("Nhân viên", nv);

        pnlNoiDung = new JPanel(new BorderLayout());
        pnlNoiDung.setBackground(BACKGROUND_COLOR);

        // ==== Panel nhập liệu (bên trái) với BoxLayout ====
        pnlTrai = new JPanel();
        pnlTrai.setLayout(new BoxLayout(pnlTrai, BoxLayout.Y_AXIS));
        pnlTrai.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlTrai.setBackground(PANEL_COLOR);
        pnlTrai.setPreferredSize(new Dimension(350, 0));

        // Tiêu đề form
        JLabel lblFormTitle = new JLabel("Thông tin nhân viên");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(HEADER_COLOR);
        lblFormTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pnlTrai.add(lblFormTitle);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 20)));

        // Mã nhân viên
        addFormField(pnlTrai, "Mã nhân viên:");
        txtMaNV = createTextField();
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(Color.decode("#ECF0F1"));
        txtMaNV.setText(NhanVienDAO.getNextNhanVienId());
        pnlTrai.add(txtMaNV);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tên nhân viên
        addFormField(pnlTrai, "Tên nhân viên:");
        txtTenNV = createTextField();
        pnlTrai.add(txtTenNV);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Số điện thoại
        addFormField(pnlTrai, "Số điện thoại:");
        txtSoDT = createTextField();
        pnlTrai.add(txtSoDT);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tuổi
        addFormField(pnlTrai, "Tuổi:");
        txtTuoi = createTextField();
        pnlTrai.add(txtTuoi);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Giới tính
        addFormField(pnlTrai, "Giới tính:");
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboGioiTinh.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cboGioiTinh.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        pnlTrai.add(cboGioiTinh);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Loại nhân viên
        addFormField(pnlTrai, "Loại nhân viên:");
        cboLoaiNV = new JComboBox<>();
        cboLoaiNV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboLoaiNV.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cboLoaiNV.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        pnlTrai.add(cboLoaiNV);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tài khoản
        addFormField(pnlTrai, "Tài khoản:");
        txtUsername = createTextField();
        pnlTrai.add(txtUsername);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Mật khẩu
        addFormField(pnlTrai, "Mật khẩu:");
        txtPassw = createTextField();
        pnlTrai.add(txtPassw);

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

        // ==== Bảng nhân viên ====
        pnlGiua = new JPanel(new BorderLayout());
        pnlGiua.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlGiua.setBackground(BACKGROUND_COLOR);

        String[] tieuDeCot = {"Mã NV", "Tên NV", "Số điện thoại", "Tuổi", "Giới tính", "Loại NV", "Tài khoản", "Mật khẩu"};
        modelNhanVien = new DefaultTableModel(tieuDeCot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblNhanVien = new JTable(modelNhanVien);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblNhanVien.setRowHeight(35);
        tblNhanVien.setSelectionBackground(Color.decode("#3498DB"));
        tblNhanVien.setSelectionForeground(Color.WHITE);
        tblNhanVien.setGridColor(Color.decode("#BDC3C7"));
        tblNhanVien.setShowGrid(true);
        tblNhanVien.setIntercellSpacing(new Dimension(1, 1));
        tblNhanVien.addMouseListener(this);

        // Style cho header của bảng
        JTableHeader header = tblNhanVien.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tieuDeCot.length; i++) {
            tblNhanVien.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        tblNhanVien.getColumnModel().getColumn(1).setPreferredWidth(100);
        JScrollPane scr = new JScrollPane(tblNhanVien);
        scr.setBorder(null);
        scr.getViewport().setBackground(Color.WHITE);

        JLabel lblTieuDe = new JLabel("QUẢN LÝ NHÂN VIÊN", JLabel.CENTER);
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

        // Layout chính
        setLayout(new BorderLayout());
        add(pnlNoiDung, BorderLayout.CENTER);

        // Action listeners
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnKhoiPhuc.addActionListener(this);

        loadLoaiNhanVien();
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
        txt.setPreferredSize(new Dimension(100, 30));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
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

    private void loadLoaiNhanVien() {
        cboLoaiNV.removeAllItems();
        dsLoaiNV = LoaiNhanVienDAO.getAllLoaiNV();

        for (LoaiNhanVien loai : dsLoaiNV) {
            cboLoaiNV.addItem(loai.getTenLoaiNV());
        }
    }

    private void DocDuLieuDB() {
        modelNhanVien.setRowCount(0);
        ArrayList<NhanVien> ds = NhanVienDAO.getAll();

        for (NhanVien nv : ds) {
            modelNhanVien.addRow(new Object[]{
                nv.getMaNhanVien(), nv.getHoTen(), nv.getSDT(),
                nv.getTuoi(), nv.getGioiTinh(),
                nv.getLnv().getTenLoaiNV(), nv.getUsername(), nv.getPassword()
            });
        }
    }

    private void ThemActions() {
        if (txtTenNV.getText().trim().isEmpty() || txtSoDT.getText().trim().isEmpty() ||
            txtTuoi.getText().trim().isEmpty() || txtUsername.getText().trim().isEmpty() ||
            txtPassw.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maNV = NhanVienDAO.getNextNhanVienId();
        String ten = txtTenNV.getText().trim();
        String sdt = txtSoDT.getText().trim();
        int tuoi;
        
        try {
            tuoi = Integer.parseInt(txtTuoi.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tuổi phải là số!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String gioiTinh = (String) cboGioiTinh.getSelectedItem();
        String username = txtUsername.getText().trim();
        String password = txtPassw.getText().trim();
        int index = cboLoaiNV.getSelectedIndex();

        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn loại nhân viên.", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LoaiNhanVien lnv = dsLoaiNV.get(index);

        if (!sdt.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải gồm 10 số.", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (tuoi < 18) {
            JOptionPane.showMessageDialog(this, "Tuổi phải từ 18 trở lên.", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        NhanVien nv = new NhanVien(maNV, username, password, ten, gioiTinh, sdt, tuoi, lnv);
        if (NhanVienDAO.add(nv)) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            DocDuLieuDB();
            xoaTrang();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại.", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void SuaActions() {
        int row = tblNhanVien.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để sửa.", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maNV = txtMaNV.getText().trim();
        String ten = txtTenNV.getText().trim();
        String sdt = txtSoDT.getText().trim();
        int tuoi;
        
        try {
            tuoi = Integer.parseInt(txtTuoi.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tuổi phải là số!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String gioiTinh = (String) cboGioiTinh.getSelectedItem();
        String username = txtUsername.getText().trim();
        String password = txtPassw.getText().trim();
        int index = cboLoaiNV.getSelectedIndex();
        LoaiNhanVien lnv = dsLoaiNV.get(index);

        if (!sdt.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải gồm 10 số.", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        NhanVien nv = new NhanVien(maNV, username, password, ten, gioiTinh, sdt, tuoi, lnv);
        if (NhanVienDAO.update(nv)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            DocDuLieuDB();
            xoaTrang();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại.", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void XoaActions() {
        int row = tblNhanVien.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên để xóa.", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maNV = tblNhanVien.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + maNV + "?", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if(NhanVienDAO.hide(maNV)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                DocDuLieuDB();
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xoaTrang() {
        txtMaNV.setText(NhanVienDAO.getNextNhanVienId());
        txtTenNV.setText("");
        txtSoDT.setText("");
        txtTuoi.setText("");
        txtUsername.setText("");
        txtPassw.setText("");
        cboGioiTinh.setSelectedIndex(0);
        if(cboLoaiNV.getItemCount() > 0) {
            cboLoaiNV.setSelectedIndex(0);
        }
        tblNhanVien.clearSelection();
    }

    private void KhoiPhucActions() {
        ArrayList<NhanVien> dsAn = NhanVienDAO.getNVAn();
        if (dsAn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có nhân viên nào bị ẩn!", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Tạo dialog với table
        JDialog dialogKhoiPhuc = new JDialog(this, "Danh sách nhân viên đã xóa", true);
        dialogKhoiPhuc.setLayout(new BorderLayout());
        dialogKhoiPhuc.setSize(900, 500);
        dialogKhoiPhuc.setLocationRelativeTo(this);
        
        // Tiêu đề
        JLabel lblTitle = new JLabel("Chọn nhân viên cần khôi phục", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(HEADER_COLOR);
        lblTitle.setBorder(new EmptyBorder(15, 10, 15, 10));
        
        // Tạo table
        String[] columns = {"Mã NV", "Tên NV", "Số điện thoại", "Tuổi", "Giới tính", "Loại NV", "Tài khoản"};
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
        
        // Style header
        JTableHeader headerRestore = tblRestore.getTableHeader();
        headerRestore.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerRestore.setBackground(HEADER_COLOR);
        headerRestore.setForeground(Color.WHITE);
        headerRestore.setPreferredSize(new Dimension(headerRestore.getWidth(), 40));
        
        // Căn giữa
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columns.length; i++) {
            tblRestore.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Thêm dữ liệu
        for (NhanVien nv : dsAn) {
            modelRestore.addRow(new Object[]{
                nv.getMaNhanVien(),
                nv.getHoTen(),
                nv.getSDT(),
                nv.getTuoi(),
                nv.getGioiTinh(),
                nv.getLnv().getTenLoaiNV(),
                nv.getUsername()
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
                String maChon = modelRestore.getValueAt(selectedRow, 0).toString();
                if (NhanVienDAO.restore(maChon)) {
                    JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thành công!");
                    DocDuLieuDB();
                    xoaTrang();
                    dialogKhoiPhuc.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(dialogKhoiPhuc, "Vui lòng chọn nhân viên cần khôi phục!");
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
        else if(o == btnKhoiPhuc) KhoiPhucActions();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tblNhanVien.getSelectedRow();
        if (row == -1) return;

        txtMaNV.setText(tblNhanVien.getValueAt(row, 0).toString());
        txtTenNV.setText(tblNhanVien.getValueAt(row, 1).toString());
        txtSoDT.setText(tblNhanVien.getValueAt(row, 2).toString());
        txtTuoi.setText(tblNhanVien.getValueAt(row, 3).toString());
        cboGioiTinh.setSelectedItem(tblNhanVien.getValueAt(row, 4).toString());
        cboLoaiNV.setSelectedItem(tblNhanVien.getValueAt(row, 5).toString());
        txtUsername.setText(tblNhanVien.getValueAt(row, 6).toString());
        txtPassw.setText(tblNhanVien.getValueAt(row, 7).toString());
        
        if(e.getClickCount() == 2 && row != -1) {
            xoaTrang();
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}