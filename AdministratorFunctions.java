import java.sql.*;
import java.util.Scanner;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;
import java.io.IOException;
public class AdministratorFunctions {

	int option;
	Connection conn;

	public AdministratorFunctions(Connection conn) {
		this.option = 0;
		this.conn = conn;
	}

		public void CreateTables() {
		try {
			Statement stm = this.conn.createStatement();

			String sql1 = "CREATE TABLE category (cID INTEGER(1) unsigned NOT NULL PRIMARY KEY, cName VARCHAR(20) NOT NULL);";
			String sql2 = "CREATE TABLE manufacturer (mID INTEGER(2) unsigned NOT NULL PRIMARY KEY, mName VARCHAR(20) NOT NULL , mAddress VARCHAR(50) NOT NULL , mPhoneNumber INTEGER(80) unsigned NOT NULL);";
			String sql3 = "CREATE TABLE part (pID INTEGER(3) unsigned NOT NULL PRIMARY KEY,pName VARCHAR(20) NOT NULL,pPrice INTEGER(5) unsigned NOT NULL,mID INTEGER(2) unsigned NOT NULL REFERENCES manufacturer(mID), cID INTEGER(1) unsigned NOT NULL REFERENCES category(cID),pWarrantyPeriod INTEGER(2) unsigned NOT NULL,pAvailableQuantity INTEGER(2) unsigned NOT NULL);";
			String sql4 = "CREATE TABLE salesperson (sID INTEGER(2) unsigned NOT NULL PRIMARY KEY, sName VARCHAR(20) NOT NULL, sAddress VARCHAR(50) NOT NULL, sPhoneNumber INTEGER(8) unsigned NOT NULL, sExperience INTEGER(1) unsigned NOT NULL);";
			String sql5 = "CREATE TABLE transaction (tID INTEGER(4) unsigned NOT NULL, pID INTEGER(3) unsigned NOT NULL REFERENCES part(pID), sID INTEGER(2) unsigned NOT NULL REFERENCES salesperson(sID), tDate DATE NOT NULL);";

			stm.addBatch(sql1);
			stm.addBatch(sql2);
			stm.addBatch(sql3);
			stm.addBatch(sql4);
			stm.addBatch(sql5);
			stm.executeBatch();
			stm.close();
			
			System.out.println("Processing...Done. Database is initialized!\n");
		} catch (SQLException e) {
			System.out.println("Error encounter: " + e.getMessage() + "\n");
		}
	}
	
	public void DeleteTables() {
		try {
			Statement stm = this.conn.createStatement();
			String sql1 = "DROP TABLE category;";
			String sql2 = "DROP TABLE manufacturer";
			String sql3 = "DROP TABLE part;";
			String sql4 = "DROP TABLE salesperson;";
			String sql5 = "DROP TABLE transaction;";

			stm.addBatch(sql1);
			stm.addBatch(sql2);
			stm.addBatch(sql3);
			stm.addBatch(sql4);
			stm.addBatch(sql5);
			stm.executeBatch();
			stm.close();
			System.out.println("Processing...Done. Database is removed!\n");
		} catch (SQLException e) {
			System.out.println("Error encounter: " + e.getMessage() + "\n");
		}
	}

