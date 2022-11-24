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

    public void ListSalespersons(){
        
    }

    public void CountSalesRecord(){

    }

    public void ShowSalesValue(){

    }

    public void ShowPopularPart(){

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
