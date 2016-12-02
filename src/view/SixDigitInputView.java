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
import javax.swing.JTextField;

import manager.PairManager;
import manager.ServerManager;

public class SixDigitInputView extends JFrame {
	JLabel label = new JLabel("휴대폰으로 보낸 인증번호를 입력하시오.");
	   JTextField enterDigit = new JTextField(10);
	   JLabel error = new JLabel("");
	   JButton enterButton = new JButton("확인");

	public SixDigitInputView() {
		super("인증번호 입력");
		
		// font
		label.setFont(new Font("THEJung110", 0, 15));
		enterDigit.setFont(new Font("THEJung110", 0, 15));
		error.setFont(new Font("THEJung110", 0, 15));
		enterButton.setFont(new Font("THEJung110", 0, 15));
		
		this.setLayout(new FlowLayout());
		this.add(label);
		this.add(enterDigit);
		this.add(error);
		this.add(enterButton);

		this.setSize(300, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// ActionListeners

		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String num = enterDigit.getText().toString();
				//if (num.equals("19473")) {
					@SuppressWarnings("unused")
					VerifiedView vv = new VerifiedView();
					dispose();
				/*} else {
					error.setText("�솗�씤 �썑 �떎�떆 �엯�젰�븯�떆�삤.");
					error.setFont(new Font("THEJung110", 0, 15));
					error.setForeground(Color.RED);
					enterDigit.setText("");
				}*/

			}
		});

	}

}
