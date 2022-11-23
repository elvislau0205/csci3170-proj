
import java.sql.*;
import java.util.Scanner;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;
import java.io.IOException;

class MainMenu {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Need to fill values
		final String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db7?autoReconnect=true&useSSL=false";
		final String dbUsername = "Group7";
		final String dbPassword = "CSCI3170";
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
		} catch (ClassNotFoundException e) {
			System.out.println("[Error]: Java MySQL DB Driver not found!!");
			System.exit(0);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		int option = 0;
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Welcome to sales system!\n");
		while (true) {
			System.out.println("-----Main menu-----");
			System.out.println("What kinds of operation would you like to perform?");
			System.out.println("1. Operations for administrator");
			System.out.println("2. Operations for salesperson");
			System.out.println("3. Operations for manager");
			System.out.println("4. Exit this program");
			System.out.print("Enter your choice: ");
			option = keyboard.nextInt();
			switch (option) {
			case 1:
				AdministratorFunctions adminFunc = new AdministratorFunctions(conn);
				adminFunc.ShowAdministratorFunctions();
				break;
			case 2:
				SalespersonFunctions salespersonFunc = new SalespersonFunctions(conn);
				salespersonFunc.ShowSalespersonFunctions();
				break;
			case 3:
				ManagerFunctions managerFunc = new ManagerFunctions(conn);
				managerFunc.ShowManagerFunctions();
				break;
			case 4:
				System.out.println("bye bye!");
				System.exit(0);
				break;
			default:
			System.out.println("Invalid Choice! Please try again with a valid choice!\n");
			}
		}
	}
}







