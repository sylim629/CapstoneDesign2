package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import manager.PairManager;
import manager.ServerManager;

public class HowToView extends JFrame {
	JLabel lorem = new JLabel("<html>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus congue nec urna eget gravida. Etiam at enim quis elit sollicitudin fermentum. Vestibulum tempor et ligula sit amet vulputate. Proin enim tellus, rutrum vitae lobortis quis, malesuada sit amet velit. Nulla accumsan rutrum nibh, vel mollis augue efficitur id.</html>");
	JButton close = new JButton("닫기");
	public HowToView(){
		super("상세 설명");
		
		Container container = this.getContentPane();
		lorem.setBorder(new EmptyBorder(15, 15, 15, 15));

		// 컴포넌트를 컨테이너에 추가
		container.add(lorem, BorderLayout.NORTH);
		container.add(close, BorderLayout.SOUTH);
		
		lorem.setFont(new Font("THEJung110", 0, 15));
		close.setFont(new Font("THEJung110", 0, 15));

		this.setSize(300, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// ActionListeners
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}
