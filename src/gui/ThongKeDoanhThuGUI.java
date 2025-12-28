package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import dao.ThongKeDoanhThuDAO;
import entity.NhanVien;

public class ThongKeDoanhThuGUI extends MenuGUI implements ActionListener {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnThongKe, btnLamMoi;
    private JComboBox<String> cboThoiGian;
    private JDateChooser dateNgayBD, dateNgayKT;
    private JTextField txtThang, txtNam;
    private JLabel lblTongDoanhThu, lblSoLuongHD;
    private DecimalFormat df = new DecimalFormat("#,###");
    
    // Màu sắc
    private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
    private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
    private static final Color HEADER_COLOR = Color.decode("#34495E");
    private static final Color PRIMARY_COLOR = Color.decode("#3498DB");
    private static final Color DANGER_COLOR = Color.decode("#E74C3C");

    public ThongKeDoanhThuGUI(NhanVien nv) {
        super("Thống kê doanh thu", nv);

        JPanel pnlContent = new JPanel(new BorderLayout(10, 10));
        pnlContent.setBackground(BACKGROUND_COLOR);
        pnlContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ======= Panel trái (form lọc thống kê) =======
        JPanel pnlLeft = createLeftPanel();

        // ======= Panel phải (bảng kết quả) =======
        JPanel pnlRight = createRightPanel();

        pnlContent.add(pnlLeft, BorderLayout.WEST);
        pnlContent.add(pnlRight, BorderLayout.CENTER);

        add(pnlContent, BorderLayout.CENTER);
        
        // Load dữ liệu ban đầu
        loadAllData();
        setVisible(true);
    }

    private JPanel createLeftPanel() {
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setBackground(PANEL_COLOR);
        pnlLeft.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#BDC3C7"), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        pnlLeft.setPreferredSize(new Dimension(320, 0));

        // Title
        JLabel lblTitle = new JLabel("Bộ lọc thống kê");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(HEADER_COLOR);
        
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.setBackground(PANEL_COLOR);
        pnlTitle.add(lblTitle);

        // Form fields
        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(PANEL_COLOR);
        pnlForm.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Combo chọn thời gian
        String[] thoiGian = {
            "Thống kê theo khoảng ngày",
            "Thống kê theo tháng",
            "Thống kê theo năm"
        };
        
        pnlForm.add(createLabel("Chọn kiểu thống kê:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));
        cboThoiGian = new JComboBox<>(thoiGian);
        cboThoiGian.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboThoiGian.setPreferredSize(new Dimension(280, 40));
        cboThoiGian.setMaximumSize(new Dimension(280, 40));
        cboThoiGian.setAlignmentX(Component.LEFT_ALIGNMENT);
        cboThoiGian.addActionListener(this);
        pnlForm.add(cboThoiGian);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 20)));

