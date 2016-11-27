import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;

import manager.ServerManager;
import manager.FriendsList;
import manager.LoginManager;
import manager.MoneyManager;
import manager.PairManager;
import view.MonthView;
import view.OptionView;

public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Main() {
		Date currentDate = Calendar.getInstance().getTime();
		int year = new Integer(new SimpleDateFormat("yyyy").format(currentDate)).intValue();
		int month = new Integer(new SimpleDateFormat("MM").format(currentDate)).intValue();
		MonthView monthView = new MonthView(this, year, month);
		this.add(monthView);

		this.setTitle(new Integer(year).toString() + " TagCalendar");

		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				monthView.setSize(getSize());
				monthView.setPreferredSize(getSize());
			}
		});
	}

	public static void main(String[] args) {
		LoginManager.sharedInstance().loginFacebook();
		new FriendsList().printfriend();
		ServerManager.sharedInstance().loadServer();
		Main calendar = new Main();
		calendar.pack();
		calendar.setSize(800, 600);
		calendar.setMinimumSize(new Dimension(340, 510));
		calendar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		calendar.setVisible(true);
		System.out.println("READY\n");
		
		// TODO 핸드폰 번호 입력 UI
		@SuppressWarnings("unused")
		OptionView option = new OptionView();
		String phoneNum = PairManager.sharedInstance().getPhoneNumber();
		// System.out.println(phoneNum);
		// MoneyInfo에 핸드폰번호 저장
		// P2P 인증번호 실시
		// dummy phone number = 01072583303
		// 서버에서 사용 금액 불러오기 -> MoneyManager, ServerManager->loadServer_moneyOnly(String phoneNum);
		
		//ServerManager.sharedInstance().loadServer_moneyOnly(phoneNum); --> PairView.java 로 ActionListener 안으로 이동
		
		calendar.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
			}
		});
	}

}
