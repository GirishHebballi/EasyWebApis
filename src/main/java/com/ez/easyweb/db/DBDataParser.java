package com.ez.easyweb.db;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.ez.easyweb.db.DBTableProps;

import org.springframework.boot.json.GsonJsonParser;

import com.google.gson.internal.LinkedTreeMap;


public class DBDataParser {
	
	private String dbTableName;
	private ArrayList<DBTableProps> dbTablePropertiesList = new ArrayList<>();
	private String filePath = "C:\\OFFICE\\DemandPortal\\DemandSrc\\easyweb\\src\\main\\resources\\";
	
	
	public DBDataParser(String table) {
		this.filePath = this.filePath + table + ".json";
		
	}
	
	public String getDbTableName() {
		return this.dbTableName;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<DBTableProps> getTableDataArray() {
		//String fileName = "C:\\OFFICE\\DemandPortal\\DemandSrc\\easyweb\\src\\main\\resources\\Assests_Laptops.json";
		String data;
		try (Stream<String> stream = Files.lines(Paths.get(this.filePath))) {

			data = stream.collect(Collectors.joining());  
			System.out.println(data);
			
			GsonJsonParser gson = new GsonJsonParser();
			Map<String, Object> listGson =  gson.parseMap(data);
			this.dbTableName = (String) listGson.get("dbTable");
			ArrayList<Object> groupList = (ArrayList<Object>) listGson.get("groups");
			groupList.forEach((groupMap) -> {
				LinkedTreeMap<String, Object> group = new LinkedTreeMap<>();
				group = (LinkedTreeMap<String, Object>)groupMap;
				
				List<Object> fields = (List<Object>) group.get("fields");
				fields.forEach((fieldData)-> {
					LinkedTreeMap<String, Object> field = new LinkedTreeMap<>();
					field= (LinkedTreeMap<String, Object>)fieldData;
					DBTableProps dbTableProperties = new DBTableProps();

					if(field.containsKey("dbConstraint")) {
						dbTableProperties.setDbConstraint((String) field.get("dbConstraint"));
					} else {
						dbTableProperties.setDbConstraint("");
					}
					dbTableProperties.setDbFieldName((String) field.get("dbName"));
					dbTableProperties.setDbType((String) field.get("dbType"));
					
					this.dbTablePropertiesList.add(dbTableProperties);

					
				});
			});
			
			System.out.println(this.dbTablePropertiesList);
			System.out.println(this.dbTableName);
			//stream.
			//stream.forEach(System.out::println);
			//return this.dbTablePropertiesList;
			System.out.println("Hi");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.dbTablePropertiesList;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DBDataParser db = new DBDataParser("laptop");
		//db.getTableData();
		

	}

}
