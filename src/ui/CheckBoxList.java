package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Component;

// Modified from http://stackoverflow.com/a/24777687/6004931
@SuppressWarnings("serial")
public class CheckBoxList extends JList<JCheckBox> {
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	public CheckBoxList() {
		setCellRenderer(new CellRenderer());
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int index = locationToIndex(e.getPoint());
				if (index != -1) {
					JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index);
					checkbox.setSelected(!checkbox.isSelected());
					repaint();
				}
			}
		});
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public CheckBoxList(ListModel<JCheckBox> model) {
		this();
		setModel(model);
	}

	public <T> CheckBoxList(ArrayList<T> list) {
		this();

		DefaultListModel<JCheckBox> model = new DefaultListModel<JCheckBox>();

		model.setSize(list.size());
		for (int i = 0; i < list.size(); i++) {
			model.setElementAt(new JCheckBox("" + list.get(i)), i);
		}

		setModel(model);
	}

	public <T> void setModel(ArrayList<T> list) {
		DefaultListModel<JCheckBox> model = new DefaultListModel<JCheckBox>();

		model.setSize(list.size());
		for (int i = 0; i < list.size(); i++) {
			model.setElementAt(new JCheckBox("" + list.get(i)), i);
		}

		setModel(model);
	}

	public ArrayList<Integer> getSelectedIndicesArrayList() {
		ListModel<JCheckBox> model = this.getModel();

		ArrayList<Integer> selectedIndices = new ArrayList<Integer>();
		for (int i = 0; i < model.getSize(); i++) {
			if (model.getElementAt(i).isSelected()) {
				selectedIndices.add(i);
			}
		}

		return selectedIndices;
	}

	protected class CellRenderer implements ListCellRenderer<JCheckBox> {
		public Component getListCellRendererComponent(JList<? extends JCheckBox> list, JCheckBox value, int index,
				boolean isSelected, boolean cellHasFocus) {
			JCheckBox checkbox = value;

			// Drawing checkbox, change the appearance here
			checkbox.setBackground(isSelected ? getSelectionBackground() : getBackground());
			checkbox.setForeground(isSelected ? getSelectionForeground() : getForeground());
			checkbox.setEnabled(isEnabled());
			checkbox.setFont(getFont());
			checkbox.setFocusPainted(false);
			checkbox.setBorderPainted(true);
			checkbox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
			return checkbox;
		}
	}
}