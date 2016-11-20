package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import model.Database;

@SuppressWarnings("serial")
public class RetrievePurchasedByColorPane extends MyCustomPane {

	public RetrievePurchasedByColorPane() {
		JLabel title = new JLabel(
				"<html>Retrieve all customers who purchased <br>all products of a particular color<br><br>");

		JLabel colorLabel = new JLabel("Color:");
		JTextField colorText = new JTextField();

		JButton submitButton = new JButton("Submit");

		// Add action to submit button
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get selected worker
				String color = colorText.getText();

				// Initialize default response
				ArrayList<String> matchingCustomers = new ArrayList<String>();

				// Attempt request
				String error = null;
				try {
					matchingCustomers = Database.getCustomersWhoPurchased(color);
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "Customers who purchased all " + color + "products", matchingCustomers);
			}
		});

		addLeftAligned(title);
		addLeftAligned(colorLabel);
		addLeftAligned(colorText);
		addLeftAligned(submitButton);
	}

}
