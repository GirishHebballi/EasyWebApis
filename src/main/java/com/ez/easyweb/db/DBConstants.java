package com.ez.easyweb.db;

public class DBConstants {
	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";
	public static final String SELECT = "select";
	public static final String LIST = "list";
	public static final String INSERT_TABLE = "inserttable";


	public static final String INSERT_QUERY = "INSERT INTO $TABLE_NAME ($COLUMNS) VALUES ($COLUMN_VALUES);";
	public static final String UPDATE_QUERY = "UPDATE $TABLE_NAME SET $UPDATE_VALUES WHERE $CONDITION;";
	public static final String DELETE_QUERY = "DELETE FROM $TABLE_NAME  WHERE $CONDITION;";
	public static final String SELECT_QUERY = "SELECT * FROM $TABLE_NAME WHERE $CONDITION;";
	public static final String SELECTALL_QUERY = "SELECT * FROM $TABLE_NAME;";
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE $TABLE_NAME ($TABLE_CONTENTS);";
	
	public static final String LIST_ALL_USERDEFINEDTYPES = "SELECT   * FROM   pg_type where typnamespace='2200' and typcategory='E';";
	public static final String LIST_ALL_DB_TABLES = "SELECT   * FROM    pg_catalog.pg_tables WHERE    schemaname != 'pg_catalog' AND schemaname != 'information_schema';";

}
