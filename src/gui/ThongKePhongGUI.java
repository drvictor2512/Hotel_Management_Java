package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import dao.ThongKePhongDAO;
import entity.NhanVien;

public class ThongKePhongGUI extends MenuGUI implements ActionListener {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnThongKe, btnLamMoi;
    private JComboBox<String> cboTrangThai, cboThoiGian, cboLoaiPhong;
    private JDateChooser dateNgayBD, dateNgayKT;
    private JLabel lblTongSoPhong, lblKetQua;
    private JTextField txtThang, txtNam;
    private DecimalFormat df = new DecimalFormat("#,###");

    private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
    private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
    private static final Color HEADER_COLOR = Color.decode("#34495E");
    private static final Color PRIMARY_COLOR = Color.decode("#3498DB");
    private static final Color DANGER_COLOR = Color.decode("#E74C3C"); // Dùng cho kết quả

    public ThongKePhongGUI(NhanVien nv) {
        super("Thống kê phòng", nv);

        JPanel pnlContent = new JPanel(new BorderLayout(10, 10));
        pnlContent.setBackground(BACKGROUND_COLOR);
        pnlContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlLeft = createLeftPanel();

      
        JPanel pnlRight = createRightPanel();

        pnlContent.add(pnlLeft, BorderLayout.WEST);
        pnlContent.add(pnlRight, BorderLayout.CENTER);

        add(pnlContent, BorderLayout.CENTER);
        
        // Load dữ liệu ban đầu
        docDuLieuDB();
        updateTimeFields(); // Cập nhật trạng thái form theo mặc định
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

        //  PHẦN TIÊU ĐỀ 
        JLabel lblTitle = new JLabel("Bộ lọc thống kê phòng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(HEADER_COLOR);
        
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.setBackground(PANEL_COLOR);
        pnlTitle.add(lblTitle);

        // PHẦN FORM 
        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS)); 
        pnlForm.setBackground(PANEL_COLOR);
        pnlForm.setBorder(new EmptyBorder(15, 0, 0, 0));

