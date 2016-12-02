package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import manager.MoneyManager;
import manager.PairManager;
import manager.ScheduleManager;
import manager.ServerManager;
import model.Schedule;

public class MonthView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String[] monthNames = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };

	private int selectedRow = -1;
	private int selectedColumn = -1;

	private JFrame mainFrame;
	private JLabel labelYear;
	private JLabel labelMonth;
	private JPopupMenu popupMenuYear;
	private JPopupMenu popupMenuMonth;

	private JPanel panelHeader;
	private JPanel panelCalendar;

	private JTable tableHeader;
	private JTable tableCalendar;
	private CalendarModel calendarModel = new CalendarModel();

	public MonthView(JFrame mainFrame, int year, int month) {
		this.mainFrame = mainFrame;
		calendarModel.setMonth(year, month);
		selectedRow = -1;
		selectedColumn = -1;

		setHeaderPanel();
		setContentPanel();
		this.setLayout(new BorderLayout());
		this.add(panelHeader, BorderLayout.NORTH);
		this.add(panelCalendar, BorderLayout.CENTER);

		ScheduleManager.sharedInstance().setEventListener(new ScheduleManager.Callbacks() {
			@Override
			public void onUpdatedSchedule(Schedule schedule) {
				setCalendarMonth(calendarModel.getYear(), calendarModel.getMonth());
			}

			@Override
			public void onAddedSchedule(Schedule schedule) {
				setCalendarMonth(calendarModel.getYear(), calendarModel.getMonth());
			}

			@Override
			public void onDeletedSchedule(Schedule schedule) {
				setCalendarMonth(calendarModel.getYear(), calendarModel.getMonth());
			}
		});
	}

	private void setHeaderPanel() {
		panelHeader = new JPanel();
		panelHeader.setLayout(new BorderLayout());
		panelHeader.setBackground(new Color(0xCD8777));

		JPanel headerText = new JPanel();
		headerText.setBackground(new Color(0xCD8777));
		headerText.setLayout(new BorderLayout());

		labelYear = new JLabel(Integer.toString(calendarModel.getYear()));
		labelYear.setFont(new Font("THEJung110", 0, 30));
		labelYear.setPreferredSize(
				new Dimension(labelYear.getPreferredSize().width + 12, labelYear.getPreferredSize().height + 15));
		labelYear.setHorizontalAlignment(JLabel.RIGHT);
		labelYear.setVerticalAlignment(JLabel.CENTER);
		labelYear.setForeground(Color.WHITE);
		labelYear.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				popupMenuYear.removeAll();
				for (int i = calendarModel.getYear() - 10; i < calendarModel.getYear() + 10; i++) {
					JMenuItem item = new JMenuItem(Integer.toString(i));
					item.addActionListener(popupMenuYearActionListener);
					popupMenuYear.add(item);
				}
				popupMenuYear.show(e.getComponent(), labelYear.getX() + 60, labelYear.getY() + 20);
				MenuSelectionManager.defaultManager().setSelectedPath(
						new MenuElement[] { popupMenuYear, (JMenuItem) popupMenuYear.getComponent(10) });
			}
		});
		headerText.add(labelYear, BorderLayout.WEST);

		labelMonth = new JLabel();
		labelMonth.setText(monthNames[calendarModel.getMonth() - 1]);
		labelMonth.setFont(new Font("THEJung150", 0, 26));
		labelMonth.setPreferredSize(
				new Dimension(labelMonth.getPreferredSize().width + 6, labelMonth.getPreferredSize().height));
		labelMonth.setHorizontalAlignment(JLabel.RIGHT);
		labelMonth.setVerticalAlignment(JLabel.CENTER);
		labelMonth.setForeground(Color.WHITE);
		labelMonth.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				popupMenuMonth.show(e.getComponent(), labelMonth.getX() - 40, labelMonth.getY() + 20);
				MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[] { popupMenuMonth,
						(JMenuItem) popupMenuMonth.getComponent(calendarModel.getMonth() - 1) });
			}
		});
		headerText.add(labelMonth, BorderLayout.EAST);

		panelHeader.add(headerText, BorderLayout.WEST);

		popupMenuYear = new JPopupMenu();
		popupMenuMonth = new JPopupMenu();
		for (String str : monthNames) {
			JMenuItem item = new JMenuItem(str);
			item.addActionListener(popupMenuMonthActionListener);
			popupMenuMonth.add(item);
		}

		tableHeader = new JTable(new CalendarHeadModel());
		tableHeader.getTableHeader().setReorderingAllowed(false);
		tableHeader.setColumnSelectionAllowed(false);
		tableHeader.setRowSelectionAllowed(false);
		tableHeader.setIntercellSpacing(new Dimension(0, 0));
		tableHeader.setDefaultRenderer(tableHeader.getColumnClass(0), new CalendarCellRenderer(true));
		tableHeader.setRowHeight(0, 30);
		tableHeader.setFont(new Font("THEJung130", 0, 14));
		tableHeader.setBorder(new MatteBorder(1, 0, 0, 0, new Color(0xD58F7F)));

		panelHeader.add(tableHeader, BorderLayout.SOUTH);
	}

	private void setContentPanel() {
		panelCalendar = new JPanel();
		panelCalendar.setLayout(null);

		tableCalendar = new JTable(calendarModel);
		tableCalendar.getTableHeader().setReorderingAllowed(false);
		tableCalendar.setColumnSelectionAllowed(false);
		tableCalendar.setRowSelectionAllowed(false);
		tableCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableCalendar.setIntercellSpacing(new Dimension(0, 0));
		tableCalendar.setDefaultRenderer(tableCalendar.getColumnClass(0), new CalendarCellRenderer(false));
		ListSelectionListener selectionListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = tableCalendar.getSelectedRow();
				int col = tableCalendar.getSelectedColumn();

				if (row == -1 || col == -1)
					return;

				DayView value = (DayView) tableCalendar.getValueAt(row, col);
				if (value.isPrevMonth())
					setCalendarMonth(calendarModel.getYear(), calendarModel.getMonth() - 1);
				if (value.isNextMonth())
					setCalendarMonth(calendarModel.getYear(), calendarModel.getMonth() + 1);

				setPreferredSize(getPreferredSize());
			}
		};
		tableCalendar.getSelectionModel().addListSelectionListener(selectionListener);
		tableCalendar.getColumnModel().getSelectionModel().addListSelectionListener(selectionListener);
		tableCalendar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				int row = tableCalendar.getSelectedRow();
				int col = tableCalendar.getSelectedColumn();

				System.out.printf("%d %d\n", row, col);

				if (row == -1 || col == -1)
					return;

				if (selectedRow == row && selectedColumn == col) {
					MoneyManager.sharedInstance().deleteMoneyList(); 
					ServerManager.sharedInstance().loadServer_moneyOnly(PairManager.sharedInstance().getPhoneNumber());
					DayDialog dialog = new DayDialog(mainFrame, calendarModel.getYear(), calendarModel.getMonth(),
							((DayView) calendarModel.getValueAt(row, col)).getDay());
					dialog.setVisible(true);
				}

				selectedRow = row;
				selectedColumn = col;
			}
		});

		panelCalendar.add(tableCalendar);
	}

	private ActionListener popupMenuYearActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			setCalendarMonth(Integer.valueOf(e.getActionCommand()), calendarModel.getMonth());
			popupMenuYear.setVisible(false);
		}
	};

	private ActionListener popupMenuMonthActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int monthIndex = -1;
			for (int i = 0; i < 12; i++) {
				if (monthNames[i].equals(e.getActionCommand())) {
					monthIndex = i;
					break;
				}
			}
			if (monthIndex != -1)
				setCalendarMonth(calendarModel.getYear(), monthIndex + 1);
			popupMenuMonth.setVisible(false);
		}
	};

	private void setCalendarMonth(int year, int month) {
		calendarModel.setMonth(year, month);
		selectedRow = -1;
		selectedColumn = -1;

		labelYear.setText(Integer.toString(calendarModel.getYear()));
		labelMonth.setText(monthNames[calendarModel.getMonth() - 1]);

		labelYear.setPreferredSize(
				new Dimension(labelYear.getFontMetrics(labelYear.getFont()).stringWidth(labelYear.getText()) + 12,
						labelYear.getPreferredSize().height));
		labelMonth.setPreferredSize(
				new Dimension(labelMonth.getFontMetrics(labelMonth.getFont()).stringWidth(labelMonth.getText()) + 6,
						labelMonth.getPreferredSize().height));

		setPreferredSize(getPreferredSize());
	}

	public void setYear(int year) {
		calendarModel.setYear(year);
	}

	public void setMonth(int month) {
		calendarModel.setMonth(month);
	}

	public int getYear() {
		return calendarModel.getYear();
	}

	public int getMonth() {
		return calendarModel.getMonth();
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);

		int height = (preferredSize.height - panelHeader.getPreferredSize().height) - 39;
		int cellHeight = height / 6;

		tableCalendar.setBounds(0, 0, panelCalendar.getSize().width, height);

		tableCalendar.setRowHeight(cellHeight);
		calendarModel.setRowSize(new Dimension(preferredSize.width / 7, cellHeight));

		tableCalendar.setRowHeight(5, height - cellHeight * 5);
		calendarModel.setRowSize(5, new Dimension(preferredSize.width / 7, height - cellHeight * 5));
	}
}

