package gui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import dao.NhanVienDAO;
import entity.NhanVien;

public class DangNhapGUI extends JFrame {
	private JTextField txtTaiKhoan;
	private JPasswordField txtMatKhau;
	public DangNhapGUI() {
		setTitle("ĐĂNG NHẬP");
		setSize(600, 530);
		setLocationRelativeTo(null);
		
		JPanel pnlTieuDe = new JPanel();
		JLabel lblTieuDe = new JLabel("Đăng nhập"); 
		lblTieuDe.setFont(new Font("Roboto", Font.BOLD, 30));
		lblTieuDe.setForeground(Color.white);
		pnlTieuDe.add(lblTieuDe);
		pnlTieuDe.setBackground(null);
		pnlTieuDe.setBounds(200, 80, 190, 40);
		
		JLabel lblTaiKhoan = new JLabel("Tài khoản");
		lblTaiKhoan.setFont(new Font("Roboto", Font.BOLD, 20));
		lblTaiKhoan.setForeground(Color.white);
		JLabel lblTaiKhoanText = new JLabel("*");
		lblTaiKhoanText.setFont(new Font("Roboto", Font.BOLD, 20));
		lblTaiKhoanText.setForeground(Color.red);
		lblTaiKhoan.setBounds(130, 210, 100, 30);
		lblTaiKhoanText.setBounds(235, 210, 250, 30);
		JLabel lblImgTaiKhoan = new JLabel();
		lblImgTaiKhoan.setIcon(new ImageIcon("src\\img\\userDangNhap.png"));
		lblImgTaiKhoan.setBounds(76, 235, 50, 50);
		txtTaiKhoan = new JTextField();
		txtTaiKhoan.setBounds(128, 235, 350, 50);
		
		JLabel lblMatKhau = new JLabel("Mật khẩu");
		lblMatKhau.setFont(new Font("Roboto", Font.BOLD, 20));
		lblMatKhau.setForeground(Color.white);
		JLabel lblMatKhauText = new JLabel("*");
		lblMatKhauText.setFont(new Font("Roboto", Font.BOLD, 20));
		lblMatKhauText.setForeground(Color.red);
		lblMatKhau.setBounds(130, 300, 100, 30);
		lblMatKhauText.setBounds(235, 300, 250, 30);
		JLabel lblImgMatKhau = new JLabel();
		lblImgMatKhau.setIcon(new ImageIcon("src\\img\\matKhauDangNhap.png"));
		lblImgMatKhau.setBounds(70, 325, 60, 60);
		txtMatKhau = new JPasswordField();
		txtMatKhau.setBounds(0, 0, 350, 50);
		txtMatKhau.setEchoChar('*');
		txtTaiKhoan.setFont(new Font("Roboto", Font.PLAIN, 20));
		txtMatKhau.setFont(new Font("Roboto", Font.PLAIN, 20));
		
		JButton btnHideAndView = new JButton();
		btnHideAndView.setIcon(new ImageIcon("src\\img\\hide.png"));
		btnHideAndView.setBounds(310, 10, 30, 30);
		btnHideAndView.setContentAreaFilled(false);
		btnHideAndView.setFocusPainted(false);
		btnHideAndView.setBorder(BorderFactory.createEmptyBorder());
		btnHideAndView.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnHideAndView.getIcon().toString().equalsIgnoreCase("src\\img\\hide.png")) {
					btnHideAndView.setIcon(new ImageIcon("src\\img\\view.png"));
					txtMatKhau.setEchoChar((char) 0);
				} else {
					btnHideAndView.setIcon(new ImageIcon("src\\img\\hide.png"));
					txtMatKhau.setEchoChar('*');
				}
			}
		});
		JLayeredPane layerPane = new JLayeredPane();
		layerPane.setLayout(null);
		layerPane.setBounds(128, 325, 350, 50);
		layerPane.add(txtMatKhau, Integer.valueOf(0));
		layerPane.add(btnHideAndView, Integer.valueOf(1));
		
		JButton btnDangNhap = new JButton("ĐĂNG NHẬP");
		btnDangNhap.setFont(new Font("Roboto", Font.BOLD, 24));
		btnDangNhap.setForeground(Color.WHITE);
		btnDangNhap.setBackground(new Color(59, 130, 246));
		btnDangNhap.setBounds(128, 400, 350, 50);
		btnDangNhap.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String user = txtTaiKhoan.getText();
				String passw = new String(txtMatKhau.getPassword());
				if (user.isEmpty() || passw.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Vui lòng nhập tài khoản và mật khẩu!", "Lỗi", JOptionPane.WARNING_MESSAGE);
		            return;
		        }
				NhanVien nv = NhanVienDAO.dangnhap(user, passw);
				if (nv != null) {
					dispose();
		            new TrangChuGUI(nv).setVisible(true);

		        } else {
		            JOptionPane.showMessageDialog(null, "Sai tài khoản hoặc mật khẩu!", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
		        }
				
			}
		});
		
		setLayout(null);
		this.getContentPane().setBackground(Color.decode("#03346E"));
		add(pnlTieuDe);
		add(lblTaiKhoan);
		add(lblTaiKhoanText);
		add(lblImgTaiKhoan);
		add(txtTaiKhoan);
		add(lblMatKhau);
		add(lblMatKhauText);
		add(lblImgMatKhau);
		add(layerPane);
		add(btnDangNhap);
		
		setVisible(true);
		
	}
	public static void main(String[] args) {
		new DangNhapGUI();
	}
}
