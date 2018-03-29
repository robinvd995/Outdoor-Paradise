package op.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import op.sql.entries.TravelerEntry;

public class GuiTravelers implements IGuiPage{

	private JPanel panel;
	
	private TripBookingPanel tripBookingPanel;
	private TripTravelerPanel tripTravelerPanel;
	
	private String name;
	private String address;
	private String iban;
	private String birthDate;
	private String sex;
	
	private List<TravelerEntry> travelerEntries;
	
	public GuiTravelers(int participants){
		
		panel = new JPanel();
		
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);

		tripBookingPanel = new TripBookingPanel();
		tripTravelerPanel = new TripTravelerPanel(participants);

		layout.putConstraint(SpringLayout.NORTH, tripBookingPanel, 0, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.EAST, tripBookingPanel, 0, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.WEST, tripBookingPanel, 0, SpringLayout.WEST, panel);

		layout.putConstraint(SpringLayout.NORTH, tripTravelerPanel, 0, SpringLayout.SOUTH, tripBookingPanel);
		layout.putConstraint(SpringLayout.EAST, tripTravelerPanel, 0, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.WEST, tripTravelerPanel, 0, SpringLayout.WEST, panel);

		panel.add(tripBookingPanel);
		panel.add(tripTravelerPanel);
		
		panel.setPreferredSize(new Dimension(500, 168 + (participants * 84 + 20)));
	}
	
	public void finValues(){
		name = tripBookingPanel.name.getText();
		address = tripBookingPanel.address.getText();
		iban = tripBookingPanel.iban.getText();
		birthDate = tripBookingPanel.birthDate.getText();
		sex = tripBookingPanel.sex.getText();
		travelerEntries = tripTravelerPanel.getTravelers();
	}
	
	public JPanel getContentPane(){
		return panel;
	}
	
	public int getPanelHeight(){
		return 1000;
	}
	
	public String getName(){
		return name;
	}
	
	public String getAddress(){
		return address;
	}
	
	public String getIban(){
		return iban;
	}
	
	public String getBirthDate(){
		return birthDate;
	}
	
	public String getSex(){
		return sex;
	}
	
	public List<TravelerEntry> getAllTravelers(){
		return travelerEntries;
	}
	
	private static class TripBookingPanel extends JPanel {

		private JTextField name, address, iban, birthDate, sex;

		private JComponent prev;

		private TripBookingPanel(){

			SpringLayout layout = new SpringLayout();
			this.setLayout(layout);

			prev = this;

			name = add(layout, "name");
			address = add(layout, "address");
			iban = add(layout, "iban");
			birthDate = add(layout, "birth day");
			sex = add(layout, "sex");
			//add(layout, "cancelation insurance", cancelationInsurance);

			this.setPreferredSize(new Dimension(200, 168));

			this.setBorder(BorderFactory.createTitledBorder("Hoofdboeker"));
		}

		private JTextField add(SpringLayout l, String name){
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			JLabel label = new JLabel(name);
			label.setPreferredSize(new Dimension(200, 24));
			panel.add(label, BorderLayout.WEST);
			JTextField field = new JTextField();
			panel.add(field, BorderLayout.CENTER);

			l.putConstraint(SpringLayout.NORTH, panel, 4, prev == this ? SpringLayout.NORTH : SpringLayout.SOUTH, prev);
			l.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, this);
			l.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, this);

			prev = panel;

			this.add(panel);
			
			return field;
		}

		/*private void add(SpringLayout l, String name, JCheckBox field){
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			JLabel label = new JLabel(name);
			label.setPreferredSize(new Dimension(200, 24));
			panel.add(label, BorderLayout.WEST);
			field = new JCheckBox();
			panel.add(field, BorderLayout.CENTER);

			l.putConstraint(SpringLayout.NORTH, panel, 4, prev == this ? SpringLayout.NORTH : SpringLayout.SOUTH, prev);
			l.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, this);
			l.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, this);

			prev = panel;

			this.add(panel);
		}*/

	}

	private static class TripTravelerPanel extends JPanel{

		private List<TravelerInput> travelers = new ArrayList<TravelerInput>();
		
		public TripTravelerPanel(int max){
			SpringLayout layout = new SpringLayout();
			this.setLayout(layout);

			JComponent prev = this;

			for(int i = 0; i < max; i++){
				TravelerInput panel = new TravelerInput();

				layout.putConstraint(SpringLayout.NORTH, panel, 4, prev == this ? SpringLayout.NORTH : SpringLayout.SOUTH, prev);
				layout.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, this);
				layout.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, this);

				prev = panel;
				this.add(panel);
				travelers.add(panel);
			}

			this.setPreferredSize(new Dimension(200, max * 84 + 20));

			this.setBorder(BorderFactory.createTitledBorder("Travelers"));
		}
		
		public List<TravelerEntry> getTravelers(){
			List<TravelerEntry> result = new ArrayList<TravelerEntry>();
			for(TravelerInput input : travelers){
				result.add(input.createEntry());
			}
			return result;
		}
	}

	private static class TravelerInput extends JPanel {

		private JTextField name, birthDate;

		private JComponent prev;

		public TravelerInput(){
			SpringLayout layout = new SpringLayout();
			this.setLayout(layout);

			prev = this;

			name = add(layout, "name");
			birthDate = add(layout, "birth day");

			this.setPreferredSize(new Dimension(600, 80));

			this.setBorder(BorderFactory.createTitledBorder("Traveler"));
		}

		private JTextField add(SpringLayout l, String name){
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			JLabel label = new JLabel(name);
			label.setPreferredSize(new Dimension(200, 24));
			panel.add(label, BorderLayout.WEST);
			JTextField field = new JTextField();
			panel.add(field, BorderLayout.CENTER);

			l.putConstraint(SpringLayout.NORTH, panel, 4, prev == this ? SpringLayout.NORTH : SpringLayout.SOUTH, prev);
			l.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, this);
			l.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, this);

			prev = panel;

			this.add(panel);
			return field;
		}
		
		public TravelerEntry createEntry(){
			return new TravelerEntry(name.getText(), birthDate.getText());
		}
	}
}
