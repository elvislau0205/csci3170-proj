import java.sql.*;
import java.util.Scanner;

import java.util.ArrayList;
import java.text.ParseException;
import java.util.Date;  
import java.text.SimpleDateFormat;  

import java.io.File;
import java.io.IOException;
public class SalespersonFunctions {

    int option;
	Connection conn;

    public SalespersonFunctions(Connection conn) {
        this.option = 0;
		this.conn = conn;
    }

    public void SearchParts(){
        boolean error = false;
        String sql1 = "select P.pID, P.pName, M.mName, C.cName, P.pAvailableQuantity, P.pWarrantyPeriod, P.pPrice from part P, manufacturer M, category C where P.cID = C.cID and P.mID = M.mID ";
        System.out.println("Choose the search criterion:");
        System.out.println("1. Part Name");
        System.out.println("2. Manufacturer Name");
        System.out.print("Choose the search criterion:");
        Scanner keyboard = new Scanner(System.in);
		option = keyboard.nextInt();
        keyboard.nextLine();
        switch (option) {
			case 1:
				sql1 += "and P.pName = \"";
				break;
			case 2:
                sql1 += "and M.mName = \"";
				break;
			default:
				System.out.println("Invalid Choice! Please try again with a valid choice!\n");
                error = true;
		}
        if(!error){
            System.out.print("Type in the Search Keyword:");
            String name = keyboard.nextLine();
            sql1 += name + "\"";
        }
        if(!error){
            System.out.println("Choose ordering:");
            System.out.println("1. By price, ascending order");
            System.out.println("2. By price, descending order");
            System.out.print("Choose the search criterion:");
            int  ordering = keyboard.nextInt();
            sql1 += "order by P.pPrice ";
            switch (ordering) {
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
        }
        if(!error){
            try {
                Statement stm = this.conn.createStatement();
                ResultSet rs = stm.executeQuery(sql1);
                ResultSetMetaData rsmd = rs.getMetaData();
				int numOfCol = rsmd.getColumnCount();
				String colName = null;
                String[] header = {"ID", "Name", "Manufacturer", "Category", "Quantity", "Warranty", "Price"};
				String colType = null;
				for(int i = 1;i <= numOfCol;i++){
					System.out.print("| " + header[i-1] + ' ');
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
				System.out.println("End of Query");
				stm.close();
            }catch (SQLException e) {
                System.out.println("Error encounter: " + e.getMessage() + "\n");
            }
        }
    }

    public void SellPart(){
        boolean error = false;
        String sql1 = "select pName, pAvailableQuantity, sID from part, salesperson where pID = ";
        System.out.print("Enter The Part ID: ");
        Scanner keyboard = new Scanner(System.in);
		String pID = keyboard.nextLine();
        System.out.print("Enter The Salesperson ID: ");
        String sID = keyboard.nextLine();
        String name = null;
        int quantity = 0;
        sql1 += pID;
        sql1 += " and sID = " + sID;
        try {
            Statement stm = this.conn.createStatement();
            ResultSet rs = stm.executeQuery(sql1);
            if(rs.next()){
                name = rs.getString("pName");
                quantity = rs.getInt("pAvailableQuantity");
            }else {
                error = true;
                System.out.print("Invalid ID! Please try again with a valid ID!\n");
            }
            if(!error){
                if(quantity > 0){
                    int count = 0;
                    String sql2 = "update part set pAvailableQuantity = pAvailableQuantity - 1 where pID = " + pID;
                    Statement stm2 = this.conn.createStatement();
                    stm2.executeUpdate(sql2);
                    stm2.close();
                    String sql3 = "select count(*) as count from transaction";
                    stm2 = this.conn.createStatement();
                    ResultSet rs2 = stm2.executeQuery(sql3);
                    if(rs2.next())
                        count = rs2.getInt("count");
                    stm2.close();
                    String sql4 = "SELECT DATE_FORMAT(CURDATE(), \"%Y-%c-%d\") as date";
                    stm2 = this.conn.createStatement();
                    rs2 = stm2.executeQuery(sql4);
                    PreparedStatement pstmt = this.conn.prepareStatement("insert into transaction(tID, pID, sID, tDate)  VALUES (?,?,?,?)");
                    if(rs2.next()){
                        pstmt.setInt(1, count + 1);
                        pstmt.setInt(2, Integer.parseInt(pID));
                        pstmt.setInt(3, Integer.parseInt(sID));
                        java.sql.Date date = rs2.getDate("date");
                        pstmt.setDate(4, date);
                    }
                    pstmt.executeUpdate();
					pstmt.close();
                    System.out.print("Product: ");
                    System.out.print(name);
                    System.out.print("(id: " + pID + ") Remaining Quantity: ");
                    System.out.println(quantity - 1);
                }
                else{
                    error = true;
                    System.out.print("The product is out of stock!\n");
                }
            }
            stm.close();
        }catch (SQLException e) {
            System.out.println("Error encounter: " + e.getMessage() + "\n");
        }
    }

    public void ShowSalespersonFunctions() {
        System.out.print("\n");
		while (true) {
			System.out.println("-----Operations for salesperson menu-----");
			System.out.println("What kinds of operation would you like to perform?");
			System.out.println("1. Search for parts");
			System.out.println("2. Sell a part");
			System.out.println("3. Return to the main menu");
			System.out.print("Enter your choice: ");
			Scanner keyboard = new Scanner(System.in);
			option = keyboard.nextInt();
			keyboard.nextLine();
			switch (option) {
			case 1:
				SearchParts();
				return;
			case 2:
				SellPart();
				return;
			case 3:
				System.out.print("\n");
				return;
			default:
				System.out.println("Invalid Choice! Please try again with a valid choice!\n");
			}
		}
    }
}
