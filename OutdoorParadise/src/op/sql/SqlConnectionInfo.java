package op.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class SqlConnectionInfo {

    private String connectionString;
    
    public SqlConnectionInfo(){}

	public String getConnectionString() {
		return connectionString;
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
