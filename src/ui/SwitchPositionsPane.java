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
public class SwitchPositionsPane extends MyCustomPane {
	JComboBox<String> qualityControllerCombo;
	JComboBox<String> technicalStaffCombo;

	public SwitchPositionsPane() {
		JLabel title = new JLabel("Switch technical staff and quality controller");

		JLabel technicalStaffLabel = new JLabel("Technical Staff:");
		technicalStaffCombo = new JComboBox<String>(Database.getAllTechnicalStaffNames());

		JLabel qualityControllerLabel = new JLabel("Quality Controller:");
		qualityControllerCombo = new JComboBox<String>(Database.getAllQualityControllerNames());

		JButton submitButton = new JButton("Submit");

		// Add action to submit Button
		// Doing anonymous function to avoid storing references to all text
		// fields in the class and using those class variables in a named
		// function
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Add accident to database
				String technicalStaffName = (String) technicalStaffCombo.getSelectedItem();
				String qualityControllerName = (String) qualityControllerCombo.getSelectedItem();

				String error = null;
				try {
					Database.switchPositions(technicalStaffName, qualityControllerName);
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "<html>Postions switched <br>" + technicalStaffName + "<->" + qualityControllerName);
			}
		});

		addLeftAligned(title);

		addLeftAligned(technicalStaffLabel);
		addLeftAligned(technicalStaffCombo);

		addLeftAligned(qualityControllerLabel);
		addLeftAligned(qualityControllerCombo);

		addLeftAligned(submitButton);
	}

	public void updateData() {
		DefaultComboBoxModel<String> qcModel = new DefaultComboBoxModel<String>(
				Database.getAllQualityControllerNames());
		qualityControllerCombo.setModel(qcModel);

		DefaultComboBoxModel<String> tsModel = new DefaultComboBoxModel<String>(Database.getAllTechnicalStaffNames());
		technicalStaffCombo.setModel(tsModel);
	}

}