import java.sql.*;
import java.util.Scanner;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;
import java.io.IOException;
public class ManagerFunctions {

    int option;
	Connection conn;

    public ManagerFunctions(Connection conn) {
        this.option = 0;
		this.conn = conn;
    }

	// In SQL List all salespersons in ascending or descending order of years of experience: 
	// The system needs to provide a method for the manager to list all salespersons in either 
	// ascending or descending order of their years of experiences. After he/she specifies 
	// the output order, the program will perform the query and return the ID, name, phone number 
	// and years of experience of each salesperson as follows:
    public void ListSalespersons(){
		boolean error = false;
		String sql1 = "SELECT sID, sName, sPhoneNumber, sExperience FROM salesperson ";
		System.out.println("Choose ordering:");
		System.out.println("1. By ascending order");
		System.out.println("2. By descending order");
		System.out.print("Choose the list ordering: ");
		Scanner keyboard = new Scanner(System.in);
		option = keyboard.nextInt();
		sql1 += "ORDER BY sExperience ";
		switch (option) {
			case 1:
				sql1 += "ASC";
				break;
			case 2:
				sql1 += "DESC";
				break;
			default:
				System.out.println("Invalid Choice! Please try again with a valid choice!\n");
				error = true;
		}
		if(!error){
			try{
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql1);
				int numOfCol = rsmd.getColumnCount();
                String[] header = {"ID", "Name", "Mobile Phone", "Years of Experience"};
				for(int i=0; i < numOfCol; i++){
					System.out.print("| " + header[i] + ' ');
				}
				while(rs.next()){
					for(int i=0 ; i < numOfCol; i++){
						String colName = rsmd.getColumnName(i);
						String colType = rsmd.getColumnTypeName(i);
						switch(colType){
							case "INT UNSIGNED":
								System.out.print("| " + rs.getInt(colName) + ' ');
								break;
							case "VARCHAR":
								System.out.print("| " + rs.getString(colName) + ' ');
								break;
							default:
								System.out.println("Invalid column type!");
						}
					}
					System.out.println('|');
				}
				System.out.println("End of Query");
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Error: " + e);
			}
		}
	}
    
	// Count the number of transaction records of each salesperson within a given range on years of experience:
	// The system has to provide an interface to allow a manager to count the number of transaction records of each salesperson 
	// within a lower bound and upper bound of years of experience given by user inclusively. 
	// After he/she enters a specific range on years of experience, the program will perform the query and 
	// return the ID, name, years of experience and number of transaction records of each salesperson within the range on years of experience specified by the user inclusively. 
	// These transaction records should be sorted in descending order of Salesperson ID and outputted as a table as follows:
    public void CountSalesRecord(){
		boolean error = false;
		String sql1 = "select sID, sName, sExperience, COUNT(tID) AS nTransaction FROM salesperson, transaction ";
		sql1 += "where salesperson.sID = transaction.sID ";
		System.out.println("Type in the lower bound of years of experience: ");
		Scanner keyboard = new Scanner(System.in);
		int lowerBound = keyboard.nextInt();
		System.out.println("Type in the upper bound of years of experience: ");
		int upperBound = keyboard.nextInt();
		sql1 += "where sExperience >= " + lowerBound + " and sExperience <= " + upperBound + " ";
		sql1 += "group by sID, sName, sExperience ";
		sql1 += "order by sID DESC ";
		if(lowerBound > upperBound){
			System.out.println("Invalid Choice! Please try again with a valid choice!\n");
			error = true;
		}
		if(!error){
			try{
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql1);
				int numOfCol = rsmd.getColumnCount();
                String[] header = {"ID", "Name", "Years of Experience", "tNumber of Transaction"};
				for(int i=0; i < numOfCol; i++){
					System.out.print("| " + header[i] + ' ');
				}
				while(rs.next()){
					for(int i=0 ; i < numOfCol; i++){
						String colName = rsmd.getColumnName(i);
						String colType = rsmd.getColumnTypeName(i);
						switch(colType){
							case "INT UNSIGNED":
								System.out.print("| " + rs.getInt(colName) + ' ');
								break;
							case "VARCHAR":
								System.out.print("| " + rs.getString(colName) + ' ');
								break;
							default:
								System.out.println("Invalid column type!");
						}
					}
				}
				System.out.println("End of Query");
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Error: " + e);
			}
		}
	}

	// Sort and list the manufacturers in descending order of total sales value: 
	// The system has to provide an interface to allow a manager to sort the manufacturers according to their total sale values. 
	// After the program performs the query, it returns the results in terms of 
	// Manufacturer ID, Manufacturer Name and Total sales value in descending order of Total sales value as a table as follows
    public void ShowSalesValue(){
		boolean error = false;
		String sql1 = "select mID, mName, SUM(pPrice) as totalSales from manufacturer, part, transaction";
		sql1 += "where mID = pID and pID = tID ";
		sql1 += "group by mID, mName ";
		sql1 += "order by totalSales DESC";
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql1);
			int numOfCol = rsmd.getColumnCount();
            String[] header = {"Manufacturer ID", "Manufacturer Name", "Total Sales Value"};
			for(int i=0; i < numOfCol; i++){
				System.out.print("| " + header[i] + ' ');
			}
			while(rs.next()){
				for(int i=0 ; i < numOfCol; i++){
					String colName = rsmd.getColumnName(i);
					String colType = rsmd.getColumnTypeName(i);
					switch(colType){
						case "INT UNSIGNED":
							System.out.print("| " + rs.getInt(colName) + ' ');
							break;
						case "VARCHAR":
							System.out.print("| " + rs.getString(colName) + ' ');
							break;
						default:
							System.out.println("Invalid column type!");
					}
				}
			}
			System.out.println("End of Query");
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
	}

	// Show the N most popular parts: The system has to provide an interface to allow a manager to show the N parts that are most popular. 
	// After the manager enters the number of parts (N) that he/she wants to list, the program will perform the query and return the N parts 
	// that are most popular in terms of Part ID, Part Name and Total Number of Transaction in descending order of Total Number of Transaction as a table as follows.
    public void ShowPopularPart(){
		boolean error = false;
		String sql1 = "select pID, pName, count(tID) as totalTransaction from part, transaction where part.pID = transaction.pID ";
		sql1 += "group by pID, pName ";
		sql1 += "order by totalTransaction DESC ";
		System.out.println("Type in the number of parts: ");
		Scanner keyboard = new Scanner(System.in);
		int n = keyboard.nextInt();
		sql1 += "LIMIT " + n;
		if(n < 0){
			System.out.println("Invalid Choice! Please try again with a valid choice!\n");
			error = true;
		}
		if(!error){
			try{
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql1);
				int numOfCol = rsmd.getColumnCount();
                String[] header = {"Part ID", "Part Name", "No. of Transaction"};
				for(int i=0; i < numOfCol; i++){
					System.out.print("| " + header[i] + ' ');
				}
				while(rs.next()){
					for(int i=0 ; i < numOfCol; i++){
						String colName = rsmd.getColumnName(i);
						String colType = rsmd.getColumnTypeName(i);
						switch(colType){
							case "INT UNSIGNED":
								System.out.print("| " + rs.getInt(colName) + ' ');
								break;
							case "VARCHAR":
								System.out.print("| " + rs.getString(colName) + ' ');
								break;
							default:
								System.out.println("Invalid column type!");
						}
					}
				}
				System.out.println("End of Query");
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Error: " + e);
			}
		}
	}

    public void ShowManagerFunctions() {
        System.out.print("\n");
		while (true) {
			System.out.println("-----Operations for manager menu-----");
			System.out.println("What kinds of operation would you like to perform?");
			System.out.println("1. List all salespersons");
			System.out.println("2. Count the no. of sales record of each salesperson under specific range on years of experience");
			System.out.println("3. Show the total sales value of each manufacturer");
            System.out.println("4. Show the N most popular part");
            System.out.println("5. Return to the main menu");
			System.out.print("Enter your choice: ");
			Scanner keyboard = new Scanner(System.in);
			option = keyboard.nextInt();
			keyboard.nextLine();
			switch (option) {
			case 1:
				ListSalespersons();
				return;
			case 2:
				CountSalesRecord();
				return;
            case 3:
				ShowSalesValue();
				return;
            case 4:
                ShowPopularPart();
				return;
			case 5:
				System.out.print("\n");
				return;
			default:
				System.out.println("Invalid Choice! Please try again with a valid choice!\n");
			}
		}
    }
}
