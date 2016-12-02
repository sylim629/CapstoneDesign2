package view;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import manager.PairManager;
import manager.ServerManager;

public class PairView extends JFrame {

	JButton saveButton = new JButton("저장");
	JTextField enterNum = new JTextField(20);
	JLabel label = new JLabel("휴대폰 번호를 입력하시오.");

	public PairView() {
		super("휴대폰 번호 입력");
 
		// font
		saveButton.setFont(new Font("THEJung110", 0, 15));
		enterNum.setFont(new Font("THEJung110", 0, 15));
		label.setFont(new Font("THEJung110", 0, 15));

		this.setLayout(new FlowLayout());
		this.add(label);
		this.add(enterNum);
		this.add(saveButton);

		this.setSize(300, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// actionListeners

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String num = enterNum.getText().toString();
				PairManager.sharedInstance().setPhoneNumber(num);
				// 돈 정보 가져옴
				ServerManager.sharedInstance().loadServer_moneyOnly(num);
				
				System.out.println(num);
				@SuppressWarnings("unused")
				SixDigitInputView input6Digit = new SixDigitInputView();
				dispose();
			}
			
		});
	}

}