        // Ngày bắt đầu
        pnlForm.add(createLabel("Ngày bắt đầu:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));
        dateNgayBD = new JDateChooser();
        dateNgayBD.setDateFormatString("dd/MM/yyyy");
        dateNgayBD.setPreferredSize(new Dimension(280, 40));
        dateNgayBD.setMaximumSize(new Dimension(280, 40));
        dateNgayBD.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateNgayBD.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlForm.add(dateNgayBD);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        // Ngày kết thúc
        pnlForm.add(createLabel("Ngày kết thúc:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));
        dateNgayKT = new JDateChooser();
        dateNgayKT.setDateFormatString("dd/MM/yyyy");
        dateNgayKT.setPreferredSize(new Dimension(280, 40));
        dateNgayKT.setMaximumSize(new Dimension(280, 40));
        dateNgayKT.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateNgayKT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlForm.add(dateNgayKT);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tháng
        pnlForm.add(createLabel("Tháng (1-12):"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));
        txtThang = new JTextField();
        txtThang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtThang.setPreferredSize(new Dimension(280, 40));
        txtThang.setMaximumSize(new Dimension(280, 40));
        txtThang.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtThang.setEnabled(false);
        pnlForm.add(txtThang);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        // Năm
        pnlForm.add(createLabel("Năm:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));
        txtNam = new JTextField();
        txtNam.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNam.setPreferredSize(new Dimension(280, 40));
        txtNam.setMaximumSize(new Dimension(280, 40));
        txtNam.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtNam.setEnabled(false);
        pnlForm.add(txtNam);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 25)));

        // Buttons
        JPanel pnlButtons = new JPanel(new GridLayout(2, 1, 0, 10));
        pnlButtons.setBackground(PANEL_COLOR);
        pnlButtons.setMaximumSize(new Dimension(280, 100));
        pnlButtons.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnThongKe = createStyledButton("Thống kê", PRIMARY_COLOR);
        btnLamMoi = createStyledButton("Làm mới", Color.decode("#95A5A6"));

        btnThongKe.addActionListener(this);
        btnLamMoi.addActionListener(this);

        pnlButtons.add(btnThongKe);
        pnlButtons.add(btnLamMoi);

        pnlForm.add(pnlButtons);

        pnlLeft.add(pnlTitle, BorderLayout.NORTH);
        pnlLeft.add(pnlForm, BorderLayout.CENTER);

        return pnlLeft;
    }

    private JPanel createRightPanel() {
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(BACKGROUND_COLOR);

        // Title
        JLabel lblTitle = new JLabel("KẾT QUẢ THỐNG KÊ DOANH THU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(HEADER_COLOR);
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Table
        String[] columnNames = {
            "Mã HD", "Ngày lập", "Khách hàng", "Phòng",
            "Loại phòng", "Nhân viên", "Phương thức", "Tổng tiền"
        };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(40);
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));

        // Center alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columnNames.length; i++) {
    		table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#BDC3C7"), 1));

        // Panel thống kê
        JPanel pnlThongKe = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlThongKe.setBackground(PANEL_COLOR);
        pnlThongKe.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Số lượng hóa đơn
        JPanel pnlSoLuong = new JPanel(new BorderLayout(10, 0));
        pnlSoLuong.setBackground(Color.WHITE);
        pnlSoLuong.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitleSL = new JLabel("Số lượng hóa đơn:");
        lblTitleSL.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        lblSoLuongHD = new JLabel("0");
        lblSoLuongHD.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSoLuongHD.setForeground(PRIMARY_COLOR);
        lblSoLuongHD.setHorizontalAlignment(JLabel.RIGHT);

        pnlSoLuong.add(lblTitleSL, BorderLayout.WEST);
        pnlSoLuong.add(lblSoLuongHD, BorderLayout.EAST);

        // Tổng doanh thu
        JPanel pnlTongDT = new JPanel(new BorderLayout(10, 0));
        pnlTongDT.setBackground(Color.WHITE);
        pnlTongDT.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DANGER_COLOR, 2),
            new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitleDT = new JLabel("Tổng doanh thu:");
        lblTitleDT.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        lblTongDoanhThu = new JLabel("0 VNĐ");
        lblTongDoanhThu.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTongDoanhThu.setForeground(DANGER_COLOR);
        lblTongDoanhThu.setHorizontalAlignment(JLabel.RIGHT);

        pnlTongDT.add(lblTitleDT, BorderLayout.WEST);
        pnlTongDT.add(lblTongDoanhThu, BorderLayout.EAST);

        pnlThongKe.add(pnlSoLuong);
        pnlThongKe.add(pnlTongDT);

        pnlRight.add(lblTitle, BorderLayout.NORTH);
        pnlRight.add(scrollPane, BorderLayout.CENTER);
        pnlRight.add(pnlThongKe, BorderLayout.SOUTH);

        return pnlRight;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(280, 45));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == cboThoiGian) {
            updateFormFields();
        } 
        else if (o == btnThongKe) {
            thongKe();
        } 
        else if (o == btnLamMoi) {
            lamMoi();
        }
    }

    private void updateFormFields() {
        int choice = cboThoiGian.getSelectedIndex();

        // Reset tất cả
        dateNgayBD.setEnabled(false);
        dateNgayKT.setEnabled(false);
        txtThang.setEnabled(false);
        txtNam.setEnabled(false);
        
        dateNgayBD.setDate(null);
        dateNgayKT.setDate(null);
        txtThang.setText("");
        txtNam.setText("");

        // Bật các trường cần thiết
        if (choice == 0) { // Theo khoảng ngày
            dateNgayBD.setEnabled(true);
            dateNgayKT.setEnabled(true);
        } else if (choice == 1) { // Theo tháng
            txtThang.setEnabled(true);
            txtNam.setEnabled(true);
        } else if (choice == 2) { // Theo năm
            txtNam.setEnabled(true);
        }
    }

    private void thongKe() {
        int choice = cboThoiGian.getSelectedIndex();
        model.setRowCount(0);

        try {
            ArrayList<Object[]> ds = null;

            if (choice == 0) { // Theo khoảng ngày
                if (dateNgayBD.getDate() == null || dateNgayKT.getDate() == null) {
                    JOptionPane.showMessageDialog(this, 
                        "Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                java.sql.Date bd = new java.sql.Date(dateNgayBD.getDate().getTime());
                java.sql.Date kt = new java.sql.Date(dateNgayKT.getDate().getTime());

                if (bd.after(kt)) {
                    JOptionPane.showMessageDialog(this, 
                        "Ngày bắt đầu phải trước hoặc bằng ngày kết thúc!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                ds = ThongKeDoanhThuDAO.thongKeDoanhThuNgay(bd, kt);
            } 
            else if (choice == 1) { // Theo tháng
                String thangStr = txtThang.getText().trim();
                String namStr = txtNam.getText().trim();

                if (thangStr.isEmpty() || namStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Vui lòng nhập đầy đủ tháng và năm!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int thang = Integer.parseInt(thangStr);
                    int nam = Integer.parseInt(namStr);

                    if (thang < 1 || thang > 12) {
                        JOptionPane.showMessageDialog(this, 
                            "Tháng phải từ 1 đến 12!",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    if (nam < 2000 || nam > 2100) {
                        JOptionPane.showMessageDialog(this, 
                            "Năm không hợp lệ!",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    ds = ThongKeDoanhThuDAO.thongKeDoanhThuThang(nam, thang);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Tháng và năm phải là số!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } 
            else if (choice == 2) { // Theo năm
                String namStr = txtNam.getText().trim();

                if (namStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Vui lòng nhập năm!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int nam = Integer.parseInt(namStr);
                    
                    if (nam < 2000 || nam > 2100) {
                        JOptionPane.showMessageDialog(this, 
                            "Năm không hợp lệ!",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    ds = ThongKeDoanhThuDAO.thongKeDoanhThuNam(nam);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Năm phải là số!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Hiển thị dữ liệu
            displayData(ds);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi thống kê: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void displayData(ArrayList<Object[]> ds) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        double tongDoanhThu = 0;
        int soLuong = 0;

        if (ds != null && !ds.isEmpty()) {
            for (Object[] row : ds) {
                Date ngayLap = (Date) row[1];
                double tongTien = Double.parseDouble(row[7].toString());
                tongDoanhThu += tongTien;
                soLuong++;

                model.addRow(new Object[]{
                    row[0],                          // Mã HD
                    sdf.format(ngayLap),            // Ngày lập
                    row[2],                          // Khách hàng
                    row[3],                          // Phòng
                    row[4],                          // Loại phòng
                    row[5],                          // Nhân viên
                    row[6],                          // Phương thức
                    df.format(tongTien) + " VNĐ"    // Tổng tiền
                });
            }
        }

        // Cập nhật thống kê
        lblSoLuongHD.setText(String.valueOf(soLuong));
        lblTongDoanhThu.setText(df.format(tongDoanhThu) + " VNĐ");

        if (soLuong == 0) {
            JOptionPane.showMessageDialog(this, 
                "Không có dữ liệu thống kê phù hợp!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void lamMoi() {
        cboThoiGian.setSelectedIndex(0);
        dateNgayBD.setDate(null);
        dateNgayKT.setDate(null);
        txtThang.setText("");
        txtNam.setText("");
        
        updateFormFields();

        loadAllData();
    }

    private void loadAllData() {
        model.setRowCount(0);
        ArrayList<Object[]> ds = ThongKeDoanhThuDAO.layHoaDonThongKe();
        displayData(ds);
    }
}