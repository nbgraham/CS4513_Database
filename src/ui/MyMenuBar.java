package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Database;

@SuppressWarnings("serial")
public class MyMenuBar extends JMenuBar {
	public static JFrame mainFrame;
	private static final JFileChooser fileChooser = new JFileChooser();

	public MyMenuBar() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILES", "csv", "comma separated value");
		fileChooser.setFileFilter(filter);

		// Build menus
		JMenu mainMenu = buildMainMenu();
		JMenu newMenu = buildNewMenu();
		JMenu retrieveMenu = buildRetrieveMenu();
		JMenu deleteMenu = buildDeleteMenu();
		JMenu switchMenu = buildSwitchMenu();

		// Add menus
		this.add(mainMenu);
		this.add(newMenu);
		this.add(retrieveMenu);
		this.add(deleteMenu);
		this.add(switchMenu);
	}

	private static JMenu buildMainMenu() {
		JMenu mainMenu = new JMenu("Future Inc.");

		JMenuItem importItem = new JMenuItem("Import Customers");
		JMenuItem exportItem = new JMenuItem("Export Customers");
		JMenuItem quitItem = new JMenuItem("Quit");

		importItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(mainFrame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						Database.importCustomers(file.getPath());
					} catch (IOException e1) {
						// TODO file chooser error handling
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO file chooser error handling
						e1.printStackTrace();
					}

				}
			}
		});

		exportItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showSaveDialog(mainFrame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						Database.exportCustomers(file.getPath());
					} catch (IOException e1) {
						// TODO file chooser error handling
						e1.printStackTrace();
					}
				}
			}
		});

		quitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
			}
		});

		mainMenu.add(importItem);
		mainMenu.add(exportItem);
		mainMenu.add(quitItem);

		return mainMenu;
	}

	private static JMenu buildNewMenu() {
		JMenu newMenu = new JMenu("New");

		// Create Menu Items
		JMenuItem newEmployee = new JMenuItem(new OpenPopupAction("Employee", new NewEmployeePane()));
		JMenuItem newProduct = new JMenuItem(new OpenPopupAction("Product", new NewProductPane()));
		JMenuItem newCustomer = new JMenuItem(new OpenPopupAction("Customer", new NewCustomerPane()));
		JMenuItem newAccount = new JMenuItem(new OpenPopupAction("Account", new NewAccountPane()));
		JMenuItem newComplaint = new JMenuItem(new OpenPopupAction("Complaint", new NewComplaintPane()));
		JMenuItem newAccident = new JMenuItem(new OpenPopupAction("Accident", new NewAccidentPane()));

		// Add menu items
		newMenu.add(newEmployee);
		newMenu.add(newProduct);
		newMenu.add(newCustomer);
		newMenu.add(newAccount);
		newMenu.add(newComplaint);
		newMenu.add(newAccident);

		return newMenu;
	}

	private static JMenu buildRetrieveMenu() {
		JMenu retrieveMenu = new JMenu("Retrieve");

		// Create Menu Items
		JMenuItem retrieveProductInfo = new JMenuItem(new OpenPopupAction("Product Info",
				"Retrieve Date Produced and Time to Make", new RetrieveDateAndTimeForProductPane()));
		JMenuItem retrieveWorkerOutput = new JMenuItem(new OpenPopupAction("Worker Output",
				"Retrieve Products Produced by Worker", new RetrieveProductsMadeByPane()));
		JMenuItem retrieveQCErrors = new JMenuItem(new OpenPopupAction("Quality Controller Errors",
				"Retrieve Number of Errors by Quality Controller", new RetrieveErrorsQCPane()));
		JMenuItem retrieveCostProduct3Repaired = new JMenuItem(new OpenPopupAction("Cost of Product3s Repaired",
				"Retrieve Cost of all Product3s that were Repaired", new RetrieveCostProduct3RepairedPane()));
		JMenuItem retrieveProductByColor = new JMenuItem(new OpenPopupAction("Purchased By Color",
				"Retrieve Customer who Purchased Product with Color", new RetrievePurchasedByColorPane()));
		JMenuItem retrieveDaysLostFromRepairs = new JMenuItem(new OpenPopupAction("Days Lost from Repairs",
				"Retreive Days Lost from Repairs", new RetrieveDaysLostFromRepairsPane()));
		JMenuItem retrieveCustomerWorkers = new JMenuItem(new OpenPopupAction("Customer Workers",
				"Retrieve Customers who are also Workers", new RetrieveCustomerWorkersPane()));
		JMenuItem retrievePurchasedOwn = new JMenuItem(new OpenPopupAction("Purchased Own",
				"Retrieve Cutomers who purchased a product he/she worked on", new RetrievePurchasedOwnPane()));
		JMenuItem retrieveAverageCost = new JMenuItem(
				new OpenPopupAction("Average Cost", new RetrieveAverageCostPane()));

		// Add menu items
		retrieveMenu.add(retrieveProductInfo);
		retrieveMenu.add(retrieveWorkerOutput);
		retrieveMenu.add(retrieveQCErrors);
		retrieveMenu.add(retrieveCostProduct3Repaired);
		retrieveMenu.add(retrieveProductByColor);
		retrieveMenu.add(retrieveDaysLostFromRepairs);
		retrieveMenu.add(retrieveCustomerWorkers);
		retrieveMenu.add(retrievePurchasedOwn);
		retrieveMenu.add(retrieveAverageCost);

		return retrieveMenu;
	}

	private static JMenu buildDeleteMenu() {
		JMenu deleteMenu = new JMenu("Delete");

		// Create Menu Items
		JMenuItem deleteAccidents = new JMenuItem(
				new OpenPopupAction("Accidents", "Delete Accidents in Range", new DeleteAccidentsPane()));

		// Add menu items
		deleteMenu.add(deleteAccidents);

		return deleteMenu;
	}

	private static JMenu buildSwitchMenu() {
		JMenu switchMenu = new JMenu("Switch");

		// Create Menu Items
		JMenuItem switchTSandQC = new JMenuItem(new OpenPopupAction("Technical Staff <-> Quality Controller",
				"Switch postions of Technical Staff and Quality Controller", new SwitchPositionsPane()));

		// Add menu items
		switchMenu.add(switchTSandQC);

		return switchMenu;
	}

}
