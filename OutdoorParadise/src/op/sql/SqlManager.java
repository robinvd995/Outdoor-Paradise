package op.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import op.sql.QueryBuilder.QueryEntryFrom;
import op.sql.QueryBuilder.QueryEntrySelect;
import op.sql.entries.ExcursionEntry;
import op.sql.entries.TravelerEntry;
import op.sql.entries.TripBookingEntry;
import op.sql.entries.TripEntry;
import op.sql.entries.TripExcursionBookingEntry;
import op.sql.entries.TripProductBookingEntry;
import op.sql.entries.TripProductEntry;

public class SqlManager {

	private static Connection connection;

	private static Set<String> tables = new TreeSet<String>();

	private static QueryBuilder builder = new QueryBuilder();

	public static final void connectToDatabase() throws SQLException, IOException {
		SqlConnectionInfo info = SqlConnectionInfo.load();
		String connectionString = //"jdbc:sqlserver://" + info.getDatabaseHost() + "," + info.getDatabaseUser() + "," + info.getDatabasePassword();
				"jdbc:" + info.getConnectionString(); /*"sqlserver://MSI\\SQLEXPRESS;DatabaseName=Database;integratedSecurity=true"*/;
				System.out.println(connectionString);
				connection = DriverManager.getConnection(connectionString);

				if(connection != null){
					DatabaseMetaData md = connection.getMetaData();
					ResultSet rs = md.getTables(null, null, "%", new String[]{"TABLE"});
					while (rs.next()) {
						String table = rs.getString(3);
						if(!table.startsWith("trace")){
							tables.add(table);
						}
					}
					System.out.println("Found " + tables.size() + " tables!");
				}
	}

	public static Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

	public static void closeConnection() throws SQLException {
		connection.close();
	}

	public static void registerTables(String... tableEntries){
		for(String s : tableEntries) tables.add(s);
	}

