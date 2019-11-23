package com.ez.easyweb.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.boot.json.GsonJsonParser;

import com.google.gson.internal.LinkedTreeMap;

public class DBInsertTable implements DBMethod {
	private String tableName;
	private DBConnector dbConnector;
	private Connection dbConnection;
	private ArrayList<DBTableProps> dbProperties = new ArrayList<>();
	
	public DBInsertTable(String table) {
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
		resultMap.put("status", "");
		try {

			dbStatement = dbConnection.createStatement();

			System.out.println(sql);
			Integer returnValue = dbStatement.executeUpdate(sql);
			resultMap.put("status", returnValue);
			System.out.println(returnValue);
		

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

	
	private String createSQLQuery() {
		String sql ="";

		sql = DBOperations.CREATE_TABLE_QUERY;

		sql = sql.replace("$TABLE_NAME", this.tableName);
		String tableDetails="UUID uuid PRIMARY KEY  NOT NULL,";
        for (DBTableProps dbTableProp : this.dbProperties) {
        	tableDetails += dbTableProp.getDbFieldName() + " " + dbTableProp.getDbType() + " " + dbTableProp.getDbConstraint() + "  ,";
        	
        }
        tableDetails =  tableDetails.substring(0,tableDetails.lastIndexOf(",") - 2);
        sql = sql.replace("$TABLE_CONTENTS", tableDetails);
        System.out.println(sql);
        return sql;
	}
	

	public HashMap<String,Object> execute(String request, String UUID) {

		HashMap<String,Object> returnData = new HashMap<>();
		String sql = this.createSQLQuery();
		returnData = this.executeSQLRequest(sql);
		return returnData;

	}
	
}
