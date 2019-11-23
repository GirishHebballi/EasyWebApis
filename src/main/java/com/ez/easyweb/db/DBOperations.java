package com.ez.easyweb.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import com.ez.easyweb.db.DBConnector;
import com.google.gson.internal.LinkedTreeMap;

import org.springframework.boot.json.GsonJsonParser;

public class DBOperations {

	private DBConnector dbConnector;
	private Connection dbConnection;
	private ArrayList<DBTableProps> dbProperties;
	private String dbTable;
	private HashMap<String, Object> resultMap = new HashMap<>();

	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static final String SELECT = "select";
	public static final String SELECTALL = "selectall";
	public static final String DELETE = "delete";
	public static final String CREATETABLE = "createtable";
	
	public static String INSERT_QUERY = "INSERT INTO $TABLE_NAME ($COLUMNS) VALUES ($COLUMN_VALUES);";
	public static String UPDATE_QUERY = "UPDATE $TABLE_NAME SET $UPDATE_VALUES WHERE $CONDITION;";
	public static String DELETE_QUERY = "DELETE FROM $TABLE_NAME  WHERE $CONDITION;";
	public static String SELECT_QUERY = "SELECT * FROM $TABLE_NAME WHERE $CONDITION;";
	public static String SELECTALL_QUERY = "SELECT * FROM $TABLE_NAME;";
	public static String CREATE_TABLE_QUERY = "CREATE TABLE $TABLE_NAME ($TABLE_CONTENTS);";


	public DBOperations(String table) {
		dbConnector = new DBConnector();
		dbConnection = dbConnector.createConnection();
		DBDataParser dbParser = new DBDataParser(table);
		this.dbProperties = dbParser.getTableDataArray();
		this.dbTable = dbParser.getDbTableName();
	}