	public static QueryResult getTableData(String table){
		if(!tables.contains(table))
			throw new RuntimeException("No table found for name " + table + "!");

		builder.add(new QueryEntrySelect("*"));
		builder.add(new QueryEntryFrom(table));
		ResultSet result = executeQuery();
		try {
			return QueryResult.createResult(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new QueryResult(0,0);
	}

	private static ResultSet executeQuery(){
		String query = builder.buildQueryString();
		try {
			Statement statement = createStatement();
			ResultSet rs = statement.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static ResultSet executeQuery(String query){
		try {
			Statement statement = createStatement();
			ResultSet rs = statement.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Set<ExcursionEntry> getAllExcursions() throws SQLException{
		String query = "SELECT * FROM Excursion";
		ResultSet resultSet = executeQuery(query);
		ResultSetMetaData md = resultSet.getMetaData();
		List<String> columnHeaders = new ArrayList<String>();

		for (int i = 0; i < md.getColumnCount(); i++) {
			String s = md.getColumnName(i + 1);
			columnHeaders.add(s);
		}

		Set<ExcursionEntry> entries = new HashSet<ExcursionEntry>();

		while (resultSet.next()) {
			int id = 0;
			String desc = "";
			boolean guide = false;
			float price = 0.0f;
			for (String header : columnHeaders) {
				switch(header){
				case "id":
					id = Integer.valueOf(String.valueOf(resultSet.getObject(header)));
					break;

				case "guide":
					guide = Boolean.valueOf(String.valueOf(resultSet.getObject(header)));
					break;

				case "description":
					desc = String.valueOf(resultSet.getObject(header));
					break;

				case "price":
					price = Float.valueOf(String.valueOf(resultSet.getObject(header)));
					break;
				}
			}
			ExcursionEntry entry = new ExcursionEntry(id, desc, guide, price);
			entries.add(entry);
		}
		return entries;
	}

	public static Set<TripProductEntry> getAllTripProducts(int tripid) throws SQLException{
		String query = "SELECT * FROM Trip_Product WHERE trip = " + tripid;
		ResultSet resultSet = executeQuery(query);
		ResultSetMetaData md = resultSet.getMetaData();
		List<String> columnHeaders = new ArrayList<String>();

		for (int i = 0; i < md.getColumnCount(); i++) {
			String s = md.getColumnName(i + 1);
			columnHeaders.add(s);
		}

		Set<TripProductEntry> entries = new HashSet<TripProductEntry>();

		while (resultSet.next()) {
			int product = 0;
			int trip = 0;
			float discount = 0;
			for (String header : columnHeaders) {
				switch(header){
				case "product":
					product = Integer.valueOf(String.valueOf(resultSet.getObject(header)));
					break;

				case "trip":
					trip = Integer.valueOf(String.valueOf(resultSet.getObject(header)));
					break;

				case "discount":
					discount = Float.valueOf(String.valueOf(resultSet.getObject(header)));
					break;
				}
			}
			TripProductEntry entry = new TripProductEntry(product, trip, discount);
			entries.add(entry);
		}
		return entries;
	}

	public static Set<TripEntry> getAllTrips() throws SQLException{
		String query = "SELECT * FROM Trip";
		ResultSet resultSet = executeQuery(query);

		Set<TripEntry> entries = new HashSet<TripEntry>();

		while (resultSet.next()) {
			int id = Integer.valueOf(String.valueOf(resultSet.getObject("id")));
			String description = String.valueOf(resultSet.getObject("description"));
			int duration = Integer.valueOf(String.valueOf(resultSet.getObject("duration")));
			float price = Float.valueOf(String.valueOf(resultSet.getObject("price")));
			int min_participants = Integer.valueOf(String.valueOf(resultSet.getObject("min_participants")));
			int max_participants = Integer.valueOf(String.valueOf(resultSet.getObject("max_participants")));
			boolean children_allowed = Boolean.valueOf(String.valueOf(resultSet.getObject("children_allowed")));
			TripEntry entry = new TripEntry(id, description, duration, price, min_participants, max_participants, children_allowed);
			entries.add(entry);
		}
		return entries;
	}

	public static int insertTripBooking(TripBookingEntry b) throws SQLException{
		String date = wrap(b.getDate());
		String startDate = wrap(b.getStartDate());
		float price = b.getPrice();
		String name = wrap(b.getName());
		String address = wrap(b.getAddress());
		String iban = wrap(b.getIban());
		String birthDate = wrap(b.getBirthDate());
		String sex = wrap(b.getSex());
		int insurance = b.isCancInsurance() ? 1 : 0;
		int trip = b.getTripId();
		String query = "INSERT INTO Trip_Booking (booking_date, start_date, price, name, address, iban, birth_date, sex, cancellation_insurance, trip)"
				+ "VALUES(" + date + "," + startDate + "," + price + "," + name + "," + address + "," + iban
				+ "," + birthDate + "," + sex + "," + insurance + "," + trip + ")";

		System.out.println(query);

		PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		int id = rs.getInt(1);
		return id;
	}
	
	public static void insertTraveler(TravelerEntry entry, int bookingId) throws SQLException{
		String name = entry.getName();
		String birthDate = entry.getBirthDate();
		
		String query = "INSERT INTO Traveler (name, birth_date, booking) VALUES (" + name + "," + birthDate + "," + bookingId + ")";
		System.out.println(query);
		
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.executeUpdate();
	}
	
	public static void insertExcursionBooking(TripExcursionBookingEntry entry) throws SQLException{
		int tripId = entry.getTripId();
		int excursionId = entry.getExcursionId();
		
		String query = "INSERT INTO Excursion_Booking (excursion, booking) VALUES (" + excursionId + "," + tripId + ")";
		System.out.println(query);
		
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.executeUpdate();
	}
	
	
	public static void insertBookingProduct(TripProductBookingEntry entry) throws SQLException{
		int productId = entry.getProductId();
		int tripId = entry.getTripId();
		int bookingId = entry.getTripBookingId();
		
		String query = "INSERT INTO Trip_Product_Booking(product, trip, booking) VALUES (" + productId + "," + tripId + "," + bookingId + ")";
		System.out.println(query);
		
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.executeUpdate();
	}
	
	public static float getPriceOfProduct(int productNumber) throws SQLException{
		String query = "SELECT price FROM product WHERE id = " + productNumber;
		System.out.println(query);
		ResultSet result = executeQuery(query);
		result.next();
		return Float.parseFloat(String.valueOf(result.getObject("price")));
	}

	public static Set<String> getTables(){
		return tables;
	}
	
	public static String wrap(String entry){
		return "'" + entry + "'";
	}
}
