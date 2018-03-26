package op;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import op.gui.Gui;
import op.sql.SqlManager;

public class Main {

	public static void main(String[] args){
		
		try {
			SqlManager.connectToDatabase();
			
			Gui gui = new Gui();
			gui.init();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static class TableList {
		
		private List<String> tables = new ArrayList<String>();
	}
}
