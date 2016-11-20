package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.jdatepicker.impl.JDatePickerImpl;

import model.Database;

@SuppressWarnings("serial")
public class NewAccidentPane extends MyCustomPane {
	private JComboBox<Integer> productIDselector;
	private JRadioButton repairButton;

	public NewAccidentPane() {
		this.setPreferredSize(new Dimension(300, 400));

		// Create accidentNumber entry
		JLabel accidentNumberLabel = new JLabel("Accident Number:");
		JSpinner accidentNumberSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));

		// Create date picker
		JLabel dateLabel = new JLabel("Date:");
		JDatePickerImpl datePicker = newMyDatePanel();

		// Create days lost entry
		JLabel daysLostLabel = new JLabel("Days Lost:");
		JSpinner daysLostSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));

		// Create Repair or Production option
		JLabel repairOrProductLabel = new JLabel("Associated with:");

		// Create product type radio buttons
		repairButton = new JRadioButton("Repair");
		repairButton.setSelected(true); // Default value
		JRadioButton productionButton = new JRadioButton("Production");

		// Group buttons
		ButtonGroup repairOrProductionOption = new ButtonGroup();
		repairOrProductionOption.add(repairButton);
		repairOrProductionOption.add(productionButton);

		productIDselector = new JComboBox<Integer>(getProductIDComboBoxModel());

		repairButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				productIDselector.setModel(getRepairComboBoxModel());
			}
		});

		productionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				productIDselector.setModel(getProductIDComboBoxModel());
			}
		});

		JButton submitButton = new JButton("Submit");

		// Add action to submit Button
		// Doing anonymous function to avoid storing references to all text
		// fields in the class and using those class variables in a named
		// function
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get all data
				int accidentNumber = getIntValFromSpinner(accidentNumberSpinner);
				Date dateOf = getSelectedDate();
				int daysLost = getIntValFromSpinner(daysLostSpinner);

				boolean isRepair = true;
				if (productionButton.isSelected()) {
					isRepair = false;
				}

				int associatedID = ((Integer) productIDselector.getSelectedItem()).intValue();

				int repairID = -1;
				int productionID = -1;

				if (isRepair) {
					repairID = associatedID;
				} else {
					productionID = associatedID;
				}

				// Add accident to database
				String error = null;
				try {
					Database.addAccident(accidentNumber, dateOf, daysLost, repairID, productionID);
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "Added accident");
			}
		});

		// Add all components
		addLeftAligned(accidentNumberLabel);
		addLeftAligned(accidentNumberSpinner);
		addLeftAligned(dateLabel);
		addLeftAligned(datePicker);
		addLeftAligned(daysLostLabel);
		addLeftAligned(daysLostSpinner);
		addLeftAligned(repairOrProductLabel);
		addLeftAligned(repairButton);
		addLeftAligned(productionButton);
		addLeftAligned(productIDselector);
		addLeftAligned(submitButton);
	}

	private static ComboBoxModel<Integer> getProductIDComboBoxModel() {
		ComboBoxModel<Integer> model = new DefaultComboBoxModel<Integer>(
				Database.getAllProductIDsArrayList().toArray(new Integer[0]));
		return model;
	}

	private static ComboBoxModel<Integer> getRepairComboBoxModel() {
		ArrayList<Integer> res = Database.getAllRepairIDsArrayList();
		ComboBoxModel<Integer> model = new DefaultComboBoxModel<Integer>(res.toArray(new Integer[res.size()]));
		return model;
	}

	public void updateData() {
		ComboBoxModel<Integer> model;

		if (repairButton.isSelected()) {
			model = getRepairComboBoxModel();
		} else {
			model = getProductIDComboBoxModel();
		}

		productIDselector.setModel(model);
	}
}
