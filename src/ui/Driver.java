package ui;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import model.Database;

public class Driver {

	public static void main(String[] args) {
		// Connect to the DB
		// Quit if cannot connect
		if (!Database.connect()) {
			System.err.println("Failed to connect to database. System not started");
			return;
		} else {
			System.out.println("Connected to database");
		}

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.out.println("Starting UI...");
				createAndShowGUI(true);
				System.out.println("UI ready");
			}
		});
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	public static JFrame createAndShowGUI(boolean exitOnClose) {
		// Create and set up the window.
		JFrame frame = new JFrame("WELCOME TO THE DATABASE SYSTEM OF FUTURE, INC.");

		// Disconnect database and exit application on close
		if (exitOnClose) {
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				frame.setVisible(false);
				Database.disconnect();
			}
		});

		// Set this main frame for necessary classes
		OpenPopupAction.mainFrame = frame;
		MyMenuBar.mainFrame = frame;

		// Set up home panel
		JPanel homePanel = new HomePanel();
		frame.getContentPane().add(homePanel);

		// Set up menu bar
		JMenuBar menuBar = new MyMenuBar();
		frame.setJMenuBar(menuBar);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
		
		return frame;
	}
}
