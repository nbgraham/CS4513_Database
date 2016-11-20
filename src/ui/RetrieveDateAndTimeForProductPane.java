package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import model.Database;
import model.DateProducedAndTimeToMake;

@SuppressWarnings("serial")
public class RetrieveDateAndTimeForProductPane extends MyCustomPane {

	private JComboBox<Integer> productIDOptions;

	public RetrieveDateAndTimeForProductPane() {
		// Create components
		JLabel retrieveDateAndTimeLabel = new JLabel("<html>Retrieve date produced and <br>time spent to make:");
		productIDOptions = new JComboBox<Integer>(getProductIDsModel());
		JButton submitButton = new JButton("Submit");

		// Add action to submit button
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get selected product ID
				int productID = (int) productIDOptions.getSelectedItem();

				// Initialize default results
				DateProducedAndTimeToMake results = new DateProducedAndTimeToMake();

				String error = null;
				try {
					results = Database.getDateAndTimeToMakeFor(productID);
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "<html>Retrieve date produced <br> and time to make <br> for product: " + productID,
						results);
			}
		});

		// Add components to pane
		addLeftAligned(retrieveDateAndTimeLabel, this);
		addLeftAligned(productIDOptions, this);
		addLeftAligned(submitButton, this);
	}

	private static ComboBoxModel<Integer> getProductIDsModel() {
		int[] productIDsInt = Database.getAllProductIDs();
		Integer[] productIDsInteger = new Integer[productIDsInt.length];
		int i = 0;
		for (int value : productIDsInt) {
			productIDsInteger[i++] = Integer.valueOf(value);
		}

		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<Integer>(productIDsInteger);

		return model;
	}

	public void updateData() {
		productIDOptions.setModel(getProductIDsModel());
	}
}
