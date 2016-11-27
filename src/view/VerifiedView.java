package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class VerifiedView extends JFrame {
	JLabel label = new JLabel("인증 완료");
	JButton okButton = new JButton("확인");
	
	public VerifiedView (){
		super("인증 완료");
		
		label.setFont(new Font("THEJung110", 0, 15));
		okButton.setFont(new Font("THEJung110", 0, 15));
		
		this.setLayout(new FlowLayout());
		this.add(label);
		this.add(okButton);
		
		this.setSize(100,100);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// ActionListener
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		
	}
}
