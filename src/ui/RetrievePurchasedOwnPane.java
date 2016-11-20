package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;

import model.Database;

@SuppressWarnings("serial")
public class RetrievePurchasedOwnPane extends MyCustomPane {

	public RetrievePurchasedOwnPane() {
		JLabel title = new JLabel("<html>Retrieve customers who <br>purchase a product <br>he/she worked on<br><br>");

		JButton submitButton = new JButton("Submit");

		// Add action to submit Button
		// Doing anonymous function to avoid storing references to all text
		// fields in the class and using those class variables in a named
		// function
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> employees = new ArrayList<String>();

				String error = null;
				try {
					employees = Database.getEmployeesWhoPurchasedOwnProduct();
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "Employees who purchased own", employees);
			}
		});

		addLeftAligned(title);
		addLeftAligned(submitButton);
	}

}
