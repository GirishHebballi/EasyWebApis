package com.ez.easyweb.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class DBConnector {
	private Connection databaseConnection = null;
	
	@Autowired
	private Environment env;
	//private final String DB_HOSTNAME = env.getProperty("database.ip"); 
/*	private final String DB_PORT = env.getProperty("database.port");
	private final String DB_NAME = env.getProperty("database.dbname");
	private final String DB_USER = env.getProperty("database.user");
	private final String DB_PASSWORD = env.getProperty("database.password");
	*/
	private final String DB_HOSTNAME = "localhost"; 
	private final String DB_PORT = "5432";
	private final String DB_NAME = "easyweb";
	private final String DB_USER = "postgres";
	private final String DB_PASSWORD = "easyweb";
	
	public DBConnector() {

	}
	
	public Connection createConnection() {
	      try {
		         Class.forName("org.postgresql.Driver");
		         databaseConnection = DriverManager
		            .getConnection("jdbc:postgresql://"+ this.DB_HOSTNAME + ":"+this.DB_PORT +"/" + this.DB_NAME,
		            this.DB_USER, this.DB_PASSWORD);
	      } catch(Exception ex) {
	    	  
	      }
	      
	      return databaseConnection;
		
	}
	
	public void closeConnection() {
		try {
			this.databaseConnection.close();
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	      Connection c = null;
	      Statement stmt = null;
	      try {
	         Class.forName("org.postgresql.Driver");
	         c = DriverManager
	            .getConnection("jdbc:postgresql://localhost:5432/easyweb",
	            "postgres", "easyweb");
	         System.out.println("Opened database successfully");

	         //stmt = c.createStatement();
	         /*String sql = "CREATE TABLE COMPANY1 " +
	            "(ID uuid PRIMARY KEY     NOT NULL," +
	            " NAME           TEXT    NOT NULL, " +
	            " AGE            INT     NOT NULL, " +
	            " ADDRESS        CHAR(50), " +
	            " SALARY         REAL)"; */
	         //stmt.executeUpdate(sql);
	         //stmt.close();
	         //c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         System.exit(0);
	      }
	      System.out.println("Table created successfully");
	      
	      
	      
	      try {
			stmt = c.createStatement();
		    ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY1;" );
		    while ( rs.next() ) {
		    	Boolean loopOut = true;
		    	int index=1;
		    	while(loopOut) {
		    		try {
				    	System.out.println(rs.getString(index));
				    	index++;

		    		}
		    		catch (PSQLException e) {
		    			loopOut = false;
						// TODO: handle exception
					}
		    	}
		    	//System.out.println(rs.getString(10));
		    	//System.out.println(rs.getString(2));
		    	//System.out.println(rs.());

		    }
		    System.out.println(rs);
	         rs.close();
	         stmt.close();
	         c.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	}

