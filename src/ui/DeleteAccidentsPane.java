package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.jdatepicker.impl.JDatePickerImpl;

import model.Database;

@SuppressWarnings("serial")
public class DeleteAccidentsPane extends MyCustomPane {

	public DeleteAccidentsPane() {
		JLabel title = new JLabel("Delete accidents in range");

		// Create date picker
		JLabel startDateLabel = new JLabel("Start Date:");
		JDatePickerImpl startDatePicker = newMyDatePanel(0);

		// Create date picker
		JLabel endDateLabel = new JLabel("End Date:");
		JDatePickerImpl endDatePicker = newMyDatePanel(1);

		JButton submitButton = new JButton("Submit");

		// Add action to submit Button
		// Doing anonymous function to avoid storing references to all text
		// fields in the class and using those class variables in a named
		// function
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Add accident to database
				Date startDate = getSelectedDate(0);
				Date endDate = getSelectedDate(1);

				String error = null;
				try {
					Database.deleteAccidentsInRange(startDate, endDate);
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "<html>Deleted accidents in range <br>" + startDate.toLocaleString() + " to <br>"
						+ endDate.toLocaleString());
			}
		});

		addLeftAligned(title);
		addLeftAligned(startDateLabel);
		addLeftAligned(startDatePicker);
		addLeftAligned(endDateLabel);
		addLeftAligned(endDatePicker);
		addLeftAligned(submitButton);
	}

}
