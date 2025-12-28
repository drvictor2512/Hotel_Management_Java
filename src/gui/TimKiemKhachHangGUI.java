package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import connectDB.ConnectDB;
import entity.NhanVien;

public class TimKiemKhachHangGUI extends MenuGUI {
    private JTextField txtMaKhachHang;
    private JTextField txtHoTen;
    private DefaultTableModel tableModel;
    private JTable table;
    
    private static final Color BACKGROUND_COLOR = Color.decode("#E8EAF6");
    private static final Color BUTTON_COLOR = Color.decode("#3A73A2");
    private static final Color HEADER_COLOR = Color.decode("#34495E");
    
    public TimKiemKhachHangGUI(NhanVien nv) {
        super("Tìm kiếm khách hàng", nv);

        // ==== Main Panel ====
        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(BACKGROUND_COLOR);

        // ==== Panel Top - Tìm kiếm ====
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Tiêu đề
        JLabel lblTitle = new JLabel("TÌM KIẾM KHÁCH HÀNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(HEADER_COLOR);
        lblTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        pnlTop.add(lblTitle);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel nhập liệu
        JPanel pnlInput = new JPanel();
        pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.X_AXIS));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        pnlInput.setMaximumSize(new Dimension(600, 40));
        
        JLabel lblMaKH = new JLabel("Mã khách hàng:");
        lblMaKH.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblMaKH.setPreferredSize(new Dimension(120, 35));
        
        txtMaKhachHang = new JTextField();
        txtMaKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMaKhachHang.setPreferredSize(new Dimension(300, 35));
        
        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblHoTen.setPreferredSize(new Dimension(120, 35));
        
        txtHoTen = new JTextField();
        txtHoTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtHoTen.setPreferredSize(new Dimension(300, 35));
        
        pnlInput.add(Box.createHorizontalGlue());
        pnlInput.add(lblMaKH);
        pnlInput.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlInput.add(txtMaKhachHang);
        pnlInput.add(Box.createRigidArea(new Dimension(20, 0)));
        pnlInput.add(lblHoTen);
        pnlInput.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlInput.add(txtHoTen);
        pnlInput.add(Box.createHorizontalGlue());
        
        pnlTop.add(pnlInput);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel buttons
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        pnlButtons.setMaximumSize(new Dimension(400, 50));
        

		JButton btnSearch = createStyledButton("Tìm kiếm", Color.decode("#2980B9"));
		JButton btnReload = createStyledButton("Tải lại", Color.decode("#27AE60"));
		
        
        btnSearch.addActionListener(e -> timKiemKhachHang());
        btnReload.addActionListener(e -> taiLaiDuLieu());
        
        pnlButtons.add(Box.createHorizontalGlue());
        pnlButtons.add(btnSearch);
        pnlButtons.add(Box.createRigidArea(new Dimension(15, 0)));
        pnlButtons.add(btnReload);
        pnlButtons.add(Box.createHorizontalGlue());
        
        pnlTop.add(pnlButtons);
        
        // ==== Bảng kết quả ====
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(BACKGROUND_COLOR);
        pnlTable.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        String[] col = { "Mã khách hàng", "Họ tên", "CCCD", "Giới tính" };
        tableModel = new DefaultTableModel(col, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setSelectionBackground(Color.decode("#3498DB"));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(Color.decode("#BDC3C7"));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Style cho header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        // Căn giữa tất cả các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < col.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setBorder(null);
        scrollpane.getViewport().setBackground(Color.WHITE);
        
        pnlTable.add(scrollpane, BorderLayout.CENTER);
        
        // Thêm các panel vào pnlMain
        pnlMain.add(pnlTop, BorderLayout.NORTH);
        pnlMain.add(pnlTable, BorderLayout.CENTER);
        
        add(pnlMain, BorderLayout.CENTER);
        
        DocDuLieuDB();
        setVisible(true);
    }
    
    // Tạo button với style đẹp
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
	
    
    // Hàm tìm kiếm khách hàng
    private void timKiemKhachHang() {
        String maKH = txtMaKhachHang.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        
        if (!maKH.isEmpty() && !maKH.matches("^KH\\d{3}$")) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng phải là KHxxx", "Lỗi nhập", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "SELECT maKH, hoTen, CCCD, gioiTinh FROM KhachHang WHERE 1=1";
            if (!maKH.isEmpty()) {
                sql += " AND maKH LIKE ?";
            }
            if (!hoTen.isEmpty()) {
                sql += " AND hoTen LIKE ?";
            }
            
            PreparedStatement stmt = con.prepareStatement(sql);
            int index = 1;
            if (!maKH.isEmpty()) {
                stmt.setString(index++, "%" + maKH + "%");
            }
            if (!hoTen.isEmpty()) {
                stmt.setString(index++, "%" + hoTen + "%");
            }
            
            ResultSet rs = stmt.executeQuery();
            boolean found = false;
            while (rs.next()) {
                String ma = rs.getString("maKH");
                String ten = rs.getString("hoTen");
                String cccd = rs.getString("CCCD");
                String gt = rs.getString("gioiTinh");
                tableModel.addRow(new Object[] { ma, ten, cccd, gt });
                found = true;
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào phù hợp!", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm khách hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // Hàm tải lại dữ liệu từ database
    private void DocDuLieuDB() {
        tableModel.setRowCount(0);
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "SELECT maKH, hoTen, CCCD, gioiTinh FROM KhachHang";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[] { rs.getString("maKH"), rs.getString("hoTen"), rs.getString("CCCD"), rs.getString("gioiTinh") });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu khách hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Hàm tải lại dữ liệu ban đầu
    private void taiLaiDuLieu() {
        txtMaKhachHang.setText("");
        txtHoTen.setText("");
        DocDuLieuDB();
    }
}
