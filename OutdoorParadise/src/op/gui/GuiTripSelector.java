package op.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import op.sql.SqlManager;
import op.sql.entries.TripEntry;

public class GuiTripSelector implements IGuiPage{

	private JPanel panel;
	
	private Set<TripEntry> trips;
	
	private JComboBox<TripEntry> tripSelector;
	private JComboBox<Integer> participantSelector;
	private JTextField date;
	private JCheckBox cancelationInsurance;
	
	private TripEntry tripEntry;
	private String dateString;
	private boolean cancIns;
	
	public GuiTripSelector(int min, int max){
		
		try {
			trips = SqlManager.getAllTrips();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Trip"));

		tripSelector = new JComboBox<TripEntry>();
		if(trips != null){
			trips.forEach(t -> tripSelector.addItem(t));
		}
		tripSelector.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				updateParticipants();
			}
			
		});
		
		participantSelector = new JComboBox<Integer>();
		updateParticipants();
		
		date = new JTextField();
		
		cancelationInsurance = new JCheckBox();
		
		SpringLayout l = new SpringLayout();
		panel.setLayout(l);
		
		LabeledComponent ts = new LabeledComponent("Trip", tripSelector);
		LabeledComponent ps = new LabeledComponent("Participants", participantSelector);
		LabeledComponent d = new LabeledComponent("Date", date);
		LabeledComponent ci = new LabeledComponent("Cancelation Insurance", cancelationInsurance);
		
		l.putConstraint(SpringLayout.NORTH, ts, 0, SpringLayout.NORTH, panel);
		l.putConstraint(SpringLayout.EAST, ts, 0, SpringLayout.EAST, panel);
		l.putConstraint(SpringLayout.WEST, ts, 0, SpringLayout.WEST, panel);
		
		l.putConstraint(SpringLayout.NORTH, ps, 5, SpringLayout.SOUTH, ts);
		l.putConstraint(SpringLayout.EAST, ps, 0, SpringLayout.EAST, panel);
		l.putConstraint(SpringLayout.WEST, ps, 0, SpringLayout.WEST, panel);
		
		l.putConstraint(SpringLayout.NORTH, d, 5, SpringLayout.SOUTH, ps);
		l.putConstraint(SpringLayout.EAST, d, 0, SpringLayout.EAST, panel);
		l.putConstraint(SpringLayout.WEST, d, 0, SpringLayout.WEST, panel);
		
		l.putConstraint(SpringLayout.NORTH, ci, 5, SpringLayout.SOUTH, d);
		l.putConstraint(SpringLayout.EAST, ci, 0, SpringLayout.EAST, panel);
		l.putConstraint(SpringLayout.WEST, ci, 0, SpringLayout.WEST, panel);
		
		panel.add(ts);
		panel.add(ps);
		panel.add(d);
		panel.add(ci);
	}
	
	private void updateParticipants(){
		participantSelector.removeAllItems();
		
		TripEntry entry = tripSelector.getItemAt(tripSelector.getSelectedIndex());
		
		int min = entry.getMin_participants();
		int max = entry.getMax_participants();
		
		for(int i = min; i <= max; i++){
			participantSelector.addItem(i);
		}
	}
	
	public int getSelectedParticipants(){
		return participantSelector.getItemAt(participantSelector.getSelectedIndex());
	}
	
	@Override
	public JPanel getContentPane() {
		return panel;
	}

	@Override
	public int getPanelHeight() {
		return 0;
	}
	
	public void finValues() {
		tripEntry = tripSelector.getItemAt(tripSelector.getSelectedIndex());
		dateString = date.getText();
		cancIns = cancelationInsurance.isSelected();
	}

	public TripEntry getSelectedTrip(){
		return tripEntry;
	}
	
	public String getDate(){
		return dateString;
	}
	
	public boolean getCancelationInsurance(){
		return cancIns;
	}
	
	private static class LabeledComponent extends JPanel {
		
		public LabeledComponent(String name, JComponent component){
			this.setLayout(new BorderLayout());
			JLabel label = new JLabel(name);
			label.setPreferredSize(new Dimension(200, 24));
			this.add(label, BorderLayout.WEST);
			this.add(component,BorderLayout.CENTER);
		}
	}
	
}
