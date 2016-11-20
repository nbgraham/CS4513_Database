package ui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.jdatepicker.impl.JDatePickerImpl;

import model.Database;

@SuppressWarnings("serial")
public class NewProductPane extends MyCustomPane {
	private int selectedProductType;
	
	private JComboBox<String> technicalStaffComboBox;
	private JComboBox<String> testerComboBox;
	private JComboBox<String> producerComboBox;

	public NewProductPane() {
		
		this.setPreferredSize(new Dimension(300, 610));

		// Create components
		JLabel newProductLabel = new JLabel("Create New Product");

		// Create product type radio buttons
		JRadioButton product1Button = new JRadioButton("Product 1");
		JRadioButton product2Button = new JRadioButton("Product 2");
		JRadioButton product3Button = new JRadioButton("Product 3");

		// Set default value
		product1Button.setSelected(true);
		selectedProductType = 1;

		// Group product type radio buttons
		ButtonGroup productTypeButtonGroup = new ButtonGroup();
		productTypeButtonGroup.add(product1Button);
		productTypeButtonGroup.add(product2Button);
		productTypeButtonGroup.add(product3Button);

		// Create product ID entry
		JLabel productIDLabel = new JLabel("Product ID");
		JSpinner productIDSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));

		// Create date picker
		JLabel dateProducedLabel = new JLabel("Date Produced:");
		JDatePickerImpl dateProduced = newMyDatePanel(0);

		// Create minutes to make entry
		JLabel minutesToMakeLabel = new JLabel("Minutes to make");
		JSpinner minutesToMakeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));

		// Create entry for product size
		JLabel sizeLabel = new JLabel("Size:");
		JLabel lengthLabel = new JLabel("Length (cm):");
		JLabel widthLabel = new JLabel("Width (cm):");
		JLabel heightLabel = new JLabel("Height (cm):");

		JSpinner lengthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
		JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
		JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));

		JLabel producerLabel = new JLabel("Worker Produced:");
		producerComboBox = new JComboBox<String>(Database.getAllWorkerNames());

		JLabel testerLabel = new JLabel("Tester:");
		testerComboBox = new JComboBox<String>(Database.getAllQualityControllerNames());
		
		JCheckBox repairCheckBox = new JCheckBox("Repaired?");
		technicalStaffComboBox = new JComboBox<String>(Database.getAllTechnicalStaffNames());
		JDatePickerImpl dateRepaired = newMyDatePanel(1);
		
		technicalStaffComboBox.setEnabled(false);
		dateRepaired.setEnabled(false);
		dateRepaired.setVisible(false);
		
		repairCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				technicalStaffComboBox.setEnabled(!technicalStaffComboBox.isEnabled());
				dateRepaired.setEnabled(!dateRepaired.isEnabled());
				dateRepaired.setVisible(!dateRepaired.isVisible());
			}
		});
		
		OptionalProductFieldPane optionalPane = new OptionalProductFieldPane();

		product1Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedProductType = 1;
				optionalPane.showPane(selectedProductType);
			}
		});

		product2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedProductType = 2;
				optionalPane.showPane(selectedProductType);
			}
		});

		product3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedProductType = 3;
				optionalPane.showPane(selectedProductType);
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

				int productID = getIntValFromSpinner(productIDSpinner);
				Date dateProduced = getSelectedDate(0);
				int minutesToMake = getIntValFromSpinner(minutesToMakeSpinner);
				int height = getIntValFromSpinner(heightSpinner);
				int length = getIntValFromSpinner(lengthSpinner);
				int width = getIntValFromSpinner(widthSpinner);
				String testerName = (String) testerComboBox.getSelectedItem();
				String producerName = (String) producerComboBox.getSelectedItem();
				int weight = optionalPane.getWeight();
				String color = optionalPane.getColor();
				String software = optionalPane.getSoftware();
				
				boolean repaired = repairCheckBox.isSelected();
				String technicalStaffName = (String) technicalStaffComboBox.getSelectedItem();
				Date dateRepaired = getSelectedDate(1);

				// Add product to database
				String error = null;
				try {
					Database.addProduct(productID, dateProduced, minutesToMake, height, length, width,
							selectedProductType, testerName, producerName, weight, color, software);
					
					if (repaired) {
						Database.addRepair(technicalStaffName, productID, dateRepaired);
					}
				} catch (SQLException e1) {
					error = e1.getMessage();
					e1.printStackTrace();
				}

				showResults(error, "Added product");
			}
		});

		// Add components to pane
		addLeftAligned(newProductLabel);

		addLeftAligned(product1Button);
		addLeftAligned(product2Button);
		addLeftAligned(product3Button);

		addLeftAligned(productIDLabel);
		addLeftAligned(productIDSpinner);

		addLeftAligned(dateProducedLabel);
		addLeftAligned(dateProduced);

		addLeftAligned(minutesToMakeLabel);
		addLeftAligned(minutesToMakeSpinner);

		addLeftAligned(sizeLabel);

		addLeftAligned(lengthLabel);
		addLeftAligned(lengthSpinner);

		addLeftAligned(widthLabel);
		addLeftAligned(widthSpinner);

		addLeftAligned(heightLabel);
		addLeftAligned(heightSpinner);

		addLeftAligned(optionalPane);

		addLeftAligned(producerLabel);
		addLeftAligned(producerComboBox);

		addLeftAligned(testerLabel);
		addLeftAligned(testerComboBox);
		
		addLeftAligned(repairCheckBox);
		addLeftAligned(technicalStaffComboBox);
		addLeftAligned(dateRepaired);

		addLeftAligned(submitButton);
	}
	
	public void updateData() {
		testerComboBox.setModel(new DefaultComboBoxModel<String>(Database.getAllQualityControllerNames()));
		producerComboBox.setModel(new DefaultComboBoxModel<String>(Database.getAllWorkerNames()));
		technicalStaffComboBox.setModel(new DefaultComboBoxModel<String>(Database.getAllTechnicalStaffNames()));
	}

}

