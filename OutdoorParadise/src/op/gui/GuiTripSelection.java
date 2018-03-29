package op.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

public class GuiTripSelection<T> implements IGuiPage  {

	private JPanel panel;

	private JList<T> list;
	private JComboBox<T> selector;
	private JButton addButton;
	private JButton removeButton;

	private DefaultListModel<T> listModel;

	private List<T> result;
	
	public GuiTripSelection(String name, Set<T> data){
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(name));
		panel.setLayout(new BorderLayout());

		listModel = new DefaultListModel<T>();
		list = new JList<T>(listModel);
		list.setBorder(BorderFactory.createEtchedBorder());

		selector = new JComboBox<T>();
		for(T d : data){
			selector.addItem(d);
		}

		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				T selection = selector.getItemAt(selector.getSelectedIndex());
				if(!listModel.contains(selection)){
					listModel.addElement(selection);
				}
			}

		});

		JPanel selectPanel = new JPanel();
		selectPanel.setLayout(new BorderLayout());
		selectPanel.add(selector, BorderLayout.CENTER);
		selectPanel.add(addButton, BorderLayout.EAST);

		removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				T selection = list.getSelectedValue();
				if(selection != null && listModel.contains(selection)){
					listModel.removeElement(selection);
				}
			}

		});

		panel.add(selectPanel, BorderLayout.NORTH);
		panel.add(list, BorderLayout.CENTER);
		panel.add(removeButton, BorderLayout.SOUTH);
	}

	@Override
	public JPanel getContentPane() {
		return panel;
	}

	public List<T> getSelectedItems(){
		return result;
	}

	@Override
	public int getPanelHeight() {
		return 0;
	}
	
	public void finValues(){
		result = new LinkedList<T>();
		for(int i = 0; i < list.getModel().getSize(); i++){
			result.add(list.getModel().getElementAt(i));
		}
	}
}
