package op.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QueryResult {

	private final int columns;
	private final int rows;
	
	private String[] columnHeaders;
	private String[][] resultData;
	
	public QueryResult(int columns, int rows){
		this.columns = columns;
		this.rows = rows;
		columnHeaders = new String[columns];
		resultData = new String[rows][columns];
	}
	
	protected void setColumnHeader(int column, String header){
		if(column < 0 || column >= columns)
			throw new IllegalArgumentException("Column header out of bounds!");
		
		columnHeaders[column] = header;
	}
	
	protected void insertData(int column, int row, String data){
		if(column < 0 || column >= columns)
			throw new IllegalArgumentException("Column out of bounds!");
		
		if(row < 0 || row >= rows)
			throw new IllegalArgumentException("Row out of bounds!");
		
		resultData[row][column] = data;
	}
	
	public String[] getColumnHeaders(){
		return columnHeaders;
	}
	
	public String[][] getData(){
		return resultData;
	}
	
	@Override
	public String toString() {
		return "QueryResult [columns=" + columns + ", rows=" + rows + ", columnHeaders="
				+ Arrays.toString(columnHeaders) + ", resultData=" + dataString() + "]";
	}
	
	private String dataString(){
		String s = "";
		for(int i = 0; i < resultData.length; i++){
			s += "[";
			for(int j = 0; j < resultData[i].length; j++){
				s += resultData[i][j] + " ";
			}
			s += "]";
		}
		return s;
	}

	public static QueryResult createResult(ResultSet resultSet) throws SQLException{
		ResultSetMetaData md = resultSet.getMetaData();
		final int columns = md.getColumnCount();
        List<String> columnHeaders = new LinkedList<String>();
        
        for (int i = 0; i < columns; i++) {
            String s = md.getColumnName(i + 1);
            columnHeaders.add(s);
        }
        
        LinkedList<String[]> rows = new LinkedList<String[]>();
        
        while(resultSet.next()){
        	String[] row = new String[columns];
        	for(int i = 0; i < columns; i++){
        		String header = columnHeaders.get(i);
        		row[i] = String.valueOf(resultSet.getObject(header));
        	}
        	rows.add(row);
        }
        
        QueryResult result = new QueryResult(columns, rows.size());
        for(int i = 0; i < columns; i++){
        	result.setColumnHeader(i, columnHeaders.get(i));
        	for(int j = 0; j < rows.size(); j++){
        		result.insertData(i, j, rows.get(j)[i]);
        	}
        }
        
        return result;
	}
}
