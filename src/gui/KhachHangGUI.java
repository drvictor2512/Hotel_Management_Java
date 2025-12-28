package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.KhachHangDAO;
import entity.KhachHang;
import entity.NhanVien;

public class KhachHangGUI extends MenuGUI implements ActionListener, MouseListener {

    private JTable tblKhachHang;
    private DefaultTableModel modelKhachHang;

    private JTextField txtMaKH, txtHoTen, txtCCCD, txtTuoi, txtSDT;
    private JComboBox<String> cboGioiTinh;
    private JButton btnThem, btnXoa, btnSua;
    private JPanel pnlNoiDung, pnlTrai, pnlGiua;

    private ArrayList<String> dsXoa = new ArrayList<>();
	private JButton btnKhoiPhuc;
    private static ArrayList<KhachHang> dsKH;

    // Màu sắc chủ đạo
    private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
    private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
    private static final Color HEADER_COLOR = Color.decode("#34495E");

    public KhachHangGUI(NhanVien nv) {
        super("Khách hàng", nv);

        pnlNoiDung = new JPanel(new BorderLayout());
        pnlNoiDung.setBackground(BACKGROUND_COLOR);

        // ==== Panel trái - nhập liệu ====
        pnlTrai = new JPanel();
        pnlTrai.setLayout(new BoxLayout(pnlTrai, BoxLayout.Y_AXIS));
        pnlTrai.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlTrai.setBackground(PANEL_COLOR);
        pnlTrai.setPreferredSize(new Dimension(350, 0));

        JLabel lblFormTitle = new JLabel("Thông tin khách hàng");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(HEADER_COLOR);
        lblFormTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pnlTrai.add(lblFormTitle);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 20)));

        addFormField(pnlTrai, "Mã khách hàng:");
        txtMaKH = createTextField();
        txtMaKH.setEditable(false);
        txtMaKH.setBackground(Color.decode("#ECF0F1"));
        pnlTrai.add(txtMaKH);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        addFormField(pnlTrai, "Họ tên:");
        txtHoTen = createTextField();
        pnlTrai.add(txtHoTen);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        addFormField(pnlTrai, "CCCD:");
        txtCCCD = createTextField();
        pnlTrai.add(txtCCCD);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        addFormField(pnlTrai, "Tuổi:");
        txtTuoi = createTextField();
        pnlTrai.add(txtTuoi);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        addFormField(pnlTrai, "Số điện thoại:");
        txtSDT = createTextField();
        pnlTrai.add(txtSDT);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        addFormField(pnlTrai, "Giới tính:");
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboGioiTinh.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cboGioiTinh.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        pnlTrai.add(cboGioiTinh);

        pnlTrai.add(Box.createVerticalGlue());
        
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

        // ==== Panel giữa - bảng dữ liệu ====
        pnlGiua = new JPanel(new BorderLayout());
        pnlGiua.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlGiua.setBackground(BACKGROUND_COLOR);

        String[] columns = {"Mã KH", "Họ tên", "CCCD", "Tuổi", "Số điện thoại", "Giới tính"};
        modelKhachHang = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblKhachHang = new JTable(modelKhachHang);
        tblKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKhachHang.setRowHeight(35);
        tblKhachHang.setSelectionBackground(Color.decode("#3498DB"));
        tblKhachHang.setSelectionForeground(Color.WHITE);
        tblKhachHang.setGridColor(Color.decode("#BDC3C7"));
        tblKhachHang.addMouseListener(this);

        JTableHeader header = tblKhachHang.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columns.length; i++)
            tblKhachHang.getColumnModel().getColumn(i).setCellRenderer(center);

        JScrollPane scr = new JScrollPane(tblKhachHang);
        scr.setBorder(null);

        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(HEADER_COLOR);
        lblTitle.setBorder(new EmptyBorder(20, 0, 20, 0));

        JPanel pnlTableContainer = new JPanel(new BorderLayout());
        pnlTableContainer.setBackground(PANEL_COLOR);
        pnlTableContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlTableContainer.add(lblTitle, BorderLayout.NORTH);
        pnlTableContainer.add(scr, BorderLayout.CENTER);

        pnlGiua.add(pnlTableContainer, BorderLayout.CENTER);
        pnlNoiDung.add(pnlGiua, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(pnlNoiDung, BorderLayout.CENTER);

        // ==== Sự kiện ====
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnKhoiPhuc.addActionListener(this);

        loadDataKhachHang();
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
    private void loadDataKhachHang() {
        KhachHangDAO khDao = new KhachHangDAO();
        dsKH = khDao.getAllKhachHang();
        modelKhachHang.setRowCount(0);

        for (KhachHang k : dsKH) {
            if (dsXoa.contains(k.getMaKH())) continue;
            modelKhachHang.addRow(new Object[]{
                    k.getMaKH(), k.getHoTen(), k.getCCCD(),
                    k.getTuoi(), k.getSDT(), k.getGioiTinh()
            });
        }

        try {
            txtMaKH.setText(khDao.getNextKhachHangId());
        } catch (Exception e) {
            txtMaKH.setText("");
        }
    }

    private void themKH() {
        String ten = txtHoTen.getText().trim();
        String cccd = txtCCCD.getText().trim();
        String tuoiStr = txtTuoi.getText().trim();
        String sdt = txtSDT.getText().trim();
        String gioiTinh = (String) cboGioiTinh.getSelectedItem();

        if (ten.isEmpty() || cccd.isEmpty() || tuoiStr.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        if (!cccd.matches("\\d{12}")) {
            JOptionPane.showMessageDialog(this, "CCCD phải gồm 12 số!");
            return;
        }
        if (!sdt.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải gồm 10 số!");
            return;
        }

        try {
            int tuoi = Integer.parseInt(tuoiStr);
            if (tuoi < 18) {
                JOptionPane.showMessageDialog(this, "Tuổi phải >= 18!");
                return;
            }
            KhachHangDAO dao = new KhachHangDAO();
            KhachHang kh = new KhachHang(dao.getNextKhachHangId(), cccd, ten, tuoi, sdt, gioiTinh, nv.getMaNhanVien());
            if (dao.addKhachHang(kh)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadDataKhachHang();
                clearForm();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tuổi phải là số!");
        }
    }

    private void suaKH() {
        int row = tblKhachHang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn khách hàng để sửa!");
            return;
        }

        try {
            String maKH = txtMaKH.getText().trim();
            String ten = txtHoTen.getText().trim();
            String cccd = txtCCCD.getText().trim();
            int tuoi = Integer.parseInt(txtTuoi.getText().trim());
            String sdt = txtSDT.getText().trim();
            String gioiTinh = (String) cboGioiTinh.getSelectedItem();

            KhachHang kh = new KhachHang(maKH, cccd, ten, tuoi, sdt, gioiTinh, nv.getMaNhanVien());
            KhachHangDAO dao = new KhachHangDAO();

            if (dao.updateKhachHang(kh)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadDataKhachHang();
                clearForm();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi dữ liệu!");
        }
    }

    private void xoaKH() {
        int row = tblKhachHang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn khách hàng để xóa!");
            return;
        }
        String ma = tblKhachHang.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa khách hàng có " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            KhachHangDAO dao = new KhachHangDAO();
            if (dao.hideKhachHang(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                loadDataKhachHang();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    private void clearForm() {
        txtHoTen.setText("");
        txtCCCD.setText("");
        txtTuoi.setText("");
        txtSDT.setText("");
        cboGioiTinh.setSelectedIndex(0);
        KhachHangDAO dao = new KhachHangDAO();
        try { txtMaKH.setText(dao.getNextKhachHangId()); } catch (Exception e) {}
        tblKhachHang.clearSelection();
    }
    private void KhoiPhucActions() {
        ArrayList<KhachHang> dsAn = KhachHangDAO.getKhachHangAn();
        if (dsAn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có khách hàng nào bị ẩn!", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Tạo dialog với table
        JDialog dialogKhoiPhuc = new JDialog(this, "Danh sách khách hàng đã xóa", true);
        dialogKhoiPhuc.setLayout(new BorderLayout());
        dialogKhoiPhuc.setSize(900, 500);
        dialogKhoiPhuc.setLocationRelativeTo(this);

        // Tiêu đề
        JLabel lblTitle = new JLabel("Chọn khách hàng cần khôi phục", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setBorder(new EmptyBorder(15, 10, 15, 10));

        // Tạo table
        String[] columns = {"Mã KH", "Họ tên", "CCCD", "Tuổi", "Số điện thoại", "Giới tính"};
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
        headerRestore.setBackground(Color.decode("#2C3E50"));
        headerRestore.setForeground(Color.WHITE);
        headerRestore.setPreferredSize(new Dimension(headerRestore.getWidth(), 40));

        // Căn giữa
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columns.length; i++) {
            tblRestore.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Thêm dữ liệu
        for (KhachHang kh : dsAn) {
            modelRestore.addRow(new Object[]{
                kh.getMaKH(),
                kh.getHoTen(),
                kh.getCCCD(),
                kh.getTuoi(),
                kh.getSDT(),
                kh.getGioiTinh()
            });
        }

        JScrollPane scrollPane = new JScrollPane(tblRestore);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnRestore = createStyledButton("Khôi phục", Color.decode("#27AE60"));
        JButton btnCancel = createStyledButton("Hủy", Color.decode("#95A5A6"));
        JPanel pnlButtons = new JPanel();
        pnlButtons.add(btnRestore);
        pnlButtons.add(btnCancel);

        btnRestore.addActionListener(e -> {
            int row = tblRestore.getSelectedRow();
            if (row >= 0) {
                String maKH = modelRestore.getValueAt(row, 0).toString();
                if (KhachHangDAO.restoreKhachHang(maKH)) {
                    JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thành công!");
                    dialogKhoiPhuc.dispose();
                    loadDataKhachHang(); // load lại bảng
                } else {
                    JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(dialogKhoiPhuc, "Vui lòng chọn khách hàng cần khôi phục!");
            }
        });

        btnCancel.addActionListener(e -> dialogKhoiPhuc.dispose());

        dialogKhoiPhuc.add(lblTitle, BorderLayout.NORTH);
        dialogKhoiPhuc.add(scrollPane, BorderLayout.CENTER);
        dialogKhoiPhuc.add(pnlButtons, BorderLayout.SOUTH);
        dialogKhoiPhuc.setVisible(true);
    }
  

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == btnThem) themKH();
        else if (o == btnSua) suaKH();
        else if (o == btnXoa) xoaKH();
        else if (o == btnKhoiPhuc) KhoiPhucActions();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tblKhachHang.getSelectedRow();
        if (row == -1) return;

        txtMaKH.setText(tblKhachHang.getValueAt(row, 0).toString());
        txtHoTen.setText(tblKhachHang.getValueAt(row, 1).toString());
        txtCCCD.setText(tblKhachHang.getValueAt(row, 2).toString());
        txtTuoi.setText(tblKhachHang.getValueAt(row, 3).toString());
        txtSDT.setText(tblKhachHang.getValueAt(row, 4).toString());
        cboGioiTinh.setSelectedItem(tblKhachHang.getValueAt(row, 5).toString());

        if (e.getClickCount() == 2) clearForm();
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