class CalendarHeadModel extends AbstractTableModel {
	private static final long serialVersionUID = 8654378532415125861L;

	String[] days = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public String getColumnName(int column) {
		return days[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return days[columnIndex];
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		days[columnIndex] = (String) aValue;
	}
}

class CalendarModel extends AbstractTableModel {
	private static final long serialVersionUID = -1892399927942291308L;

	private int yearNumber;
	private int monthNumber;

	int[] numDays = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	DayView[][] calendar = new DayView[6][7];

	public CalendarModel() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				calendar[i][j] = new DayView();
			}
		}
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public int getRowCount() {
		return 6;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return calendar[rowIndex][columnIndex];
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		calendar[rowIndex][columnIndex] = (DayView) aValue;
	}

	public void setRowSize(Dimension dimension) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				calendar[i][j].setPreferredSize(dimension);
			}
		}
	}

	public void setRowSize(int row, Dimension dimension) {
		for (int i = 0; i < 7; i++) {
			calendar[row][i].setPreferredSize(dimension);
		}
	}

	public void setYear(int year) {
		setMonth(year, monthNumber);
	}

	public void setMonth(int month) {
		setMonth(yearNumber, month);
	}

	public int getYear() {
		return yearNumber;
	}

	public int getMonth() {
		return monthNumber;
	}

	public void setMonth(int year, int month) {
		if (month < 1) {
			year--;
			month = 12;
		}
		if (month > 12) {
			year++;
			month = 1;
		}
		yearNumber = year;
		monthNumber = month;

		Date currentTime = Calendar.getInstance().getTime();
		Integer todayYear = new Integer(new SimpleDateFormat("yyyy").format(currentTime)).intValue();
		Integer todayMonth = new Integer(new SimpleDateFormat("MM").format(currentTime)).intValue();
		Integer todayDay = new Integer(new SimpleDateFormat("dd").format(currentTime)).intValue();

		int offset = getStartDay(yearNumber, monthNumber);
		int num = daysInMonth(yearNumber, monthNumber);
		int prevNum = daysInMonth(yearNumber, monthNumber - 1);
		int prevStart = prevNum - offset % 7;
		for (int i = prevStart; i < prevNum; ++i) {
			calendar[0][i - prevStart].setDate(year, month - 1, i + 1);
			calendar[0][i - prevStart].setPrevMonth(true);
			calendar[0][i - prevStart].setNextMonth(false);
			if (todayYear == year && todayMonth == month - 1 && i + 1 == todayDay)
				calendar[0][i - prevStart].setToday(true);
			else
				calendar[0][i - prevStart].setToday(false);
		}
		for (int i = 0; i < num; ++i, ++offset) {
			calendar[offset / 7][offset % 7].setDate(year, month, i + 1);
			calendar[offset / 7][offset % 7].setPrevMonth(false);
			calendar[offset / 7][offset % 7].setNextMonth(false);
			if (todayYear == year && todayMonth == month && i + 1 == todayDay)
				calendar[offset / 7][offset % 7].setToday(true);
			else
				calendar[offset / 7][offset % 7].setToday(false);
		}
		for (int i = 0; offset < 6 * 7; ++i, ++offset) {
			calendar[offset / 7][offset % 7].setDate(year, month + 1, i + 1);
			calendar[offset / 7][offset % 7].setPrevMonth(false);
			calendar[offset / 7][offset % 7].setNextMonth(true);
			if (todayYear == year && todayMonth == month + 1 && i + 1 == todayDay)
				calendar[offset / 7][offset % 7].setToday(true);
			else
				calendar[offset / 7][offset % 7].setToday(false);
		}
		fireTableDataChanged();
	}

	private int getStartDay(int year, int month) {
		final int START_DAY_FOR_JAN_1_1800 = 3;
		int totalNumberOfDays = getTotalNumberOfDays(year, month);

		return (totalNumberOfDays + START_DAY_FOR_JAN_1_1800) % 7;
	}

	private int getTotalNumberOfDays(int year, int month) {
		int total = 0;

		int yearCount = year - 1800;
		total = yearCount * 365 + yearCount / 400 + yearCount / 4 - yearCount / 100 + (isLeapYear(year) ? 0 : 1);

		for (int i = 1; i < month; i++)
			total = total + daysInMonth(year, i);

		return total;
	}

	private boolean isLeapYear(int year) {
		return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
	}

	private int daysInMonth(int year, int month) {
		if (month < 1) {
			year--;
			month = 12;
		}
		if (month > 12) {
			year++;
			month = 1;
		}
		int days = numDays[month];
		if (month == 2 && isLeapYear(year))
			++days;
		return days;
	}
}

class CalendarCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 5214495388243952251L;

	private boolean isHeadCell = false;

	public CalendarCellRenderer(boolean isHead) {
		isHeadCell = isHead;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused,
			int row, int column) {
		if (isHeadCell) {
			setHorizontalAlignment(JLabel.CENTER);
			setBackground(new Color(0xD58F7F));
			setForeground(Color.WHITE);

			super.getTableCellRendererComponent(table, value, false, false, row, column);

			return this;
		} else {
			if (row == table.getSelectedRow() && column == table.getSelectedColumn())
				focused = true;

			if (value != null)
				((DayView) value).setFocused(focused);

			return (Component) value;
		}
	}
}