        // 1. Loại thống kê 
        String[] trangThai = {"Tất cả phòng", "Phòng đặt nhiều nhất", "Phòng đặt ít nhất"};
        pnlForm.add(createLabel("Loại thống kê:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));
        cboTrangThai = createStyledComboBox(trangThai);
        cboTrangThai.addActionListener(this);
        pnlForm.add(cboTrangThai);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        // 2. Loại phòng
        String[] loaiPhong = {"Tất cả", "Standard", "Superior", "VIP"}; 
        pnlForm.add(createLabel("Loại phòng:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));
        cboLoaiPhong = createStyledComboBox(loaiPhong);
        pnlForm.add(cboLoaiPhong);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        // 3. Thời gian thống kê
        String[] thoiGian = {"Thống kê theo ngày", "Thống kê theo tháng", "Thống kê theo năm"};
        pnlForm.add(createLabel("Theo thời gian:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));
        cboThoiGian = createStyledComboBox(thoiGian);
        cboThoiGian.addActionListener(this);
        pnlForm.add(cboThoiGian);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 20)));

        // Các ô nhập liệu thời gian (Ngày BD, KT, Tháng, Năm)
        pnlForm.add(createLabel("Ngày bắt đầu:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));
        dateNgayBD = createStyledDateChooser();
        pnlForm.add(dateNgayBD);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        pnlForm.add(createLabel("Ngày kết thúc:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));
        dateNgayKT = createStyledDateChooser();
        pnlForm.add(dateNgayKT);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        pnlForm.add(createLabel("Tháng (1-12):"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));
        txtThang = createStyledTextField();
        pnlForm.add(txtThang);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 15)));

        pnlForm.add(createLabel("Năm:"));
        pnlForm.add(Box.createRigidArea(new Dimension(0, 8)));
        txtNam = createStyledTextField();
        pnlForm.add(txtNam);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 25)));

        // PHẦN NÚT BẤM
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

        //SẮP XẾP VÀO PANEL CHÍNH
        pnlLeft.add(pnlTitle, BorderLayout.NORTH);  // Tiêu đề ở trên cùng
        pnlLeft.add(pnlForm, BorderLayout.CENTER); // Form ở ngay bên dưới tiêu đề

        return pnlLeft;
    }
    private JPanel createRightPanel() {
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10)); 
        pnlRight.setBackground(BACKGROUND_COLOR);

        // Title
        JLabel lblTitle = new JLabel("KẾT QUẢ THỐNG KÊ TẦN SUẤT ĐẶT PHÒNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(HEADER_COLOR);
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Table
        String[] columnNames = {"Mã phòng", "Tên phòng", "Loại phòng", "Tầng", "Giá", "Số lần đặt"};
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

        // Panel thống kê tóm tắt 
        JPanel pnlThongKe = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlThongKe.setBackground(PANEL_COLOR);
        pnlThongKe.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Tổng số phòng
        JPanel pnlTongPhong = new JPanel(new BorderLayout(10, 0));
        pnlTongPhong.setBackground(Color.WHITE);
        pnlTongPhong.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitleTSP = new JLabel("Tổng số phòng tìm thấy:");
        lblTitleTSP.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        lblTongSoPhong = new JLabel("0");
        lblTongSoPhong.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTongSoPhong.setForeground(PRIMARY_COLOR);
        lblTongSoPhong.setHorizontalAlignment(JLabel.RIGHT);

        pnlTongPhong.add(lblTitleTSP, BorderLayout.WEST);
        pnlTongPhong.add(lblTongSoPhong, BorderLayout.EAST);

        // Kết quả 
        JPanel pnlKetQua = new JPanel(new BorderLayout(10, 0));
        pnlKetQua.setBackground(Color.WHITE);
        pnlKetQua.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DANGER_COLOR, 2),
            new EmptyBorder(15, 20, 15, 20)
        ));

        lblKetQua = new JLabel("Kết quả:");
        lblKetQua.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblKetQua.setForeground(DANGER_COLOR);
        lblKetQua.setHorizontalAlignment(JLabel.CENTER);

        pnlKetQua.add(lblKetQua, BorderLayout.CENTER);

        pnlThongKe.add(pnlTongPhong);
        pnlThongKe.add(pnlKetQua);

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
    
    private JDateChooser createStyledDateChooser() {
    	JDateChooser dc = new JDateChooser();
    	dc.setDateFormatString("dd/MM/yyyy");
    	dc.setPreferredSize(new Dimension(280, 40));
    	dc.setMaximumSize(new Dimension(280, 40));
    	dc.setAlignmentX(Component.LEFT_ALIGNMENT);
    	dc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    	return dc;
    }
    
    private JTextField createStyledTextField() {
    	JTextField tf = new JTextField();
    	tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    	tf.setPreferredSize(new Dimension(280, 40));
    	tf.setMaximumSize(new Dimension(280, 40));
    	tf.setAlignmentX(Component.LEFT_ALIGNMENT);
    	return tf;
    }
    
    private JComboBox<String> createStyledComboBox(String[] items) {
    	JComboBox<String> cbo = new JComboBox<>(items);
    	cbo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    	cbo.setPreferredSize(new Dimension(280, 40));
    	cbo.setMaximumSize(new Dimension(280, 40));
    	cbo.setAlignmentX(Component.LEFT_ALIGNMENT);
    	return cbo;
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
    
    
    private void docDuLieuDB() {
        model.setRowCount(0);
        ArrayList<Object[]> ds = ThongKePhongDAO.layDanhSachPhong();   
        int totalRooms = 0; 
        int totalDat = 0; 
        
        if (ds != null) {
            for (Object[] row : ds) {
                // Tính tổng số lần đặt
                if (row[5] instanceof Integer) {
                     totalDat += (int) row[5]; 
                }
                
                model.addRow(new Object[]{
                    row[0],
                    row[1],
                    row[2],
                    row[3],
                    df.format((Double) row[4]),
                    row[5]
                });
            }
            totalRooms = ds.size();
            
           
            lblKetQua.setText("Tổng số lần đặt: " + totalDat + " lần"); 

        } else {
            lblKetQua.setText("Không tìm thấy kết quả phù hợp!");
        }
        
        lblTongSoPhong.setText(String.valueOf(totalRooms)); // Cập nhật tổng số phòng
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        if (o == cboTrangThai) {
            String trangThai = (String) cboTrangThai.getSelectedItem();
            if (trangThai.contains("nhiều") || trangThai.contains("ít")) {
                // Khi chọn thống kê theo tần suất, vô hiệu hóa tất cả các trường thời gian 
                // vì logic DAO hiện tại không sử dụng chúng.
                cboThoiGian.setEnabled(true); // Vô hiệu hóa CBO thời gian
                dateNgayBD.setEnabled(true);
                dateNgayKT.setEnabled(true);
                txtThang.setEnabled(false);
                txtNam.setEnabled(false);

            } else { // "Tất cả phòng"
                cboThoiGian.setEnabled(true);
                updateTimeFields(); // Cập nhật trạng thái thời gian theo cboThoiGian
            }

        } else if (o == cboThoiGian) {
            // Chỉ cập nhật trạng thái nếu đang ở chế độ "Tất cả phòng"
            if (cboTrangThai.getSelectedItem().toString().equals("Tất cả phòng")) {
                updateTimeFields();
            }
            
        } else if (o == btnThongKe) {
            xuLyThongKe();
            
        } else if (o == btnLamMoi) {
            lamMoi();
        }
    }
   
    private void updateTimeFields() {
        int choice = cboThoiGian.getSelectedIndex();
        
        // Reset tất cả
        dateNgayBD.setEnabled(false);
        dateNgayKT.setEnabled(false);
        txtThang.setEnabled(false);
        txtNam.setEnabled(false);
        
        // Bật các trường cần thiết
        if (cboTrangThai.getSelectedItem().toString().equals("Tất cả phòng")) {
            if (choice == 0) { // Theo ngày
                dateNgayBD.setEnabled(true);
                dateNgayKT.setEnabled(true);
            } else if (choice == 1) { // Theo tháng
                txtThang.setEnabled(true);
                txtNam.setEnabled(true);
            } else if (choice == 2) { // Theo năm
                txtNam.setEnabled(true);
            }
        }
    }
    
    

    private void xuLyThongKe() {
        String loai = (String) cboLoaiPhong.getSelectedItem();
        String loaiThongKe = (String) cboTrangThai.getSelectedItem();
        ArrayList<Object[]> ds = null;

        // Luôn reset kết quả 
        lblKetQua.setText("Kết quả:"); 

        try {

            // 1. Thống kê theo tần suất (nhiều nhất/ít nhất)
            if ("Phòng đặt nhiều nhất".equals(loaiThongKe)) {
                ds = ThongKePhongDAO.thongKePhongDatNhieuNhat(loai);
                if (ds != null && !ds.isEmpty()) {
                    Object[] row = ds.get(0);
                    String tenPhong = (String) row[1];
                    int soLan = (int) row[5];
                    lblKetQua.setText("Phòng đặt nhiều nhất: " + tenPhong + " (" + soLan + " lần)");
                } else {
                    lblKetQua.setText("Phòng đặt nhiều nhất: Không có dữ liệu");
                }

            } else if ("Phòng đặt ít nhất".equals(loaiThongKe)) {
                ds = ThongKePhongDAO.thongKePhongDatItNhat(loai);
                if (ds != null && !ds.isEmpty()) {
                    int soLan = (int) ds.get(0)[5]; // số lần nhỏ nhất
                    lblKetQua.setText("Tổng số lần đặt: " + soLan + " lần");
                } else {
                    lblKetQua.setText("Phòng đặt ít nhất: Không có dữ liệu");
                }

            } else {
                // 2. Thống kê theo thời gian (Tất cả phòng)
                int choice = cboThoiGian.getSelectedIndex();

                if (choice == 0) { // Theo ngày
                    if (dateNgayBD.getDate() == null || dateNgayKT.getDate() == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc!");
                        return;
                    }

                    java.sql.Date bd = new java.sql.Date(dateNgayBD.getDate().getTime());
                    java.sql.Date kt = new java.sql.Date(dateNgayKT.getDate().getTime());

                    if (bd.after(kt)) {
                        JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc!");
                        return;
                    }

                    // Gọi DAO cho thống kê theo ngày
                    ds = ThongKePhongDAO.thongKeTheoNgay(bd, kt, loai);

                } else if (choice == 1) { // Theo tháng
                    String thangStr = txtThang.getText().trim();
                    String namStr = txtNam.getText().trim();

                    if (thangStr.isEmpty() || namStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tháng và năm!");
                        return;
                    }

                    int thang = Integer.parseInt(thangStr);
                    int nam = Integer.parseInt(namStr);

                    if (thang < 1 || thang > 12) {
                        JOptionPane.showMessageDialog(this, "Tháng phải từ 1 đến 12!");
                        return;
                    }
                    // Gọi DAO cho thống kê theo tháng
                    ds = ThongKePhongDAO.thongKeTheoThang(nam, thang, loai);

                } else if (choice == 2) { // Theo năm
                    String namStr = txtNam.getText().trim();

                    if (namStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Vui lòng nhập năm!");
                        return;
                    }

                    int nam = Integer.parseInt(namStr);
                    // Gọi DAO cho thống kê theo năm
                    ds = ThongKePhongDAO.thongKeTheoNam(nam, loai);
                }
            }

            // Cập nhật bảng và Footer
            model.setRowCount(0);
            int totalRooms = 0;
            int totalDat = 0; // Biến mới: Tổng số lần đặt
            
            if (ds != null && !ds.isEmpty()) {
                for (Object[] row : ds) {
                    // Tính tổng số lần đặt
                    totalDat += (int) row[5]; 
                    
                    model.addRow(new Object[]{
                        row[0],
                        row[1],
                        row[2],
                        row[3],
                        df.format((Double) row[4]),
                        row[5]
                    });
                }
                totalRooms = ds.size();
                
                // CẬP NHẬT KẾT QUẢ NỔI BẬT CHO TRƯỜNG HỢP "TẤT CẢ PHÒNG"
                if ("Tất cả phòng".equals(loaiThongKe)) {
                     String thoiGian = (String) cboThoiGian.getSelectedItem();
                     String tenThoiGian = thoiGian.substring(thoiGian.lastIndexOf(" ") + 1); // Lấy chữ "ngày", "tháng" hoặc "năm"
                     String ketQua = String.format("Tổng số lần đặt phòng theo %s: %d lần", 
                                                 tenThoiGian, 
                                                 totalDat);
                     lblKetQua.setText(ketQua);
                }

            } else {
                lblKetQua.setText("Không tìm thấy kết quả phù hợp!");
            }
            lblTongSoPhong.setText(String.valueOf(totalRooms));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ cho tháng/năm!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thống kê: " + ex.getMessage(), "Lỗi Hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }


    
    private void lamMoi() {
        cboTrangThai.setSelectedIndex(0);
        cboLoaiPhong.setSelectedIndex(0);
        cboThoiGian.setSelectedIndex(0);
        dateNgayBD.setDate(null);
        dateNgayKT.setDate(null);
        txtThang.setText("");
        txtNam.setText("");
        
        // Đảm bảo các trường được bật/tắt đúng theo mặc định ("Thống kê theo ngày")
        updateTimeFields(); 
        docDuLieuDB();
    }
}