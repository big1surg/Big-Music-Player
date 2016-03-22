package me.PiMP.com;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JInternalFrame;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PiMP extends JFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PiMP frame = new PiMP();
					ImageIcon img = new ImageIcon("cd.png");
					frame.setTitle("P i M P");
					frame.setIconImage(img.getImage());
					frame.isResizable();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public PiMP() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 450, 300);
		getContentPane().setLayout(null);
		
		Icon playButton = new ImageIcon("playButton.png");
		Image img = ((ImageIcon) playButton).getImage();
		Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		Icon pause = new ImageIcon("pause.png");
		Image imgPause = ((ImageIcon) pause).getImage();
		Image newimgPause = imgPause.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIconPause = new ImageIcon(newimgPause);
		JButton btnNewButton = new JButton(newIcon);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(btnNewButton.getIcon()==newIcon){
					btnNewButton.setIcon(newIconPause);
				}else {
					btnNewButton.setIcon(newIcon);
				}
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setBounds(163, 199, 89, 23);
		btnNewButton.setSize(60, 60);
		getContentPane().add(btnNewButton);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGap(0, 434, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGap(0, 261, Short.MAX_VALUE)
		);
		getContentPane().setLayout(groupLayout);
	}
	
	public void Button(){
		
	}
}
