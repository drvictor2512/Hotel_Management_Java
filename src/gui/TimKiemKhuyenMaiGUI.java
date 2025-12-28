package gui;

import java.awt.*;
import java.awt.event.*;
// [ĐÃ XÓA] java.io.*
import java.sql.Date; // [SỬA LẠI] Chỉ cần import Date
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List; // [THÊM] Import List

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

// [ĐÃ XÓA] connectDB.ConnectDB;
// [THÊM] Import DAO và Entity
import dao.KhuyenMaiDAO;
import entity.KhuyenMai;
import entity.NhanVien;


public class TimKiemKhuyenMaiGUI extends MenuGUI {
    private JTextField txtMaKM;
    private JTextField txtTenKM;
    private DefaultTableModel tableModel;
    private JTable table;
    
    // [ĐÃ XÓA] dsXoa và FILE_XOA

    // Định dạng ngày
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // Định nghĩa màu sắc (Hex) - Lấy từ chuẩn
    private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
    private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
    private static final Color HEADER_COLOR = Color.decode("#34495E");

    public TimKiemKhuyenMaiGUI(NhanVien nv) {
        super("Tìm kiếm khuyến mãi", nv);

        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(BACKGROUND_COLOR);

        // ==== Panel Top - Tìm kiếm (Giống chuẩn) ====
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(PANEL_COLOR);
        pnlTop.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Tiêu đề
        JLabel lblTitle = new JLabel("TÌM KIẾM KHUYẾN MÃI", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(HEADER_COLOR);
        lblTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        pnlTop.add(lblTitle);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 30)));

        // Panel nhập liệu (Sửa lại cho 2 trường)
        JPanel pnlInput = new JPanel();
        pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.X_AXIS));
        pnlInput.setBackground(PANEL_COLOR);
        pnlInput.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        pnlInput.setMaximumSize(new Dimension(700, 40)); // Tăng kích thước

        JLabel lblMaKM = new JLabel("Mã KM:");
        lblMaKM.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblMaKM.setPreferredSize(new Dimension(80, 35));

        txtMaKM = new JTextField();
        txtMaKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMaKM.setPreferredSize(new Dimension(200, 35));
        txtMaKM.setMaximumSize(new Dimension(200, 35));

        JLabel lblTenKM = new JLabel("Tên KM:");
        lblTenKM.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblTenKM.setPreferredSize(new Dimension(80, 35));

        txtTenKM = new JTextField();
        txtTenKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTenKM.setPreferredSize(new Dimension(200, 35));
        txtTenKM.setMaximumSize(new Dimension(200, 35));

        pnlInput.add(Box.createHorizontalGlue());
        pnlInput.add(lblMaKM);
        pnlInput.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlInput.add(txtMaKM);
        pnlInput.add(Box.createRigidArea(new Dimension(20, 0))); // Thêm khoảng cách
        pnlInput.add(lblTenKM);
        pnlInput.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlInput.add(txtTenKM);
        pnlInput.add(Box.createHorizontalGlue());

        pnlTop.add(pnlInput);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel buttons (Giống chuẩn)
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
        pnlButtons.setBackground(PANEL_COLOR);
        pnlButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        pnlButtons.setMaximumSize(new Dimension(400, 50));

        JButton btnSearch = createStyledButton("Tìm kiếm", Color.decode("#2980B9"));
        JButton btnReload = createStyledButton("Tải lại", Color.decode("#27AE60"));

        // Dùng ActionListener nặc danh (Giống chuẩn)
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timKiemKhuyenMai(); // [SỬA LẠI] Logic dùng DAO
            }
        });

        btnReload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DocDuLieuDB(); // [SỬA LẠI] Đổi tên hàm
            }
        });

        pnlButtons.add(Box.createHorizontalGlue());
        pnlButtons.add(btnSearch);
        pnlButtons.add(Box.createRigidArea(new Dimension(15, 0)));
        pnlButtons.add(btnReload);
        pnlButtons.add(Box.createHorizontalGlue());

        pnlTop.add(pnlButtons);

        // ==== Bảng kết quả (Giống chuẩn) ====
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(BACKGROUND_COLOR);
        pnlTable.setBorder(new EmptyBorder(10, 20, 20, 20));

        String[] col = {"Mã KM", "Tên khuyến mãi", "Mức giảm (%)", "Điều kiện áp dụng", "Ngày bắt đầu", "Ngày kết thúc"};
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

        // Style cho header (Giống chuẩn)
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        // Căn giữa tất cả các cột (Giống chuẩn)
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

        // Thêm các panel chính vào pnlMain
        pnlMain.add(pnlTop, BorderLayout.NORTH);
        pnlMain.add(pnlTable, BorderLayout.CENTER);

        // Add pnlMain vào frame
        add(pnlMain, BorderLayout.CENTER);

        // LOAD DỮ LIỆU BAN ĐẦU
        DocDuLieuDB();

        setVisible(true);
    }

    // Tạo button với style đẹp (Lấy từ chuẩn)
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

    // [ĐÃ XÓA] Hàm docDsXoaTamTuFile()

    // HÀM TÌM KIẾM KHUYẾN MÃI (SỬA LẠI - DÙNG DAO)
    private void timKiemKhuyenMai() {
        // [ĐÃ XÓA] docDsXoaTamTuFile();
        String maKM = txtMaKM.getText().trim();
        String tenKM = txtTenKM.getText().trim();
        tableModel.setRowCount(0); // Xóa dữ liệu cũ

        // KIỂM TRA HỢP LỆ TRƯỚC KHI TÌM
        if (!maKM.isEmpty() && !maKM.matches("^KM\\d{3}$")) {
            JOptionPane.showMessageDialog(this,
                    "Mã khuyến mãi phải có dạng KM + 3 chữ số (VD: KM001, KM123)",
                    "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!tenKM.isEmpty() && !tenKM.matches("^[\\p{L}0-9\\s]+$")) {
            JOptionPane.showMessageDialog(this,
                    "Tên khuyến mãi chỉ được chứa chữ, số và dấu cách!",
                    "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Logic tìm kiếm bằng DAO
        try {
            boolean found = false;
            if (!maKM.isEmpty()) {
                // Tìm theo Mã
                KhuyenMai km = KhuyenMaiDAO.getKhuyenMaiById(maKM);
                if (km != null && km.isVisible()) { // Kiểm tra nếu còn hiển thị
                    addRowToTable(km);
                    found = true;
                }
            } else if (!tenKM.isEmpty()) {
                // Tìm theo Tên
                List<KhuyenMai> ds = KhuyenMaiDAO.findKhuyenMaiByName(tenKM);
                for (KhuyenMai km : ds) {
                    if (km.isVisible()) { // Chỉ hiển thị KM còn hoạt động
                        addRowToTable(km);
                        found = true;
                    }
                }
            } else {
                // Nếu không nhập gì, tải lại toàn bộ (hoặc báo lỗi)
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã hoặc Tên để tìm kiếm.");
                return;
            }

            if (!found) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy khuyến mãi nào phù hợp!",
                        "Kết quả", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tìm kiếm khuyến mãi: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // HÀM TẢI LẠI TOÀN BỘ DỮ LIỆU (SỬA LẠI - DÙNG DAO)
    private void DocDuLieuDB() {
        // [ĐÃ XÓA] docDsXoaTamTuFile();
        tableModel.setRowCount(0);
        txtMaKM.setText("");
        txtTenKM.setText("");

        try {
            // DAO.getAllKhuyenMai() đã tự lọc visible = 1
            ArrayList<KhuyenMai> ds = KhuyenMaiDAO.getAllKhuyenMai(); 
            for (KhuyenMai km : ds) {
                // [ĐÃ XÓA] Lọc bằng dsXoa
                addRowToTable(km);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải lại dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // HÀM HỖ TRỢ THÊM DÒNG VÀO BẢNG
    private void addRowToTable(KhuyenMai km) {
        String ngayBD = km.getNgayBatDau() != null ? sdf.format(Date.valueOf(km.getNgayBatDau())) : "";
        String ngayKT = km.getNgayKetThuc() != null ? sdf.format(Date.valueOf(km.getNgayKetThuc())) : "";
        
        tableModel.addRow(new Object[]{
            km.getMaKM(),
            km.getTenKM(),
            km.getMucGiamGia(),
            km.getDieuKienApDung(),
            ngayBD,
            ngayKT
        });
    }
}