package op.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import op.sql.QueryResult;
import op.sql.SqlManager;

public class Gui {

	private JFrame frame;
	private JTable table;
	private JScrollPane tablePanel;
	
	private JMenuBar menuBar;
	
	public Gui(){}
	
	public void init(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		frame = new JFrame("OP Database Manager");
		frame.setSize(1024, 800);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		
		BorderLayout layout = new BorderLayout();
		
		JPanel inputPanel = new JPanel();
		initInputPanel(inputPanel);
		
		table = new ScrollTable();
		tablePanel = new JScrollPane(table);
		
		mainPanel.setLayout(layout);
		
		mainPanel.add(inputPanel, BorderLayout.NORTH);
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		
		menuBar = new JMenuBar();
		JMenu bookingMenu = new JMenu("Booking");
		JMenuItem bookingTripItem = new JMenuItem("Book Trip");
		bookingTripItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				GuiTripBooking gui = new GuiTripBooking();
				gui.openWindow(frame);
			}
			
		});
		
		bookingMenu.add(bookingTripItem);
		menuBar.add(bookingMenu);
		frame.add(menuBar, BorderLayout.NORTH);
		
		frame.add(mainPanel);
		frame.setVisible(true);
	}
	
	private void initInputPanel(JPanel inputPanel){
		inputPanel.setPreferredSize(new Dimension(1024, 50));
		inputPanel.setBorder(BorderFactory.createTitledBorder("Input"));
		
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10);
		inputPanel.setLayout(layout);
		
		JLabel label = new JLabel("Table");
		
		JComboBox<String> tableBox = new JComboBox<String>();
		SqlManager.getTables().forEach(s -> tableBox.addItem(s));
		
		JButton button = new JButton("Get");
		button.setPreferredSize(new Dimension(100, 60));
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = tableBox.getItemAt(tableBox.getSelectedIndex());
				System.out.println(s);
				initTableData(s);
			}
			
		});
		
		inputPanel.add(label, BorderLayout.WEST);
		inputPanel.add(tableBox, BorderLayout.CENTER);
		inputPanel.add(button, BorderLayout.EAST);
	}
	
	private void initTableData(String tableName){
		QueryResult result = SqlManager.getTableData(tableName);
		TableModel model = createTableModel(result.getColumnHeaders(), result.getData());
		table.setModel(model);
		for(int i = 0; i < table.getColumnCount(); i++){
			table.getColumnModel().getColumn(i).setMinWidth(200);
		}
		table.setMinimumSize(new Dimension(table.getColumnCount() * 200, 200));
		table.validate();
		tablePanel.repaint();
	}
	
	private TableModel createTableModel(String[] columns, String[][] data){
		return new AbstractTableModel(){

			@Override
			public int getColumnCount() {
				return columns.length;
			}

			@Override
			public int getRowCount() {
				return data.length;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				System.out.println(rowIndex + "," + columnIndex);
				return data[rowIndex][columnIndex];
			}
			
			@Override
			public String getColumnName(int index){
				return columns[index];
			}
		};
	}
	
	private static class ScrollTable extends JTable {
		
		public boolean getScrollableTracksViewportWidth() {
		    return false;
		  }
	}
}