@SuppressWarnings("serial")
class OptionalProductFieldPane extends MyCustomPane {
	private JTextField softwareText;
	private JTextField colorText;
	private JSpinner weightSpinner;

	private JPanel container;

	public OptionalProductFieldPane() {
		super(false);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		// Product 1 pane
		JPanel product1Pane = new JPanel();
		product1Pane.setLayout(new BoxLayout(product1Pane, BoxLayout.Y_AXIS));
		JLabel softwareLabel = new JLabel("Software:");
		softwareText = new JTextField();

		addLeftAligned(softwareLabel, product1Pane);
		addLeftAligned(softwareText, product1Pane);

		// Product 2 pane
		JPanel product2Pane = new JPanel();
		product2Pane.setLayout(new BoxLayout(product2Pane, BoxLayout.Y_AXIS));

		JLabel colorLabel = new JLabel("Color:");
		colorText = new JTextField();

		addLeftAligned(colorLabel, product2Pane);
		addLeftAligned(colorText, product2Pane);

		// Product 3 pane
		JPanel product3Pane = new JPanel();
		product3Pane.setLayout(new BoxLayout(product3Pane, BoxLayout.Y_AXIS));

		JLabel weightLabel = new JLabel("Weight (kg):");
		weightSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));

		addLeftAligned(weightLabel, product3Pane);
		addLeftAligned(weightSpinner, product3Pane);

		// Container for cards
		container = new JPanel(new CardLayout());

		container.add(product1Pane, "1");
		container.add(product2Pane, "2");
		container.add(product3Pane, "3");

		CardLayout layout = (CardLayout) container.getLayout();
		layout.show(container, "1");

		addLeftAligned(container);
	}

	public void showPane(int productNum) {
		String paneCode = "" + productNum;

		CardLayout layout = (CardLayout) container.getLayout();

		layout.show(container, paneCode);
	}

	public int getWeight() {
		return getIntValFromSpinner(weightSpinner);
	}

	public String getSoftware() {
		return softwareText.getText();
	}

	public String getColor() {
		return colorText.getText();

	}
}
