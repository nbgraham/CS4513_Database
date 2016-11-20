package ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class OpenPopupAction extends AbstractAction {
	private String windowTitle;
	private MyCustomPane innerPane;
	public static JFrame mainFrame;

	public OpenPopupAction(String menuName, String _windowTitle, MyCustomPane _innerPane) {
		super(menuName);

		windowTitle = _windowTitle;
		innerPane = _innerPane;
	}

	public OpenPopupAction(String menuName, MyCustomPane _innerPane) {
		super(menuName);

		if (mainFrame == null) {
			throw new NullPointerException("mainFrame is null");
		}

		windowTitle = "Add a New " + menuName;
		innerPane = _innerPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mainFrame.setEnabled(false);
		innerPane.updateData();
		popup(windowTitle, innerPane);
	}

	private static void popup(String name, JPanel innerPane) {
		JFrame frame = new JFrame(name);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				mainFrame.setEnabled(true);
			}
		});

		frame.getContentPane().add(innerPane);

		frame.pack();
		frame.setVisible(true);
	}

}
