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
public class RetrieveCostProduct3RepairedPane extends MyCustomPane {
	private JComboBox<String> qualityControllerCombo;

	public RetrieveCostProduct3RepairedPane() {
		JLabel title = new JLabel(
				"<html>Get the total costs of the products <br>in the product3 category which were <br>repaired at the request of a <br>particular quality controller<br><br>");

		// Create qualityController entry
		JLabel qualityControllerLabel = new JLabel("Quality Controller");
		qualityControllerCombo = new JComboBox<String>(Database.getAllQualityControllerNames());

		JButton submitButton = new JButton("Submit");

		// Add action to submit Button
		// Doing anonymous function to avoid storing references to all text
		// fields in the class and using those class variables in a named
		// function
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedQualityController = (String) qualityControllerCombo.getSelectedItem();

				float cost = 0;
				String error = null;
				try {
					cost = Database.retrieveProduct3Cost(selectedQualityController);
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "<html>Retrieve cost of product3s <br>repaired by " + selectedQualityController,
						cost);
			}
		});

		// Add components to pane
		addLeftAligned(title);
		addLeftAligned(qualityControllerLabel);
		addLeftAligned(qualityControllerCombo);
		addLeftAligned(submitButton);
	}

	public void updateData() {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(Database.getAllQualityControllerNames());
		qualityControllerCombo.setModel(model);
	}

}
