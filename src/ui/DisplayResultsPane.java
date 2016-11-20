package ui;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class DisplayResultsPane extends MyCustomPane {
	public DisplayResultsPane(String error, String description) {
		if (error != null) {
			JLabel errorLabel = new JLabel("Error:");
			JLabel errorL = new JLabel(error);

			addLeftAligned(errorLabel);
			addLeftAligned(errorL);
		} else {
			JLabel descriptionLabel = new JLabel(description);

			addLeftAligned(descriptionLabel);
		}
	}

	public <T> DisplayResultsPane(String error, String description, ArrayList<T> values) {
		this(error, description);

		if (values.size() < 1) {
			JLabel emptyResult = new JLabel("Empty result");
			addLeftAligned(emptyResult);
		} else {
			JList resultsList = new JList<Integer>();

			if (values.get(0) instanceof Integer) {
				resultsList = new JList<Integer>(values.toArray(new Integer[values.size()]));
			} else if (values.get(0) instanceof String) {
				resultsList = new JList<String>(values.toArray(new String[values.size()]));
			}

			addLeftAligned(resultsList);

		}

	}

	public <T> DisplayResultsPane(String error, String description, T value) {
		this(error, description);

		JLabel resultLabel = new JLabel("" + value);

		addLeftAligned(resultLabel);
	}
}
