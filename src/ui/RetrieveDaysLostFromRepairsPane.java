package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;

import model.Database;

@SuppressWarnings("serial")
public class RetrieveDaysLostFromRepairsPane extends MyCustomPane {

	public RetrieveDaysLostFromRepairsPane() {
		JLabel title = new JLabel(
				"<html>Retrieve the total number of <br>work days lost due to accidents <br>in repairing the products <br>which got complaints");

		JButton submitButton = new JButton("Submit");

		// Add action to submit Button
		// Doing anonymous function to avoid storing references to all text
		// fields in the class and using those class variables in a named
		// function
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Add accident to database
				int daysLost = 0;

				String error = null;
				try {
					daysLost = Database.retrieveDaysLostFromRepairs();
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "Days lost from repairs: ", daysLost);
			}
		});

		addLeftAligned(title);
		addLeftAligned(submitButton);
	}

}
