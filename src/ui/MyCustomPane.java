package ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

@SuppressWarnings("serial")
public class MyCustomPane extends JPanel {
	private final static int pad = 10;
	private UtilDateModel[] dateModels = new UtilDateModel[1];

	public MyCustomPane() {
		defaultSizeAndLayout();
		addBorder();
	}

	public MyCustomPane(boolean defaults) {
		if (defaults) {
			defaultSizeAndLayout();
			addBorder();
		}
	}

	protected static void addLeftAligned(JComponent component, Container container) {
		component.setAlignmentX(Component.LEFT_ALIGNMENT);
		container.add(component);
	}

	protected void addLeftAligned(JComponent component) {
		component.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(component);
	}

	protected void defaultSizeAndLayout() {
		this.setPreferredSize(new Dimension(300, 200));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	protected void addBorder() {
		Border beveledBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		Border paddingBorder = BorderFactory.createEmptyBorder(pad, pad, pad, pad);
		Border compoundBorder = BorderFactory.createCompoundBorder(beveledBorder, paddingBorder);
		this.setBorder(compoundBorder);
	}

	protected JDatePickerImpl newMyDatePanel(int index) {
		while (index >= dateModels.length) {
			dateModels = resize(dateModels);
		}

		dateModels[index] = new UtilDateModel(new Date());
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(dateModels[index], p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

		return datePicker;
	}

	protected Date getSelectedDate(int index) {
		Date result = null;

		if (index < dateModels.length && dateModels[index] != null) {
			result = dateModels[index].getValue();
		}

		return result;
	}

	protected JDatePickerImpl newMyDatePanel() {
		return newMyDatePanel(0);
	}

	protected Date getSelectedDate() {
		return getSelectedDate(0);
	}

	protected static int getIntValFromSpinner(JSpinner spinner) {
		try {
			spinner.commitEdit();
		} catch (java.text.ParseException ex) {
			System.out.println(ex.toString());
		}

		return (int) spinner.getValue();
	}

	protected static float getFloatValFromSpinner(JSpinner spinner) {
		try {
			spinner.commitEdit();
		} catch (java.text.ParseException ex) {
			System.out.println(ex.toString());
		}

		return (float) (((double) spinner.getValue()) * 1.0f);
	}

	private static UtilDateModel[] resize(UtilDateModel[] arr) {
		UtilDateModel[] tmp = new UtilDateModel[2 * arr.length];
		System.arraycopy(arr, 0, tmp, 0, arr.length);
		return tmp;
	}

	protected static <T> void showResults(String error, String description, ArrayList<T> value) {
		JFrame frame = new JFrame("Results");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel resultsPane = new DisplayResultsPane(error, description, value);
		frame.getContentPane().add(resultsPane);

		frame.pack();
		frame.setVisible(true);
	}

	protected static <T> void showResults(String error, String description, T value) {
		JFrame frame = new JFrame("Results");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel resultsPane = new DisplayResultsPane(error, description, value);
		frame.getContentPane().add(resultsPane);

		frame.pack();
		frame.setVisible(true);
	}

	protected static <T> void showResults(String error, String description) {
		JFrame frame = new JFrame("Results");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel resultsPane = new DisplayResultsPane(error, description);
		frame.getContentPane().add(resultsPane);

		frame.pack();
		frame.setVisible(true);
	}

	public void updateData() {
		return;
	}
}
