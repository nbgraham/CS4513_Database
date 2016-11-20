package ui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import model.Database;
import model.EmployeeType;

@SuppressWarnings("serial")
public class NewEmployeePane extends MyCustomPane {

	public NewEmployeePane() {
		this.setPreferredSize(new Dimension(300, 400));

		// Create components
		JLabel newEmployeeLabel = new JLabel("Create New Employee:");

		// Create product type radio buttons
		JRadioButton technicalStaffButton = new JRadioButton("Technical Staff");
		JRadioButton qualityControllerButton = new JRadioButton("Quality Controller");
		JRadioButton workerButton = new JRadioButton("Worker");
		workerButton.setSelected(true); // Default Value

		// Group product type radio buttons
		ButtonGroup employeeTypeButtonGroup = new ButtonGroup();
		employeeTypeButtonGroup.add(technicalStaffButton);
		employeeTypeButtonGroup.add(qualityControllerButton);
		employeeTypeButtonGroup.add(workerButton);

		JLabel nameLabel = new JLabel("Name:");
		JTextField nameText = new JTextField();

		JLabel addressLabel = new JLabel("Address:");
		JTextField addressText = new JTextField();

		// Create optional components for each product
		OptionalEmployeeFieldPane optionalField = new OptionalEmployeeFieldPane();

		technicalStaffButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionalField.showPane(EmployeeType.TECHNICAL_STAFF);
			}
		});

		qualityControllerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionalField.showPane(EmployeeType.QUALITY_CONTROLLER);
			}
		});

		workerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionalField.showPane(EmployeeType.WORKER);
			}
		});

		// Create submit button
		JButton submitButton = new JButton("Submit");

		// Add action to submit Button
		// Doing anonymous function to avoid storing references to all text
		// fields in the class and using those class variables in a named
		// function
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get data
				EmployeeType type = optionalField.getEmployeeType();

				String name = nameText.getText();

				String address = addressText.getText();

				int maxProductsDaily = optionalField.getMaxProductsDaily();
				int productType = optionalField.getProductType();
				String technicalPosition = optionalField.getTechnicalPosition();

				// Add employee to database
				String error = null;
				try {
					if (type == EmployeeType.WORKER) {
						Database.addWorker(name, address, maxProductsDaily);
					} else if (type == EmployeeType.QUALITY_CONTROLLER) {
						Database.addQualityController(name, address, productType);
					} else if (type == EmployeeType.TECHNICAL_STAFF) {
						Database.addTechnicalStaff(name, address, technicalPosition);
					}
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "Added employee");
			}
		});

		// Add components to pane
		addLeftAligned(newEmployeeLabel);
		addLeftAligned(workerButton);
		addLeftAligned(technicalStaffButton);
		addLeftAligned(qualityControllerButton);

		addLeftAligned(nameLabel);
		addLeftAligned(nameText);

		addLeftAligned(addressLabel);
		addLeftAligned(addressText);

		addLeftAligned(optionalField);

		addLeftAligned(submitButton);
	}

}

@SuppressWarnings("serial")
class OptionalEmployeeFieldPane extends MyCustomPane {
	private final static String WORKER_PANEL = "workerPanel";
	private final static String TECHNICAL_STAFF_PANEL = "ts";
	private final static String QUALITY_CONTROLLER_PANEL = "qs";

	private JSpinner maxProductsDailySpinner;
	private JRadioButton product1Button;
	private JRadioButton product2Button;
	private JRadioButton product3Button;
	private JTextField technicalPositionText;

	private EmployeeType selectedType = EmployeeType.WORKER;
	private JPanel container;

	public OptionalEmployeeFieldPane() {
		super(false);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		// Worker Pane
		JPanel workerPane = new JPanel();
		workerPane.setLayout(new BoxLayout(workerPane, BoxLayout.Y_AXIS));

		JLabel maxProductsDailyLabel = new JLabel("Max products daily");
		maxProductsDailySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));

		addLeftAligned(maxProductsDailyLabel, workerPane);
		addLeftAligned(maxProductsDailySpinner, workerPane);

		// Technical Staff Pane
		JPanel tsPane = new JPanel();
		tsPane.setLayout(new BoxLayout(tsPane, BoxLayout.Y_AXIS));
		JLabel technicalPositionLabel = new JLabel("Technical Position:");
		technicalPositionText = new JTextField();

		addLeftAligned(technicalPositionLabel, tsPane);
		addLeftAligned(technicalPositionText, tsPane);

		// Quality Controller Pane
		JPanel qsPane = new JPanel();
		qsPane.setLayout(new BoxLayout(qsPane, BoxLayout.Y_AXIS));

		JLabel productTypeLabel = new JLabel("Product type:");

		// Create product type radio buttons
		product1Button = new JRadioButton("Product 1");
		product1Button.setSelected(true); // Default value
		product2Button = new JRadioButton("Product 2");
		product3Button = new JRadioButton("Product 3");

		// Group product type radio buttons
		ButtonGroup productTypeButtonGroup = new ButtonGroup();
		productTypeButtonGroup.add(product1Button);
		productTypeButtonGroup.add(product2Button);
		productTypeButtonGroup.add(product3Button);

		addLeftAligned(productTypeLabel, qsPane);
		addLeftAligned(product1Button, qsPane);
		addLeftAligned(product2Button, qsPane);
		addLeftAligned(product3Button, qsPane);

		// Container for cards
		container = new JPanel(new CardLayout());

		container.add(workerPane, WORKER_PANEL);
		container.add(qsPane, QUALITY_CONTROLLER_PANEL);
		container.add(tsPane, TECHNICAL_STAFF_PANEL);

		CardLayout layout = (CardLayout) container.getLayout();
		layout.show(container, WORKER_PANEL);

		addLeftAligned(container);
	}

	public EmployeeType getEmployeeType() {
		return selectedType;
	}

	public void showPane(EmployeeType type) {
		CardLayout layout = (CardLayout) container.getLayout();

		if (type == EmployeeType.WORKER) {
			layout.show(container, WORKER_PANEL);
		} else if (type == EmployeeType.TECHNICAL_STAFF) {
			layout.show(container, TECHNICAL_STAFF_PANEL);
		} else if (type == EmployeeType.QUALITY_CONTROLLER) {
			layout.show(container, QUALITY_CONTROLLER_PANEL);
		} else {
			throw new IllegalArgumentException();
		}

		selectedType = type;
	}

	public int getMaxProductsDaily() {
		int maxProductsDaily = -1;

		if (selectedType == EmployeeType.WORKER) {
			maxProductsDaily = getIntValFromSpinner(maxProductsDailySpinner);
		}

		return maxProductsDaily;
	}

	public int getProductType() {
		int productType = -1;

		if (selectedType == EmployeeType.QUALITY_CONTROLLER) {
			productType = 1;
			if (product2Button.isSelected()) {
				productType = 2;
			} else if (product3Button.isSelected()) {
				productType = 3;
			}
		}

		return productType;
	}

	public String getTechnicalPosition() {
		String technicalPosition = null;

		if (selectedType == EmployeeType.TECHNICAL_STAFF) {
			technicalPosition = technicalPositionText.getText();
		}

		return technicalPosition;

	}

}
