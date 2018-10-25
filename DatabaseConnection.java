package org.protege.editor.owl.fuzzification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DatabaseConnection {

    private static final Logger log = Logger.getLogger(DatabaseConnection.class);

	private Connection db_connection;
	Object [][] data;
	String[] columnNames;
	
	// Create a URL that identifies database
	private String url ="jdbc:mysql://localhost:3306/ProtegeDB";
	private String username = "protegeuser";
	private String password = "p27Lv15";
	
	DatabaseConnection () {
	   
	   //load JDBC driver		   
		try {
		      Class.forName("com.mysql.jdbc.Driver").newInstance();
		      System.out.println("JDBC driver loaded.");
		    } catch (Exception E) {
		      System.out.println("JDBC Driver error");
		    }
			
	  //creating a database connection
	   try {
		   db_connection = DriverManager.getConnection (url,username,password);
	   }	
	   catch (SQLException e ){	
		   log.info("Could not connect to the database");
		   log.info( e.getMessage() );
	   }	
    }
   
	//executes an SQL Querry
	//stores the column names in []columnNames and the data in [][]data
	void SQLQuery (String SQLStatement) {
		
	  try {
		Statement sqlstatement = db_connection.createStatement();
		ResultSet rs = sqlstatement.executeQuery(SQLStatement);
		
		ResultSetMetaData md = rs.getMetaData();
		
		
		int columnCount=md.getColumnCount();
		columnNames= new String[columnCount];
		for (int i=0;i<columnNames.length;i++) columnNames[i]=md.getColumnName(i+1);
				
		//Counting the rows
		int rowCount =0;
		rs.beforeFirst(); 
		while (rs.next() ) {
			 rowCount++;				
		    }
		
		//receiving the data
		data = new Object [rowCount][columnCount]; 	
		int j=0;
		for (int i=0;i<columnCount;i++){
			 rs.beforeFirst();
			  if (md.getColumnTypeName(i+1).equals("VARCHAR")) {   
				  j=0;
				 while (rs.next() ) {
					data[j][i]=rs.getString(columnNames[i]);
					System.out.println(data[j][i]);
					j++;					
			    }
			  }
			  else if (md.getColumnTypeName(i+1).equals("INT UNSIGNED")) {
				  j=0;
				  while (rs.next() ) {
					  data[j][i]=rs.getInt(columnNames[i]);
					  j++;
				 }  
			  }
	   }
	
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}

	
}
