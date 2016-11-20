package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import model.Database;

@SuppressWarnings("serial")
public class RetrieveProductsMadeByPane extends MyCustomPane {
	JComboBox<String> workerOptions;

	public RetrieveProductsMadeByPane() {
		// Create components
		JLabel retrieveProductsLabel = new JLabel("Retrieve products made by worker:");
		workerOptions = new JComboBox<String>(Database.getAllWorkerNames());
		JButton submitButton = new JButton("Submit");

		// Add action to submit button
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get selected worker
				String selectedWorker = (String) workerOptions.getSelectedItem();

				// Initialize default response
				ArrayList<Integer> productsMadeBy = new ArrayList<Integer>();

				// Attempt request
				String error = null;
				try {
					productsMadeBy = Database.getProductsMadeBy(selectedWorker);
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "Products made by " + selectedWorker, productsMadeBy);
			}
		});

		// Add components to pane
		addLeftAligned(retrieveProductsLabel, this);
		addLeftAligned(workerOptions, this);
		addLeftAligned(submitButton, this);
	}

	public void updateData() {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(Database.getAllWorkerNames());
		workerOptions.setModel(model);
	}
}
