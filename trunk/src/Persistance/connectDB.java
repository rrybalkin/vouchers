package Persistance;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class connectDB {

	// метод для получения информации о всех посетителях
	public static String[] getAllVisitors(){
		List<String> MESSAGE = new LinkedList<String>();
		try {
			Statement stmt = connectToBase();
			ResultSet rs = stmt.executeQuery("SELECT * FROM talons");
			
			while (rs.next()) {
				MESSAGE.add(rs.getString(1)+" "+rs.getString(3)+" "+rs.getString(2)+" "+rs.getString(4)+" "+rs.getString(5)+" "+rs.getString(6));
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			MESSAGE = null;
			System.out.println("Error!");
		}
		String[] messages = new String[MESSAGE.size()];
		MESSAGE.toArray(messages);
		return messages;
	}
	
	// метод для получения информации о посетителе по его id
	public static String getVisitorByID(int visitor_id){
		String answer = "";
		try {
			Statement stmt = connectToBase();
			ResultSet rs = stmt.executeQuery("SELECT * FROM talons WHERE visitor_id="+visitor_id);
			
			while (rs.next()) {
				answer = (rs.getString(1)+" "+rs.getString(3)+" "+rs.getString(2)+" "+rs.getString(4)+" "+rs.getString(5)+" "+rs.getString(6));
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			answer = null;
			System.out.println("Error!");
		}
		return answer;
	}
	
	// метод для добавления колличества обедов посетителю
	public static void setLaunchForVisitor(int visitor_id, int count_lunch){
		try {
			Statement stmt = connectToBase();
			stmt.executeQuery("UPDATE talons SET count_lunch=count_lunch+"+count_lunch+" WHERE visitor_id = "+visitor_id);	
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error!");
		}
	}
	
	// метод для дбавления колличества ужинов посетителю
	public static void setDinnerForVisitor(int visitor_id, int count_dinner){
		try {
			Statement stmt = connectToBase();
			stmt.executeQuery("UPDATE talons SET count_dinner=count_dinner+"+count_dinner+" WHERE visitor_id = "+visitor_id);	
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error!");
		}
	}
	
	// метод для добавления нового посетителя
	public static void addNewVisitor(String[] info){
		try {
			Statement stmt = connectToBase();
			stmt.executeQuery("INSERT INTO talons (firstname, lastname, middlename, count_lunch, count_dinner) VALUES ('"+info[0]+"','"+info[1]+"','"+info[2]+"' , 0, 0)");
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error!");
		}
	}
	
	// метод для обнуления колличетсва обедов у посетителя
	public static void removeLaunchForVisitor(int visitor_id){
		try {
			Statement stmt = connectToBase();
			stmt.executeQuery("UPDATE talons SET count_lunch=0 WHERE visitor_id = "+visitor_id);	
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error!");
		}	
	}
	
	// метод для обнуления колличетсва ужинов у посетителя
	public static void removeDinnerForVisitor(int visitor_id){
		try {
			Statement stmt = connectToBase();
			stmt.executeQuery("UPDATE talons SET count_dinner=0 WHERE visitor_id = "+visitor_id);	
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error!");
		}
	}
	
	// метод для обнуления колличества обедов у всех посетителей
		public static void removeLaunchForAll(){
			try {
				Statement stmt = connectToBase();
				stmt.executeQuery("UPDATE talons SET count_lunch=0");	
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error!");
			}	
		}
		
		// метод для обнуления колличества ужинов у всех посетителей
		public static void removeDinnerForAll(){
			try {
				Statement stmt = connectToBase();
				stmt.executeQuery("UPDATE talons SET count_dinner=0");
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error!");
			}
		}
	
	// метод для удаления посетителя по его id
	public static void removeVisitorByID(int visitor_id){
		try {
			Statement stmt = connectToBase();
			stmt.executeQuery("DELETE FROM talons WHERE visitor_id ="+visitor_id);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error!");
		}
	}

	// метод для получения всех посетителей и колличества обедов и ужинов для них
	public static Map<String, String> getInfoAboutAll() {
		Map<String, String> info = new HashMap<String, String>();
		StringBuffer query = new StringBuffer(1000);
		query.append("SELECT lastname || ' ' || firstname || ' ' || middlename AS name,");
		query.append("	count_lunch,");
		query.append("	count_dinner");
		query.append("	FROM talons");
		
		try {
			Statement stmt = connectToBase();
			ResultSet rs = stmt.executeQuery(query.toString());
			
			while (rs.next()) {
				String FIO = rs.getString(1);
				String countLunchThenDinner = rs.getString(2) + ";" + rs.getString(3);
				info.put(FIO, countLunchThenDinner);
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			info = null;
			System.out.println("Error in method 'getInfoAboutAll'!");
		}
		
		return info;
	}
	
	// the method of connect to database
	private static Statement connectToBase() {
		/**
		 * эта строка загружает драйвер DB.
		 */
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver is not found.");
		}

		// the need for right working
		Locale.setDefault(Locale.ENGLISH);

		Connection conn;
		try {
			// connecting... and return object to connectivity
			conn = DriverManager
					.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
							"system", "admin");

			if (conn == null) {
				System.out.println("Нет соединения с БД!");
				System.exit(0);
			}

			return conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
