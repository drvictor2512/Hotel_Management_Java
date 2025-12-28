package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import entity.NhanVien;

public class TrangChuGUI extends MenuGUI {
	public TrangChuGUI(NhanVien nv) {
		super("Trang chủ", nv);
		
        JPanel pnlCenter = new JPanel();
        JLabel lblTitle = new JLabel(
                "Chào mừng " + nv.getHoTen() + " đến với hệ thống quản lý khách sạn Victory",
                SwingConstants.CENTER
            );
            lblTitle.setFont(new Font("Roboto", Font.BOLD, 22));
            lblTitle.setForeground(new Color(0, 51, 102));
        
        ImageIcon icon = new ImageIcon("src//img//hotel.png");
        Image img = icon.getImage().getScaledInstance(1600, 900, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        JLabel lblIcon = new JLabel(icon);
        pnlCenter.add(lblTitle);
        pnlCenter.add(Box.createVerticalStrut(60));
        pnlCenter.add(lblIcon);
        
        add(pnlCenter, BorderLayout.CENTER);
		setVisible(true);
		
	}

}
