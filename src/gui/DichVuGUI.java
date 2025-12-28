package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.DichVuDAO;
import entity.DichVu;
import entity.NhanVien;

public class DichVuGUI extends MenuGUI implements ActionListener, MouseListener {
    private JTable tblDichVu;
    private DefaultTableModel modelDichVu;
    private JTextField txtMaDV, txtTenDV, txtGiaDV;
    private JComboBox<String> cboDonViTinh;
    private JButton btnThem, btnSua, btnXoa, btnKhoiPhuc;
    private JPanel pnlNoiDung, pnlTrai, pnlGiua;

    private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
    private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
    private static final Color HEADER_COLOR = Color.decode("#34495E");

    public DichVuGUI(NhanVien nv) {
        super("Dịch vụ", nv); 

        pnlNoiDung = new JPanel(new BorderLayout());
        pnlNoiDung.setBackground(BACKGROUND_COLOR);

        pnlTrai = new JPanel();
        pnlTrai.setLayout(new BoxLayout(pnlTrai, BoxLayout.Y_AXIS));
        pnlTrai.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlTrai.setBackground(PANEL_COLOR);
        pnlTrai.setPreferredSize(new Dimension(350, 0));

        JLabel lblFormTitle = new JLabel("Thông tin dịch vụ");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(HEADER_COLOR);
        lblFormTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pnlTrai.add(lblFormTitle);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 20)));

        addFormField(pnlTrai, "Mã dịch vụ:");
        txtMaDV = createTextField();
        txtMaDV.setEditable(false);
        txtMaDV.setBackground(Color.decode("#ECF0F1"));
        txtMaDV.setText(DichVuDAO.getNextDichVuId());
        pnlTrai.add(txtMaDV);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        addFormField(pnlTrai, "Tên dịch vụ:");
        txtTenDV = createTextField();
        pnlTrai.add(txtTenDV);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        addFormField(pnlTrai, "Giá dịch vụ:");
        txtGiaDV = createTextField();
        pnlTrai.add(txtGiaDV);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        addFormField(pnlTrai, "Đơn vị tính:");
        String[] donViTinhOptions = {"Lần", "Chai", "Lon", "Suất", "Kg", "Lượt", "Giờ", "Ngày", "Người"};
        cboDonViTinh = new JComboBox<>(donViTinhOptions);
        cboDonViTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboDonViTinh.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cboDonViTinh.setPreferredSize(new Dimension(100, 35));
        cboDonViTinh.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        cboDonViTinh.setBackground(Color.WHITE);
        pnlTrai.add(cboDonViTinh);

        pnlTrai.add(Box.createVerticalGlue());

        JPanel pnlNut = new JPanel();
        pnlNut.setLayout(new BoxLayout(pnlNut, BoxLayout.Y_AXIS));
        pnlNut.setBackground(PANEL_COLOR);
        pnlNut.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        pnlNut.setBorder(new EmptyBorder(20, 0, 0, 0));

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

        pnlGiua = new JPanel(new BorderLayout());
        pnlGiua.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlGiua.setBackground(BACKGROUND_COLOR);

        // THÊM CỘT ĐƠN VỊ TÍNH
        String[] tieuDe = {"Mã DV", "Tên DV", "Giá DV", "Đơn vị tính"};
        modelDichVu = new DefaultTableModel(tieuDe, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDichVu = new JTable(modelDichVu);
        tblDichVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblDichVu.setRowHeight(35);
        tblDichVu.setSelectionBackground(Color.decode("#3498DB"));
        tblDichVu.setSelectionForeground(Color.WHITE);
        tblDichVu.setGridColor(Color.decode("#BDC3C7"));
        tblDichVu.setShowGrid(true);
        tblDichVu.setIntercellSpacing(new Dimension(1, 1));
        tblDichVu.addMouseListener(this);

        JTableHeader header = tblDichVu.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Căn giữa cho tất cả các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tieuDe.length; i++) {
            tblDichVu.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Thiết lập độ rộng cột
        tblDichVu.getColumnModel().getColumn(0).setPreferredWidth(80);  // Mã DV
        tblDichVu.getColumnModel().getColumn(1).setPreferredWidth(200); // Tên DV
        tblDichVu.getColumnModel().getColumn(2).setPreferredWidth(120); // Giá DV
        tblDichVu.getColumnModel().getColumn(3).setPreferredWidth(100); // Đơn vị tính

        JScrollPane scr = new JScrollPane(tblDichVu);
        scr.setBorder(null);
        scr.getViewport().setBackground(Color.WHITE);

        JLabel lblTieuDe = new JLabel("QUẢN LÝ DỊCH VỤ", JLabel.CENTER);
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

        add(pnlNoiDung, BorderLayout.CENTER);

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnKhoiPhuc.addActionListener(this);

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

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txt.setPreferredSize(new Dimension(100, 30));
        txt.setAlignmentX(JTextField.LEFT_ALIGNMENT);
        return txt;
    }

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
        ArrayList<DichVu> dsDV = DichVuDAO.getAllDichVu();
        modelDichVu.setRowCount(0);

        for (DichVu dv : dsDV) {
            modelDichVu.addRow(new Object[]{
                dv.getMaDV(), 
                dv.getTenDV(), 
                String.format("%,.0f VNĐ", dv.getGiaDV()),
                dv.getDonViTinh() 
            });
        }
    }

    private boolean validateFields() {
        if (txtTenDV.getText().trim().isEmpty() || txtGiaDV.getText().trim().isEmpty()) {
             JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.", 
                 "Thông báo", JOptionPane.WARNING_MESSAGE);
             return false;
        }
        
        try {
            double giaDV = Double.parseDouble(txtGiaDV.getText().trim());
            if (giaDV <= 0) {
                JOptionPane.showMessageDialog(this, "Giá dịch vụ phải lớn hơn 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá dịch vụ phải là số hợp lệ!");
            return false;
        }
        return true;
    }

    private void ThemDV() {
        if (!validateFields()) return;
        
        try {
            String tenDV = txtTenDV.getText().trim();
            double giaDV = Double.parseDouble(txtGiaDV.getText().trim());
            String donViTinh = cboDonViTinh.getSelectedItem().toString(); // THÊM MỚI
            String maDV = DichVuDAO.getNextDichVuId();
            String maNV = nv.getMaNhanVien(); 

            DichVu dv = new DichVu(maDV, tenDV, giaDV, maNV, donViTinh); // CẬP NHẬT

            if (DichVuDAO.addDichVu(dv)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                DocDuLieuDB();
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá dịch vụ phải là số hợp lệ!");
        }
    }

    private void SuaDV() {
        int row = tblDichVu.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần sửa!");
            return;
        }
        
        if (!validateFields()) return;

        try {
            String maDV = txtMaDV.getText().trim();
            String tenDV = txtTenDV.getText().trim();
            double giaDV = Double.parseDouble(txtGiaDV.getText().trim());
            String donViTinh = cboDonViTinh.getSelectedItem().toString(); // THÊM MỚI
            String maNV = nv.getMaNhanVien(); 

            DichVu dv = new DichVu(maDV, tenDV, giaDV, maNV, donViTinh); // CẬP NHẬT
            
            if (DichVuDAO.updateDichVu(dv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                DocDuLieuDB();
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá dịch vụ phải là số hợp lệ");
        }
    }

    private void XoaDV() {
        int row = tblDichVu.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ để xóa.");
            return;
        }
        
        String maDV = tblDichVu.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa dịch vụ " + maDV + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (DichVuDAO.hide(maDV)) {
                JOptionPane.showMessageDialog(this, "Xóa dịch vụ thành công");
                DocDuLieuDB();
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại");
            }
        }
    }

    private void KhoiPhucDV() {
        ArrayList<DichVu> dsAn = DichVuDAO.getDVAn();
        if (dsAn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dịch vụ nào bị ẩn!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialogKhoiPhuc = new JDialog(this, "Danh sách dịch vụ đã xóa", true);
        dialogKhoiPhuc.setLayout(new BorderLayout());
        dialogKhoiPhuc.setSize(800, 500); // Tăng kích thước
        dialogKhoiPhuc.setLocationRelativeTo(this);

        JLabel lblTitle = new JLabel("Chọn dịch vụ cần khôi phục", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(HEADER_COLOR);
        lblTitle.setBorder(new EmptyBorder(15, 10, 15, 10));

        // THÊM CỘT ĐƠN VỊ TÍNH
        String[] columns = {"Mã DV", "Tên DV", "Giá DV", "Đơn vị tính"};
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

        JTableHeader headerRestore = tblRestore.getTableHeader();
        headerRestore.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerRestore.setBackground(HEADER_COLOR);
        headerRestore.setForeground(Color.WHITE);
        headerRestore.setPreferredSize(new Dimension(headerRestore.getWidth(), 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columns.length; i++) {
            tblRestore.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        for (DichVu dv : dsAn) {
            modelRestore.addRow(new Object[]{
                dv.getMaDV(),
                dv.getTenDV(),
                String.format("%,.0f VNĐ", dv.getGiaDV()),
                dv.getDonViTinh() // THÊM MỚI
            });
        }

        JScrollPane scrollPane = new JScrollPane(tblRestore);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
        pnlButtons.setBorder(new EmptyBorder(10, 10, 15, 10));

        JButton btnConfirm = createStyledButton("Khôi phục", Color.decode("#27AE60"));
        JButton btnCancel = createStyledButton("Hủy", Color.decode("#95A5A6"));

        btnConfirm.addActionListener(e -> {
            int selectedRow = tblRestore.getSelectedRow();
            if (selectedRow >= 0) {
                String maChon = modelRestore.getValueAt(selectedRow, 0).toString();
                if (DichVuDAO.restore(maChon)) {
                    JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thành công!");
                    DocDuLieuDB();
                    xoaTrang();
                    dialogKhoiPhuc.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(dialogKhoiPhuc, "Vui lòng chọn dịch vụ cần khôi phục!");
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
    
    private void xoaTrang() {
        txtMaDV.setText(DichVuDAO.getNextDichVuId());
        txtTenDV.setText("");
        txtGiaDV.setText("");
        cboDonViTinh.setSelectedIndex(0); // THÊM MỚI: Reset về mục đầu tiên
        tblDichVu.clearSelection();
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == btnThem) ThemDV();
        else if (o == btnSua) SuaDV();
        else if (o == btnXoa) XoaDV();
        else if (o == btnKhoiPhuc) KhoiPhucDV();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tblDichVu.getSelectedRow();
        if (row == -1) return;

        txtMaDV.setText(tblDichVu.getValueAt(row, 0).toString());
        txtTenDV.setText(tblDichVu.getValueAt(row, 1).toString());
        
        // Lấy giá và loại bỏ format
        String giaStr = tblDichVu.getValueAt(row, 2).toString();
        giaStr = giaStr.replace(" VNĐ", "").replace(",", "");
        txtGiaDV.setText(giaStr);
        
        // THÊM MỚI: Set đơn vị tính
        String donViTinh = tblDichVu.getValueAt(row, 3).toString();
        cboDonViTinh.setSelectedItem(donViTinh);
        
        if(e.getClickCount() == 2 && row != -1) {
            xoaTrang(); 
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}