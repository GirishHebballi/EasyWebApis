package com.ez.easyweb.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.boot.json.GsonJsonParser;

import com.google.gson.internal.LinkedTreeMap;

public class DBDelete implements DBMethod {

	private String tableName;
	private DBConnector dbConnector;
	private Connection dbConnection;
	private ArrayList<DBTableProps> dbProperties = new ArrayList<>();
	
	public DBDelete(String table) {
		this.tableName = table;
		//Creates DB Connection
		dbConnector = new DBConnector();
		dbConnection = dbConnector.createConnection();
		//Parses tablename.json file and stores the properties of DB in Array of DBTableProps Object 
		DBDataParser dbParser = new DBDataParser(table);
		this.dbProperties = dbParser.getTableDataArray();
		
	}

	@Override
	public HashMap<String,Object>  executeSQLRequest(String sql) {
		HashMap<String,Object> resultMap = new HashMap<>();
		Statement dbStatement = null;
		ResultSet sqlResultSet = null;
		resultMap.put("status", "");
		try {

			dbStatement = dbConnection.createStatement();

				dbStatement.executeUpdate(sql, 1);
				sqlResultSet = dbStatement.getGeneratedKeys();
				while (sqlResultSet.next()) {
					String uuid = sqlResultSet.getString(1);
					resultMap.put("UUID", uuid);
					System.out.println(uuid);

				}
				if(sqlResultSet.getFetchSize() == 0) {
					resultMap.put("UUID", "0");
					resultMap.put("status", "Zero Rows Inserted");

				}
				sqlResultSet.close();
		

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			resultMap.put("UUID", "0");

			resultMap.put("status", e.getClass().getName() +" " + e.getMessage() );
		} finally {
			try {
				
				dbStatement.close();
				dbConnector.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		return resultMap;
	}

	
	private String createSQLQuery(String UUID) {
		String sql ="";

		sql = DBOperations.DELETE_QUERY;
		sql = sql.replace("$TABLE_NAME", this.tableName);
        if(UUID != null && UUID != "") {
			sql = sql.replace("$CONDITION", "UUID = '" + UUID + "'");
        } else {
        	//THROW ERROR HERE SAYING CONDTION IS NOT PRESENT
        }

			System.out.println(sql);
        return sql;
	}
	


	public HashMap<String,Object> execute(String request, String UUID) {

		HashMap<String,Object> returnData = new HashMap<>();
		String sql = this.createSQLQuery(UUID);

		returnData = this.executeSQLRequest(sql);
		return returnData;

	}

	
}
