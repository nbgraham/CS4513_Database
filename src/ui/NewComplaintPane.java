package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.jdatepicker.impl.JDatePickerImpl;

import model.Database;

@SuppressWarnings("serial")
public class NewComplaintPane extends MyCustomPane {
	private JComboBox<Integer> associatedPurchaseSelector;

	public NewComplaintPane() {
		this.setPreferredSize(new Dimension(300, 350));

		// Create account number entry
		JLabel complaintIDLabel = new JLabel("Complaint ID:");
		JSpinner complaintIDSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));

		// Create date picker
		JLabel dateLabel = new JLabel("Date:");
		JDatePickerImpl datePicker = newMyDatePanel();

		// Create description entry
		JLabel descriptionLabel = new JLabel("Description:");
		JTextField descriptionText = new JTextField();

		// Create expected treatment entry
		JLabel expectedTreatmentLabel = new JLabel("Expected treatment:");
		JTextField expectedTreatmentText = new JTextField();

		// Create associated purchase selection
		JLabel associatedPurchaseLabel = new JLabel("Associated Purchase:");
		associatedPurchaseSelector = new JComboBox<Integer>(getPurchaseIDs());

		JButton submitButton = new JButton("Submit");

		// Add action to submit Button
		// Doing anonymous function to avoid storing references to all text
		// fields in the class and using those class variables in a named
		// function
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get all data
				int complaintID = getIntValFromSpinner(complaintIDSpinner);
				Date dateOf = getSelectedDate();
				String description = descriptionText.getText();
				String expectedTreatment = expectedTreatmentText.getText();
				int purchaseID = ((Integer) associatedPurchaseSelector.getSelectedItem()).intValue();

				// Add complaint to database
				String error = null;
				try {
					Database.addComplaint(complaintID, dateOf, description, expectedTreatment, purchaseID);
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "Added complaint");
			}
		});

		// Add all components
		addLeftAligned(complaintIDLabel);
		addLeftAligned(complaintIDSpinner);
		addLeftAligned(dateLabel);
		addLeftAligned(datePicker);
		addLeftAligned(descriptionLabel);
		addLeftAligned(descriptionText);
		addLeftAligned(expectedTreatmentLabel);
		addLeftAligned(expectedTreatmentText);
		addLeftAligned(associatedPurchaseLabel);
		addLeftAligned(associatedPurchaseSelector);
		addLeftAligned(submitButton);
	}

	private static ComboBoxModel<Integer> getPurchaseIDs() {
		ArrayList<Integer> res = Database.getPurchaseIDs();
		ComboBoxModel<Integer> model = new DefaultComboBoxModel<Integer>(res.toArray(new Integer[res.size()]));
		return model;
	}

	public void updateData() {
		associatedPurchaseSelector.setModel(getPurchaseIDs());
	}

}
