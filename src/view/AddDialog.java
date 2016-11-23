package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import manager.FriendsList;
import manager.ScheduleManager;
import manager.ServerManager;
import model.Schedule;

public class AddDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int year;
	private int month;
	private int day;

	private Schedule schedule = null;

	private JTextField tfTitle;
	private JTextArea taMemo;

	private JButton stickerButton;
	private JButton startDayButton;
	private JButton endDayButton;
	private JButton saveButton;
	private JButton deleteButton;
	private JButton cancelButton;

	private JButton tagAtButton;
	private JLabel friendListLabel;
	
	private JLabel spendingLabel;

	private Date startDate;
	private Date endDate;

	private Component componentParent;

	public AddDialog(Component comp, Schedule schedule) {
		this.schedule = schedule;

		initialize(comp, -1, -1, -1);
	}

	public AddDialog(Component comp, int year, int month, int day) {
		initialize(comp, year, month, day);
	}

	private void initialize(Component comp, int year, int month, int day) {
		componentParent = comp;
		setLayout(new BorderLayout());
		setSize(450, 300);
		setLocation(comp.getX() + (comp.getSize().width - getSize().width) / 2,
				comp.getY() + (comp.getSize().height - getSize().height) / 2);
		setResizable(false);
		setModal(true);

		if (schedule == null) {
			this.year = year;
			this.month = month;
			this.day = day;
		} else {
			this.year = Integer.parseInt(new SimpleDateFormat("yyyy").format(schedule.getStartDate()));
			this.month = Integer.parseInt(new SimpleDateFormat("MM").format(schedule.getStartDate()));
			this.day = Integer.parseInt(new SimpleDateFormat("dd").format(schedule.getStartDate()));
		}

		setHeaderPanel();
		setBodyPanel(year);
	}

	private void setHeaderPanel() {
		JPanel subjectPanel = new JPanel();
		JPanel emptyPanel = new JPanel();
		subjectPanel.setLayout(new BorderLayout());
		subjectPanel.setBorder(new EmptyBorder(0, 0, 0, 5));
		emptyPanel.setPreferredSize(new Dimension(10, 10));
		subjectPanel.add(emptyPanel, BorderLayout.NORTH);

		JLabel labelTitle = new JLabel();
		labelTitle.setText("  Subject ");
		labelTitle.setFont(new Font("THEJung130", 0, 12));
		subjectPanel.add(labelTitle, BorderLayout.WEST);

		tfTitle = new JTextField();
		if (schedule != null)
			tfTitle.setText(schedule.getSubject());
		tfTitle.addKeyListener(keyListener);
		subjectPanel.add(tfTitle, BorderLayout.CENTER);

		JPanel panelDayButtons = new JPanel();
		panelDayButtons.setLayout(new GridLayout(1, 2));
		panelDayButtons.setBorder(new EmptyBorder(0, 5, 5, 5));
		if (schedule != null) {
			startDate = schedule.getStartDate();
			endDate = schedule.getEndDate();
		} else {
			try {
				startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(String.format("%04d-%02d-%02d 00:00:00", this.year, this.month, this.day));
				endDate = startDate;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		startDayButton = new JButton();
		startDayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DaySelector daySelector = new DaySelector(componentParent, new DaySelector.Callbacks() {
					@Override
					public void onUpdatedDate(Integer year, Integer month, Integer day, Integer hour, Integer minute) {
						String textFormat = String.format("%04d-%02d-%02d %02d:%02d:00", year, month, day, hour,
								minute);
						startDayButton.setText(textFormat);
						try {
							startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(textFormat);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}, startDate);
				daySelector.setVisible(true);
			}
		});
		startDayButton.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate));
		startDayButton.addKeyListener(keyListener);
		panelDayButtons.add(startDayButton);

		endDayButton = new JButton();
		endDayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DaySelector daySelector = new DaySelector(componentParent, new DaySelector.Callbacks() {
					@Override
					public void onUpdatedDate(Integer year, Integer month, Integer day, Integer hour, Integer minute) {
						String textFormat = String.format("%04d-%02d-%02d %02d:%02d:00", year, month, day, hour,
								minute);
						endDayButton.setText(textFormat);
						try {
							endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(textFormat);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}, endDate);
				daySelector.setVisible(true);
			}
		});
		endDayButton.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate));
		endDayButton.addKeyListener(keyListener);
		panelDayButtons.add(endDayButton);

		JPanel firstBodyRight = new JPanel();
		firstBodyRight.setLayout(new BorderLayout());
		firstBodyRight.add(subjectPanel, BorderLayout.NORTH);
		firstBodyRight.add(panelDayButtons, BorderLayout.CENTER);

		stickerButton = new JButton();
		stickerButton.setPreferredSize(new Dimension(56, 56));

		JPanel firstBody = new JPanel();
		firstBody.setLayout(new FlowLayout());
		firstBody.add(stickerButton);
		firstBody.add(firstBodyRight);
		firstBodyRight.setPreferredSize(new Dimension(getWidth() - 80, 66));
		this.add(firstBody, BorderLayout.NORTH);
	}

	private void setBodyPanel(int year) {
		JPanel memoPanel = new JPanel();
		memoPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
		memoPanel.setLayout(new BorderLayout());

		taMemo = new JTextArea();
		if (schedule != null)
			taMemo.setText(schedule.getContent());
		taMemo.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0xAAAAAA)));
		taMemo.addKeyListener(keyListener);

		memoPanel.add(taMemo, BorderLayout.CENTER);
		this.add(memoPanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout());

		JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tagAtButton = new JButton("@");
		tagAtButton.setPreferredSize(new Dimension(35, 35));
		tagPanel.add(tagAtButton);
		friendListLabel = new JLabel();
		tagPanel.add(friendListLabel);
		
		JPanel spendingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		spendingLabel = new JLabel();
		spendingPanel.add(spendingLabel);

		if (year == -1) {
			FriendsList friendsList = new FriendsList();
			ArrayList<String> taggedFriendsId = schedule.getTaggedFriends();
			String names = "";
			for (int i = 0; i < taggedFriendsId.size(); i++) {
				names += friendsList.getFriendName(taggedFriendsId.get(i)) + ",";
			}
			friendListLabel.setText(names);
			
			spendingLabel.setText("이 날 지출한 총 금액 : " + schedule.getMoneySpent());
		}
		
		tagAtButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TagListView tagFriendsDialog = new TagListView(componentParent, new TagListView.Callbacks() {

					@Override
					public void onUpdatedDate(ArrayList<String> selectedFriends) {
						String friendNames = "";
						for (int i = 0; i < selectedFriends.size(); i++) {
							friendNames += selectedFriends.get(i) + ",";
						}
						friendListLabel.setText(friendNames);
						// System.out.println(friendListLabel.getText());
					}
				});
				tagFriendsDialog.setVisible(true);
			}
		});
		tagAtButton.addKeyListener(keyListener);

		bottomPanel.add(tagPanel, BorderLayout.NORTH);
		bottomPanel.add(spendingPanel, BorderLayout.EAST);

		JPanel buttonPanel = new JPanel();
		if (schedule != null)
			buttonPanel.setLayout(new GridLayout(1, 3));
		else
			buttonPanel.setLayout(new GridLayout(1, 2));
		buttonPanel.setBorder(new EmptyBorder(0, 5, 5, 5));

		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Schedule newSchedule = new Schedule();
				newSchedule.setSubject(tfTitle.getText());
				newSchedule.setStartDate(startDate);
				newSchedule.setEndDate(endDate);
				newSchedule.setContent(taMemo.getText());
				ArrayList<String> taggedFriends = new ArrayList<>();
				String[] friendNames = friendListLabel.getText().split(",");
				FriendsList friendsList = new FriendsList();
				for (int i = 0; i < friendNames.length; i++) {
					if (friendNames[i].isEmpty())
						continue;
					taggedFriends.add(friendsList.getFriendID(friendNames[i]));
				}
				newSchedule.setTaggedFriends(taggedFriends);

				if (newSchedule.getSubject().length() < 1) {
					JOptionPane.showMessageDialog(null, "Subject empty", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (newSchedule.getEndDate().compareTo(newSchedule.getStartDate()) < 0) {
					JOptionPane.showMessageDialog(null, "Start-time faster then End-time", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (schedule != null) {
					newSchedule.setIndex(schedule.getIndex());
					ScheduleManager.sharedInstance().updateSchedule(newSchedule, newSchedule.getIndex());
					ServerManager.sharedInstance().modifyServer(Integer.parseInt(schedule.getServerID()));
				} else {
					ScheduleManager.sharedInstance().addSchedule(newSchedule);
					ServerManager.sharedInstance().saveServer();
				}
				setVisible(false);
			}
		});
		saveButton.addKeyListener(keyListener);
		buttonPanel.add(saveButton);

		if (schedule != null) {
			deleteButton = new JButton("Delete");
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String id = schedule.getServerID();
					boolean isDeleted = ScheduleManager.sharedInstance().deleteSchedule(schedule);
					if (isDeleted)
						ServerManager.sharedInstance().deleteServer(Integer.parseInt(id));
					setVisible(false);
				}
			});
			deleteButton.addKeyListener(keyListener);
			buttonPanel.add(deleteButton);
		}

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		cancelButton.addKeyListener(keyListener);
		buttonPanel.add(cancelButton);

		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	private KeyListener keyListener = new KeyListener() {
		private boolean keyMatch1 = false;
		private boolean keyMatch2 = false;

		@Override
		public void keyTyped(KeyEvent arg0) {
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			if (arg0.getKeyCode() == 157)
				keyMatch1 = false;

			if (arg0.getKeyCode() == 87)
				keyMatch2 = false;
		}

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == 157)
				keyMatch1 = true;

			if (arg0.getKeyCode() == 87)
				keyMatch2 = true;

			if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE || (keyMatch1 && keyMatch2))
				setVisible(false);
		}
	};
}
