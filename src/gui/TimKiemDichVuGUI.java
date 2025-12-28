package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import dao.DichVuDAO;
import entity.DichVu;
import entity.NhanVien;

public class TimKiemDichVuGUI extends MenuGUI {
    private JTextField txtMaDV;
    private JTextField txtTenDV;
    private DefaultTableModel tableModel;
    private JTable table;

    private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
    private static final Color PANEL_COLOR = Color.decode("#F5F7FA");
    private static final Color HEADER_COLOR = Color.decode("#34495E");

    public TimKiemDichVuGUI(NhanVien nv) {
        super("Tìm kiếm dịch vụ", nv);

        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(BACKGROUND_COLOR);

        // PANEL TOP
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(PANEL_COLOR);
        pnlTop.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel lblTitle = new JLabel("TÌM KIẾM DỊCH VỤ", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(HEADER_COLOR);
        lblTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        pnlTop.add(lblTitle);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 30)));

        // PANEL INPUT 
        JPanel pnlInput = new JPanel();
        pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.X_AXIS));
        pnlInput.setBackground(PANEL_COLOR);
        pnlInput.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        pnlInput.setMaximumSize(new Dimension(700, 40));

        JLabel lblMaDV = new JLabel("Mã dịch vụ:");
        lblMaDV.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblMaDV.setPreferredSize(new Dimension(100, 35));

        txtMaDV = new JTextField();
        txtMaDV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMaDV.setPreferredSize(new Dimension(200, 35));
        txtMaDV.setMaximumSize(new Dimension(200, 35));

        JLabel lblTenDV = new JLabel("Tên dịch vụ:");
        lblTenDV.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblTenDV.setPreferredSize(new Dimension(100, 35));

        txtTenDV = new JTextField();
        txtTenDV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTenDV.setPreferredSize(new Dimension(200, 35));
        txtTenDV.setMaximumSize(new Dimension(200, 35));

        pnlInput.add(Box.createHorizontalGlue());
        pnlInput.add(lblMaDV);
        pnlInput.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlInput.add(txtMaDV);
        pnlInput.add(Box.createRigidArea(new Dimension(20, 0)));
        pnlInput.add(lblTenDV);
        pnlInput.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlInput.add(txtTenDV);
        pnlInput.add(Box.createHorizontalGlue());

        pnlTop.add(pnlInput);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 20)));

        // BUTTONS 
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
        pnlButtons.setBackground(PANEL_COLOR);
        pnlButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        pnlButtons.setMaximumSize(new Dimension(400, 50));

        JButton btnSearch = createStyledButton("Tìm kiếm", Color.decode("#2980B9"));
        JButton btnReload = createStyledButton("Tải lại", Color.decode("#27AE60"));

        btnSearch.addActionListener(e -> timKiemDichVu());
        btnReload.addActionListener(e -> DocDuLieuDB());

        pnlButtons.add(Box.createHorizontalGlue());
        pnlButtons.add(btnSearch);
        pnlButtons.add(Box.createRigidArea(new Dimension(15, 0)));
        pnlButtons.add(btnReload);
        pnlButtons.add(Box.createHorizontalGlue());

        pnlTop.add(pnlButtons);

        // TABLE 
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(BACKGROUND_COLOR);
        pnlTable.setBorder(new EmptyBorder(10, 20, 20, 20));

        String[] col = {"Mã dịch vụ", "Tên dịch vụ", "Đơn giá"};
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

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

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

        pnlMain.add(pnlTop, BorderLayout.NORTH);
        pnlMain.add(pnlTable, BorderLayout.CENTER);

        add(pnlMain, BorderLayout.CENTER);
        DocDuLieuDB();
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(140, 45));
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

   // HÀM TÌM KIẾM DỊCH VỤ
    private void timKiemDichVu() {
        String maDV = txtMaDV.getText().trim();
        String tenDV = txtTenDV.getText().trim();
        tableModel.setRowCount(0);

        if (maDV.isEmpty() && tenDV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã hoặc Tên để tìm kiếm.");
            return;
        }

        try {
            ArrayList<DichVu> ds = new DichVuDAO().searchDichVu(maDV, tenDV);

            if (ds.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy dịch vụ nào phù hợp!");
                return;
            }

            for (DichVu dv : ds) {
                addRowToTable(dv);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tìm kiếm dịch vụ: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // HÀM TẢI DỮ LIỆU
    private void DocDuLieuDB() {
        tableModel.setRowCount(0);
        txtMaDV.setText("");
        txtTenDV.setText("");

        try {
            ArrayList<DichVu> ds = DichVuDAO.getAllDichVu();
            for (DichVu dv : ds) {
                addRowToTable(dv);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải lại dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

   // THÊM DỮ LIỆU VÀO BẢNG
    private void addRowToTable(DichVu dv) {
        tableModel.addRow(new Object[]{
            dv.getMaDV(),
            dv.getTenDV(),
            dv.getGiaDV()
        });
    }
}