	public void LoadData(String path) throws IOException {
		String parentPath = new File(System.getProperty("user.dir")).getParent();
		File directoryPath = new File(parentPath + "/" + path);
		Scanner sc = null;
		boolean error = false;
		if (directoryPath.list() != null) {

			File category = new File(directoryPath + "/" + "category.txt");
			sc = new Scanner(category);
			String input;
			while (sc.hasNextLine()) {
				try {
					input = sc.nextLine();
					String[] values = input.split("\t", -1);
					int cId = Integer.parseInt(values[0]);
					String cName = values[1];
					PreparedStatement pstmt = this.conn
							.prepareStatement("INSERT INTO category(cId,cName) VALUES (?,?)");
					pstmt.setInt(1, cId);
					pstmt.setString(2, cName);
					pstmt.executeUpdate();
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("Something went wrong while inserting rows, Please make sure all tables are created\n");
					error = true;
					break;
				}
			}

			File manufacturer = new File(directoryPath + "/" + "manufacturer.txt");
			sc = new Scanner(manufacturer);
			while (sc.hasNextLine()) {
				try {
					input = sc.nextLine();
					String[] values = input.split("\t", -1);
					int manufacturerID = Integer.parseInt(values[0]);
					String manufacturerName = values[1];
					String manufacturerAddress = values[2];
					int manufacturerPhoneNumber = Integer.parseInt(values[3]);
					PreparedStatement pstmt = this.conn
							.prepareStatement("INSERT INTO manufacturer(mID,mName,mAddress,mPhoneNumber) VALUES (?,?,?,?)");
					pstmt.setInt(1, manufacturerID);
					pstmt.setString(2, manufacturerName);
					pstmt.setString(3, manufacturerAddress);
					pstmt.setInt(4, manufacturerPhoneNumber);
					pstmt.executeUpdate();
					pstmt.close();
				} catch (SQLException e) {
					error = true;
					break;
				}
			}

			File part = new File(directoryPath + "/" + "part.txt");
			sc = new Scanner(part);
			while (sc.hasNextLine()) {
				try {
					input = sc.nextLine();
					String[] values = input.split("\t", -1);
					int partID = Integer.parseInt(values[0]);
					String partName = values[1];
					int partPrice = Integer.parseInt(values[2]);
					int partManufacturerID = Integer.parseInt(values[3]);
					int partCategoryID = Integer.parseInt(values[4]);
					int partWarranty = Integer.parseInt(values[5]);
					int partAvailableQuantity = Integer.parseInt(values[6]);
					PreparedStatement pstmt = this.conn
							.prepareStatement("INSERT INTO part(pID,pName,pPrice,mID,cID,pWarrantyPeriod,pAvailableQuantity) VALUES (?,?,?,?,?,?,?)");
					pstmt.setInt(1, partID);
					pstmt.setString(2, partName);
					pstmt.setInt(3, partPrice);
					pstmt.setInt(4, partManufacturerID);
					pstmt.setInt(5, partCategoryID);
					pstmt.setInt(6, partWarranty);
					pstmt.setInt(7, partAvailableQuantity);
					pstmt.executeUpdate();
					pstmt.close();
				} catch (SQLException e) {
					error = true;
					break;
				}
			}

			File salesperson = new File(directoryPath + "/" + "salesperson.txt");
			sc = new Scanner(salesperson);
			while (sc.hasNextLine()) {
				try {
					input = sc.nextLine();
					String[] values = input.split("\t", -1);
					int salespersonID = Integer.parseInt(values[0]);
					String salespersonName = values[1];
					String salespersonAddress = values[2];
					int salespersonPhoneNumber = Integer.parseInt(values[3]);
					int salespersonExperience = Integer.parseInt(values[4]);
					PreparedStatement pstmt = this.conn
							.prepareStatement("INSERT INTO salesperson(sID,sName,sAddress,sPhoneNumber,sExperience) VALUES (?,?,?,?,?)");
					pstmt.setInt(1, salespersonID);
					pstmt.setString(2, salespersonName);
					pstmt.setString(3, salespersonAddress);
					pstmt.setInt(4, salespersonPhoneNumber);
					pstmt.setInt(5, salespersonExperience);
					pstmt.executeUpdate();
					pstmt.close();
				} catch (SQLException e) {
					error = true;
					break;
				}
			}

			File transaction = new File(directoryPath + "/" + "transaction.txt");
			sc = new Scanner(transaction);
			while (sc.hasNextLine()) {
				try {
					input = sc.nextLine();
					String[] values = input.split("\t", -1);
					int transactionID = Integer.parseInt(values[0]);
					int partID = Integer.parseInt(values[1]);
					int salespersonID = Integer.parseInt(values[2]);
					String[] dateString = values[3].split("/", -1);
					
					try {
						String date1 = dateString[2] + "/" + dateString[1] + "/" + dateString[0];
						PreparedStatement pstmt = this.conn.prepareStatement(
								"INSERT INTO transaction(tID,pID,sID,tDate) VALUES (?,?,?,?)");
						Date date = new SimpleDateFormat("yyyy/MM/dd").parse(date1);
						java.sql.Date manufacture = new java.sql.Date(date.getTime()); 
						
						pstmt.setInt(1, transactionID);
						pstmt.setInt(2, partID);
						pstmt.setInt(3, salespersonID);
						pstmt.setDate(4, manufacture);
						pstmt.executeUpdate();
						pstmt.close();
					}catch (ParseException e) {
						System.out.println("Invalid date: " + e + "\n");
						break;
					}
				} catch (SQLException e) {
					error = true;
					break;
				}
			}

			if (!error) {
				System.out.println("Processing...Done. Data is inputted to the database!\n");
			}
		} else {
			throw new IOException("Folder does not exist!\n");
		}
	}

