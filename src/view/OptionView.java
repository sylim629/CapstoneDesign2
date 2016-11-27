package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class OptionView extends JFrame {
	JLabel description = new JLabel("<html>해당 사용자: 사용하는 은행에서 SMS 알림 서비스를 이용 중이신 분.<br><br>핸드폰으로 받은 결제 내역 SMS에서"
			+ " 결제 금액을 불러와 달력에 추가 할 수 있습니다.이 기능을 활성화 하려면 사용자의 휴대폰 번호가 필요합니다.<br><br>"
			+ "이 기능을 원하시는 사용자는 NEXT를 누르세요. 원하지 않는 사용자는 SKIP을 누르세요. 자세한 설명을 보기 위해선 \"상세설명\"을 누르세요.</html>");
	JButton skip = new JButton("SKIP");
	JButton next = new JButton("NEXT");
	JButton howTo = new JButton("상세설명");
	
	public OptionView (){
		super("PiCalendar 추가 기능!");
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		buttons.add(skip);
		buttons.add(howTo);
		buttons.add(next);
		buttons.setBorder(new EmptyBorder(15, 15, 15, 15));
		
		//컴포넌트를 넣을 컨테이너 구하기
		Container container = this.getContentPane();
		
		description.setBorder(new EmptyBorder(15, 15, 15, 15));
		
		//컴포넌트를 컨테이너에 추가
		container.add(description, BorderLayout.NORTH);
		container.add(buttons, BorderLayout.SOUTH);
		
		this.setSize(300,300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// actionListeners
		
		next.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
			      @SuppressWarnings("unused")
				PairView pair = new PairView();  
			   }
			});
	}
}
