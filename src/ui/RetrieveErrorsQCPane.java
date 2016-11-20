package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import model.Database;

@SuppressWarnings("serial")
public class RetrieveErrorsQCPane extends MyCustomPane {
	JComboBox<String> qualityControllerOptions;

	public RetrieveErrorsQCPane() {
		// Create components
		JLabel retrieveErrorsLabel = new JLabel("<html>Retrieve number of errors <br>produced by quality controller:");
		qualityControllerOptions = new JComboBox<String>(Database.getAllQualityControllerNames());
		JButton submitButton = new JButton("Submit");

		// Add action to submit button
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedQC = (String) qualityControllerOptions.getSelectedItem();

				int errors = 0;
				String error = null;
				try {
					errors = Database.getErrorsFrom(selectedQC);
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "Errors by " + selectedQC, errors);
			}
		});

		// Add components to pane
		addLeftAligned(retrieveErrorsLabel, this);
		addLeftAligned(qualityControllerOptions, this);
		addLeftAligned(submitButton, this);
	}

	public void updateData() {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(Database.getAllQualityControllerNames());
		qualityControllerOptions.setModel(model);
	}

}
