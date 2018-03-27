package op.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import op.sql.QueryBuilder.QueryEntryFrom;
import op.sql.QueryBuilder.QueryEntrySelect;

public class SqlManager {

    private static Connection connection;

    private static Set<String> tables = new TreeSet<String>();
    
    private static QueryBuilder builder = new QueryBuilder();

    public static final void connectToDatabase() throws SQLException, IOException {
    	SqlConnectionInfo info = SqlConnectionInfo.load();
    	String connectionString = //"jdbc:sqlserver://" + info.getDatabaseHost() + "," + info.getDatabaseUser() + "," + info.getDatabasePassword();
    			"jdbc:" + /*info.getConnectionString();*/ "sqlserver://MSI\\SQLEXPRESS;DatabaseName=Database;integratedSecurity=true";
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
    
    public static Set<String> getTables(){
    	return tables;
    }
}