	private HashMap<String,Object>  executeSQLRequest(String sql, final String operationType) {
		Statement dbStatement = null;
		ResultSet sqlResultSet = null;
		resultMap.put("status", "");
		try {

			dbStatement = dbConnection.createStatement();

			switch (operationType) {

			case DBOperations.INSERT:
			case DBOperations.UPDATE:
			case DBOperations.DELETE:
				dbStatement.executeUpdate(sql, 1);
				sqlResultSet = dbStatement.getGeneratedKeys();
				while (sqlResultSet.next()) {
					String uuid = sqlResultSet.getString(1);
					resultMap.put("UUID", uuid);
					System.out.println(uuid);

				}
				if(sqlResultSet.getFetchSize() == 0) {
					resultMap.put("UUID", "0");
					if(operationType == DBOperations.INSERT) {
						resultMap.put("status", "Zero Rows Inserted");
		
					}
					if(operationType == DBOperations.DELETE) {
						resultMap.put("status", "Zero Rows Deleted/ UUID already deleted or not found");
						
					}
				}
				sqlResultSet.close();
				break;
			case DBOperations.CREATETABLE:
				System.out.println(sql);
				Integer returnValue = dbStatement.executeUpdate(sql);
				resultMap.put("status", returnValue);
				System.out.println(returnValue);

				break;
			case DBOperations.SELECT:

				sqlResultSet = dbStatement.executeQuery(sql);
				while (sqlResultSet.next()) {
					this.resultMap.put("UUID", sqlResultSet.getString("UUID"));

				    for (DBTableProps tableProps: this.dbProperties) { 
						this.resultMap.put(tableProps.getDbFieldName(),sqlResultSet.getString(tableProps.getDbFieldName()) );
				    }
				   
					
					
					
/*					Boolean loopOut = true;
					int index = 1;
					while (loopOut) {
						try {
							
							System.out.println(sqlResultSet.getString(index));
							index++;

						} catch (PSQLException e) {
							loopOut = false;
							// TODO: handle exception
						}
					}*/

				}
				 sqlResultSet.close();
				break;
			case DBOperations.SELECTALL:

				sqlResultSet = dbStatement.executeQuery(sql);
				ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
				while (sqlResultSet.next()) {
					HashMap<String, Object> tmpResultMap = new HashMap<>();
					tmpResultMap.put("UUID", sqlResultSet.getString("UUID"));

				    for (DBTableProps tableProps: this.dbProperties) { 
				    	tmpResultMap.put(tableProps.getDbFieldName(),sqlResultSet.getString(tableProps.getDbFieldName()) );
				    }
				    resultList.add(tmpResultMap);
				}
				this.resultMap.put("ListData", resultList);
			    sqlResultSet.close();

				default: 
					System.out.println("This operation is not implemented");
					break;

			}


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

	
	private String createSQLQuery(String request, String operationType, String UUID) {
		String sql ="";
		System.out.println(request);
		GsonJsonParser gson = new GsonJsonParser();
		LinkedTreeMap<String, Object> requestDataMap = new LinkedTreeMap<>();
		if(null != request && !request.equalsIgnoreCase("") ) {
			requestDataMap = (LinkedTreeMap<String, Object>) gson.parseMap(request);

		}
        	
			switch (operationType) {
			
			case DBOperations.DELETE:
				sql = DBOperations.DELETE_QUERY;
				sql = sql.replace("$TABLE_NAME", this.dbTable);
		        if(UUID != null && UUID != "") {
					sql = sql.replace("$CONDITION", "UUID = '" + UUID + "'");
		        } else {
		        	//THROW ERROR HERE SAYING CONDTION IS NOT PRESENT
		        }
				break;
			case DBOperations.INSERT:
				String COLUMNS = "UUID,";
				String VALUES = "uuid_generate_v4() ,";
		        for (DBTableProps dbTableProp : this.dbProperties) {
		        	if (requestDataMap.containsKey(dbTableProp.getDbFieldName())) {
			        	VALUES += "'" + (String) requestDataMap.get(dbTableProp.getDbFieldName()) + "' ,";
			        	COLUMNS += dbTableProp.getDbFieldName() + "  ," ;

		        	}
		        
		        }
				sql = DBOperations.INSERT_QUERY;
				sql = sql.replace("$TABLE_NAME", this.dbTable);
				System.out.println(COLUMNS.length());
				sql = sql.replace("$COLUMNS", COLUMNS.substring(0, COLUMNS.length() - 2));
				sql = sql.replace("$COLUMN_VALUES", VALUES.substring(0, VALUES.length() - 2));

				break;
			case DBOperations.UPDATE:
				String CONDITION = "'UUID' = ";
				String UPDATE_VALUES = "";
		        for (DBTableProps dbTableProp : this.dbProperties) {
		        	if (requestDataMap.containsKey(dbTableProp.getDbFieldName())) {
			        	UPDATE_VALUES = dbTableProp.getDbFieldName() + "=" +  (String) requestDataMap.get(dbTableProp.getDbFieldName()) + "  ,";

		        	}
		        
		        }
		        
		        if(requestDataMap.containsKey("UUID")) {
		        	CONDITION += requestDataMap.get("UUID");
		        } else {
		        	//THROW ERROR HERE SAYING CONDTION IS NOT PRESENT
		        }
		        
				sql = DBOperations.UPDATE_QUERY;
				sql = sql.replace("$TABLE_NAME", this.dbTable);
				sql = sql.replace("$UPDATE_VALUES", UPDATE_VALUES.substring(0, UPDATE_VALUES.length() - 2));
				sql = sql.replace("$CONDITION", CONDITION);

				break;		
			case DBOperations.SELECT:
				sql = DBOperations.SELECT_QUERY;
				sql = sql.replace("$TABLE_NAME", this.dbTable);
		        if(UUID != null && UUID != "") {
					sql = sql.replace("$CONDITION", "UUID = '" + UUID + "'");
		        } else {
		        	//THROW ERROR HERE SAYING CONDTION IS NOT PRESENT
		        }
		        break;
		        
			case DBOperations.SELECTALL:
				sql = DBOperations.SELECTALL_QUERY;

				sql = sql.replace("$TABLE_NAME", this.dbTable);
				break;
				
			case DBOperations.CREATETABLE:
				sql = DBOperations.CREATE_TABLE_QUERY;

				sql = sql.replace("$TABLE_NAME", this.dbTable);
				String tableDetails="UUID uuid PRIMARY KEY  NOT NULL,";
		        for (DBTableProps dbTableProp : this.dbProperties) {
		        	tableDetails += dbTableProp.getDbFieldName() + " " + dbTableProp.getDbType() + " " + dbTableProp.getDbConstraint() + "  ,";
		        	
		        }
		        tableDetails =  tableDetails.substring(0,tableDetails.lastIndexOf(",") - 2);
		        sql = sql.replace("$TABLE_CONTENTS", tableDetails);
		        break;

				
			}
			System.out.println(sql);
        return sql;
	}
	

	public HashMap<String,Object> update(String request) {

		HashMap<String,Object> returnData = new HashMap<>();
		String sql = this.createSQLQuery(request, DBOperations.UPDATE, null);
		returnData = this.executeSQLRequest(sql, DBOperations.UPDATE);
		return returnData;

	}

	public HashMap<String,Object> insert(String request) {

		HashMap<String,Object> returnData = new HashMap<>();
		String sql = this.createSQLQuery(request, DBOperations.INSERT, null);

		returnData = this.executeSQLRequest(sql, DBOperations.INSERT);
		return returnData;

	}
	public HashMap<String,Object> insertTable() {

		HashMap<String,Object> returnData = new HashMap<>();
		String sql = this.createSQLQuery(null, DBOperations.CREATETABLE, null);
		returnData = this.executeSQLRequest(sql, DBOperations.CREATETABLE);
		return returnData;

	}
	public HashMap<String,Object> delete(String request, String UUID) {

		HashMap<String,Object> returnData = new HashMap<>();
		String sql = this.createSQLQuery(null, DBOperations.DELETE, UUID);

		returnData = this.executeSQLRequest(sql, DBOperations.DELETE);
		return returnData;

	}
	public HashMap<String,Object> select(String request, String UUID) {

		HashMap<String,Object> returnData = new HashMap<>();
		String sql = this.createSQLQuery(null,DBOperations.SELECT, UUID);

		returnData = this.executeSQLRequest(sql, DBOperations.SELECT);
		return returnData;

	}
	
	public HashMap<String,Object> selectAll() {

		HashMap<String,Object> returnData = new HashMap<>();
		String sql = this.createSQLQuery(null, DBOperations.SELECTALL, null);
		returnData = this.executeSQLRequest(sql, DBOperations.SELECTALL);
		return returnData;

	}	
	

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBOperations dbOp = new DBOperations("laptop");
		//dbOp.insert();

	}

}
