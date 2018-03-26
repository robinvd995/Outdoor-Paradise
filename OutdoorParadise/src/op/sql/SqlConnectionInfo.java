package op.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class SqlConnectionInfo {

    private String databaseHost;
    private String databaseName;
    private String databaseUser;
    private String databasePassword;
    
    public SqlConnectionInfo(){}

	public String getDatabaseHost() {
		return databaseHost;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}

	public String getDatabaseUser() {
		return databaseUser;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}
	
	public static SqlConnectionInfo load() throws IOException{
		File file = new File("connection.json");
		BufferedReader reader = new BufferedReader(new FileReader(file));

		String jsonData = "";
		if(reader != null){
			String line = "";
			while((line = reader.readLine()) != null){
				jsonData = jsonData + line.trim();
			}
		}
		
		reader.close();

		Gson gson = new Gson();
		SqlConnectionInfo data = gson.fromJson(jsonData, SqlConnectionInfo.class);
		System.out.println(data);
		return data;
	}
}
