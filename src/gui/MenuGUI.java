package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import entity.NhanVien;

public class MenuGUI extends JFrame {
	protected NhanVien nv;
    
    public MenuGUI(String title, NhanVien nv) {
    	this.nv = nv;
        setTitle(title);
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar mnu = new JMenuBar();
        mnu.setOpaque(true);
        mnu.setBackground(new Color(3, 52, 110));
        mnu.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JLabel lblBrand = new JLabel("VICTORY");
        lblBrand.setForeground(Color.white);
        lblBrand.setFont(new Font("Roboto", Font.BOLD, 16));
        lblBrand.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        lblBrand.setPreferredSize(new Dimension(100, 30));
        lblBrand.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new TrangChuGUI(nv).setVisible(true);
            }
        });
        mnu.add(lblBrand);
        mnu.add(Box.createHorizontalGlue());
        
        // Main menu icons (giữ nguyên nếu có)
        ImageIcon iconDanhMuc = createIcon("📋", new Color(100, 149, 237));
        ImageIcon iconXuLy = createIcon("⚙", new Color(46, 204, 113));
        ImageIcon iconTimKiem = createIcon("🔍", new Color(241, 196, 15));
        ImageIcon iconThongKe = createIcon("📊", new Color(231, 76, 60));

        String[] mainMenus = {"Danh mục", "Xử lý", "Tìm kiếm", "Thống kê"};
        ImageIcon[] icons = {iconDanhMuc, iconXuLy, iconTimKiem, iconThongKe};
        
        // Submenu data with emoji icons
        String[][][] subMenusWithIcons = {
            // Danh mục
            {
                {"Khách hàng", "👤"},
                {"Phòng", "🏠"},
                {"Loại phòng", "🏘️"},
                {"Nhân viên", "👨‍💼"},
                {"Loại nhân viên", "👥"},
                {"Khuyến mãi", "🎁"},
                {"Dịch vụ", "🛎️"}
            },
            // Xử lý
            {
                {"Đặt phòng", "📝"},
                {"Nhận phòng", "✅"},
                {"Huỷ phòng", "❌"},
                {"Đổi phòng", "🔄"},
                {"Lập hoá đơn", "🧾"}
            },
            // Tìm kiếm
            {
                {"Tìm kiếm khách hàng", "🔍"},
                {"Tìm kiếm phòng", "🔎"},
                {"Tìm kiếm nhân viên", "👓"},
                {"Tìm kiếm khuyến mãi", "🔖"},
                {"Tìm kiếm dịch vụ", "🔦"}
            },
            // Thống kê
            {
                {"Thống kê theo doanh thu", "💰"},
                {"Thống kê theo khách hàng", "📈"},
                {"Thống kê theo phòng", "📉"}
            }
        };
        
        boolean isQuanLy = nv.getLnv().getTenLoaiNV().equalsIgnoreCase("Quản lý");
        
        for (int i = 0; i < mainMenus.length; i++) {
            JMenu mn = new JMenu(mainMenus[i]);
            mn.setForeground(Color.WHITE);
            mn.setFont(new Font("Roboto", Font.PLAIN, 14));
            mn.setPreferredSize(new Dimension(130, 35));
            mn.setIcon(icons[i]);
            mn.setIconTextGap(8);

            // Add submenus with emoji icons
            for (String[] subItem : subMenusWithIcons[i]) {
                String subName = subItem[0];
                String emoji = subItem[1];
                
                JMenuItem mni = new JMenuItem(subName);
                
                // Tạo icon từ emoji
                ImageIcon emojiIcon = createEmojiIcon(emoji, 18);
                mni.setIcon(emojiIcon);
                
                mni.setFont(new Font("Roboto", Font.PLAIN, 13));
                mni.setIconTextGap(8);
                mni.addActionListener(e -> handleMenuClick(subName));
                mn.add(mni);
            }
            
            // Phân quyền
            if(!isQuanLy) {
            	if(mainMenus[i].equals("Danh mục")) {
            		for(Component comp: mn.getMenuComponents()) {
            			JMenuItem mni  = (JMenuItem) comp;
            			if (!mni.getText().equals("Khách hàng") && !mni.getText().equals("Dịch vụ")) {
                            mni.setEnabled(false);
                        }
            		}
            	}
            	else if (mainMenus[i].equals("Thống kê")) {
            		 mn.setEnabled(true);
            		 for(Component comp : mn.getMenuComponents()) {
            			 JMenuItem mni  = (JMenuItem) comp;
            			 mni.setEnabled(false);
            		 }
            	 }
            	else if(mainMenus[i].equals("Tìm kiếm")) {
            		for(Component comp: mn.getMenuComponents()) {
            			JMenuItem mni  = (JMenuItem) comp;
            			if (mni.getText().equals("Tìm kiếm nhân viên")) {
                            mni.setEnabled(false);
                        }
            		}
            	}
            }
            
            mnu.add(mn);
            if (i < mainMenus.length - 1) {
                mnu.add(Box.createHorizontalStrut(10));
            }
        }

        mnu.add(Box.createHorizontalGlue());
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(3, 52, 110));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFont(new Font("Roboto", Font.PLAIN, 14));
        btnLogout.setPreferredSize(new Dimension(110, 35));
        btnLogout.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new DangNhapGUI();
			}
		});
        mnu.add(btnLogout);
        mnu.setPreferredSize(new Dimension(100, 60));
        setJMenuBar(mnu);
    }
    
    // Tạo icon từ emoji
    private ImageIcon createEmojiIcon(String emoji, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Vẽ background tròn để icon dễ nhìn hơn
        g2d.setColor(new Color(52, 152, 219, 180)); // Màu xanh dương nhạt
        g2d.fillRoundRect(2, 2, size-4, size-4, size/2, size/2);
        
        // Set font cho emoji
        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, (int)(size * 0.6));
        g2d.setFont(emojiFont);
        
        // Get font metrics để center text
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(emoji);
        int textHeight = fm.getHeight();
        int x = (size - textWidth) / 2;
        int y = ((size - textHeight) / 2) + fm.getAscent();
        
        // Draw emoji với màu trắng
        g2d.setColor(Color.WHITE);
        g2d.drawString(emoji, x, y);
        g2d.dispose();
        
        return new ImageIcon(image);
    }
    
    // Tạo icon với text (cho main menu)
    private ImageIcon createIcon(String text, Color bgColor) {
        int size = 28;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Vẽ nền tròn
        g2d.setColor(bgColor);
        g2d.fillRoundRect(2, 0, size-4, size-4, 8, 8);
        
        // Vẽ text/emoji
        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 16);
        g2d.setFont(emojiFont);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int x = (size - textWidth) / 2;
        int y = ((size - textHeight) / 2) + fm.getAscent();
        
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
        g2d.dispose();
        
        return new ImageIcon(image);
    }

    protected void handleMenuClick(String sub) {
        dispose();

        switch(sub) {
		case "Khách hàng":
			new KhachHangGUI(nv).setVisible(true);
			break;
		case "Phòng":
            new PhongGUI(nv).setVisible(true);
            break;
		case "Loại phòng":
			new LoaiPhongGUI(nv).setVisible(true);
			break;
        case "Nhân viên":
            new NhanVienGUI(nv).setVisible(true);
            break;
        case "Loại nhân viên":
			new LoaiNhanVienGUI(nv).setVisible(true);
			break;
        case "Khuyến mãi":
            new KhuyenMaiGUI(nv).setVisible(true);
            break;
        case "Dịch vụ":
            new DichVuGUI(nv).setVisible(true);
            break;
        case "Đặt phòng":
            new DatPhongGUI(nv).setVisible(true);
            break;
        case("Nhận phòng"):
        	new NhanPhongGUI(nv).setVisible(true);;
        	break;
        case "Huỷ phòng":
            new HuyPhongGUI(nv).setVisible(true);
            break;
        case "Đổi phòng":
            new DoiPhongGUI(nv).setVisible(true);
            break;
        case "Lập hoá đơn":
            new HoaDonGUI(nv).setVisible(true);
            break;
        case "Thống kê theo doanh thu":
            new ThongKeDoanhThuGUI(nv).setVisible(true);
            break;
        case "Thống kê theo khách hàng":
            new ThongKeKhachHangGUI(nv).setVisible(true);
            break;
        case "Thống kê theo phòng":
            new ThongKePhongGUI(nv).setVisible(true);
            break;
        case "Tìm kiếm phòng":
            new TimKiemPhongGUI(nv).setVisible(true);
            break;
        case "Tìm kiếm khách hàng":
            new TimKiemKhachHangGUI(nv).setVisible(true);
            break;
        case "Tìm kiếm dịch vụ":
            new TimKiemDichVuGUI(nv).setVisible(true);
            break;
        case "Tìm kiếm nhân viên":
            new TimKiemNhanVienGUI(nv).setVisible(true);
            break;
        case "Tìm kiếm khuyến mãi":
            new TimKiemKhuyenMaiGUI(nv).setVisible(true);
            break;
		}
    }
}