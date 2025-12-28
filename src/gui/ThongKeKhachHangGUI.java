package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import dao.ThongKeKhachHangDAO;
import entity.KhachHang;
import entity.NhanVien;

public class ThongKeKhachHangGUI extends MenuGUI implements ActionListener {
    
    private JTable table;
    private DefaultTableModel model;
    
    private JButton btnThongKe, btnLamMoi;
    private JComboBox<String> cmbThoiGian, cmbLuaTuoi, cmbGioiTinh, cmbSoLuong;
    private JDateChooser dateNgayBD, dateNgayKT;
    private JTextField txtThang, txtNam;
    private JLabel lblSoLuongKH, lblKetQua;

    private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
    private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
    private static final Color HEADER_COLOR = Color.decode("#34495E");
    private static final Color PRIMARY_COLOR = Color.decode("#3498DB");
    private static final Color DANGER_COLOR = Color.decode("#E74C3C");

    public ThongKeKhachHangGUI(NhanVien nv) {
        super("Thống kê khách hàng", nv);

        JPanel pnlContent = new JPanel(new BorderLayout(10, 10));
        pnlContent.setBackground(BACKGROUND_COLOR);
        pnlContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        pnlContent.add(createLeftPanel(), BorderLayout.WEST);
        pnlContent.add(createRightPanel(), BorderLayout.CENTER);

        add(pnlContent, BorderLayout.CENTER);

        docDuLieuDB();
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

        JLabel lblTitle = new JLabel("Bộ lọc thống kê");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(HEADER_COLOR);

        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.setBackground(PANEL_COLOR);
        pnlTitle.add(lblTitle);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(PANEL_COLOR);
        pnlForm.setBorder(new EmptyBorder(15, 0, 0, 0));

        pnlForm.add(createLabel("Chọn kiểu thống kê:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));

        String[] arrThoiGian = {
            "Thống kê theo ngày",
            "Thống kê theo tháng",
            "Thống kê theo năm"
        };
        cmbThoiGian = new JComboBox<>(arrThoiGian);
        cmbThoiGian.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbThoiGian.setPreferredSize(new Dimension(280, 40));
        cmbThoiGian.setMaximumSize(new Dimension(280, 40));
        cmbThoiGian.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbThoiGian.addActionListener(this);
        pnlForm.add(cmbThoiGian);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        pnlForm.add(createLabel("Loại thống kê:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));

        String[] arrLoai = {"Tất cả khách hàng", "Thống kê nhiều nhất", "Thống kê ít nhất"};
        cmbSoLuong = new JComboBox<>(arrLoai);
        cmbSoLuong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbSoLuong.setPreferredSize(new Dimension(280, 40));
        cmbSoLuong.setMaximumSize(new Dimension(280, 40));
        cmbSoLuong.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlForm.add(cmbSoLuong);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        pnlForm.add(createLabel("Lứa tuổi:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));

        String[] arrTuoi = {"Tất cả", "18–39", "40–59", "60+"};
        cmbLuaTuoi = new JComboBox<>(arrTuoi);
        cmbLuaTuoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbLuaTuoi.setPreferredSize(new Dimension(280, 40));
        cmbLuaTuoi.setMaximumSize(new Dimension(280, 40));
        cmbLuaTuoi.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlForm.add(cmbLuaTuoi);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        pnlForm.add(createLabel("Giới tính:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));

        String[] arrGT = {"Tất cả", "Nam", "Nữ"};
        cmbGioiTinh = new JComboBox<>(arrGT);
        cmbGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbGioiTinh.setPreferredSize(new Dimension(280, 40));
        cmbGioiTinh.setMaximumSize(new Dimension(280, 40));
        cmbGioiTinh.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlForm.add(cmbGioiTinh);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

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

        pnlForm.add(createLabel("Tháng (1–12):"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));

        txtThang = new JTextField();
        txtThang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtThang.setPreferredSize(new Dimension(280, 40));
        txtThang.setMaximumSize(new Dimension(280, 40));
        txtThang.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtThang.setEnabled(false);
        pnlForm.add(txtThang);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

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

        JLabel lblTitle = new JLabel("THỐNG KÊ KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(HEADER_COLOR);
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        pnlRight.add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"Mã KH", "Họ tên", "CCCD", "Tuổi", "SĐT", "Giới tính", "Số lần thuê"};

        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
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

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < columns.length; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Color.decode("#BDC3C7"), 1));
        pnlRight.add(scroll, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlBottom.setBackground(PANEL_COLOR);
        pnlBottom.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel pnlSoLuong = createStatPanel(PRIMARY_COLOR, "Số lượng khách hàng:");
        lblSoLuongKH = createStatValue(PRIMARY_COLOR);
        pnlSoLuong.add(lblSoLuongKH, BorderLayout.EAST);

        JPanel pnlKetQua = createStatPanel(DANGER_COLOR, "Kết quả:");
        lblKetQua = createStatValue(DANGER_COLOR);
        pnlKetQua.add(lblKetQua, BorderLayout.EAST);

        pnlBottom.add(pnlSoLuong);
        pnlBottom.add(pnlKetQua);

        pnlRight.add(pnlBottom, BorderLayout.SOUTH);

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

    private JPanel createStatPanel(Color borderColor, String title) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        p.add(lbl, BorderLayout.WEST);

        return p;
    }

    private JLabel createStatValue(Color color) {
        JLabel lbl = new JLabel("0");
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(color);
        return lbl;
    }
    
    private void docDuLieuDB() {
        try {
            model.setRowCount(0);
            ArrayList<KhachHang> ds = ThongKeKhachHangDAO.getTatCaKhachHang();
            int tongSoLanTra = 0;  
            for (KhachHang kh : ds) {
                int soLanTra = ThongKeKhachHangDAO.demSoLanTraPhong(kh.getMaKH());
                tongSoLanTra += soLanTra;  

                model.addRow(new Object[]{
                        kh.getMaKH(),
                        kh.getHoTen(),
                        kh.getCCCD(),
                        kh.getTuoi(),
                        kh.getSDT(),
                        kh.getGioiTinh(),
                        soLanTra
                });
            }

            lblSoLuongKH.setText(String.valueOf(ds.size()));
            lblKetQua.setText(String.valueOf(tongSoLanTra));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == cmbThoiGian) updateTimeFields();
        else if (o == btnThongKe) xuLyThongKe();
        else if (o == btnLamMoi) lamMoi();
    }

    private void updateTimeFields() {
        int c = cmbThoiGian.getSelectedIndex();
        dateNgayBD.setEnabled(c == 0);
        dateNgayKT.setEnabled(c == 0);
        txtThang.setEnabled(c == 1);
        txtNam.setEnabled(c >= 1);
        if (c != 0) {
            dateNgayBD.setDate(null);
            dateNgayKT.setDate(null);
        }
        if (c != 1) txtThang.setText("");
        if (c == 0) txtNam.setText("");
    }


    private void xuLyThongKe() {
        int choice = cmbThoiGian.getSelectedIndex();

        String gioiTinh = cmbGioiTinh.getSelectedItem().equals("Tất cả") ? null : (String) cmbGioiTinh.getSelectedItem();

        String luaTuoi = cmbLuaTuoi.getSelectedItem().equals("Tất cả") ? null : (String) cmbLuaTuoi.getSelectedItem();

        String loaiKH = (String) cmbSoLuong.getSelectedItem();

        ArrayList<Object[]> ds = new ArrayList<>();

        lblKetQua.setText(" ");
        model.setRowCount(0);

        try {

            if (choice == 0) {
                Date bd = dateNgayBD.getDate();
                Date kt = dateNgayKT.getDate();

                if (bd == null || kt == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu và kết thúc!");
                    return;
                }

                if (bd.after(kt)) {
                    JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc!");
                    return;
                }

                // Khách hàng đặt phòng nhiều nhất
                if (loaiKH.contains("nhiều")) {
                    ds = ThongKeKhachHangDAO.getKhachHangTraPhongNhieuNhat(bd, kt, gioiTinh, luaTuoi);

                    if (!ds.isEmpty()) {
                        KhachHang kh = (KhachHang) ds.get(0)[0];
                        int soLan = (int) ds.get(0)[1];

                        lblKetQua.setText(
                           ""
                            + kh.getHoTen() + " (" + soLan + " lần)"
                        );
                    }
                }

                // Khách hàng đặt phòng ít nhất
                else if (loaiKH.contains("ít")) {
                    ds = ThongKeKhachHangDAO.getKhachHangTraPhongItNhat(bd, kt, gioiTinh, luaTuoi);

                    if (!ds.isEmpty()) {
                        int soLan = (int) ds.get(0)[1];
                        String ten = "";

                        for (Object[] o : ds) {
                            ten += ((KhachHang) o[0]).getHoTen() + ", ";
                        }

                        lblKetQua.setText(
                            " "
                            + ten + " (" + soLan + " lần)"
                        );
                    }
                }

                
                else {
                    ds = ThongKeKhachHangDAO.getKhachHangVaNgayTraPhong(bd, kt, gioiTinh, luaTuoi);
                    lblKetQua.setText("Tổng số khách hàng: " + ds.size());
                }
            }

         // thống kê theo tháng
            else if (choice == 1) {
                if (txtThang.getText().isEmpty() || txtNam.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập tháng và năm!");
                    return;
                }

                int thang = Integer.parseInt(txtThang.getText());
                int nam = Integer.parseInt(txtNam.getText());

                if (thang < 1 || thang > 12) {
                    JOptionPane.showMessageDialog(this, "Tháng phải từ 1 đến 12!");
                    return;
                }
       
                if (loaiKH.contains("nhiều")) {
                    ds = ThongKeKhachHangDAO.getKhachHangTraPhongNhieuNhat(
                            java.sql.Date.valueOf(nam + "-" + thang + "-01"),
                            java.sql.Date.valueOf(nam + "-" + thang + "-31"),
                            gioiTinh, luaTuoi);
                    if (!ds.isEmpty()) {
                        KhachHang kh = (KhachHang) ds.get(0)[0];
                        int soLan = (int) ds.get(0)[1];
                        lblKetQua.setText(kh.getHoTen() + " (" + soLan + " lần)");
                    }
                } else if (loaiKH.contains("ít")) {
                    ds = ThongKeKhachHangDAO.getKhachHangTraPhongItNhat(
                            java.sql.Date.valueOf(nam + "-" + thang + "-01"),
                            java.sql.Date.valueOf(nam + "-" + thang + "-31"),
                            gioiTinh, luaTuoi);
                    if (!ds.isEmpty()) {
                        int soLan = (int) ds.get(0)[1];
                        String ten = "";
                        for (Object[] o : ds) {
                            ten += ((KhachHang) o[0]).getHoTen() + ", ";
                        }
                        lblKetQua.setText(ten + " (" + soLan + " lần)");
                    }
                } else {
                    ds = ThongKeKhachHangDAO.getKhachHangTheoThang(nam, thang, gioiTinh, luaTuoi);
                    lblKetQua.setText("Tổng số khách hàng đặt trong tháng này: " + ds.size());
                }
            }

            //thống kê theo năm
            else if (choice == 2) {
                if (txtNam.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập năm!");
                    return;
                }

                int nam = Integer.parseInt(txtNam.getText());

                if (loaiKH.contains("nhiều")) {
                    ds = ThongKeKhachHangDAO.getKhachHangTraPhongNhieuNhat(
                            java.sql.Date.valueOf(nam + "-01-01"),
                            java.sql.Date.valueOf(nam + "-12-31"),
                            gioiTinh, luaTuoi);
                    if (!ds.isEmpty()) {
                        KhachHang kh = (KhachHang) ds.get(0)[0];
                        int soLan = (int) ds.get(0)[1];
                        lblKetQua.setText(kh.getHoTen() + " (" + soLan + " lần)");
                    }
                } else if (loaiKH.contains("ít")) {
                    ds = ThongKeKhachHangDAO.getKhachHangTraPhongItNhat(
                            java.sql.Date.valueOf(nam + "-01-01"),
                            java.sql.Date.valueOf(nam + "-12-31"),
                            gioiTinh, luaTuoi);
                    if (!ds.isEmpty()) {
                        int soLan = (int) ds.get(0)[1];
                        String ten = "";
                        for (Object[] o : ds) {
                            ten += ((KhachHang) o[0]).getHoTen() + ", ";
                        }
                        lblKetQua.setText(ten + " (" + soLan + " lần)");
                    }
                } else {
                    ds = ThongKeKhachHangDAO.getKhachHangTheoNam(nam, gioiTinh, luaTuoi);
                    lblKetQua.setText("Tổng số khách hàng đặt trong năm nay: " + ds.size());
                }
            }


            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (Object[] row : ds) {
                KhachHang kh = (KhachHang) row[0];
                Object value = row[1];

                String display = "";
                if (value instanceof Date) {
                    display = sdf.format((Date) value);
                } else if (value instanceof Number) { 
                    display = ((Number) value).intValue() + " lần";
                }

                model.addRow(new Object[]{
                        kh.getMaKH(),
                        kh.getHoTen(),
                        kh.getCCCD(),
                        kh.getTuoi(),
                        kh.getSDT(),
                        kh.getGioiTinh(),
                        display
                });
            }

            if (ds.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu phù hợp!");
                lblKetQua.setText("Không có dữ liệu");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thống kê: " + ex.getMessage());
        }
    }

    private void lamMoi() {
        model.setRowCount(0);
        lblKetQua.setText(" ");
        dateNgayBD.setDate(null);
        dateNgayKT.setDate(null);
        txtThang.setText("");
        txtNam.setText("");
        cmbGioiTinh.setSelectedIndex(0);
        cmbLuaTuoi.setSelectedIndex(0);
        cmbSoLuong.setSelectedIndex(0);
        cmbThoiGian.setSelectedIndex(0);
        updateTimeFields();
        docDuLieuDB();
    }
}
