package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*; // Giữ lại cho BufferedReader/Writer nếu cần, nhưng logic file sẽ bị xóa
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import dao.KhuyenMaiDAO;
import dao.NhanVienDAO; // NhanVienDAO không được dùng, nhưng giữ lại phòng trường hợp
import entity.KhuyenMai;
import entity.NhanVien;

public class KhuyenMaiGUI extends MenuGUI implements ActionListener, MouseListener {
    private JTable tblKhuyenMai;
    private DefaultTableModel modelKhuyenMai;

    private JTextField txtMaKM, txtTenKM, txtMucGiam;
    private JComboBox<String> cboDieuKien;
    private JDateChooser dateNgayBD, dateNgayKT;
    private JButton btnThem, btnXoa, btnSua, btnLamMoi; 
    
    private JPanel pnlNoiDung, pnlTrai, pnlGiua;

    private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
    private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
    private static final Color HEADER_COLOR = Color.decode("#34495E");

    private static ArrayList<KhuyenMai> dsKM;

    public KhuyenMaiGUI(NhanVien nv) {
        super("Khuyến mãi", nv);

        //  Main content panel
        pnlNoiDung = new JPanel(new BorderLayout());
        pnlNoiDung.setBackground(BACKGROUND_COLOR);

        //  Left panel: form (BoxLayout Y_AXIS)
        pnlTrai = new JPanel();
        pnlTrai.setLayout(new BoxLayout(pnlTrai, BoxLayout.Y_AXIS));
        pnlTrai.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlTrai.setBackground(PANEL_COLOR);
        pnlTrai.setPreferredSize(new Dimension(360, 0));

        JLabel lblFormTitle = new JLabel("Thông tin khuyến mãi");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(HEADER_COLOR);
        lblFormTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pnlTrai.add(lblFormTitle);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 20)));

        // Mã KM
        addFormField(pnlTrai, "Mã khuyến mãi:");
        txtMaKM = createTextField();
        txtMaKM.setEditable(false);
        txtMaKM.setBackground(Color.decode("#ECF0F1"));
        txtMaKM.setText(KhuyenMaiDAO.getNextKhuyenMaiId());
        pnlTrai.add(txtMaKM);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tên KM
        addFormField(pnlTrai, "Tên khuyến mãi:");
        txtTenKM = createTextField();
        pnlTrai.add(txtTenKM);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Mức giảm
        addFormField(pnlTrai, "Mức giảm (%):");
        txtMucGiam = createTextField();
        pnlTrai.add(txtMucGiam);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

        // Điều kiện
        addFormField(pnlTrai, "Điều kiện áp dụng:");
        String[] dk = {"Khách thường xuyên", "Trẻ em", "Ngày lễ", "Mã thưởng"};
        cboDieuKien = new JComboBox<>(dk);
        cboDieuKien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboDieuKien.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cboDieuKien.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        cboDieuKien.setSelectedIndex(-1);
        pnlTrai.add(cboDieuKien);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));

     
        // Ngày bắt đầu
        addFormField(pnlTrai, "Ngày bắt đầu:");

        dateNgayBD = new JDateChooser();
        dateNgayBD.setDateFormatString("dd/MM/yyyy");

        // Ép kích thước cho toàn bộ JDateChooser
        Dimension dateSize = new Dimension(Integer.MAX_VALUE, 35); 
        dateNgayBD.setPreferredSize(dateSize);
        dateNgayBD.setMaximumSize(dateSize);
        dateNgayBD.setMinimumSize(new Dimension(100, 35)); // Giữ chiều cao

        // Lấy ra JTextField bên trong để chỉnh luôn
        JTextField txtDateBD = (JTextField) dateNgayBD.getDateEditor().getUiComponent();
        txtDateBD.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        dateNgayBD.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlTrai.add(dateNgayBD);
        pnlTrai.add(Box.createRigidArea(new Dimension(0, 15)));


        // Ngày kết thúc
        addFormField(pnlTrai, "Ngày kết thúc:");

        dateNgayKT = new JDateChooser();
        dateNgayKT.setDateFormatString("dd/MM/yyyy");

        // Ép kích thước giống như trên
        dateNgayKT.setPreferredSize(dateSize);
        dateNgayKT.setMaximumSize(dateSize);
        dateNgayKT.setMinimumSize(new Dimension(100, 35)); // Giữ chiều cao

        // Lấy JTextField con và chỉnh
        JTextField txtDateKT = (JTextField) dateNgayKT.getDateEditor().getUiComponent();
        txtDateKT.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        dateNgayKT.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlTrai.add(dateNgayKT);
        pnlTrai.add(Box.createVerticalGlue());

        // Buttons panel - 2 rows 
        JPanel pnlNut = new JPanel();
        pnlNut.setLayout(new BoxLayout(pnlNut, BoxLayout.Y_AXIS));
        pnlNut.setBackground(PANEL_COLOR);
        pnlNut.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        pnlNut.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Row 1: Thêm, Sửa
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

        // Row 2: Xóa, Khôi Phục
        JPanel pnlNutHang2 = new JPanel();
        pnlNutHang2.setLayout(new BoxLayout(pnlNutHang2, BoxLayout.X_AXIS));
        pnlNutHang2.setBackground(PANEL_COLOR);
        pnlNutHang2.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        pnlNutHang2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        btnXoa = createStyledButton("Xóa", Color.decode("#E74C3C"));
        btnLamMoi = createStyledButton("Khôi Phục", Color.decode("#F39C12")); // Đổi màu giống NhanVienGUI

        pnlNutHang2.add(btnXoa);
        pnlNutHang2.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlNutHang2.add(btnLamMoi);

        pnlNut.add(pnlNutHang1);
        pnlNut.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlNut.add(pnlNutHang2);

        pnlTrai.add(pnlNut);
        pnlNoiDung.add(pnlTrai, BorderLayout.WEST);

        //  Right panel: table
        pnlGiua = new JPanel(new BorderLayout());
        pnlGiua.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlGiua.setBackground(BACKGROUND_COLOR);

        String[] tieuDeCot = {"Mã KM", "Tên KM", "Mức giảm (%)", "Điều kiện", "Ngày bắt đầu", "Ngày kết thúc"};
        modelKhuyenMai = new DefaultTableModel(tieuDeCot, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tblKhuyenMai = new JTable(modelKhuyenMai);
        tblKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKhuyenMai.setRowHeight(35);
        tblKhuyenMai.setSelectionBackground(Color.decode("#3498DB"));
        tblKhuyenMai.setSelectionForeground(Color.WHITE);
        tblKhuyenMai.setGridColor(Color.decode("#BDC3C7"));
        tblKhuyenMai.setShowGrid(true);
        tblKhuyenMai.setIntercellSpacing(new Dimension(1, 1));
        tblKhuyenMai.addMouseListener(this);

        // Style header
        JTableHeader header = tblKhuyenMai.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Center renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tieuDeCot.length; i++) {
            tblKhuyenMai.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scr = new JScrollPane(tblKhuyenMai);
        scr.setBorder(null);
        scr.getViewport().setBackground(Color.WHITE);

        JLabel lblTieuDe = new JLabel("QUẢN LÝ KHUYẾN MÃI", JLabel.CENTER);
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

        // Main layout
        setLayout(new BorderLayout());
        add(pnlNoiDung, BorderLayout.CENTER);

        // Events
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this); 
        tblKhuyenMai.addMouseListener(this);

        loadDataKhuyenMai();

        setVisible(true);
    }

    //  helper UI methods 
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
            public void mouseEntered(MouseEvent evt) { btn.setBackground(bgColor.brighter()); }
            public void mouseExited(MouseEvent evt) { btn.setBackground(bgColor); }
        });

        return btn;
    }

    //  Data load 
    private void loadDataKhuyenMai() {
        // DAO sẽ tự động lọc (visible = 1)
        dsKM = (ArrayList<KhuyenMai>) KhuyenMaiDAO.getAllKhuyenMai(); 
        modelKhuyenMai.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (KhuyenMai km : dsKM) {
            
            
            java.util.Date ngayBD = java.sql.Date.valueOf(km.getNgayBatDau());
            java.util.Date ngayKT = java.sql.Date.valueOf(km.getNgayKetThuc());

            modelKhuyenMai.addRow(new Object[]{
                km.getMaKM(),
                km.getTenKM(),
                km.getMucGiamGia(),
                km.getDieuKienApDung(),
                sdf.format(ngayBD),
                sdf.format(ngayKT)
            });
        }
    }

    // ===== CRUD handlers (giữ logic giống code gốc) =====
    private String phatSinhMaKM() {
        return KhuyenMaiDAO.getNextKhuyenMaiId();
    }

    private void themKM() {
        try {
            String ma = phatSinhMaKM();
            String ten = txtTenKM.getText().trim();
            String mucGiamStr = txtMucGiam.getText().trim();
            String dieuKien = (String) cboDieuKien.getSelectedItem();

            LocalDate ngayBD = null, ngayKT = null;
            if (dateNgayBD.getDate() != null)
                ngayBD = dateNgayBD.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (dateNgayKT.getDate() != null)
                ngayKT = dateNgayKT.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Kiểm tra rỗng
            if (ten.isEmpty() || mucGiamStr.isEmpty() || dieuKien == null || ngayBD == null || ngayKT == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Regex kiểm tra hợp lệ
            if (!ten.matches("^[\\p{L}0-9\\s]+$")) {
                JOptionPane.showMessageDialog(this, "Tên khuyến mãi chỉ được chứa chữ, số và dấu cách!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

           
            double mucGiam = Double.parseDouble(mucGiamStr);
            if (mucGiam <= 0 || mucGiam > 100) {
                JOptionPane.showMessageDialog(this, "Mức giảm giá phải trong khoảng (0,100]!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            

            //double mucGiam = Double.parseDouble(mucGiamStr);

            if (ngayBD.isAfter(ngayKT)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước hoặc bằng ngày kết thúc!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Constructor 7 tham số (mặc định visible=true)
            KhuyenMai km = new KhuyenMai(ma, ten, mucGiam, dieuKien, ngayBD, ngayKT, true);
            boolean ok = KhuyenMaiDAO.addKhuyenMai(km);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!");
                loadDataKhuyenMai();
                txtMaKM.setText(phatSinhMaKM());
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mức giảm giá phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaKM() {
        int row = tblKhuyenMai.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần sửa!");
            return;
        }

        try {
            String ma = txtMaKM.getText().trim();
            String ten = txtTenKM.getText().trim();
            String mucGiamStr = txtMucGiam.getText().trim();
            String dieuKien = (String) cboDieuKien.getSelectedItem();

            if (ten.isEmpty() || mucGiamStr.isEmpty() || dieuKien == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (dateNgayBD.getDate() == null || dateNgayKT.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu và kết thúc!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }


            double mucGiam = Double.parseDouble(mucGiamStr);
            if (mucGiam <= 0 || mucGiam > 100) {
                JOptionPane.showMessageDialog(this, "Mức giảm giá phải trong khoảng (0,100]!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate ngayBD = dateNgayBD.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ngayKT = dateNgayKT.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (ngayBD.isAfter(ngayKT)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước hoặc bằng ngày kết thúc!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            KhuyenMai km = new KhuyenMai(ma, ten, mucGiam, dieuKien, ngayBD, ngayKT, true); // Sửa: Dùng constructor 7 tham số
            boolean ok = KhuyenMaiDAO.updateKhuyenMai(km);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadDataKhuyenMai();
                clearForm();
                txtMaKM.setText(phatSinhMaKM());
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mức giảm giá phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void xoaKhuyenMai() {
        int selectedRow = tblKhuyenMai.getSelectedRow();
        if (selectedRow >= 0) {
            String maChon = modelKhuyenMai.getValueAt(selectedRow, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khuyến mãi", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (KhuyenMaiDAO.hideKhuyenMai(maChon)) {
                    JOptionPane.showMessageDialog(this, "Khuyến mãi đã được xóa!");
                    loadDataKhuyenMai(); // load lại danh sách
                    clearForm();
                    txtMaKM.setText(phatSinhMaKM());
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần xóa!");
        }
    }


    private void clearForm() {
        txtMaKM.setText(phatSinhMaKM()); // Luôn đặt lại mã mới
        txtTenKM.setText("");
        txtMucGiam.setText("");
        cboDieuKien.setSelectedIndex(-1);
        dateNgayBD.setDate(null);
        dateNgayKT.setDate(null);
        tblKhuyenMai.clearSelection();
    }
    
    private void KhoiPhucActions() {
        ArrayList<KhuyenMai> dsAn = (ArrayList<KhuyenMai>) KhuyenMaiDAO.getKhuyenMaiAn();
        if (dsAn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có khuyến mãi nào bị ẩn!", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Tạo dialog với table
        JDialog dialogKhoiPhuc = new JDialog(this, "Danh sách khuyến mãi đã xóa", true);
        dialogKhoiPhuc.setLayout(new BorderLayout());
        dialogKhoiPhuc.setSize(900, 500);
        dialogKhoiPhuc.setLocationRelativeTo(this);
        
        // Tiêu đề
        JLabel lblTitle = new JLabel("Chọn khuyến mãi cần khôi phục", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(HEADER_COLOR);
        lblTitle.setBorder(new EmptyBorder(15, 10, 15, 10));
        
        // Tạo table
        String[] columns = {"Mã KM", "Tên KM", "Mức giảm (%)", "Điều kiện", "Ngày bắt đầu", "Ngày kết thúc"};
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
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        // Thêm dữ liệu
        for (KhuyenMai km : dsAn) {
            java.util.Date ngayBD = java.sql.Date.valueOf(km.getNgayBatDau());
            java.util.Date ngayKT = java.sql.Date.valueOf(km.getNgayKetThuc());

            modelRestore.addRow(new Object[]{
                km.getMaKM(),
                km.getTenKM(),
                km.getMucGiamGia(),
                km.getDieuKienApDung(),
                sdf.format(ngayBD),
                sdf.format(ngayKT)
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
                if (KhuyenMaiDAO.restoreKhuyenMai(maChon)) {
                    JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thành công!");
                    loadDataKhuyenMai();
                    clearForm();
                    dialogKhoiPhuc.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialogKhoiPhuc, "Khôi phục thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(dialogKhoiPhuc, "Vui lòng chọn khuyến mãi cần khôi phục!");
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
        if (o == btnThem) themKM();
        else if (o == btnSua) suaKM();
        else if (o == btnXoa) xoaKhuyenMai();
        else if (o == btnLamMoi) KhoiPhucActions(); 
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tblKhuyenMai.getSelectedRow();
        if (row == -1) return;

        
        if (e.getClickCount() == 2 && row != -1) {
            clearForm();
            return;
        }

        txtMaKM.setText(tblKhuyenMai.getValueAt(row, 0).toString());
        txtTenKM.setText(tblKhuyenMai.getValueAt(row, 1).toString());
        txtMucGiam.setText(tblKhuyenMai.getValueAt(row, 2).toString());
        cboDieuKien.setSelectedItem(tblKhuyenMai.getValueAt(row, 3).toString());

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String strNgayBD = tblKhuyenMai.getValueAt(row, 4).toString();
            String strNgayKT = tblKhuyenMai.getValueAt(row, 5).toString();
            LocalDate ngayBD = LocalDate.parse(strNgayBD, formatter);
            LocalDate ngayKT = LocalDate.parse(strNgayKT, formatter);
            dateNgayBD.setDate(java.sql.Date.valueOf(ngayBD));
            dateNgayKT.setDate(java.sql.Date.valueOf(ngayKT));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}