package op.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import op.sql.SqlManager;
import op.sql.entries.ExcursionEntry;
import op.sql.entries.TravelerEntry;
import op.sql.entries.TripBookingEntry;
import op.sql.entries.TripExcursionBookingEntry;
import op.sql.entries.TripProductBookingEntry;
import op.sql.entries.TripProductEntry;

public class GuiTripBooking {

	private JDialog window;

	private int stage = 0;
	
	private JPanel curPanel;
	
	private GuiTripSelector gts;
	private GuiTravelers gt;
	private GuiTripSelection<ExcursionEntry> excursionSelection;
	private GuiTripSelection<TripProductEntry> productSelection;
	
	private JButton nextButton;
	
	protected GuiTripBooking(){ }

	public void openWindow(JFrame parent){
		window = new JDialog(parent, true);
		window.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		window.setSize(600, 600);
		window.setLocationRelativeTo(parent);
		
		nextButton = new JButton("Next");
		nextButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				stage++;
				showPage();
			}
			
		});
		
		showPage();
		
		window.setVisible(true);
	}
	
	private void showPage(){
		switch(stage){
		case 0:
			loadPage(gts = new GuiTripSelector(3, 5));
			break;
		case 1:
			gts.finValues();
			loadPage(gt = new GuiTravelers(gts.getSelectedParticipants() - 1));
			break;
			
		case 2:
			gt.finValues();
			try {
				Set<ExcursionEntry> entries = SqlManager.getAllExcursions();
				loadPage(excursionSelection = new GuiTripSelection<ExcursionEntry>("Excursions", entries));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			
		case 3:
			excursionSelection.finValues();
			try {
				Set<TripProductEntry> entries = SqlManager.getAllTripProducts(gts.getSelectedTrip().getId());
				loadPage(productSelection = new GuiTripSelection<TripProductEntry>("Products", entries));
				nextButton.setText("Book!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 4:
			productSelection.finValues();
			finalizeBooking();
			break;
		}
	}
	
	private void loadPage(IGuiPage page){
		
		if(curPanel != null){
			window.remove(curPanel);
		}
		
		curPanel = new JPanel();
		curPanel.setLayout(new BorderLayout());
		
		curPanel.add(new JScrollPane(page.getContentPane()), BorderLayout.CENTER);
		curPanel.add(nextButton, BorderLayout.SOUTH);
		
		window.add(curPanel);
		window.repaint();
		window.validate();
	}
	
	private void finalizeBooking(){
		Date curDate = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(curDate);
		
		try {
			float price = calcPrice();
			TripBookingEntry trip = new TripBookingEntry(currentDate, gts.getDate(), price, gt.getName(), gt.getAddress(), gt.getIban(), gt.getBirthDate(), gt.getSex(), gts.getCancelationInsurance(), gts.getSelectedTrip().getId());
			
			int id = SqlManager.insertTripBooking(trip);
			System.out.println("id= " + id);
			
			for(ExcursionEntry entry : excursionSelection.getSelectedItems()){
				TripExcursionBookingEntry booking = new TripExcursionBookingEntry(id, entry.getId());
				SqlManager.insertExcursionBooking(booking);
			}
			
			for(TripProductEntry entry : productSelection.getSelectedItems()){
				TripProductBookingEntry productEntry = new TripProductBookingEntry(id, entry.getTrip(), entry.getProduct());
				SqlManager.insertBookingProduct(productEntry);
			}
			
			for(TravelerEntry entry : gt.getAllTravelers()){
				SqlManager.insertTraveler(entry, id);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private float calcPrice() throws SQLException{
		float price = gts.getSelectedTrip().getPrice();
		
		for(ExcursionEntry entry : excursionSelection.getSelectedItems()){
			price += entry.getPrice();
		}
		
		for(TripProductEntry entry : productSelection.getSelectedItems()){
			price += entry.getDiscount();
		}
		
		return price;
	}
}
