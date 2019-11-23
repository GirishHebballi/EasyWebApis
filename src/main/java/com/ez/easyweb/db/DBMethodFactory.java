package com.ez.easyweb.db;

public class DBMethodFactory {
	
	public DBMethod getDBMethod(String DBMethodType, String table) {
		
		if(DBMethodType == null) {
			return null;
		}
		
		if(DBMethodType.equalsIgnoreCase(DBConstants.INSERT)) {
			return new DBInsert(table);
		}
		
		if(DBMethodType.equalsIgnoreCase(DBConstants.UPDATE)) {
			return new DBUpdate(table);
		}
		
		if(DBMethodType.equalsIgnoreCase(DBConstants.SELECT)) {
			return new DBSelect(table);
		}
		
		if(DBMethodType.equalsIgnoreCase(DBConstants.DELETE)) {
			return new DBDelete(table);
		}
		
		if(DBMethodType.equalsIgnoreCase(DBConstants.LIST)) {
			return new DBList(table);
		}
		if(DBMethodType.equalsIgnoreCase(DBConstants.INSERT_TABLE)) {
			return new DBInsertTable(table);
		}
		return null;
		
	}

}
