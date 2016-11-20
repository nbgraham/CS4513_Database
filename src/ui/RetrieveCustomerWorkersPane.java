package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;

import model.Database;

@SuppressWarnings("serial")
public class RetrieveCustomerWorkersPane extends MyCustomPane {

	public RetrieveCustomerWorkersPane() {
		this.setPreferredSize(new Dimension(300, 150));

		JLabel title = new JLabel("<html>Retrieve all customers who <br>are also workers");
		JButton submitButton = new JButton("Submit");

		// Add action to submit Button
		// Doing anonymous function to avoid storing references to all text
		// fields in the class and using those class variables in a named
		// function
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ArrayList<String> names = new ArrayList<String>();

				String error = null;
				try {
					names = Database.getAllCustomerWhoAreWorkers();
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "All customers who are workers", names);
			}
		});

		addLeftAligned(title);
		addLeftAligned(submitButton);
	}

}
