package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import model.Database;

@SuppressWarnings("serial")
public class RetrieveAverageCostPane extends MyCustomPane {
	public RetrieveAverageCostPane() {
		JLabel title = new JLabel("<html>Retrieve average cost of <br> products produced in year: ");
		JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(2016, 1900, 2500, 1));

		// Create submit button
		JButton submitButton = new JButton("Submit");

		// Add action to submit Button
		// Doing anonymous function to avoid storing references to all text
		// fields in the class and using those class variables in a named
		// function
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get data
				int year = getIntValFromSpinner(yearSpinner);

				float averageCost = -1;

				// Add account to database
				String error = null;
				try {
					averageCost = Database.getAverageCost(year);
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "<html>Average cost of products <br> produced in " + year, averageCost);
			}
		});

		addLeftAligned(title);
		addLeftAligned(yearSpinner);
		addLeftAligned(submitButton);
	}
}
