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
public class NewAccountPane extends MyCustomPane {

	private JComboBox<Integer> productIDselector;
	private int selectedProductType;

	public NewAccountPane() {
		this.setPreferredSize(new Dimension(300, 350));

		// Create account number entry
		JLabel accountNumberLabel = new JLabel("Account Number:");
		JSpinner accountNumberSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));

		// Create date picker
		JLabel dateLabel = new JLabel("Date:");
		JDatePickerImpl datePicker = newMyDatePanel();

		// Create product cost entry
		JLabel productCostLabel = new JLabel("Product Cost:");
		JSpinner productCostSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 0.1));

		JLabel productIDLabel = new JLabel("For product:");
		productIDselector = new JComboBox<Integer>(getProductIDs());

		JButton submitButton = new JButton("Submit");

		// Add action to submit Button
		// Doing anonymous function to avoid storing references to all text
		// fields in the class and using those class variables in a named
		// function
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get all data
				int accountNumber = getIntValFromSpinner(accountNumberSpinner);
				Date dateEstablished = getSelectedDate();

				float productCost = getFloatValFromSpinner(productCostSpinner);

				int productID = ((Integer) productIDselector.getSelectedItem()).intValue();

				// Add account to database
				String error = null;
				try {
					Database.addAccount(accountNumber, dateEstablished, productCost, productID);
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "Added account");
			}
		});

		// Add all components
		addLeftAligned(accountNumberLabel);
		addLeftAligned(accountNumberSpinner);
		addLeftAligned(dateLabel);
		addLeftAligned(datePicker);
		addLeftAligned(productCostLabel);
		addLeftAligned(productCostSpinner);
		addLeftAligned(productIDLabel);
		addLeftAligned(productIDselector);
		addLeftAligned(submitButton);
	}

	private static ComboBoxModel<Integer> getProductIDs() {
		ArrayList<Integer> res = Database.getAllProductIDsArrayList();
		ComboBoxModel<Integer> model = new DefaultComboBoxModel<Integer>(res.toArray(new Integer[res.size()]));
		return model;
	}

	public void updateData() {
		productIDselector.setModel(getProductIDs());
	}
}
