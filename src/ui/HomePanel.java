package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HomePanel extends JPanel {
	public HomePanel() {
		this.setLayout(new GridBagLayout());

		// Instantiate grid constraints to be modified and used by all panels
		GridBagConstraints c = new GridBagConstraints();

		// Add retrieve products made by pane
		JPanel retrieveProductsMadeByPane = new RetrieveProductsMadeByPane();

		// Set size and location in grid
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;

		this.add(retrieveProductsMadeByPane, c);

		// Add retrieve date produced and time spent on product pane
		JPanel retrieveDateAndTimeForProductPane = new RetrieveDateAndTimeForProductPane();

		// Set size and location in grid
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 1;

		this.add(retrieveDateAndTimeForProductPane, c);

		// Add retrieve errors from a particular quality controller pane
		JPanel retrieveErrorsPane = new RetrieveErrorsQCPane();

		// Set size and location in grid
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 1;

		this.add(retrieveErrorsPane, c);

		// Add new product pane
		JPanel newProductPane = new NewProductPane();

		// Set size and location in grid
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 3;
		c.gridwidth = 1;

		this.add(newProductPane, c);
	}
}
