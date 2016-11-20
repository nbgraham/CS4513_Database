package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import model.Database;

@SuppressWarnings("serial")
public class NewCustomerPane extends MyCustomPane {
	private CheckBoxList purchasesList;

	public NewCustomerPane() {
		this.setPreferredSize(new Dimension(300, 200));

		// Create name entry
		JLabel nameLabel = new JLabel("Name:");
		JTextField nameText = new JTextField();

		// Create address entry
		JLabel addressLabel = new JLabel("Address:");
		JTextField addressText = new JTextField();

		JLabel purchasesLabel = new JLabel("Purchases: ");
		purchasesList = new CheckBoxList(Database.getAllProductIDsArrayList());

		JButton submitButton = new JButton("Submit");

		// Add action to submit Button
		// Doing anonymous function to avoid storing references to all text
		// fields in the class and using those class variables in a named
		// function
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get all data
				String name = nameText.getText();
				String address = addressText.getText();

				ArrayList<Integer> purchaseIDs = new ArrayList<Integer>();
				ArrayList<Integer> purchasesIndices = purchasesList.getSelectedIndicesArrayList();
				for (int i = 0; i < purchasesIndices.size(); i++) {
					String selectedText = purchasesList.getModel().getElementAt(purchasesIndices.get(i)).getText();
					purchaseIDs.add(new Integer(selectedText));
				}

				// Add complaint to database
				String error = null;
				try {
					Database.addCustomer(name, address, purchaseIDs);
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "Added customer");
			}
		});

		// Add all components
		addLeftAligned(nameLabel);
		addLeftAligned(nameText);
		addLeftAligned(addressLabel);
		addLeftAligned(addressText);
		addLeftAligned(purchasesLabel);
		addLeftAligned(purchasesList);
		addLeftAligned(submitButton);
	}

	public void updateData() {
		purchasesList.setModel(Database.getAllProductIDsArrayList());
	}

}