	public void ShowRecords(String table) throws IOException {
		String sql = null;
		boolean error = false;
		switch(table){
			case "category":
				sql = "SELECT * FROM category;";
				break;
			case "manufacturer":
				sql = "SELECT * FROM manufacturer";
				break;
			case "part":
				sql = "SELECT * FROM part;";
				break;
			case "salesperson":
				sql = "SELECT * FROM salesperson;";
				break;
			case "transaction":
				sql = "SELECT * FROM transaction;";
				break;
			default:
				error = true;
				throw new IOException("Table does not exist!\n");
		}
		if (!error) {
			try {
				System.out.println("Content of table " + table + ":");
				Statement stm = this.conn.createStatement();
				ResultSet rs = stm.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				int numOfCol = rsmd.getColumnCount();
				String colName = null;
				String colType = null;
				for(int i = 1;i <= numOfCol;i++){
					colName = rsmd.getColumnName(i);
					System.out.print("| " + colName + ' ');
				}
				System.out.println('|');
				while(rs.next()){
					for(int i = 1;i <= numOfCol;i++){
						colName = rsmd.getColumnName(i);
						colType = rsmd.getColumnTypeName(i);
						switch(colType){
							case "INT UNSIGNED":
								System.out.print("| " + rs.getInt(colName) + ' ');
								break;
							case "VARCHAR":
								System.out.print("| " + rs.getString(colName) + ' ');
								break;
							case "DATE":
								System.out.print("| " + rs.getDate(colName) + ' ');
						}
					}
					System.out.println('|');
				}
				System.out.println();
				stm.close();
			} catch (SQLException e) {
				System.out.println("Error encounter: " + e.getMessage() + "\n");
			}
		}
	}

	public void ShowAdministratorFunctions() {
		System.out.print("\n");
		while (true) {
			System.out.println("-----Operations for administrator menu-----");
			System.out.println("What kinds of operation would you like to perform?");
			System.out.println("1. Create all tables");
			System.out.println("2. Delete all tables");
			System.out.println("3. Load from datafile");
			System.out.println("4. Show content of a table");
			System.out.println("5. Return to the main menu");
			System.out.print("Enter your choice: ");
			Scanner keyboard = new Scanner(System.in);
			option = keyboard.nextInt();
			keyboard.nextLine();
			switch (option) {
			case 1:
				CreateTables();
				break;
			case 2:
				DeleteTables();
				break;
			case 3:
				System.out.print("\nType in the Source Data Folder Path: ");
				String path = keyboard.nextLine();
				try {
					LoadData(path);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 4:
				System.out.print("\nWhich table would you like to show: ");
				String table = keyboard.nextLine();
				try {
					ShowRecords(table);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 5:
				System.out.print("\n");
				return;
			default:
				System.out.println("Invalid Choice! Please try again with a valid choice!\n");
			}
		}
	}
}
